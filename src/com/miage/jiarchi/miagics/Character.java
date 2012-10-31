package com.miage.jiarchi.miagics;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Character {
    protected Texture mTexture;
    protected int mMoveDirection;
    protected Vector2 mPosition;
    protected Vector3 mProjection;

    public final static int MOVE_LEFT = 1;
    public final static int MOVE_RIGHT = 2;
    public final static int MOVE_TOP = 3;
    public final static int MOVE_BOTTOM = 4;
    
    public Character() {
        mTexture = new Texture(Gdx.files.internal("animated/droid_from_android.png"));
        mPosition = new Vector2();
        mProjection = new Vector3();
        // mBody = new body
    }
    
    public void setMoveDirection(int direction){
    	mMoveDirection=direction;
    	
    	
    	
    }
    
    public void render(SpriteBatch batch, Camera cam) {
    	update(Gdx.graphics.getDeltaTime());
    	cam.project(mProjection.set(mPosition.x, mPosition.y, 0));
        batch.draw(mTexture, mProjection.x, mProjection.y, 110, 130);
    }
    
    public void update(float timeDelta){
    	
        switch (mMoveDirection){
        case MOVE_LEFT: 
            this.mPosition.x = this.mPosition.x - timeDelta * 20.0f;
            break;
            
        case MOVE_RIGHT: 
            this.mPosition.x = this.mPosition.x + timeDelta * 20.0f;
            break;
        /*
        case MOVE_TOP : this.pos.y=this.pos.y+10;
        case MOVE_BOTTOM : this.pos.y=this.pos.y-10;*/
        }
      
    	
    	
    	
    }
    
    
}
