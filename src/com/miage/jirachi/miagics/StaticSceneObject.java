package com.miage.jirachi.miagics;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class StaticSceneObject extends SceneObject {
	//protected Texture mTexture;
	//correspond au model
	protected Body objectModel;
	protected Vector2 objectModelOrigin;
	
	//render
	protected Sprite objectSprite;
	protected Texture objectTexture;
	
	public StaticSceneObject(String refName, String path) {
		super(refName);
		//mTexture = new Texture(Gdx.files.internal(path));
		createBottle();	
		createSprites();
	}
	
	private void createBottle(){
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/test.json"));
		//code aurelien
		
		BodyDef bd = new BodyDef();
		
		//bd.position.set(5,-10);
		bd.type=BodyType.DynamicBody;
		
		FixtureDef fd = new FixtureDef();
		fd.density = 1;
		fd.friction = 1f;
		//fd.restitution=0.9f;
		
		objectModel = PhysicsController.getInstance().getWorld().createBody(bd);
		
		loader.attachFixture(objectModel, "test01", fd, 8f);
		objectModelOrigin = loader.getOrigin("test01", 8f).cpy();
	}
	
	
	private void createSprites(){
		objectTexture = new Texture(Gdx.files.internal("data/test/test.png"));
		objectTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		objectSprite = new Sprite(objectTexture);
		objectSprite.setSize(8f, 8f*objectSprite.getHeight()/objectSprite.getWidth());
		objectSprite.setScale(39.0f);
	}
	
	
	public void render(SpriteBatch batch, Camera cam){
		mPosition = objectModel.getPosition();
		cam.project(mProjection.set(mPosition.x, mPosition.y,0));
		
		objectSprite.setPosition(mProjection.x, mProjection.y);
		objectSprite.setOrigin(objectModelOrigin.x, objectModelOrigin.y);
		objectSprite.setRotation(objectModel.getAngle() * MathUtils.radiansToDegrees);
		
		objectSprite.draw(batch);
	}

}
