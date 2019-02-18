package com.nettystu.server.handler;

import com.nettystu.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class AuthenHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(!SessionUtil.hasLogin(ctx.channel())) {
            ctx.channel().close();
        } else {
            // 直接调用 pipeline 的 remove() 方法删除自身
            ctx.pipeline().remove(this);
            super.channelRead(ctx, msg);
        }
    }

    /**
     * 覆盖了 handlerRemoved() 方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

    }
}
