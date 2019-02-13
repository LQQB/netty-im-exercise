package com.nettystu.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;

public class FirstClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * channelActive() 方法在客户端连接建立成功后立即被调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date()+": 客户端写出数据");

        ByteBuf byteBuf = getByteBuf(ctx);

        ctx.channel().writeAndFlush(byteBuf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.println(new Date() + "客户端读取到数据 ->" + byteBuf.toString(Charset.forName("utf-8")));

    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        // 1. 获取二进制抽象 bytebuf
        // ctx.alloc() 获得一个 ByteBuf 的内存管理器，这个内存管理器的作用是分配一个 ByteBBuf
        ByteBuf buf = ctx.alloc().buffer();

        // 准备数据，并指定字符集为 UTF-8
        byte[] bytes = "您好我是LQB的客户端".getBytes(Charset.forName("UTF-8"));

        // 3. 填充数据到 ByteBuf
        buf.writeBytes(bytes);

        return buf;
    }
}
