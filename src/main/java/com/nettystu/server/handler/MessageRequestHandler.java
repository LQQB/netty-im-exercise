package com.nettystu.server.handler;

import com.nettystu.protocol.PacketCodeC;
import com.nettystu.protocol.request.MessageRequestPacket;
import com.nettystu.protocol.response.MessageResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket messageRequestPacket) throws Exception {

        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        System.out.println(new Date() + "  收到客户端消息[ " + messageRequestPacket.getMessage() +" ] ");
        messageResponsePacket.setMessage("服务端发送消息 [ "+ messageRequestPacket.getMessage() +" ] ");
        ctx.channel().writeAndFlush(messageResponsePacket);
    }

}
