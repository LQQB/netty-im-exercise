package com.nettystu;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                // 1. 指定线程模式
                .group(workerGroup)
                // 2. 指定 IO 类型 为 NIO
                .channel(NioSocketChannel.class)
                // 3. IO 处理逻辑
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception { }
                });

        connect(bootstrap,"127.0.0.1", 8000);
    }

    private static void connect(Bootstrap bootstrap, String host, int port) {
        // 4. 建立连接
        bootstrap.connect(host, port).addListener( future -> {
            if(future.isSuccess()) {
                System.out.println("连接成功");
            } else {
                System.err.println("连接失败");
                // 连接失败递归调用自己
                connect(bootstrap,"127.0.0.1", 8000);
            }
        });
    }

}
