package com.miage.jirachi.miagics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public abstract class SceneObject extends Image {
	protected String mReferenceName;
	
	/**
	 * Constructeur
	 * @param refName Nom de reference de l'objet
	 */
	protected SceneObject(String refName){
		mReferenceName = refName;
	}
	
	/**
     * Definit la position de l'objet
     * @param x Position X
     * @param y Position Y
     */
    public abstract void setPosition(float x, float y);
	
	/**
	 * @return Position de l'objet
	 */
	public abstract Vector2 getPosition();
	
	/**
	 * Definit l'echelle de l'objet
	 */
	public abstract void setScale(float sX, float sY);
}
