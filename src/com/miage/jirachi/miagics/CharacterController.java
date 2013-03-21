package com.miage.jirachi.miagics;


import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.miage.jirachi.resource.ResourceAnimated;
import com.miage.jirachi.resource.ResourceManager;

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
	 * Créée un joueur
	 */
	public static Player createCharacter(String texName) {
		Texture persoTex = new Texture(Gdx.files.internal("animated/" + texName + ".png"));
        ResourceAnimated persoRes = (ResourceAnimated)ResourceManager.getInstance().getResource("animated/" + texName + ".rs");
        TextureRegion persoRegions[][] = TextureRegion.split(persoTex, persoTex.getWidth() / persoRes.columns, persoTex.getHeight() / persoRes.lines);
        
        Player newPlayer = new Player(persoRes, persoRegions);
        CharacterController.getInstance().addCharacter(newPlayer);
        
        return newPlayer;
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
