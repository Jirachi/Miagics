package com.miage.jiarchi.miagics;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Character {
    protected Texture mTexture;
    protected int mMoveDirection;
    protected Vector2 mPosition;

    public final static int MOVE_LEFT = 1;
    public final static int MOVE_RIGHT = 2;
    public final static int MOVE_TOP = 3;
    public final static int MOVE_BOTTOM = 4;
    
    public Character() {
        mTexture = new Texture(Gdx.files.internal("animated/droid_from_android.png"));
        // mBody = new body
    }
    
    public void setMoveDirection(int direction){
    	mMoveDirection=direction;
    	
    	switch (mMoveDirection){
    	case MOVE_LEFT : this.mPosition.x=this.mPosition.x-10;
    	case MOVE_RIGHT : this.mPosition.x=this.mPosition.x+10;/*
    	case MOVE_TOP : this.pos.y=this.pos.y+10;
    	case MOVE_BOTTOM : this.pos.y=this.pos.y-10;*/
    	}
    	
    }
    
    public void render(SpriteBatch batch, Camera cam) {
    	update(Gdx.graphics.getDeltaTime());
        batch.draw(mTexture, 10, 20, 110, 130);
    }
    
    public void update(float timeDelta){
    	mPosition.x+= timeDelta*50;
    	
    	
    	
    }
    
    
}
