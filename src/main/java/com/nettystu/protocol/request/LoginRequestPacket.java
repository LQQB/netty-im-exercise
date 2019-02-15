package com.nettystu.protocol.request;

import com.nettystu.protocol.command.Command;
import com.nettystu.protocol.Packet;
import lombok.Data;

@Data
public class LoginRequestPacket extends Packet {

    private String userId;

    private String username;

    private String password;

    public Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
