package com.nettystu.codec;

import com.nettystu.protocol.Packet;
import com.nettystu.protocol.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class PackerDecoder extends ByteToMessageDecoder {

    /**
     *
     * @param channelHandlerContext
     * @param byteBuf
     * @param list 通过往这个 List 里面添加解码后的结果对象，就可以自动实现结果往下一个 handler 进行传递，
     *             这样，我们就实现了解码的逻辑 handler。
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        list.add(PacketCodeC.INSTANCE.decode(byteBuf));
    }
}
