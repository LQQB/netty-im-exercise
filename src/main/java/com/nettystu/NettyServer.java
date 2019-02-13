package com.nettystu;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

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
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {

                    }
                });

        serverBootstrap.bind(8000);

    }


}
