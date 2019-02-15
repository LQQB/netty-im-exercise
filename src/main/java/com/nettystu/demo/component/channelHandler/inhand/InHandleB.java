package com.nettystu.demo.component.channelHandler.inhand;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class InHandleB extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("InHandleB: " + msg);
        super.channelRead(ctx, msg);
    }
}
