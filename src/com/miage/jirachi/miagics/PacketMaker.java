package com.miage.jirachi.miagics;

public class PacketMaker {
    public static BitStream makeMovePacket(short direction) {
        BitStream data = new BitStream();
        
        switch (direction) {
        case Character.MOVE_LEFT:
            data.write(Opcodes.CMSG_MOVE_LEFT);
            break;
            
        case Character.MOVE_RIGHT:
            data.write(Opcodes.CMSG_MOVE_RIGHT);
            break;
            
        case Character.MOVE_NOT:
            data.write(Opcodes.CMSG_MOVE_STOP);
            break;
        }
        
        return data;
    }
}
