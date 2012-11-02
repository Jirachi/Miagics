package com.miage.jiarchi.miagics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StaticSceneObject extends SceneObject {
	protected Texture mTexture;
	
	public StaticSceneObject(String refName, String path) {
		super(refName);
		mTexture = new Texture(Gdx.files.internal(path));
	}
	

	public void render(SpriteBatch batch, Camera cam){
		cam.project(mProjection.set(mPosition.x, mPosition.y, 0));
		batch.draw(mTexture, mProjection.x, mProjection.y , 110, 130);
	}

}
