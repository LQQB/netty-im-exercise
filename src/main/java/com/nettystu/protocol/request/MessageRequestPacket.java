package com.nettystu.protocol.request;

import com.nettystu.protocol.Packet;
import com.nettystu.protocol.command.Command;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.nettystu.protocol.command.Command.MESSAGE_REQUEST;

@Data
@NoArgsConstructor   // 生成一个无参数的构造方法
public class MessageRequestPacket extends Packet {
    public MessageRequestPacket(String message) {
        this.message = message;
    }

    private String message;

    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }
}
