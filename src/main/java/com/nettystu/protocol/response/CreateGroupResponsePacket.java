package com.nettystu.protocol.response;

import com.nettystu.protocol.Packet;

import static com.nettystu.protocol.command.Command.CREATE_GROUP_RESPONSE;

public class CreateGroupResponsePacket extends Packet {


    @Override
    public Byte getCommand() {
        return CREATE_GROUP_RESPONSE;
    }
}
