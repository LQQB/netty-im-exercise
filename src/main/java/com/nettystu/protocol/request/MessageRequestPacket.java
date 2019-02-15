package com.nettystu.protocol.request;

import com.nettystu.protocol.Packet;
import com.nettystu.protocol.command.Command;
import lombok.Data;

import static com.nettystu.protocol.command.Command.MESSAGE_REQUEST;

@Data
public class MessageRequestPacket extends Packet {

    private String message;

    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }
}
