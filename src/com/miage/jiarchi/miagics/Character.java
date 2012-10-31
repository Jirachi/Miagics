package com.miage.jiarchi.miagics;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Character {
    protected Texture mTexture;
    protected int mMoveDirection;
    protected Vector2 mPosition;
    protected Vector3 mProjection;
    
    protected float mTempsAccumule;
    
    //attributs d'animation
    protected TextureRegion region;
    protected TextureRegion[]        walkFrames; 
    protected TextureRegion[][] tmp;
    //indices courants
    protected int currentColumn;
    protected int currentLine;
    
    
    
    public final static int MOVE_LEFT = 1;
    public final static int MOVE_RIGHT = 2;
    public final static int MOVE_TOP = 3;
    public final static int MOVE_BOTTOM = 4;
    public final static int MOVE_NOT = 0;
    
    private static final int        FRAME_COLS = 3;         // #1
    private static final int        FRAME_ROWS = 9; 
    
    float stateTime;
    
    public Character() {
    	mTempsAccumule=0;
        mTexture = new Texture(Gdx.files.internal("animated/droid_from_android.png"));
        region = new TextureRegion(mTexture, 0, 0, 50, 86);
        mPosition = new Vector2(-5,-5);
        mProjection = new Vector3();
        // mBody = new body
        
         tmp = TextureRegion.split(mTexture, mTexture.getWidth() / FRAME_COLS, mTexture.getHeight() / FRAME_ROWS);                                // #10
        		                walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        		                int index = 0;
        		                for (int i = 0; i < FRAME_ROWS; i++) {
        		                        for (int j = 0; j < FRAME_COLS; j++) {
        		                                walkFrames[index++] = tmp[i][j];
        		                        }
        		                }
        		                
    }
    
    public void setMoveDirection(int direction){
    	mMoveDirection=direction;
    }
    
    
    
    
    public Vector2 getPosition() {
		return mPosition;
	}

	public void render(SpriteBatch batch, Camera cam) {
    	update(Gdx.graphics.getDeltaTime());
    	cam.project(mProjection.set(mPosition.x, mPosition.y, 0));
    	
        batch.draw(this.tmp[currentLine][currentColumn], mProjection.x, mProjection.y, 50, 86);
    }
    
    public void update(float timeDelta){
    	mTempsAccumule+=timeDelta;
        switch (mMoveDirection){
        case MOVE_LEFT: 
            this.mPosition.x = this.mPosition.x - timeDelta * 20.0f;
            currentLine = 3;
            if(mTempsAccumule>0.1f){
            	currentColumn++;
            	mTempsAccumule=0;
        		if(currentColumn==FRAME_COLS){
        			currentColumn=0;
        		}
            }
            break;
            
        case MOVE_RIGHT: 
            this.mPosition.x = this.mPosition.x + timeDelta * 20.0f;
            currentLine = 1;
            if(mTempsAccumule>0.1f){
            	currentColumn++;
            	mTempsAccumule=0;
            	if(currentColumn==FRAME_COLS){
            		currentColumn=0;
            	}
            }
            break;
            
        case MOVE_NOT: 
            //this.mPosition.x = this.mPosition.x + timeDelta * 20.0f;
            currentLine = 0;
            break;
        /*
        case MOVE_TOP : this.pos.y=this.pos.y+10;
        case MOVE_BOTTOM : this.pos.y=this.pos.y-10;*/
        }
      
    	
    	
    	
    }
    
    
}
