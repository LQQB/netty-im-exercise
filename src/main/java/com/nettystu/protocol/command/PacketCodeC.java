package com.nettystu.protocol.command;

import com.nettystu.serialize.JSONSerializer;
import com.nettystu.serialize.Serializer;
import com.nettystu.serialize.SerializerAlgorithm;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.Map;

import static com.nettystu.protocol.command.Command.LOGIN_REQUEST;

public class PacketCodeC {

    private static final int MAGIC_NUMBER = 0x00000123;
    private static final Map<Byte, Class<? extends Packet>> packetTypeMap;
    private static final Map<Byte, Serializer> serializerMap;

    static {
        packetTypeMap = new HashMap<Byte, Class<? extends Packet>>();
        packetTypeMap.put(LOGIN_REQUEST, LoginRequstPacket.class);

        serializerMap = new HashMap<Byte, Serializer>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(), serializer);
    }

    public ByteBuf encode(Packet packet) {

        // 创建 ByteBuf 对象
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();

        // 序列化 Java 对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // 实际编码过程
        // 魔数，版本号，序列化算法，指令，数据长度，数据
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }


    public Packet decode(ByteBuf byteBuf) {
        // 跳过 MAGIC_NUMBER
        byteBuf.skipBytes(4);

        // 跳过版本号
        byteBuf.skipBytes(1);

        // 序列化算法标识
        byte serializerAlgorithm = byteBuf.readByte();

        // 指示
        byte command = byteBuf.readByte();

        // 获取数据长度
        int length = byteBuf.readInt();

        // 获取数据
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Class<? extends Packet> requestType = getPacketType(command);
        Serializer serializer = getSerializer(serializerAlgorithm);

        if(requestType != null && serializer !=null) {
            return serializer.deSerialize(requestType, bytes);
        }

        return null;
    }


    private Serializer getSerializer(Byte serializerAlgorithm) {
        return serializerMap.get(serializerAlgorithm);
    }

    private Class<? extends Packet> getPacketType(Byte command) {
        return packetTypeMap.get(command);
    }

}
