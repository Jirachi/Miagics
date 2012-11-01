package com.miage.jiarchi.miagics;


import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CharacterController {
	protected static CharacterController mSingleton;
	protected List<Character> mCharacters;
	protected Player mSelf;
	
	
	public CharacterController(){
		mCharacters = new ArrayList<Character>();
		mSingleton=null;
		mSelf=null;
		
	}
	
	
	public Player getSelf(){
		return mSelf;
	}
	
	public void renderAll(SpriteBatch mBatch, Camera mCamera){
		for(int i=0;i<mCharacters.size();i++){
			mCharacters.get(i).render(mBatch, mCamera);
		}
	}
	
	public void addCharacter(Character mChara){
		mCharacters.add(mChara);		
	}	
	
}
