package com.jiamin.NioServer.server80;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;

public class Client {
    public static void main(String[] args) {
        EventLoopGroup wokerGroup = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(wokerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        System.out.println("-------------------------------");
                        ByteBuf buf = (ByteBuf)msg;
                        System.out.println("客户端接收到" + buf);
                    }
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        ChannelFuture channelFuture = ctx.writeAndFlush(Unpooled.copyInt(1, 2, 3, 4));
                        System.out.println("客户端开始发送数据");

                        channelFuture.addListener(new ChannelFutureListener() {
                            @Override
                            public void operationComplete(ChannelFuture future) throws Exception {
                                if(future.isSuccess()){
//                                    System.out.println("客户端发送数据成功，断开连接");
//                                    ChannelFuture close = ctx.channel().close();
//                                    close.addListener(new ChannelFutureListener() {
//                                        @Override
//                                        public void operationComplete(ChannelFuture future) throws Exception {
//                                            if(future.isSuccess()){
//                                                System.out.println("断开成功");
//                                            }
//                                        }
//                                    });
                                }
                            }
                        });

                    }


                });
        ChannelFuture future = bootstrap.connect("127.0.0.1", 8888);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.isSuccess()){
                    System.out.println("客户端启动成功");
                }else{
                    Future<?> future1 = wokerGroup.shutdownGracefully();
                    future1.syncUninterruptibly();
                    System.out.println("客户端关闭");
                }
            }
        });



    }
}
