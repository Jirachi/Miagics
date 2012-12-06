package com.miage.jirachi.miagics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.miage.jirachi.resource.ResourceAnimated;

public class Player extends Character {
    // == Attributs
    // Temps entre chaque synchronisation de position avec le serveur
    public final static float POS_SYNC_DELAY = 0.5f;
    
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
		}
		
		super.setMoveDirection(direction);
	}
	
	@Override
	public void act(float timeDelta) {
	    if (mTimeSincePosSync >= POS_SYNC_DELAY) {
	        Packet packet = PacketMaker.makeSyncPosition(getPosition().x, getPosition().y);
	        NetworkController.getInstance().sendUnreliable(packet);
	        mTimeSincePosSync = 0.0f;
	    }
	    
	    super.act(timeDelta);
	}
}
