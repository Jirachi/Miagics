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

	/**
	 * Renvoie l'instance unique de la classe
	 * @return
	 */
	public static NetworkController getInstance() {
		if (mSingleton == null) {
			mSingleton = new NetworkController();
		}

		return mSingleton;
	}

	/**
	 * Constructeur
	 */
	private NetworkController() {
		mPackets = new ArrayList<Packet>();
	}

	/**
	 * Connecte le controleur a un serveur
	 * @param ip L'IP du serveur
	 * @param port (inutilisé)
	 * @throws IOException Si la connexion échoue
	 */
	public void connect(String ip, int port) throws IOException {
	    // On créé et connecte le client
		mSocket = new Client();
		mSocket.start();
		mSocket.setIdleThreshold(1000000);
		mSocket.setKeepAliveTCP(2000);
		
		try {
		    mSocket.connect(1000, ip, 37153, 35173);
		} catch (Exception e) { }
		
		mSocket.updateReturnTripTime();
		
		// On enregistre les classes serializables pour les transférer
		Kryo kryo = mSocket.getKryo();
		kryo.register(Packet.class);
		kryo.register(byte[].class);

		// On ajoute le listener qui va etre appele quand un packet arrive
		mSocket.addListener(new Listener() {
			public void received (Connection connection, Object object) {
				if (object instanceof Packet) {
				    // On met les paquets en liste car il faut etre dans le thread
				    // de rendu pour interagir avec libgdx.
					mPackets.add((Packet)object);
				}
			}
		});
	}

	/**
	 * Mise a jour du controleur, traite les paquets
	 */
	public void update() {
	    // On traite les paquets qu'on a recu depuis la derniere frame
		for (int i = 0; i < mPackets.size(); i++) {
			Packet packet = mPackets.get(i);
			
			BitStream data = new BitStream(packet.data);
	
			switch (packet.opcode) {
			case Opcodes.SMSG_BOOTME:
				PacketHandler.handleBootMe(data);
				break;
	
			case Opcodes.SMSG_MOVE_LEFT:
			    PacketHandler.handleMoveLeft(data);
			    break;
	
			case Opcodes.SMSG_MOVE_RIGHT:
			    PacketHandler.handleMoveRight(data);
			    break;
	
			case Opcodes.SMSG_MOVE_STOP:
			    PacketHandler.handleMoveStop(data);
			    break;
	
			case Opcodes.SMSG_PLAYER_CONNECT:
			    PacketHandler.handlePlayerConnect(data);
			    break;
	
			case Opcodes.SMSG_PLAYER_EXISTING:
			    PacketHandler.handlePlayerExisting(data);
			    break;
			    
			case Opcodes.SMSG_SET_HEALTH:
				PacketHandler.handleSetHealth(data);
				break;
				
			case Opcodes.SMSG_JUMP:
			    PacketHandler.handleJump(data);
			    break;
			    
			case Opcodes.SMSG_SYNC_POSITION:
			    PacketHandler.handleSetPosition(data);
			    break;
			    
			case Opcodes.SMSG_SPAWN_GAMEOBJECT:
			    PacketHandler.handleSpawnGameObject(data);
			    break;
			    
			case Opcodes.SMSG_GAMEOBJECT_FORCE_POSITION:
			    PacketHandler.handleGameObjectForcePosition(data);
			    break;
			    
			default:
			    Log.e("NetworkController", "Opcode non gere: " + packet.opcode);
			    break;
			}
		}
		
		mPackets.clear();
	}

	/**
	 * Envoie un paquet de facon sure
	 * @param packet
	 */
	public void send(Packet packet) {
		try {
			mSocket.sendTCP(packet);
		} catch (Exception e) { }
	}
	
	/**
	 * Envoie un paquet rapidement mais n'est pas sur d'arriver
	 * @param packet
	 */
	public void sendUnreliable(Packet packet) {
	    try {
	    	mSocket.sendTCP(packet);
	    } catch (Exception e) {}
	}
}
