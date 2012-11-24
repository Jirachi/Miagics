package com.miage.jirachi.miagics;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Character extends Image {
	protected Texture mTexture;
	protected int mMoveDirection = MOVE_NOT;
	
	protected float mTempsAccumule;
	protected float mMoveSpeed = 200.0f;
	
	// propriétés réseau
	protected long mNetworkId;
	
	// propriétés physiques
	protected Body mPhysicsBody;
	protected Fixture mPhysicsChestFixture;
	protected Fixture mPhysicsSensorFixture;
	protected long mLastGroundTime;
	protected float mStillTime;

	// attributs d'animation
	protected TextureRegion[][] mTextureRegions;
	protected int mOppose;

	// constantes de mouvement
	public final static int MOVE_LEFT = 1;
	public final static int MOVE_RIGHT = 2;
	/*public final static int MOVE_TOP = 3;
	public final static int MOVE_BOTTOM = 4;*/
	public final static int MOVE_NOT = 0;
	
	// TODO: Framework animation (voir redmine)
	private Animation mIdleAnimation;
	private Animation mWalkAnimation;

	/**
	 * Default constructor
	 */
	public Character(TextureRegion[][] tex) {
	    // On recupere les regions de texture pour l'animation, et on les passe
	    // a la superclasse
        super(tex[0][0]);
	   
        // Initialisation des variables
        mTextureRegions = tex;
		mTempsAccumule = 0;
		mOppose = MOVE_RIGHT;
		
		// Init des animations (TODO: Framework animation, voir redmine)
		mIdleAnimation = new Animation(0.05f, tex[0]);
		mWalkAnimation = new Animation(0.05f, tex[1]);
		mIdleAnimation.setPlayMode(Animation.LOOP_PINGPONG);
		mWalkAnimation.setPlayMode(Animation.LOOP_PINGPONG);
		
		// Construction du body physique
		buildPhysicsBody();
		
		// On definit l'echelle du perso (1/2 de la taille reelle)
		this.scaleX = this.scaleY = 0.5f;
	}

	/**
	 * Force la position du joueur
	 */
	public void setPosition(float f, float g) {
		mPhysicsBody.setTransform(new Vector2(f,g), 0);
	}
	
	/**
	 * Définit dans quel sens le personnage doit se déplacer chaque frame
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
	 * Retourne la position dans la scàne de l'objet
	 * @return Position
	 */
	public Vector2 getPosition() {
		return mPhysicsBody.getPosition();
	}

	/**
	 * Met à jour le personnage
	 * @note Appelé par render()
	 * @param timeDelta
	 */
	public void update(float timeDelta) {
	    // Mise a jour de l'animation
	    if (mMoveDirection == MOVE_NOT) {
	        this.setRegion(mIdleAnimation.getKeyFrame(mTempsAccumule += timeDelta, true));
	    }
	    else {
	        this.setRegion(mWalkAnimation.getKeyFrame(mTempsAccumule += timeDelta, true));
	    }
	    
		// Mise à jour des propriétés physiques
        Vector2 vel = mPhysicsBody.getLinearVelocity(); 
        boolean grounded = isTouchingGround();
        
        // On estime être sur le sol si on le touche, ou si on l'a
        // récemment touché pour compenser le manque de précision
        if (grounded) {
            mLastGroundTime = System.nanoTime();
        } else {
            if (System.nanoTime() - mLastGroundTime < 100000000) {
                grounded = true;
            }
        }
 
        // On limite la vitesse de mouvement à la vitesse max
        if (Math.abs(vel.x) > mMoveSpeed) {            
            vel.x = Math.signum(vel.x) * mMoveSpeed;
            mPhysicsBody.setLinearVelocity(vel.x, vel.y);
        }
        
 
        // On calcule le temps qu'on a passé sans bouger, et on applique l'inertie (90%)
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
            // Si on ne bouge pas et si on est arràté depuis 200ms, on rapplique les frottements
            // au sol
            if(mMoveDirection == MOVE_NOT && mStillTime > 0.2) {
                mPhysicsChestFixture.setFriction(200f);
                mPhysicsSensorFixture.setFriction(200f);
            }
            else {
                // Sinon on garde un frottement minimal
                mPhysicsChestFixture.setFriction(0.2f);
                mPhysicsSensorFixture.setFriction(0.2f);
            }
        }       
 
        // Si on va a gauche et qu'on est pas déjË† Ë† la vitesse max
        if(mMoveDirection == MOVE_LEFT && vel.x > -mMoveSpeed) {
            mPhysicsBody.applyLinearImpulse(-mMoveSpeed / 4.0f, 0, mPhysicsBody.getWorldCenter().x, mPhysicsBody.getWorldCenter().y);
        } 
 
        // Si on va a droite et qu'on est pas déjË† Ë† la vitesse max
        if(mMoveDirection == MOVE_RIGHT && vel.x < mMoveSpeed) {
            mPhysicsBody.applyLinearImpulse(mMoveSpeed / 4.0f, 0, mPhysicsBody.getWorldCenter().x, mPhysicsBody.getWorldCenter().y);
        }
        
        super.x = getPosition().x;
        super.y = getPosition().y;
	}

	/**
	 * Mirroir horizontal des textures
	 */
	protected void flipTextures() {
		for (int i = 0; i < mTextureRegions.length; i++) {
			for (int j = 0; j < mTextureRegions[i].length; j++) {
				this.mTextureRegions[i][j].flip(true, false);
			}
		}
	}
	
	/**
	 * Créée les propriétés physiques du personnage
	 */
	protected void buildPhysicsBody() {
	    // Définition de l'ensemble du corps
	    BodyDef def = new BodyDef();
	    
	    // C'est un corps dynamique (qui bouge :3)
        def.type = BodyType.DynamicBody;
        
        // On créé le conteneur
        mPhysicsBody = PhysicsController.getInstance().getWorld().createBody(def);
 
        // On définit ensuite deux composantes du corps : une boite pour le torse,
        // et un cercle pour les pieds (afin d'éviter de rester coincé face Ë† des petits
        // obstacles).
        PolygonShape poly = new PolygonShape();
        
        // TODO: Taille de la boite variable en fonction de la taille réelle
        // du personnage
        poly.setAsBox(0.45f, 0.45f);
        mPhysicsChestFixture = mPhysicsBody.createFixture(poly, 1);
        
        // On libàre les ressources
        poly.dispose();         
 
        // Màme chose pour les pieds :3
        CircleShape circle = new CircleShape();     
        circle.setRadius(0.45f);
        
        // On place les pieds en dessous du torse
        circle.setPosition(new Vector2(0, -0.45f));
        
        mPhysicsSensorFixture = mPhysicsBody.createFixture(circle, 0);     
        circle.dispose();       
 
        // On active la détection continue des collisions
        // Cela permet d'être sûr que le joueur ne va pas traverser des murs
        // ou autres si il se déplace tràs vite.
        mPhysicsBody.setBullet(true);
        
        // On empàche le personnage de faire des rotations impromptues
        mPhysicsBody.setFixedRotation(true);
	}
	
	/**
	 * Renvoie si oui ou non le personnage touche le sol (c-a-d qu'il n'est pas en train
	 * de tomber)
	 * @param deltaTime
	 * @return boolean
	 */
	public boolean isTouchingGround() {
	    // On récupàre la liste des collisions en cours
        List<Contact> contactList = PhysicsController.getInstance().getWorld().getContactList();
        
        // Pour chaque point de contact
        for(int i = 0; i < contactList.size(); i++) {
            Contact contact = contactList.get(i);
            
            // Si un des cotés touche les pieds du personnage
            if(contact.isTouching() && (contact.getFixtureA() == mPhysicsSensorFixture ||
               contact.getFixtureB() == mPhysicsSensorFixture)) {             
 
                // On récupère la position du personnage
                Vector2 pos = mPhysicsBody.getPosition();
                
                // On vérifie que les points de contacts sont bien juste en dessous des pieds
                WorldManifold manifold = contact.getWorldManifold();
                boolean below = true;
                
                for (int j = 0; j < manifold.getNumberOfContactPoints(); j++) {
                    below &= (manifold.getPoints()[j].y < pos.y - 1.5f);
                }
 
                if (below) {
                    // Tous les points de contact sont bien en-dessous, on est calés
                    return true;
                }
 
                return false;
            }
        }
        return false;
    }
	
	/**
	 * Definit l'identifiant reseau du personnage
	 * @param id
	 */
	public void setNetworkId(long id) {
		mNetworkId = id;
	}
	
	/**
	 * @return L'identifiant reseau du personnage
	 */
	public long getNetworkId() {
		return mNetworkId;
	}
}
