package com.nettystu.protocol.response;

import com.nettystu.protocol.Packet;
import lombok.Data;

import static com.nettystu.protocol.command.Command.LOGOUT_RESPONSE;


@Data
public class LogoutResponsePacket extends Packet {

    private boolean success;

    private String reason;


    @Override
    public Byte getCommand() {
        return LOGOUT_RESPONSE;
    }
}
