package com.nettystu.test;

import com.nettystu.protocol.command.LoginRequstPacket;
import com.nettystu.protocol.command.Packet;
import com.nettystu.protocol.command.PacketCodeC;
import com.nettystu.serialize.JSONSerializer;
import com.nettystu.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import org.junit.Assert;
import org.junit.Test;

public class PacketCodeTest {

    @Test
    public void encode() {
        Serializer serializer = new JSONSerializer();

        LoginRequstPacket loginRequestPacket = new LoginRequstPacket();
        loginRequestPacket.setVersion((byte) 1);
        loginRequestPacket.setUserId(001);
        loginRequestPacket.setUsername("QB");
        loginRequestPacket.setPassword("123456");

        // 将 packet 类 编码之后再解码的
        PacketCodeC packetCodeC = new PacketCodeC();
        ByteBuf byteBuf = packetCodeC.encode(loginRequestPacket);
        Packet decodedPacket = packetCodeC.decode(byteBuf);

        // 将 loginRequestPacket 跟 解码之后再编码的 decodedPacket ，序列化后比较
        Assert.assertArrayEquals(serializer.serialize(loginRequestPacket), serializer.serialize(decodedPacket));

    }

}
