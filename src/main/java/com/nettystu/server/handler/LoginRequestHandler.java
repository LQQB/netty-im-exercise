package com.nettystu.server.handler;

import com.nettystu.protocol.request.LoginRequestPacket;
import com.nettystu.protocol.response.LoginResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginRequestPacket loginRequestPacket)  {
        System.out.println(new Date() + ": 收到客户端登录请求……");

        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(loginRequestPacket.getVersion());
        if(vrify(loginRequestPacket)) {
            System.out.println(new Date() + "登录成功");
            loginResponsePacket.setSuccess(true);
        } else {
            loginResponsePacket.setSuccess(false);
            loginResponsePacket.setReason("账户密码校验失败");
            System.out.println(new Date() + "登录失败");
        }
        channelHandlerContext.channel().writeAndFlush(loginResponsePacket);

    }


    private boolean vrify(LoginRequestPacket loginRequestPacket) {
        return true;
    }

}
