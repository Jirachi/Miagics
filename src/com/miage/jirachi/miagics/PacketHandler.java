package com.miage.jirachi.miagics;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.miage.jirachi.resource.ResourceAnimated;
import com.miage.jirachi.resource.ResourceManager;


public class PacketHandler {
    // SMSG_BOOT_ME
    public static void handleBootMe(BitStream data) {
        long myNetworkId = data.readLong();
        CharacterController.getInstance().getSelf().setNetworkId(myNetworkId);
    }
    
    // SMSG_MOVE_LEFT
    public static void handleMoveLeft(BitStream data) {
        long networkId = data.readLong();
        Character c = CharacterController.getInstance().getCharacter(networkId);

        if (c != null) {
            c.setMoveDirection(Character.MOVE_LEFT);
        }
    }
    
    // SMSG_MOVE_RIGHT
    public static void handleMoveRight(BitStream data) {
        long networkId = data.readLong();
        Character c = CharacterController.getInstance().getCharacter(networkId);

        if (c != null) {
            c.setMoveDirection(Character.MOVE_RIGHT);
        }
    }
    
    // SMSG_MOVE_STOP
    public static void handleMoveStop(BitStream data) {
        long networkId = data.readLong();
        Character c = CharacterController.getInstance().getCharacter(networkId);

        if (c != null) {
            c.setMoveDirection(Character.MOVE_NOT);
        }
    }
    
    // SMSG_PLAYER_CONNECT
    public static void handlePlayerConnect(BitStream data) {
        Texture persoTex = new Texture(Gdx.files.internal("animated/buffallo.png"));
        TextureRegion persoRegions[][] = TextureRegion.split(persoTex, persoTex.getWidth() / 3, persoTex.getHeight() / 9);
        
        Player newPlayer = new Player((ResourceAnimated)ResourceManager.getInstance().getResource("animated/buffallo.rs"), persoRegions);
        newPlayer.setNetworkId(data.readLong());
        CharacterController.getInstance().addCharacter(newPlayer);
    }
    
    // SMSG_PLAYER_EXISTING
    public static void handlePlayerExisting(BitStream data) {
        Texture persoTex = new Texture(Gdx.files.internal("animated/buffallo.png"));
        TextureRegion persoRegions[][] = TextureRegion.split(persoTex, persoTex.getWidth() / 3, persoTex.getHeight() / 9);
        
        Player newPlayer = new Player((ResourceAnimated)ResourceManager.getInstance().getResource("animated/buffallo.rs"), persoRegions);
        newPlayer.setNetworkId(data.readLong());
        newPlayer.setPosition(data.readFloat(), data.readFloat());

        CharacterController.getInstance().addCharacter(newPlayer);
    }
    
    // SMSG_SYNC_POSITION
    public static void handleSyncPosition(BitStream data) {
        Character c = CharacterController.getInstance().getCharacter(data.readLong());
        c.setPosition(data.readInt(), data.readInt());
    }
    
    // SMSG_SET_HEALTH
    public static void handleSetHealth(BitStream data){
    	long networkId = data.readLong();
    	Character c = CharacterController.getInstance().getCharacter(networkId);
    	if(c != null){
    		c.setHealth(data.readInt());
    	}
    }
    
    // SMSG_HANDLE_JUMP
    public static void handleJump(BitStream data) {
        long networkId = data.readLong();
        Character c = CharacterController.getInstance().getCharacter(networkId);
        if(c != null){
            c.jump();
        }
    }
    
    // SMSG_SET_POSITION
    public static void handleSetPosition(BitStream data) {
        long networkId = data.readLong();
        float x = data.readFloat();
        float y = data.readFloat();
        
        Character c = CharacterController.getInstance().getCharacter(networkId);
        if(c != null){
            c.setPosition(x, y);
        }
    }
    
    // SMSG_SPAWN_GAMEOBJECT
    public static void handleSpawnGameObject(BitStream data) {
        long networkId = data.readLong();
        float x = data.readFloat(),
                y = data.readFloat();
        String resource = data.readString();
        int physicsType = data.readInt();
        
        // On cree l'objet
        GameObject go = new GameObject(networkId, resource);
        go.setPosition(x, y);
        
        switch (physicsType) {
        case GameObject.PHYSICS_TYPE_DYNAMIC:
            go.getPhysicsBody().setType(BodyType.DynamicBody);
            break;
            
        case GameObject.PHYSICS_TYPE_NO_COLLISION:
            Log.e("PacketHandler", "PHYSICS_TYPE_NO_COLLISION IS NOT HANDLED YET!!!");
            break;
            
        case GameObject.PHYSICS_TYPE_STATIC:
            go.getPhysicsBody().setType(BodyType.StaticBody);
            break;
            
        default:
            Log.e("PacketHandler", "PhysicsType " + physicsType + " unknown!");
            break;
        }
        
        // On ajoute l'objet
        GameObjectController.getInstance().addObject(go);
    }
    
    // SMSG_GAMEOBJECT_FORCE_POSITION
    public static void handleGameObjectForcePosition(BitStream data) {
        long networkId = data.readLong();
        float x = data.readFloat();
        float y = data.readFloat();
        
        GameObject obj = GameObjectController.getInstance().getGameObject(networkId);
        obj.setPosition(x, y);
    }
    
    // SMSG_GAMEOBJECT_ANIMATE
    public static void handleGameObjectAnimate(BitStream data) {
        long networkId = data.readLong();
        String animation = data.readString();
        
        GameObject obj = GameObjectController.getInstance().getGameObject(networkId);
        obj.getAnimationProvider().playAnimation(animation);
    }
    
    // SMSG_GAMEOBJECT_MOVE
    public static void handleGameObjectMove(BitStream data) {
        long networkId = data.readLong();
        float targetX = data.readFloat();
        float targetY = data.readFloat();
        float time = data.readFloat();
        
        GameObject obj = GameObjectController.getInstance().getGameObject(networkId);
        obj.moveTo(targetX, targetY, time);
    }
}
