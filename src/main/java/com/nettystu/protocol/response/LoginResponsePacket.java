package com.nettystu.protocol.response;

import com.nettystu.protocol.Packet;
import com.nettystu.protocol.command.Command;
import lombok.Data;

import static com.nettystu.protocol.command.Command.LOGIN_RESPONSE;

@Data
public class LoginResponsePacket extends Packet {

    private boolean success;

    private String reason;

    @Override
    public Byte getCommand() {
        return LOGIN_RESPONSE;
    }
}
