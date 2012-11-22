package com.miage.jirachi.miagics;

public class Player extends Character {
    @Override
    public void setMoveDirection(int direction) {
        super.setMoveDirection(direction);
        
        BitStream packet = PacketMaker.makeMovePacket((short)direction);
        NetworkController.getInstance().send(packet);
    }
}
