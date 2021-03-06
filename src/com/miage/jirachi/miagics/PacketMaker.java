package com.miage.jirachi.miagics;

public class PacketMaker {
	public static Packet makeBootMe() {
		Packet packet = new Packet();
		packet.opcode = Opcodes.CMSG_BOOTME;
		
		return packet;
	}
	
	// CMSG_MOVE_...
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
    
    // CMSG_SYNC_POSITION
    public static Packet makeSyncPosition(float x, float y) {
        Packet packet = new Packet();
        BitStream data = new BitStream();
        
        data.write(x);
        data.write(y);
        
        packet.opcode = Opcodes.CMSG_SYNC_POSITION;
        packet.data = data.getBytesP();
        
        return packet;
    }
    
    // CMSG_JUMP
    public static Packet makeJump() {
        Packet packet = new Packet();
        packet.opcode = Opcodes.CMSG_JUMP;
        return packet;
    }
    
    // CMSG_USE_GAMEOBJECT
    public static Packet makeUseGameObject() {
        Packet packet = new Packet();
        packet.opcode = Opcodes.CMSG_USE_GAMEOBJECT;
        return packet;
    }
    
    // CMSG_FIGHT
    public static Packet makeFight() {
    	Packet packet = new Packet();
    	packet.opcode = Opcodes.CMSG_FIGHT;
    	return packet;
    }
}
