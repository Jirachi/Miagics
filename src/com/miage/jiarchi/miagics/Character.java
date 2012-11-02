package com.miage.jiarchi.miagics;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.WorldManifold;

public class Character {
	protected Texture mTexture;
	protected int mMoveDirection;
	protected Vector2 mPosition;
	protected Vector3 mProjection;

	protected float mTempsAccumule;
	protected float mMoveSpeed = 20.0f;
	
	// propri�t�s physiques
	protected Body mPhysicsBody;
	protected Fixture mPhysicsChestFixture;
	protected Fixture mPhysicsSensorFixture;
	protected long mLastGroundTime;
	protected float mStillTime;

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
	 * D�finit dans quel sens le personnage doit se d�placer chaque frame
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
	 * Retourne la position dans la sc�ne de l'objet
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
		mPosition = mPhysicsBody.getPosition();
		cam.project(mProjection.set(mPosition.x, mPosition.y, 0));
		
		batch.draw(this.mTmp[mCurrentLine][mCurrentColumn], mProjection.x, mProjection.y, (56), 80);
	}

	/**
	 * Met � jour le personnage
	 * @note Appel� par render()
	 * @param timeDelta
	 */
	public void update(float timeDelta) {
		mTempsAccumule += timeDelta;
		
		switch (mMoveDirection) {
		case MOVE_LEFT: 
			mCurrentLine = 1;
			break;

		case MOVE_RIGHT:
			mCurrentLine = 1;
			break;

		case MOVE_NOT: 
			mCurrentLine = 0;
			break;
		}

		// Mise � jour de l'animation
		if (mTempsAccumule > 0.1f) {
            mCurrentColumn++;
            mTempsAccumule = 0;
            
            if (mCurrentColumn == FRAME_COLS) {
                mCurrentColumn = 0;
            }
        }

		// Mise � jour des propri�t�s physiques
        Vector2 vel = mPhysicsBody.getLinearVelocity();
        Vector2 pos = mPhysicsBody.getPosition();     
        boolean grounded = isTouchingGround();
        
        // On estime �tre sur le sol si on le touche, ou si on l'a
        // r�cemment touch� pour compenser le manque de pr�cision
        if (grounded) {
            mLastGroundTime = System.nanoTime();
        } else {
            if (System.nanoTime() - mLastGroundTime < 100000000) {
                grounded = true;
            }
        }
 
        // On limite la vitesse de mouvement � la vitesse max
        if (Math.abs(vel.x) > mMoveSpeed) {            
            vel.x = Math.signum(vel.x) * mMoveSpeed;
            mPhysicsBody.setLinearVelocity(vel.x, vel.y);
        }
 
        // On calcule le temps qu'on a pass� sans bouger, et on applique l'inertie (90%)
        if (mMoveDirection == MOVE_NOT) {         
            mStillTime += Gdx.graphics.getDeltaTime();
            mPhysicsBody.setLinearVelocity(vel.x * 0.9f, vel.y);
        }
        else { 
            mStillTime = 0;
        }           
 
        // On retire la friction si on saute
        if (!grounded) {         
            mPhysicsChestFixture.setFriction(0f);
            mPhysicsSensorFixture.setFriction(0f);            
        } else {
            // Si on ne bouge pas et si on est arr�t� depuis 200ms, on rapplique les frottements
            // au sol
            if(mMoveDirection == MOVE_NOT && mStillTime > 0.2) {
                mPhysicsChestFixture.setFriction(100f);
                mPhysicsSensorFixture.setFriction(100f);
            }
            else {
                // Sinon on garde un frottement minimal
                mPhysicsChestFixture.setFriction(0.2f);
                mPhysicsSensorFixture.setFriction(0.2f);
            }
        }       
 
        // Si on va a gauche et qu'on est pas d�j� � la vitesse max
        if(mMoveDirection == MOVE_LEFT && vel.x > -mMoveSpeed) {
            mPhysicsBody.applyLinearImpulse(-2f, 0, pos.x, pos.y);
        } 
 
        // Si on va a droite et qu'on est pas d�j� � la vitesse max
        if(mMoveDirection == MOVE_RIGHT && vel.x < mMoveSpeed) {
            mPhysicsBody.applyLinearImpulse(2f, 0, pos.x, pos.y);
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
	 * Cr��e les propri�t�s physiques du personnage
	 */
	protected void buildPhysicsBody() {
	    // D�finition de l'ensemble du corps
	    BodyDef def = new BodyDef();
	    
	    // C'est un corps dynamique (qui bouge :3)
        def.type = BodyType.DynamicBody;
        
        // On cr�� le conteneur
        mPhysicsBody = PhysicsController.getInstance().getWorld().createBody(def);
 
        // On d�finit ensuite deux composantes du corps : une boite pour le torse,
        // et un cercle pour les pieds (afin d'�viter de rester coinc� face � des petits
        // obstacles).
        PolygonShape poly = new PolygonShape();
        
        // TODO: Taille de la boite variable en fonction de la taille r�elle
        // du personnage
        poly.setAsBox(0.45f, 1.4f);
        mPhysicsChestFixture = mPhysicsBody.createFixture(poly, 1);
        
        // On lib�re les ressources
        poly.dispose();         
 
        // M�me chose pour les pieds :3
        CircleShape circle = new CircleShape();     
        circle.setRadius(0.45f);
        
        // On place les pieds en dessous du torse
        circle.setPosition(new Vector2(0, -1.4f));
        
        mPhysicsSensorFixture = mPhysicsBody.createFixture(circle, 0);     
        circle.dispose();       
 
        // On active la d�tection continue des collisions
        // Cela permet d'�tre s�r que le joueur ne va pas traverser des murs
        // ou autres si il se d�place tr�s vite.
        mPhysicsBody.setBullet(true);
        
        // On emp�che le personnage de faire des rotations impromptues
        mPhysicsBody.setFixedRotation(true);
	}
	
	/**
	 * Renvoie si oui ou non le personnage touche le sol (c-a-d qu'il n'est pas en train
	 * de tomber)
	 * @param deltaTime
	 * @return boolean
	 */
	public boolean isTouchingGround() {
	    // On r�cup�re la liste des collisions en cours
        List<Contact> contactList = PhysicsController.getInstance().getWorld().getContactList();
        
        // Pour chaque point de contact
        for(int i = 0; i < contactList.size(); i++) {
            Contact contact = contactList.get(i);
            
            // Si un des cot�s touche les pieds du personnage
            if(contact.isTouching() && (contact.getFixtureA() == mPhysicsSensorFixture ||
               contact.getFixtureB() == mPhysicsSensorFixture)) {             
 
                // On r�cup�re la position du personnage
                Vector2 pos = mPhysicsBody.getPosition();
                
                // On v�rifie que les points de contacts sont bien juste en dessous des pieds
                WorldManifold manifold = contact.getWorldManifold();
                boolean below = true;
                
                for (int j = 0; j < manifold.getNumberOfContactPoints(); j++) {
                    below &= (manifold.getPoints()[j].y < pos.y - 1.5f);
                }
 
                if (below) {
                    // Tous les points de contact sont bien en-dessous, on est cal�s
                    return true;
                }
 
                return false;
            }
        }
        return false;
    }
}
