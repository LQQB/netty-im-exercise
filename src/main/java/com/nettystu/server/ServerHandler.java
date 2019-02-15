package com.nettystu.server;

import com.nettystu.protocol.Packet;
import com.nettystu.protocol.PacketCodeC;
import com.nettystu.protocol.request.LoginRequestPacket;
import com.nettystu.protocol.response.LoginResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.Buffer;
import java.util.Date;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("客户端开始登录....");

        ByteBuf requestBuf = (ByteBuf) msg;
        Packet packet =  PacketCodeC.INSTANCE.decode(requestBuf);

        if(packet instanceof LoginRequestPacket) {
            // 登录流程
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;

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

            // 登录响应
            ByteBuf responseBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginResponsePacket);
            ctx.channel().writeAndFlush(responseBuf);
        }
    }

    private boolean vrify(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}
