package com.miage.jirachi.miagics;

import java.util.ArrayList;

import aurelienribon.bodyeditor.BodyEditorLoader;

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

public class StaticSceneObject extends SceneObject {
	//correspond au model
	protected Body mObjectModel;
	protected Vector2 mObjectModelOrigin;
	protected String mPath;
	
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
		mObjectModel = null;
		mObjectModelOrigin = null;
		mObjectSprite = null;
		mObjectTexture = null;
		
		createSprites();
		createObject();	
	}
	
	/**
	 * Cree l'objet physique
	 */
	private void createObject() {
		// TODO: changer les chaines de caracteres par l'argument.
	    BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/test.json"));
		
		//code aurelien
	    // Si on a pas deja un body, on en cree un
	    if (mObjectModel == null) {
    		BodyDef bd = new BodyDef();
    		bd.type=BodyType.DynamicBody;
    		
    		mObjectModel = PhysicsController.getInstance().getWorld().createBody(bd);
	    } else {
	        // On a deja un body, on supprime les fixtures associees
	        ArrayList<Fixture> fixtures = mObjectModel.getFixtureList();
	        
	        for (int i = 0; i < fixtures.size(); i++) {
	            mObjectModel.destroyFixture(mObjectModel.getFixtureList().get(i));
	        }
	    }
		
	    FixtureDef fd = new FixtureDef();
        fd.density = 50;
        fd.friction = 1f;
        fd.restitution = 0f;
	    
	    // On charge et cree la fixture
		loader.attachFixture(mObjectModel, "test01", fd, super.width);
		mObjectModelOrigin = loader.getOrigin("test01", super.width).cpy();
	}
	
	/**
	 * Charge  le sprite
	 */
	private void createSprites() {
		mObjectTexture = new Texture(Gdx.files.internal("data/test/test.png"));
		mObjectTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		super.setRegion(new TextureRegion(mObjectTexture,mObjectTexture.getWidth(),mObjectTexture.getHeight()));
		super.width = mObjectTexture.getWidth();
        super.height = mObjectTexture.getHeight();
	}
	
	/**
	 * Met a jour la position du sprite
	 */
	public void update() {
	    super.originX = mObjectModelOrigin.x;
	    super.originY = mObjectModelOrigin.y;
	    super.rotation = mObjectModel.getAngle() * MathUtils.radiansToDegrees;
	    super.x = getPosition().x;
	    super.y = getPosition().y;
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

    @Override
    /**
     * Definit l'echelle de l'objet
     */
    public void setScale(float sX, float sY) {
        super.width = mObjectTexture.getWidth() * sX;
        super.height = mObjectTexture.getHeight() * sY;
        
        // On recree l'objet physique pour correspondre a la nouvelle taille
        createObject();
    }

}
