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
import com.badlogic.gdx.physics.box2d.World;

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
		createObject(path);	
		
		createSprites();
	}
	
	
	private void createObject(String path){
		//changer les chaines de caracteres par l'argument.
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/test.json"));
		//code aurelien
		
		BodyDef bd = new BodyDef();
		
		bd.position.set(10,-10);
		bd.type=BodyType.DynamicBody;
		
		
		FixtureDef fd = new FixtureDef();
		fd.density = 50;
		fd.friction = 1f;
		fd.restitution=0f;
		
		objectModel = PhysicsController.getInstance().getWorld().createBody(bd);
		
		loader.attachFixture(objectModel, "test01", fd, 8.0f );
		objectModelOrigin = loader.getOrigin("test01", 8.0f).cpy();
	}
	
	
	private void createSprites(){
		objectTexture = new Texture(Gdx.files.internal("data/test/test.png"));
		objectTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		objectSprite = new Sprite(objectTexture);
		objectSprite.setSize(8f, 8f*objectSprite.getHeight()/objectSprite.getWidth());
	}
	public void render(SpriteBatch batch, Camera cam){
		
		mPosition = objectModel.getPosition();
		cam.project(mProjection.set(mPosition.x, mPosition.y,0));
		
		//Vector2 bottlePos = objectModel.getPosition();
		objectSprite.setPosition(mProjection.x, mProjection.y);
		objectSprite.setScale(38.0f);
		objectSprite.setOrigin(objectModelOrigin.x, objectModelOrigin.y);
		objectSprite.setRotation(objectModel.getAngle() * MathUtils.radiansToDegrees);
		
		
		
		
		//batch.draw(objectSprite, mProjection.x, mProjection.y,0.0f,0.0f,256,256,1.0f,1.0f, objectModel.getAngle() * 3.1415f);
		objectSprite.draw(batch)	;
		//batch.draw(objectTexture, mProjection.x, mProjection.y , 256, 256);
	}

}
