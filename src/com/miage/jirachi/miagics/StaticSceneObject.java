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
	protected Texture mTexture;
	//correspond au model
	protected Body objectModel;
	protected Vector2 objectModelOrigin;
	
	//render
	protected Sprite objectSprite;
	private Texture objectTexture;
	
	public StaticSceneObject(String refName, String path) {
		super(refName);
		mTexture = new Texture(Gdx.files.internal(path));
		
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/test.json"));
		//code aurelien
		BodyDef bd = new BodyDef();
		bd.position.set(5,0);
		bd.type=BodyType.DynamicBody;
		
		
		FixtureDef fd = new FixtureDef();
		fd.density = 50;
		fd.friction = 1f;
		//fd.restitution=0.9f;
		
		World world = PhysicsController.getInstance().getWorld();
		objectModel = world.createBody(bd);
		loader.attachFixture(objectModel, "test01", fd,8f );
		objectModelOrigin = loader.getOrigin("test01", 8).cpy();
		
	}
	/*
	private void createObject(){
		
	
	}*/

	public void render(SpriteBatch batch, Camera cam){
		//objectTexture = new Texture(Gdx.files.internal("data/test.png"));
		/*objectTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		objectSprite = new Sprite(objectTexture);
		objectSprite.setSize(8, 8*objectSprite.getHeight()/objectSprite.getWidth());
		Vector2 bottlePos = objectModel.getPosition().sub(objectModelOrigin);
		objectSprite.setPosition(bottlePos.x, bottlePos.y);
		objectSprite.setOrigin(objectModelOrigin.x, objectModelOrigin.y);*/
		//objectSprite.setRotation(objectModel.getAngle() * MathUtils.radiansToDegrees);
		cam.project(mProjection.set(mPosition.x, mPosition.y, 0));
		batch.draw(mTexture, mProjection.x, mProjection.y , 110, 130);
	}

}
