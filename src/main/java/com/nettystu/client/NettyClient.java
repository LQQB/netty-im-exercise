package com.nettystu.client;

import com.nettystu.client.handler.LoginResponseHandler;
import com.nettystu.client.handler.MessageResponseHandler;
import com.nettystu.codec.PackerDecoder;
import com.nettystu.codec.PackerEncoder;
import com.nettystu.codec.Spliter;
import com.nettystu.protocol.request.LoginRequestPacket;
import com.nettystu.protocol.request.MessageRequestPacket;
import com.nettystu.session.Session;
import com.nettystu.util.SessionUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class NettyClient {

    private static int MAX_RETRY = 5;

    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(new PackerDecoder());
                        ch.pipeline().addLast(new LoginResponseHandler());
                        ch.pipeline().addLast(new MessageResponseHandler());
                        ch.pipeline().addLast(new PackerEncoder());
                    }
                });

        connect(bootstrap,"127.0.0.1", 8001, MAX_RETRY);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        // 4. 建立连接
        bootstrap.connect(host, port).addListener( future -> {
            if(future.isSuccess()) {
                System.out.println("连接成功，启动控制台线程");
                Channel channel = ( (ChannelFuture) future).channel();
                // 连接成功启动控制台线程
                startConsoleThread(channel);

            } else if(retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 重连时间间隔
                int delay = 1 << order;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                bootstrap.config().group().schedule( () -> connect(bootstrap, host, port, retry-1), delay,
                        TimeUnit.SECONDS);
            }
        });
    }

    private static void startConsoleThread(Channel channel) {
        Scanner scanner = new Scanner(System.in);
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();

        new Thread( () -> {
           while (!Thread.interrupted()) {
                if(!SessionUtil.hasLogin(channel)) {
                    System.out.println("输入用户名登录: ");
                    String usernmae = scanner.nextLine();
                    loginRequestPacket.setUsername(usernmae);
                    loginRequestPacket.setPassword("123456");   // 密码默认
                    channel.writeAndFlush(loginRequestPacket);
                    waitForLoginResponse();
                } else {
                    String toUserId = scanner.next();
                    String message = scanner.next();
                    channel.writeAndFlush(new MessageRequestPacket(toUserId, message));
                }
           }
        }).start();
    }

    private static void waitForLoginResponse() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {
        }
    }
}
