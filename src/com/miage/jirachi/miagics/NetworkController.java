package com.miage.jirachi.miagics;

import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class NetworkController {
	private static NetworkController mSingleton = null;
	private Client mSocket;
	private ArrayList<Packet> mPackets;

	public static NetworkController getInstance() {
		if (mSingleton == null) {
			mSingleton = new NetworkController();
		}

		return mSingleton;
	}

	public NetworkController() {
		mPackets = new ArrayList<Packet>();
	}

	public void connect(String ip, int port) throws IOException {
		mSocket = new Client();
		mSocket.start();
		mSocket.connect(1000, ip, 37153, 35173);
		
		Kryo kryo = mSocket.getKryo();
		kryo.register(Packet.class);
		kryo.register(byte[].class);

		mSocket.addListener(new Listener() {
			public void received (Connection connection, Object object) {
				if (object instanceof Packet) {
					mPackets.add((Packet)object);
				}
			}
		});
	}

	public void update() {
		for (int i = 0; i < mPackets.size(); i++) {
			Packet packet = mPackets.get(i);
			
			BitStream data = new BitStream(packet.data);
	
			switch (packet.opcode) {
			case Opcodes.SMSG_BOOTME:
				long myNetworkId = data.readLong();
				CharacterController.getInstance().getSelf().setNetworkId(myNetworkId);
				break;
	
			case Opcodes.SMSG_MOVE_LEFT:
			{
				long networkId = data.readLong();
				Character c = CharacterController.getInstance().getCharacter(networkId);
	
				if (c == null) {
					Log.e("Reseau", "Joueur introuvable pour maj move");
				} else {
					c.setMoveDirection(Character.MOVE_LEFT);
				}
			}
			break;
	
			case Opcodes.SMSG_MOVE_RIGHT:
			{
				long networkId = data.readLong();
				Character c = CharacterController.getInstance().getCharacter(networkId);
	
				if (c == null) {
					Log.e("Reseau", "Joueur introuvable pour maj move");
				} else {
					c.setMoveDirection(Character.MOVE_RIGHT);
				}
			}
			break;
	
			case Opcodes.SMSG_MOVE_STOP:
			{
				long networkId = data.readLong();
				Character c = CharacterController.getInstance().getCharacter(networkId);
	
				if (c == null) {
					Log.e("Reseau", "Joueur introuvable pour maj move");
				} else {
					c.setMoveDirection(Character.MOVE_NOT);
				}
			}
			break;
	
			case Opcodes.SMSG_PLAYER_CONNECT:
			{
				Player newPlayer = new Player();
				newPlayer.setNetworkId(data.readLong());
				CharacterController.getInstance().addCharacter(newPlayer);
			}
			break;
	
			case Opcodes.SMSG_PLAYER_EXISTING:
			{
				Player newPlayer = new Player();
				newPlayer.setNetworkId(data.readLong());
				newPlayer.setPosition(data.readInt(), data.readInt());
	
				CharacterController.getInstance().addCharacter(newPlayer);
			}
			break;
			}
		}
		
		mPackets.clear();
	}

	public void send(Packet packet) {
		mSocket.sendTCP(packet);
	}
}
