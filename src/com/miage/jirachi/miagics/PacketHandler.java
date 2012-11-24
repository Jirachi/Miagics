package com.miage.jirachi.miagics;


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
        Player newPlayer = new Player();
        newPlayer.setNetworkId(data.readLong());
        CharacterController.getInstance().addCharacter(newPlayer);
    }
    
    public static void handlePlayerExisting(BitStream data) {
        Player newPlayer = new Player();
        newPlayer.setNetworkId(data.readLong());
        newPlayer.setPosition(data.readFloat(), data.readFloat());

        CharacterController.getInstance().addCharacter(newPlayer);
    }
    
    public static void handleSyncPosition(BitStream data) {
        Character c = CharacterController.getInstance().getCharacter(data.readLong());
        c.setPosition(data.readInt(), data.readInt());
    }
}
