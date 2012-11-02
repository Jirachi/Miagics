package com.miage.jiarchi.miagics;


import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
	 * @return Le joueur en lui-m�me
	 */
	public Player getSelf() {
		return mSelf;
	}
	
	/**
	 * D�finit le joueur (soi-m�me)
	 * @param p Moi :D
	 */
	public void setSelf(Player p) {
	    mSelf = p;
	}
	
	/**
	 * Effectue le rendu de tous les personnages
	 * @param batch
	 * @param camera
	 */
	public void renderAll(SpriteBatch batch, Camera camera) {
		for(int i=0;i<mCharacters.size();i++){
			mCharacters.get(i).render(batch, camera);
		}
	}
	
	/**
	 * Ajoute un personnage au contr�leur
	 * @param chara
	 */
	public void addCharacter(Character chara) {
		mCharacters.add(chara);
	}	
	
}
