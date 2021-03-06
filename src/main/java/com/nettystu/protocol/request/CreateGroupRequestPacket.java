package com.nettystu.protocol.request;

import com.nettystu.protocol.Packet;
import lombok.Data;

import java.util.List;

import static com.nettystu.protocol.command.Command.CREATE_GROUP_REQUEST;

@Data
public class CreateGroupRequestPacket extends Packet {

    private List<String> userIdList;


    @Override
    public Byte getCommand() {
        return CREATE_GROUP_REQUEST;
    }
}
