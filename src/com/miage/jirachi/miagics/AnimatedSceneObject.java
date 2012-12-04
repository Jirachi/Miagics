package com.miage.jirachi.miagics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimatedSceneObject extends StaticSceneObject {
	protected Texture mTexture;
	
	protected TextureRegion mRegion;
	protected float mTempsAccumule;
	
	public AnimatedSceneObject(String refName, String path) {
		super(refName, path);
		
		mTexture = new Texture(Gdx.files.internal(path));
		mRegion = new TextureRegion(mTexture, 0, 0, 50, 86);
		
		setRegion(mRegion);
	}
	
	@Override
	public void act(float timeDelta) {
		mTempsAccumule += timeDelta;
		
		// TODO: Animation (utiliser le framework, cf redmine)
	}
}
