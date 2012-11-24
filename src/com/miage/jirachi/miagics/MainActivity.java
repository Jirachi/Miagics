package com.miage.jirachi.miagics;
import java.io.IOException;

import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.FadeIn;
import com.badlogic.gdx.scenes.scene2d.actions.FadeOut;
import com.badlogic.gdx.scenes.scene2d.actions.Parallel;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleTo;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;

public class MainActivity  extends AndroidApplication {
	public static float PPX;
	public static float PPY;
	public static Stage mStage;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;
        config.useWakelock = true;
        config.useGL20 = true;
        initialize(new GameClient(), config);
    }

    SceneObject test2;
    SceneBackground backGTexture;
    AnimatedSceneObject test3;
    Camera mCamera;
    SpriteBatch mBatch;    
    Button testBouton;

    public class GameClient implements ApplicationListener, InputProcessor {		
        @Override
        public void create() {
        	PPX = (float)Gdx.graphics.getWidth() / 800.f;
    		PPY = (float)Gdx.graphics.getHeight() / 480.f;
            
    		CharacterController.getInstance().setSelf(new Player());
            
    		//test2 =  new StaticSceneObject("","data/test.png");
            //test3 = new AnimatedSceneObject("","animated/droid_from_android.png");
            
    		backGTexture = new SceneBackground("data/background/decor1.png");
            
    		Texture mTextBouton = new Texture(Gdx.files.internal("buttons/BoutonSaut.png"));
            Texture mTextBouton2 = new Texture(Gdx.files.internal("buttons/BoutonSaut2.png"));
            TextureRegion mTextRBouton = new TextureRegion(mTextBouton,0,0,226,226);
            TextureRegion mTextRBouton2 = new TextureRegion(mTextBouton2,0,0,226,226);
            testBouton = new Button(mTextRBouton,mTextRBouton2);
            
            testBouton.setClickListener(new ClickListener(){
				@Override
				public void click(Actor arg0, float arg1, float arg2) {
				    System.out.println("lol");
				}
            	
            });
            mStage = new Stage(800, 400, false);
            mBatch = new SpriteBatch();
            
            // === TEST PHYSIQUE
            // On créé un sol
            PhysicsController.getInstance().createEdge(BodyType.StaticBody, -4000, -10, 3000, -10, 0);
           
            // === RESEAU
            try {
                NetworkController.getInstance().connect("192.168.0.10", 37153);
                //NetworkController.getInstance().connect("friboks.ouverta.fr", 37153);
                NetworkController.getInstance().send(PacketMaker.makeBootMe());
            } catch (IOException e) {
                Log.e("Reseau", e.getMessage());
            }
            mCamera = mStage.getCamera();
            mStage.addActor(testBouton);
            testBouton.action(Parallel.$(Sequence.$(FadeOut.$(2), FadeIn.$(2)),
            	      Sequence.$(ScaleTo.$(0.1f, 0.1f, 1.5f), ScaleTo.$(1.0f, 1.0f, 1.5f))));
            Gdx.input.setInputProcessor(this);
        }
      
        @Override
        public void resize(int width, int height) {
            // TODO Auto-generated method stub
        }

        @Override
        public void render() {
        	mCamera.position.set(CharacterController.getInstance().getSelf().getPosition().x, CharacterController.getInstance().getSelf().getPosition().y+5, 0);
            mCamera.update();
            Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        	NetworkController.getInstance().update();
            PhysicsController.getInstance().update();
            
            mBatch.begin();
            
            backGTexture.render(mBatch,mCamera);
            
            //render all du chara controller
            CharacterController.getInstance().renderAll(mBatch, mCamera);
            //testBouton.draw(mBatch, logLevel);
           //test2.render(mBatch, mCamera);
            //test3.render(mBatch, mCamera);
            
            mBatch.end();
         // Affiche les éléments physiques (pour débug)
            mStage.draw();
            PhysicsController.getInstance().drawDebug(mCamera.combined);
        }

        @Override
        public void pause() {
        	// TODO Auto-generated method stub
        }

        @Override
        public void resume() {
            // TODO Auto-generated method stub
        }

        @Override
        public void dispose() {
            mStage.dispose();
        }

        @Override
        public boolean keyDown(int arg0) {
            mStage.keyDown(arg0);
            return false;
        }

        @Override
        public boolean keyTyped(char arg0) {
            mStage.keyTyped(arg0);
            return false;
        }

        @Override
        public boolean keyUp(int arg0) {
            mStage.keyUp(arg0);
            return false;
        }

        @Override
        public boolean scrolled(int arg0) {
            mStage.scrolled(arg0);
            return false;
        }

        @Override
        public boolean touchDragged(int arg0, int arg1, int arg2) {
            mStage.touchDragged(arg0, arg1, arg2);
            return false;
        }

        @Override
        public boolean touchMoved(int arg0, int arg1) {
            mStage.touchMoved(arg0, arg1);
            return false;
        }

        @Override
        public boolean touchDown(int x, int y, int pointerId, int button) {
            if (!mStage.touchDown(x,y,pointerId,button)) {
            	if(x<=(Gdx.graphics.getWidth()/2)){
            		CharacterController.getInstance().getSelf().setMoveDirection(Character.MOVE_LEFT);
            	} else {
            		CharacterController.getInstance().getSelf().setMoveDirection(Character.MOVE_RIGHT);
            	}
            }	
        	
            return true;
        }

        @Override
        public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
            if (!mStage.touchUp(arg0, arg1, arg2, arg3)) {
                CharacterController.getInstance().getSelf().setMoveDirection(Character.MOVE_NOT);
            }
            return true;
        }
    }
}