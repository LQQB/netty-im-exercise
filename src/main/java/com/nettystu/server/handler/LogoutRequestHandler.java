package com.nettystu.server.handler;

import com.nettystu.protocol.request.LogoutRequestPacket;
import com.nettystu.protocol.response.LogoutResponsePacket;
import com.nettystu.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LogoutRequestHandler  extends SimpleChannelInboundHandler<LogoutRequestPacket> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutRequestPacket logoutRequestPacket) throws Exception {
        SessionUtil.unBindSession(ctx.channel());
        LogoutResponsePacket logoutResponsePacket = new LogoutResponsePacket();
        logoutResponsePacket.setSuccess(true);
        ctx.channel().writeAndFlush(logoutResponsePacket);
    }
}
