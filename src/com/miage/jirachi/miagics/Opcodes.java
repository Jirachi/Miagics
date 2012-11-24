package com.miage.jirachi.miagics;

/**
 * Liste des identifiants de paquets réseau.
 * 
 * Il y a un identifiant CMSG pour les requêtes envoyées par le client,
 * et un identifiant SMSG correspondant (si nécessaire) pour la réponse
 * serveur.
 *
 */
public class Opcodes {
	// Le client demande d'être booté :3 (recevoir les informations générales)
    public final static short CMSG_BOOTME = 1;
    
    // Le serveur renvoie les informations générales de la partie
    public final static short SMSG_BOOTME = 2;
    
    // Le serveur signale qu'un joueur s'est connecté
    public final static short SMSG_PLAYER_CONNECT = 3;
    
    // Le serveur signale qu'un joueur était déjà connecté lors du join
    public final static short SMSG_PLAYER_EXISTING = 4;
    
    // Paquets/réponses de mouvement
    public final static short CMSG_MOVE_LEFT    = 5;
    public final static short SMSG_MOVE_LEFT    = 6;
    public final static short CMSG_MOVE_RIGHT   = 7;
    public final static short SMSG_MOVE_RIGHT   = 8;
    public final static short CMSG_MOVE_STOP    = 9;
    public final static short SMSG_MOVE_STOP    = 10;
    
    // Paquet/réponse de saut
    public final static short CMSG_MOVE_JUMP    = 11;
    public final static short SMSG_MOVE_JUMP    = 12;
    
    // Synchro de position
    public final static short CMSG_SYNC_POSITION = 13;
    public final static short SMSG_SYNC_POSITION = 14;
}
