package com.jiamin.NioServer.server80;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;

public class DoorCheckHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
//        out(buf);
        ctx.fireChannelRead(buf.retain());
    }

    public static void out( ByteBuf buf){
        byte[] content;
        if(buf.hasArray()) {
            content = buf.array();
        }else{
            content = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(),content);
        }
        String line = new String(content, StandardCharsets.ISO_8859_1);
        line = line.replace("\r\n", "CRLF\r\n");
        System.out.print(line);
    }
}
