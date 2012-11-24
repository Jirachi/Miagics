package com.miage.jirachi.miagics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class SceneBackground {
	
	protected Texture mBackgroundTexture;
	protected Vector2 mPosBGLeft;
	protected Vector2 mPosBGRight;
	protected Vector3 mProjectionL;
	protected Vector3 mProjectionR;
	protected float mProfondeur;
	
	public SceneBackground(String path) {
		mBackgroundTexture = new Texture(Gdx.files.internal(path));
		mPosBGRight = new Vector2(0,-200.5f);
		mPosBGLeft = new Vector2(-mBackgroundTexture.getWidth(),-200.5f);
		mProfondeur = 0;
	}
	
	
public void render(SpriteBatch batch, Camera cam){
		
			mProjectionL = new Vector3();
			mProjectionR = new Vector3();
			//testCam.x= testCam.x - (cam.position.x - positionPrecedente.x);
			cam.project(mProjectionR.set(mPosBGRight.x, mPosBGRight.y,0));
			cam.project(mProjectionL.set(mPosBGLeft.x, mPosBGLeft.y,0));
			//mProjectionL.x=mProjectionR.x-mBackgroundTexture.getWidth();
		
		if(mProjectionL.x>0){
			mPosBGRight.x=mPosBGLeft.x;
			mPosBGLeft.x=mPosBGRight.x-mBackgroundTexture.getWidth();
			//cam.project(mProjectionR.set(mPosBGRight.x, mPosBGRight.y,0));
		}
		if(mProjectionR.x<0){
			mPosBGRight.x=mPosBGLeft.x;
			mPosBGLeft.x=mPosBGRight.x+mBackgroundTexture.getWidth();
			//cam.project(mProjectionR.set(mPosBGRight.x, mPosBGRight.y,0));
		}
		
		
		//batch.draw(objectSprite, mProjection.x, mProjection.y,0.0f,0.0f,256,256,1.0f,1.0f, objectModel.getAngle() * 3.1415f);
		//objectSprite.draw(batch)	;
		batch.draw(mBackgroundTexture, mProjectionR.x, mProjectionR.y,mBackgroundTexture.getWidth()*MainActivity.PPX,mBackgroundTexture.getHeight()*MainActivity.PPY);
		batch.draw(mBackgroundTexture, mProjectionL.x, mProjectionL.y,mBackgroundTexture.getWidth()*MainActivity.PPX,mBackgroundTexture.getHeight()*MainActivity.PPY);
	}


}
