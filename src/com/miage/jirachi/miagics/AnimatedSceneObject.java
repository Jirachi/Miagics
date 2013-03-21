package com.miage.jirachi.miagics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.miage.jirachi.resource.ResourceAnimated;

public class AnimatedSceneObject extends StaticSceneObject {
	protected AnimationProvider mAnimations;
	protected TextureRegion[][] mTextureRegions;
	protected ResourceAnimated mResourceAnimated;
	
	public AnimatedSceneObject(String refName, String path) {
		super(refName, path);
		mResourceAnimated = (ResourceAnimated)mResource;
		
		mTextureRegions = TextureRegion.split(mObjectTexture, 
		        mObjectTexture.getWidth() / mResourceAnimated.columns, 
		        mObjectTexture.getHeight() / mResourceAnimated.lines);
		
		mAnimations = new AnimationProvider((ResourceAnimated)mResource, mTextureRegions);
	}
	
	@Override
	public void act(float timeDelta) {
		this.setDrawable(new TextureRegionDrawable(mAnimations.getKeyFrame(timeDelta)));
	}
	
	/**
	 * Retourne l'instance du framework animation
	 */
	public AnimationProvider getAnimationProvider() {
	    return mAnimations;
	}
}
