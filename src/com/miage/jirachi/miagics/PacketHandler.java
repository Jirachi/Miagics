package com.miage.jirachi.miagics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.miage.jirachi.resource.ResourceAnimated;
import com.miage.jirachi.resource.ResourceManager;


public class PacketHandler {
    public static void handleBootMe(BitStream data) {
        long myNetworkId = data.readLong();
        CharacterController.getInstance().getSelf().setNetworkId(myNetworkId);
    }
    
    public static void handleMoveLeft(BitStream data) {
        long networkId = data.readLong();
        Character c = CharacterController.getInstance().getCharacter(networkId);

        if (c != null) {
            c.setMoveDirection(Character.MOVE_LEFT);
        }
    }
    
    public static void handleMoveRight(BitStream data) {
        long networkId = data.readLong();
        Character c = CharacterController.getInstance().getCharacter(networkId);

        if (c != null) {
            c.setMoveDirection(Character.MOVE_RIGHT);
        }
    }
    
    public static void handleMoveStop(BitStream data) {
        long networkId = data.readLong();
        Character c = CharacterController.getInstance().getCharacter(networkId);

        if (c != null) {
            c.setMoveDirection(Character.MOVE_NOT);
        }
    }
    
    public static void handlePlayerConnect(BitStream data) {
        Texture persoTex = new Texture(Gdx.files.internal("animated/droid_from_android.png"));
        TextureRegion persoRegions[][] = TextureRegion.split(persoTex, persoTex.getWidth() / 3, persoTex.getHeight() / 9);
        
        Player newPlayer = new Player((ResourceAnimated)ResourceManager.getInstance().getResource("animated/droid_from_android.rs"), persoRegions);
        newPlayer.setNetworkId(data.readLong());
        CharacterController.getInstance().addCharacter(newPlayer);
    }
    
    public static void handlePlayerExisting(BitStream data) {
        Texture persoTex = new Texture(Gdx.files.internal("animated/droid_from_android.png"));
        TextureRegion persoRegions[][] = TextureRegion.split(persoTex, persoTex.getWidth() / 3, persoTex.getHeight() / 9);
        
        Player newPlayer = new Player((ResourceAnimated)ResourceManager.getInstance().getResource("animated/droid_from_android.rs"), persoRegions);
        newPlayer.setNetworkId(data.readLong());
        newPlayer.setPosition(data.readFloat(), data.readFloat());

        CharacterController.getInstance().addCharacter(newPlayer);
    }
    
    public static void handleSyncPosition(BitStream data) {
        Character c = CharacterController.getInstance().getCharacter(data.readLong());
        c.setPosition(data.readInt(), data.readInt());
    }
}
