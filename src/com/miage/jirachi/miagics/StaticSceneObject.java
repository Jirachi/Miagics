package com.miage.jirachi.miagics;

import java.util.ArrayList;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.miage.jirachi.resource.Resource;
import com.miage.jirachi.resource.ResourceManager;

public class StaticSceneObject extends SceneObject {
	//correspond au model
	protected Body mObjectModel;
	protected Vector2 mObjectModelOrigin;
	protected String mPath;
	protected Resource mResource;
	protected FixtureDef mFixtureDefinition;
	
	//render
	protected Sprite mObjectSprite;
	protected Texture mObjectTexture;
	
	/**
	 * Constructeur
	 * @param refName Nom de reference
	 * @param path Chemin vers l'image de representation
	 */
	public StaticSceneObject(String refName, String path) {
		super(refName);
		
		mPath = path;
		mResource = ResourceManager.getInstance().getResource(mPath);
		mObjectModel = null;
		mObjectModelOrigin = null;
		mObjectSprite = null;
		mObjectTexture = null;
		
		createSprites();
		createObject();	
	}
	
	/**
     * Constructeur
     * @param refName Nom de reference
     * @param path Chemin vers l'image de representation
     * @param sX Echelle X
     * @param sY Echelle Y
     */
    public StaticSceneObject(String refName, String path, float sX, float sY) {
        super(refName);
        
        mPath = path;
        mObjectModel = null;
        mObjectModelOrigin = null;
        mObjectSprite = null;
        mObjectTexture = null;
        
        createSprites();
        super.setWidth(super.getWidth() * sX);
        super.setHeight(super.getHeight() * sY);
        createObject(); 
    }
    
    public Body getPhysicsBody() {
    	return mObjectModel;
    }
	
    public void setDensity(float density) {
    	mFixtureDefinition.density = density;
    }
    
	/**
	 * Cree l'objet physique
	 */
	private void createObject() {		
	    // Si on a deja un body, on le supprime
	    if (mObjectModel != null) {
	        // On a deja un body, on supprime les fixtures associees
	        ArrayList<Fixture> fixtures = mObjectModel.getFixtureList();
	        
	        for (int i = 0; i < fixtures.size(); i++) {
	            mObjectModel.destroyFixture(mObjectModel.getFixtureList().get(i));
	        }
	        
	        assert(mObjectModel.getFixtureList().size() == 0);

	        // FIXME: Si on supprime juste les fixtures, il reste des morceaux non supprimes.
	        // Workaround pour le moment, on supprime et recree tout le body.
	        BodyDef bd = new BodyDef();
            bd.type=BodyType.DynamicBody;
            
            Body newModel = PhysicsController.getInstance().getWorld().createBody(bd);
            
            newModel.setAngularDamping(mObjectModel.getAngularDamping());
            newModel.setAngularVelocity(mObjectModel.getAngularVelocity());
            newModel.setLinearVelocity(mObjectModel.getLinearVelocity());
            newModel.setTransform(mObjectModel.getTransform().getPosition(), mObjectModel.getAngle());
            
	        mObjectModel.getWorld().destroyBody(mObjectModel);
	        mObjectModel = newModel;
	    } else {
	        BodyDef bd = new BodyDef();
	        bd.type=BodyType.DynamicBody;
	        
	        mObjectModel = PhysicsController.getInstance().getWorld().createBody(bd);
	    }

	    mFixtureDefinition = new FixtureDef();
	    mFixtureDefinition.density = 10;
	    mFixtureDefinition.friction = 1f;
	    mFixtureDefinition.restitution = 0f;
	    
	    // On charge et cree la fixture
	    String fixtureName = mResource.file.substring(mResource.file.lastIndexOf('/')+1);
	    
	    try {
	    	PhysicsController.getInstance().getBodyEditorLoader().attachFixture(mObjectModel, fixtureName, mFixtureDefinition, super.getWidth());
			mObjectModelOrigin = PhysicsController.getInstance().getBodyEditorLoader().getOrigin(fixtureName, super.getWidth()).cpy();	
	    }
	    catch (RuntimeException ex) {
	    	Log.e("Physics", "Couldn't load physics model for resource " + fixtureName);
	    }
		
	}
	
	/**
	 * Charge  le sprite
	 */
	private void createSprites() {
		mObjectTexture = new Texture(Gdx.files.internal(mResource.file));
		mObjectTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		super.setDrawable(new TextureRegionDrawable(new TextureRegion(mObjectTexture,mObjectTexture.getWidth(),mObjectTexture.getHeight())));
		super.setWidth(mObjectTexture.getWidth());
        super.setHeight(mObjectTexture.getHeight());
	}
	
	/**
	 * Met a jour la position du sprite
	 */
	@Override
	public void act(float timeDelta) {
		if (mObjectModelOrigin != null) {
			super.setOriginX(mObjectModelOrigin.x);
	    	super.setOriginY(mObjectModelOrigin.y);
		} else {
			super.setOriginX(0);
			super.setOriginY(0);
		}
	    super.setRotation(mObjectModel.getAngle() * MathUtils.radiansToDegrees);
	    super.setX(getPosition().x);
	    super.setY(getPosition().y);
	}
	
	@Override
	/**
	 * Retourne la position de l'objet
	 */
	public Vector2 getPosition() {
	    return mObjectModel.getPosition().sub(mObjectModelOrigin);
	}
	
	@Override
	/**
	 * Definit la position de l'objet
	 */
	public void setPosition(float x, float y) {
	    mObjectModel.setTransform(x,y,0);
	}

	public Texture getTexture() {
		return mObjectTexture;
	}
	
    @Override
    /**
     * Definit l'echelle de l'objet
     */
    public void setScale(float sX, float sY) {
        super.setWidth(mObjectTexture.getWidth() * sX);
        super.setHeight(mObjectTexture.getHeight() * sY);
        
        // On recree l'objet physique pour correspondre a la nouvelle taille
        createObject();
    }

}
