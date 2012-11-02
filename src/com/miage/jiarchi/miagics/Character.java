package com.miage.jiarchi.miagics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Character {
	protected Texture mTexture;
	protected int mMoveDirection;
	protected Vector2 mPosition;
	protected Vector3 mProjection;

	protected float mTempsAccumule;
	protected float mMoveSpeed = 20.0f;
	
	// propriŽtŽs physiques
	protected Body mPhysicsBody;
	protected Fixture mPhysicsChestFixture;
	protected Fixture mPhysicsSensorFixture;

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
		
		buildPhysicsBody();

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
	protected void flipTextures() {
		for (int i = 0; i < mTmp.length; i++) {
			for (int j = 0; j < mTmp[i].length; j++) {
				this.mTmp[i][j].flip(true, false);
			}
		}
	}
	
	/**
	 * CrŽŽe les propriŽtŽs physiques du personnage
	 */
	protected void buildPhysicsBody() {
	    // DŽfinition de l'ensemble du corps
	    BodyDef def = new BodyDef();
	    
	    // C'est un corps dynamique (qui bouge :3)
        def.type = BodyType.DynamicBody;
        
        // On crŽŽ le conteneur
        mPhysicsBody = PhysicsController.getInstance().getWorld().createBody(def);
 
        // On dŽfinit ensuite deux composantes du corps : une boite pour le torse,
        // et un cercle pour les pieds (afin d'Žviter de rester coincŽ face ˆ des petits
        // obstacles).
        PolygonShape poly = new PolygonShape();
        
        // TODO: Taille de la boite variable en fonction de la taille rŽelle
        // du personnage
        poly.setAsBox(0.45f, 1.4f);
        mPhysicsChestFixture = mPhysicsBody.createFixture(poly, 1);
        
        // On libre les ressources
        poly.dispose();         
 
        // Mme chose pour les pieds :3
        CircleShape circle = new CircleShape();     
        circle.setRadius(0.45f);
        
        // On place les pieds en dessous du torse
        circle.setPosition(new Vector2(0, -1.4f));
        
        mPhysicsSensorFixture = mPhysicsBody.createFixture(circle, 0);     
        circle.dispose();       
 
        // On active la dŽtection continue des collisions
        // Cela permet d'tre sžr que le joueur ne va pas traverser des murs
        // ou autres si il se dŽplace trs vite.
        mPhysicsBody.setBullet(true);
        
        // On empche le personnage de faire des rotations impromptues
        mPhysicsBody.setFixedRotation(true);
	}
}
