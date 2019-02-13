package com.nettystu;

import io.netty.bootstrap.Bootstrap;
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
                // .channel() 来指定 IO 模型(NioServerSocketChannel.class 是 Nio 模型 , OioServerSocketChannel.class 是 BIO 模型
                .channel(NioServerSocketChannel.class)
                // childHandler() 方法 给引导类创建一个 ChannelInitializer,主要用来定义后续每条连接的数据读写，业务处理逻辑
                // ChannelInitializer 有一个泛型参数 NioSocketChannel，这个类 就是 Netty 对 NIO 类型的连接抽象。
                // 前面用到的 NioServerSocketChannel 也是对 NIO连接的抽象， NioServerSocketChannel 和 NioSocketChannel 的概念可以跟
                // ServerSocket 以及 Socket 两个概念对应
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        System.out.println("服务端启动中");
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
