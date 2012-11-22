package com.miage.jirachi.miagics;

public class Player extends Character {


	@Override
	public void setMoveDirection(int direction) {
		if (this == CharacterController.getInstance().getSelf()) {
			if (mMoveDirection != direction) {
				Packet packet = PacketMaker.makeMovePacket((short)direction);
				NetworkController.getInstance().send(packet);	
			}
		}
		
		super.setMoveDirection(direction);
	}
}
