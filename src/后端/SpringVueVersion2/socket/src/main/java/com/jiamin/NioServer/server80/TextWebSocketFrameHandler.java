package com.jiamin.NioServer.server80;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.jiamin.NioServer.pojo.MyHttpInputMessage;
import com.jiamin.mapper.RecordMapper;
import com.jiamin.service.RecordService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.example.common.entity.Record;
import org.example.common.entity.websocket.Connect;
import org.example.common.entity.websocket.PrivateMessage;
import org.example.common.entity.websocket.TextFrame;
import org.example.common.entity.websocket.User;
import org.springframework.http.HttpInputMessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private ChannelGroup group;
    private FastJsonHttpMessageConverter converter;
    private Map<String, Channel> map;
    private RecordService recordService;
    private ThreadPoolExecutor executor;


    public TextWebSocketFrameHandler(Map<String, Channel> map, ChannelGroup group) {
        this.map = map;
        this.group = group;
        this.recordService = new RecordService();
        this.executor = new ThreadPoolExecutor(8,
                8,
                0,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardPolicy());
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setDateFormat("yyyy-MM-dd");
        config.setCharset(StandardCharsets.UTF_8);
        config.setSerializerFeatures(
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty
        );
        converter.setFastJsonConfig(config);
        this.converter = converter;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            ctx.pipeline().remove(MyHttpRequestHandler.class);
            Channel client = ctx.channel();
            group.add(client);
        } else {
            super.userEventTriggered(ctx, evt);
        }

    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        ByteBuf content = msg.content();
        byte[] buf = null;
        if (content.hasArray()) {
            buf = content.array();
        } else {
            buf = new byte[content.readableBytes()];
            content.getBytes(content.readerIndex(), buf);
        }

        System.out.println(new String(buf, StandardCharsets.UTF_8));
        HttpInputMessage httpInputMessage = new MyHttpInputMessage(buf);
        TextFrame frame = (TextFrame) converter.read(TextFrame.class, httpInputMessage);
        handle(frame, ctx, msg);

    }

    private void handle(TextFrame frame, ChannelHandlerContext ctx, TextWebSocketFrame msg) throws IOException {
        String type = frame.getType();
        String text = frame.getText();
        System.out.println("-----------type--------");
        System.out.println(type);
        System.out.println("-----------text--------");
        System.out.println(text);
        if (type.equals("/simple/exit")) {
            User user = (User) converter.read(User.class, new MyHttpInputMessage(text));
            map.remove(user.getUserName());
            group.remove(ctx.channel());
            group.writeAndFlush(new TextWebSocketFrame
                    ("{\"type\": \"" + type + "\"," +
                            "\"text\": " + text + "}"
                    ));
        } else if (type.equals("/simple/welcome")) {
            User user = (User) converter.read(User.class, new MyHttpInputMessage(text));
            map.put(user.getUserName(), ctx.channel());
            group.add(ctx.channel());
            group.writeAndFlush(new TextWebSocketFrame
                    ("{\"type\": \"" + type + "\"," +
                            "\"text\": " + text + "}"
                    )
            );
        } else if (type.startsWith("/simple")) {
            User user = (User) converter.read(User.class, new MyHttpInputMessage(text));
            group.writeAndFlush(new TextWebSocketFrame
                    ("{\"type\": \"" + type + "\"," +
                            "\"text\": " + text + "}"
                    ));
        } else if (type.equals("/complex/applyConnect")) {
            Connect connect = (Connect) converter.read(Connect.class, new MyHttpInputMessage(text));
            String targetUserName = connect.getTargetUserName();
            Channel channel = map.get(targetUserName);
            if (channel == null) return;
            channel.writeAndFlush(new TextWebSocketFrame
                    ("{\"type\": \"/user/" + targetUserName + "/simple/applyConnect\"," +
                            "\"text\": " + text + "}"
                    ));
        } else if (type.equals("/complex/agreeConnect")) {
            Connect connect = (Connect) converter.read(Connect.class, new MyHttpInputMessage(text));
            String targetUserName = connect.getTargetUserName();
            Channel channel = map.get(targetUserName);
            if (channel == null) return;
            channel.writeAndFlush(new TextWebSocketFrame
                    ("{\"type\": \"/user/" + targetUserName + "/simple/agreeConnect\"," +
                            "\"text\": " + text + "}"
                    ));
        } else if (type.equals("/complex/sendMessageToUser")) {
            PrivateMessage privateMessage = (PrivateMessage) converter.read(PrivateMessage.class, new MyHttpInputMessage(text));
            String targetUserName = privateMessage.getTargetName();
            Channel channel = map.get(targetUserName);
            if (channel == null) return;
            channel.writeAndFlush(new TextWebSocketFrame
                    ("{\"type\": \"/user/" + targetUserName + "/simple/sendMessageToUser\"," +
                            "\"text\": " + text + "}"
                    ));
            record(privateMessage);
        }
    }

    private void record(PrivateMessage privateMessage) {
        executor.execute(() -> recordService.insertOneRecord(privateMessage.wrap()));
    }
}
