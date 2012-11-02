package com.miage.jiarchi.miagics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
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
	protected float mMoveSpeed = 20.0f;

	// attributs d'animation
	protected TextureRegion mRegion;
	protected TextureRegion[][] mTmp;
	protected int mOppose;

	// indices courants
	protected int mCurrentColumn;
	protected int mCurrentLine;

	public final static int MOVE_LEFT = -1;
	public final static int MOVE_RIGHT = 1;
	public final static int MOVE_TOP = 3;
	public final static int MOVE_BOTTOM = 4;
	public final static int MOVE_NOT = 0;

	private static final int        FRAME_COLS = 3;         // #1
	private static final int        FRAME_ROWS = 9; 

	/**
	 * Default constructor
	 */
	public Character() {
		mTempsAccumule=0;
		mOppose=1;
		mTexture = new Texture(Gdx.files.internal("animated/droid_from_android.png"));
		mRegion = new TextureRegion(mTexture, 0, 0, 50, 86);
		mPosition = new Vector2(-5,-5);
		mProjection = new Vector3();
		// mBody = new body

		mTmp = TextureRegion.split(mTexture, mTexture.getWidth() / FRAME_COLS, mTexture.getHeight() / FRAME_ROWS);
	}

	/**
	 * DŽfinit dans quel sens le personnage doit se dŽplacer chaque frame
	 * @param direction
	 */
	public void setMoveDirection(int direction) {
	    if (direction != MOVE_NOT && mOppose != direction) {
	        // On tourne les textures puisqu'on va dans l'autre sens
	        flipTextures();
	        mOppose = direction;
	    }
	    
	    mMoveDirection = direction;
	}

	/**
	 * Retourne la position dans la scne de l'objet
	 * @return Position
	 */
	public Vector2 getPosition() {
		return mPosition;
	}

	/**
	 * Dessine le personnage
	 * @param batch
	 * @param cam
	 */
	public void render(SpriteBatch batch, Camera cam) {
		update(Gdx.graphics.getDeltaTime());
		cam.project(mProjection.set(mPosition.x, mPosition.y, 0));
		
		batch.draw(this.mTmp[mCurrentLine][mCurrentColumn], mProjection.x, mProjection.y, (56), 80);
	}

	/**
	 * Met ˆ jour le personnage
	 * @note AppelŽ par render()
	 * @param timeDelta
	 */
	public void update(float timeDelta) {
		mTempsAccumule += timeDelta;
		
		switch (mMoveDirection) {
		case MOVE_LEFT: 
			this.mPosition.x = this.mPosition.x - timeDelta * mMoveSpeed;
			mCurrentLine = 1;
			break;

		case MOVE_RIGHT: 
			this.mPosition.x = this.mPosition.x + timeDelta * mMoveSpeed;
			mCurrentLine = 1;
			break;

		case MOVE_NOT: 
			mCurrentLine = 0;
			break;
		}

		// Mise ˆ jour de l'animation
		if (mTempsAccumule > 0.1f) {
            mCurrentColumn++;
            mTempsAccumule = 0;
            
            if (mCurrentColumn == FRAME_COLS) {
                mCurrentColumn = 0;
            }
        }

	}

	/**
	 * Mirroir horizontal des textures
	 */
	public void flipTextures() {
		for (int i = 0; i < mTmp.length; i++) {
			for (int j = 0; j < mTmp[i].length; j++) {
				this.mTmp[i][j].flip(true, false);
			}
		}
		
	}
}
