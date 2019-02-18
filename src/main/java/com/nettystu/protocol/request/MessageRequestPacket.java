package com.nettystu.protocol.request;

import com.nettystu.protocol.Packet;
import com.nettystu.protocol.command.Command;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.nettystu.protocol.command.Command.MESSAGE_REQUEST;

@Data
@NoArgsConstructor   // 生成一个无参数的构造方法
public class MessageRequestPacket extends Packet {

    private String toUserId;

    private String message;

    public MessageRequestPacket(String toUserId, String message) {
        this.toUserId = toUserId;
        this.message = message;
    }

    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }
}
