package com.nettystu.protocol.request;

import com.nettystu.protocol.Packet;

import static com.nettystu.protocol.command.Command.CREATE_GROUP_REQUEST;

public class CreateGroupRequestPacket extends Packet {


    @Override
    public Byte getCommand() {
        return CREATE_GROUP_REQUEST;
    }
}
