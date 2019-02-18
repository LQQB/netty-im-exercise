package com.nettystu.protocol.response;

import com.nettystu.protocol.Packet;
import lombok.Data;

import static com.nettystu.protocol.command.Command.LOGIN_RESPONSE;
import static com.nettystu.protocol.command.Command.MESSAGE_RESPONSE;

@Data
public class MessageResponsePacket extends Packet {

    private String fromUserId;

    private String fromUserName;

    private String message;

    @Override
    public Byte getCommand() {
        return MESSAGE_RESPONSE;
    }
}
