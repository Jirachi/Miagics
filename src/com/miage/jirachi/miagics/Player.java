package com.miage.jirachi.miagics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.miage.jirachi.resource.ResourceAnimated;

public class Player extends Character {
    // == Attributs
    // Temps entre chaque synchronisation de position avec le serveur
    public final static float POS_SYNC_DELAY = 0.3f;
    
    protected float mTimeSincePosSync = 0;

    // == Constructeur
    public Player(ResourceAnimated resourceAnimated, TextureRegion[][] tex) {
        super(resourceAnimated, tex);
    }
    
    // == Methodes   
	@Override
	public void setMoveDirection(int direction) {
	    // Si le joueur qui a change de direction, c'est nous, alors
	    // on le signale au serveur.
		if (this == CharacterController.getInstance().getSelf()) {
			if (mMoveDirection != direction) {
				Packet packet = PacketMaker.makeMovePacket((short)direction);
				NetworkController.getInstance().send(packet);	
			}
			
			if (mMoveDirection == Character.MOVE_NOT) {
				// On ne bouge pas, Lag compensation => on indique notre position
				doSyncPosition();
			}
		}
		
		super.setMoveDirection(direction);
	}
	
	@Override
	public void jump() {
	    super.jump();
	    
	    // Si c'est nous qui sautons, on le signale au serveur
	    if (this == CharacterController.getInstance().getSelf()) {
	        Packet packet = PacketMaker.makeJump();
	        NetworkController.getInstance().send(packet);
	    }
	}
	
	@Override
	public void act(float timeDelta) {
	    mTimeSincePosSync += timeDelta;
	    
	    if (mTimeSincePosSync >= POS_SYNC_DELAY && this == CharacterController.getInstance().getSelf()) {
	        doSyncPosition();
	        mTimeSincePosSync = 0.0f;
	    }
	    
	    super.act(timeDelta);
	}
	
	private void doSyncPosition() {
		Packet packet = PacketMaker.makeSyncPosition(getRawPosition().x, getRawPosition().y);
        NetworkController.getInstance().sendUnreliable(packet);
	}
}
