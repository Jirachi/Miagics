package com.miage.jirachi.miagics;


import java.util.ArrayList;
import java.util.List;

public class CharacterController {
	private static CharacterController mSingleton = null;
	
	protected List<Character> mCharacters;
	protected Player mSelf;
	
	/**
	 * Renvoie l'instance unique de la classe
	 * @return
	 */
	public static CharacterController getInstance() {
	    if (mSingleton == null) {
	        mSingleton = new CharacterController();
	    }
	    
	    return mSingleton;
	}
	
	/**
	 * Constructeur
	 */
	private CharacterController(){
		mCharacters = new ArrayList<Character>();
		mSingleton=null;
		mSelf=null;
		
	}

	/**
	 * @return Le joueur en lui-même
	 */
	public Player getSelf() {
		return mSelf;
	}
	
	/**
	 * DŽfinit le joueur (soi-même)
	 * @param p Moi :D
	 */
	public void setSelf(Player p) {
	    addCharacter(p);
		mSelf = p;
	}
	
	/**
	 * Retourne le personnage ayant l'id réseau spécifié
	 */
	public Character getCharacter(long id) {
		for (int i = 0; i < mCharacters.size(); i++) {
			Character c = mCharacters.get(i);
			if (c.getNetworkId() == id)
				return c;
		}
		
		return null;
	}
	
	/**
	 * ---- Les mises a jour sont faites dans Character.act(..) - mais il se peut qu'on jour on ait
	 * des actions communes a faire sur tous les personnages.
	 * @param batch
	 * @param camera
	 */
	public void update() {
	}
	
	/**
	 * Ajoute un personnage au contrôleur
	 * @param chara
	 */
	public void addCharacter(Character chara) {
		mCharacters.add(chara);
		MainActivity.mStage.addActor(chara);
	}	
	
}
