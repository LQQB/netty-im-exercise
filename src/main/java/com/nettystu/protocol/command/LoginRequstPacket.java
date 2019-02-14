package com.nettystu.protocol.command;

import lombok.Data;

@Data
public class LoginRequstPacket extends Packet{

    private Integer userId;

    private String username;

    private String password;

    public Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
