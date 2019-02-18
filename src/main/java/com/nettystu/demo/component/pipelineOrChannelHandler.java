package com.nettystu.demo.component;

import com.nettystu.demo.component.channelHandler.inhand.InHandleA;
import com.nettystu.demo.component.channelHandler.inhand.InHandleB;
import com.nettystu.demo.component.channelHandler.inhand.InHandleC;
import com.nettystu.demo.component.channelHandler.outhand.OutHandleA;
import com.nettystu.demo.component.channelHandler.outhand.OutHandleB;
import com.nettystu.demo.component.channelHandler.outhand.OutHandleC;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class pipelineOrChannelHandler {

    public static void main(String[] args) {
        NioEventLoopGroup boas = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap
                .group(boas, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // inBound，处理读数据的逻辑链
                        ch.pipeline().addLast(new InHandleA());
                        ch.pipeline().addLast(new InHandleB());
                        ch.pipeline().addLast(new InHandleC());

                        // outBound，处理写数据的逻辑链
                        ch.pipeline().addLast(new OutHandleA());
                        ch.pipeline().addLast(new OutHandleB());
                        ch.pipeline().addLast(new OutHandleC());
                    }
                });
        serverBootstrap.bind(8000);
    }

}
