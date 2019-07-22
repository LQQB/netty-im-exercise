package com.nettystu.client.handler;

import com.nettystu.protocol.response.LoginResponsePacket;
import com.nettystu.session.Session;
import com.nettystu.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;


public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginResponsePacket loginResponsePacket)  {
        String userId = loginResponsePacket.getUserId();

        String username = loginResponsePacket.getUserName();

        if (loginResponsePacket.isSuccess()) {
            System.out.println("[" + username + "] 登录成功，userId 为: " + loginResponsePacket.getUserId());
            // 客户端跟服务端的session 不共享
            SessionUtil.bindSession(new Session(userId, username), channelHandlerContext.channel());
        } else {
            System.out.println(new Date() + ": 客户端登录失败，原因：" + loginResponsePacket.getReason());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连接被关闭!");
    }

}
