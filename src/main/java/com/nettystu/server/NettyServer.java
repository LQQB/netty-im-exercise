package com.nettystu.server;

import com.nettystu.codec.PackerDecoder;
import com.nettystu.codec.PackerEncoder;
import com.nettystu.codec.Spliter;
import com.nettystu.demo.leftcycle.LifeCycleTestHandler;
import com.nettystu.server.handler.LoginRequestHandler;
import com.nettystu.server.handler.MessageRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class NettyServer {

    public static void main(String[] args) {

        NioEventLoopGroup boassGroup = new NioEventLoopGroup();  // 监听端口，accept 新连接
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(); // 处理每一条连接的读写数据

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(boassGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        System.out.println("服务端启动中");
                        // 指定连接数据读写逻辑
                        ch.pipeline().addLast(new LifeCycleTestHandler());
                        ch.pipeline().addLast(new Spliter());   // 拒绝非本协议连接
                        ch.pipeline().addLast(new PackerDecoder());
                        ch.pipeline().addLast(new LoginRequestHandler());
                        ch.pipeline().addLast(new MessageRequestHandler());
                        ch.pipeline().addLast(new PackerEncoder());
                    }
                });

        bind(serverBootstrap, 8000);
    }


    private static void bind(ServerBootstrap bootstrap,  int port) {
        // .bind() 是一个异步的方法，它一调用就会立即返回一个 ChannelFuture，给 ChannelFuture 添加监听器 GenericFutureListener 然后我们
        // 就可以在 GenericFutureListener 的 operationComplete 方法里面，监听端口是否绑定成功
        bootstrap.bind(port).addListeners(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if(future.isSuccess()) {
                    System.out.println("端口绑定成功");
                } else {
                    System.err.println("端口绑定失败");
                    bind(bootstrap, port+1);
                }
            }
        });
    }

}
