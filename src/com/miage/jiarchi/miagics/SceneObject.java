package com.miage.jiarchi.miagics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class SceneObject {
	protected String mReferenceName;
	protected Vector2 mPosition;
	protected float mAngle;
	protected SpriteBatch mBatch;
	protected Camera mCam;
	
	public SceneObject(String refName){
		mReferenceName = refName;
	}
	
	public void setPosition(Vector2 pos){
		mPosition = pos;
	}
	
	public void setAngle(float angle){
		mAngle = angle;
	}
	
	public abstract void render(SpriteBatch batch, Camera cam);
}
