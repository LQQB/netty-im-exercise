package com.nettystu.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class NettyClient {

    private static int MAX_RETRY = 5;

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
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 指定连接数据的读写逻辑
                        ch.pipeline().addLast(new FirstClientHandler());
                    }
                });

        connect(bootstrap,"127.0.0.1", 8000, MAX_RETRY);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        // 4. 建立连接
        bootstrap.connect(host, port).addListener( future -> {
            if(future.isSuccess()) {
                System.out.println("连接成功");
            } else if(retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 重连时间间隔
                int delay = 1 << order;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                // bootstrap.config().group().schedule() 定时任务调用
                // bootstrap.config() 返回 BootstrapConfig，它是对 Bootstrap 配置参数的抽象
                // bootstrap.config().group() 返回的是我们一开始配置的线程模型 workerGroup，
                // workerGroup 的 schedule 方法实现定时任务逻辑
                bootstrap.config().group().schedule( () -> connect(bootstrap, host, port, retry-1), delay,
                        TimeUnit.SECONDS);
            }
        });
    }

}
