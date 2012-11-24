package com.miage.jirachi.miagics;

public class PacketMaker {
	public static Packet makeBootMe() {
		Packet packet = new Packet();
		packet.opcode = Opcodes.CMSG_BOOTME;
		
		return packet;
	}
	
    public static Packet makeMovePacket(short direction) {
    	Packet packet = new Packet();
        
        switch (direction) {
        case Character.MOVE_LEFT:
            packet.opcode = Opcodes.CMSG_MOVE_LEFT;
            break;
            
        case Character.MOVE_RIGHT:
        	packet.opcode = Opcodes.CMSG_MOVE_RIGHT;
            break;
            
        case Character.MOVE_NOT:
        	packet.opcode = Opcodes.CMSG_MOVE_STOP;
            break;
        }
        
        return packet;
    }
    
    public static Packet makeSyncPosition(float x, float y) {
        Packet packet = new Packet();
        BitStream data = new BitStream();
        
        data.write(x);
        data.write(y);
        
        packet.data = data.getBytesP();
        
        return packet;
    }
}
