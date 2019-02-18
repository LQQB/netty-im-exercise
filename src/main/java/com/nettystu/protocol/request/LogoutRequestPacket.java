package com.nettystu.protocol.request;

import com.nettystu.protocol.Packet;

import static com.nettystu.protocol.command.Command.LOGOUT_REQUEST;

public class LogoutRequestPacket extends Packet {
    @Override
    public Byte getCommand() {
        return LOGOUT_REQUEST;
    }
}
