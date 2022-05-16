package com.jiamin.NioServer.server80;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


public class MyHttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    public int count = 0;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        DoorCheckHandler.out(req.content());
        String reqRes = req.uri();
        reqRes = URLDecoder.decode(reqRes, "UTF-8");
        String path = null;
        File file = null;
        HttpResponse httpResponse = null;
        path = MyHttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation().toURI().toString();
        boolean error = false;

        /*websocket请求*/
        if (reqRes.startsWith("/ws")) {
            ctx.fireChannelRead(req.retain());
            return;
        }
        if (path.startsWith("file:")) {
            path = path.substring(6);
        }
        /*图片上传请求*/
        if (reqRes.startsWith("/upload/headImage")) {
            ByteBuf buf = req.content();
            byte[] content = null;
            if (buf.hasArray()) {
                content = buf.array();
            } else {
                content = new byte[buf.readableBytes()];
                buf.getBytes(buf.readerIndex(), content);
            }
            try {
                upload(content, path);
                httpResponse = new DefaultHttpResponse(req.protocolVersion(), HttpResponseStatus.OK);
            } catch (IOException e) {
                e.printStackTrace();
                httpResponse = new DefaultHttpResponse(req.protocolVersion(), HttpResponseStatus.SERVICE_UNAVAILABLE);
            }
            ctx.write(httpResponse);
            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            future.addListener(ChannelFutureListener.CLOSE);
        }

        if (reqRes.equals("/")) {
            reqRes = "/index.html";
        }
        int queryStartStartIndex = reqRes.lastIndexOf("?");
        int suffixEndIndex = queryStartStartIndex == -1 ? reqRes.length() : queryStartStartIndex;

        file = new File(path + reqRes.substring(0, suffixEndIndex));
        if (!file.exists()) {
            error = true;
            file = new File(path + "404.png");
        }
        /*知道访问呢的文件后，查看缓存是否能继续使用*/
        if (cache(ctx, req, file.lastModified())) return;

        String contentType = "text/plain";
        if (!error) {
            String suffix = reqRes.substring(reqRes.lastIndexOf(".") + 1, suffixEndIndex);
            if (suffix.equals("css")) {
                contentType = "text/css";
            } else if (suffix.equals("js")) {
                contentType = "application/x-javascript";
            } else if (suffix.equals("png")) {
                contentType = "image/png";
            } else if (suffix.equals("jpg") || suffix.equals("jpeg")) {
                contentType = "image/jpg";
            } else if (suffix.equals("html")) {
                contentType = "text/html; charset=UTF-8";
            }
        }

        if (!error) {
            httpResponse = new DefaultHttpResponse(req.protocolVersion(), HttpResponseStatus.OK);
        } else {
            httpResponse = new DefaultHttpResponse(req.protocolVersion(), HttpResponseStatus.NOT_FOUND);
        }

        httpResponse.headers().set("Content-Type", contentType);
        httpResponse.headers().set("Content-Length", file.length());
        /* 缓存相关*/
        //生成Cache-Control: max-age = 3600
        httpResponse.headers().set("Cache-Control", "max-age=3600");
        //生成Last-Modified: xx
        long instant = file.lastModified();
        ZonedDateTime GMTTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(instant), ZoneId.of("GMT"));
        String GMTTimeFormat = DateTimeFormatter.RFC_1123_DATE_TIME.format(GMTTime);
        httpResponse.headers().set("Last-Modified", GMTTimeFormat);

//        boolean keepAlive = false;
//        if (req.headers().contains("Connection") && req.headers().get("Connection").equals("keep-alive")) {
//            keepAlive = true;
//            httpResponse.headers().set("Connection", "keep-alive");
//        }

        ctx.write(httpResponse);
        if (ctx.pipeline().get(SslHandler.class) == null) {
            ctx.write(new DefaultFileRegion(new FileInputStream(file).getChannel(), 0, file.length()));
        } else {
            ctx.write(new ChunkedNioFile(file));
        }
        ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        //考虑到获取服务器是静态资源服务器，而websocket的连接并非该连接，因此废用长连接
//        if (!keepAlive) {
//            future.addListener(ChannelFutureListener.CLOSE);
//        }
        future.addListener(ChannelFutureListener.CLOSE);
    }

    private boolean cache(ChannelHandlerContext ctx, FullHttpRequest req, long lastModified) {
        if (req.headers().contains("If-Modified-Since")) {
            String GMTTimeFormat = req.headers().get("If-Modified-Since");
            ZonedDateTime GMTTime = ZonedDateTime.parse(GMTTimeFormat, DateTimeFormatter.RFC_1123_DATE_TIME);
            //没有更新，则直接返回304
            if (GMTTime.toInstant().toEpochMilli() >= lastModified) {
                DefaultHttpResponse response = new DefaultHttpResponse(req.protocolVersion(), HttpResponseStatus.NOT_MODIFIED);
                ctx.write(response);
                ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
                future.addListener(ChannelFutureListener.CLOSE);
                return true;
            }
        }
        return false;
    }

    private void upload(byte[] content, String path) throws IOException {
        //先获取boundary的长度
        int boundaryLen = 0;
        while (content[boundaryLen] != '\r') {
            boundaryLen++;
        }

        //此时指针停在'\r',+2跳过接下来的'\n'
        int disposition = boundaryLen + 2;
        //寻找第一个name
        int left, right;
        left = right = disposition;
        //'name'的长度4
        while (right - left < 4) {
            byte b = content[right];
            if (right - left == 0 && b == 'n') {
                right++;
            } else if (right - left == 1 && b == 'a') {
                right++;
            } else if (right - left == 2 && b == 'm') {
                right++;
            } else if (right - left == 3 && b == 'e') {
                right++;
            } else {
                right++;
                left = right;
            }
        }
        //此时r定位到name后面的一个字符
        //找到后面的第一个双引号
        while (content[right] != '"') {
            right++;
        }
        //定位到图片名称的起始索引
        right++;
        left = right;
        while (content[right] != '"') {
            right++;
        }
        String headImageName = new String(content, left, right - left);

        left = right;
        //出现2个连续回车，出现正式数据
        while (true) {
            if (content[left++] != '\r') continue;
            left++;
            if (content[left++] != '\r') continue;
            left++;
            break;
        }
        //图片数据长度 - boundary长度 和 两个crlf 和 --
        int len = content.length - left - boundaryLen - 6;
        //放到image文件夹下
        File file = new File(path + "image/" + headImageName);
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(content, left, len);
    }
}
