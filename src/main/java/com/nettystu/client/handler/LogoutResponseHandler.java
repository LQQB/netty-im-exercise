package com.nettystu.client.handler;

import com.nettystu.protocol.response.LogoutResponsePacket;
import com.nettystu.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LogoutResponseHandler extends SimpleChannelInboundHandler<LogoutResponsePacket>  {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutResponsePacket logoutResponsePacket) throws Exception {
        SessionUtil.unBindSession(ctx.channel());
    }
}
