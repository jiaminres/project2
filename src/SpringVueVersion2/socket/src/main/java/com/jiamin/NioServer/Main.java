package com.jiamin.NioServer;

import com.jiamin.NioServer.server80.ServerInitializerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.ImmediateEventExecutor;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        start();
    }

    public static void start() {
        Map<String, Channel> map = new ConcurrentHashMap<>();
        DefaultChannelGroup group = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(2);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ServerInitializerHandler(map, group));
        ChannelFuture future = serverBootstrap.bind(80);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.isSuccess()){
                    System.out.println("服务器启动成功");
                }else{
                    System.out.println("服务器启动失败，尝试关闭资源");
                    Future<?> wf = workerGroup.shutdownGracefully();
                    Future<?> bf = bossGroup.shutdownGracefully();
                    future.channel().close();
                    bf.syncUninterruptibly();
                    wf.syncUninterruptibly();
                    System.out.println("关闭资源成功");
                }
            }
        });
        future.channel().closeFuture().syncUninterruptibly();
        System.out.println("channel关闭");
    }
}
