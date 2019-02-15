package com.nettystu.client;

import com.nettystu.protocol.Packet;
import com.nettystu.protocol.PacketCodeC;
import com.nettystu.protocol.request.LoginRequestPacket;
import com.nettystu.protocol.response.LoginResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;
import java.util.UUID;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("客户端开始登录....");
        LoginRequestPacket loginRequstPacket = new LoginRequestPacket();

        loginRequstPacket.setUserId(UUID.randomUUID().toString());
        loginRequstPacket.setUsername("QB");
        loginRequstPacket.setPassword("123456");

        ByteBuf byteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginRequstPacket);
        ctx.channel().writeAndFlush(byteBuf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)  {
        ByteBuf rsponseBuf = (ByteBuf) msg;
        Packet packet = PacketCodeC.INSTANCE.decode(rsponseBuf);

        if(packet instanceof LoginResponsePacket) {

            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;

            if (loginResponsePacket.isSuccess()) {
                System.out.println(new Date() + ": 客户端登录成功");
            } else {
                System.out.println(new Date() + ": 客户端登录失败，原因：" + loginResponsePacket.getReason());
            }
        }
    }
}
