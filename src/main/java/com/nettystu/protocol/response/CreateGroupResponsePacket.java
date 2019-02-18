package com.nettystu.protocol.response;

import com.nettystu.protocol.Packet;
import lombok.Data;

import java.util.List;

import static com.nettystu.protocol.command.Command.CREATE_GROUP_RESPONSE;

@Data
public class CreateGroupResponsePacket extends Packet {

    private List<String> usernameList;

    private boolean success;

    private String groupId;

    @Override
    public Byte getCommand() {
        return CREATE_GROUP_RESPONSE;
    }
}
