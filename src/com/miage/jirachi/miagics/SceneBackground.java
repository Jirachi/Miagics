package com.miage.jirachi.miagics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class SceneBackground {
	
	protected Texture mBackgroundTexture;
	protected Vector2 testCam;
	protected Vector2 positionPrecedente;
	protected Vector3 mProjection;
	
	public SceneBackground(String path) {
		mBackgroundTexture = new Texture(Gdx.files.internal(path));
		testCam = new Vector2();
		testCam.x=0;
		testCam.y=0;
		positionPrecedente = new Vector2();
		positionPrecedente.x=0;
		positionPrecedente.y=0;
		//positionPrecedente = new Camera();
		
	}
	
	
public void render(SpriteBatch batch, Camera cam){
		
			mProjection = new Vector3();
			//testCam.x= testCam.x - (cam.position.x - positionPrecedente.x);
			cam.project(mProjection.set(testCam.x, testCam.y,0));
			
			positionPrecedente.x = cam.position.x;
	
		
		
		
		//batch.draw(objectSprite, mProjection.x, mProjection.y,0.0f,0.0f,256,256,1.0f,1.0f, objectModel.getAngle() * 3.1415f);
		//objectSprite.draw(batch)	;
		batch.draw(mBackgroundTexture, mProjection.x, 0);
		
	}


}
