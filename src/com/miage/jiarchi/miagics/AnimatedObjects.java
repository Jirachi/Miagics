package com.miage.jiarchi.miagics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class AnimatedObjects extends StaticSceneObject {
	protected Texture mTexture;
	protected Vector3 mProjection;
	protected TextureRegion mRegion;
	protected TextureRegion[][] mTmp;
	protected float mTempsAccumule;
	protected int mCurrentColumn;
	protected int mCurrentLine;
	
	private static final int FRAME_COLS = 3;
	private static final int FRAME_ROWS = 9; 
	
	public AnimatedObjects(String refName, String path) {
		super(refName, path);
		mProjection = new Vector3();
		mTexture = new Texture(Gdx.files.internal(path));
		mRegion = new TextureRegion(mTexture, 0, 0, 50, 86);
		
		mTmp = TextureRegion.split(mTexture, mTexture.getWidth() / FRAME_COLS, mTexture.getHeight() / FRAME_ROWS);
		mCurrentLine = 0;
	}
	
	public void render(SpriteBatch batch, Camera cam) {
		update(Gdx.graphics.getDeltaTime());
		cam.project(mProjection.set(mPosition.x, mPosition.y, 0));
		
		batch.draw(mTmp[mCurrentLine][mCurrentColumn], mProjection.x, mProjection.y, 120, 130);
	}
	
	public void update(float deltaTime){
		mTempsAccumule += deltaTime;
		if (mTempsAccumule > 0.1f) {
            mCurrentColumn++;
            mTempsAccumule = 0;
            
            if (mCurrentColumn == FRAME_COLS) {
                mCurrentColumn = 0;
            }
		
		mTempsAccumule = 0;
		}

	}
}
