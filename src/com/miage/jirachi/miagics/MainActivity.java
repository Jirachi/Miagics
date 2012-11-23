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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class MainActivity  extends AndroidApplication {
	public static float PPX;
	public static float PPY;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
		
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = true;
        config.useCompass = false;
        config.useWakelock = true;
        config.useGL20 = true;
        initialize(new GameClient(), config);
        
        /*FileHandle handle = Gdx.files.internal("data/myfile.txt");*/
        
    }

    SceneObject test2;
    SceneBackground backGTexture;
    AnimatedSceneObject test3;
    Camera mCamera;
    SpriteBatch mBatch;    

    public class GameClient implements ApplicationListener, InputProcessor {		
        @Override
        public void create() {
        	PPX = (float)Gdx.graphics.getWidth() / 800.f;
    		PPY = (float)Gdx.graphics.getHeight() / 480.f;
            CharacterController.getInstance().setSelf(new Player());
            test2 =  new StaticSceneObject("","data/test.png");
            //test3 = new AnimatedSceneObject("","animated/droid_from_android.png");
            backGTexture = new SceneBackground("data/background/decor1.png");
            
            mCamera = new OrthographicCamera(800, 480);
            mCamera.update();
            mBatch = new SpriteBatch();
            
            // === TEST PHYSIQUE
            // On créé un sol
            PhysicsController.getInstance().createEdge(BodyType.StaticBody, -2000, -10, 1000, -10, 0);
           
            // === RESEAU
            try {
                NetworkController.getInstance().connect("192.168.0.10", 37153);
                NetworkController.getInstance().send(PacketMaker.makeBootMe());
            } catch (IOException e) {
                Log.e("Reseau", e.getMessage());
            }
            
            Gdx.input.setInputProcessor(this);
        }
        
        //test de background a virer si ne marche pas
    
        @Override
        public void resize(int width, int height) {
            // TODO Auto-generated method stub
        }

        @Override
        public void render() {
            Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        	NetworkController.getInstance().update();
            PhysicsController.getInstance().update();
            
            
            
        
            mBatch.begin();
          
            
            backGTexture.render(mBatch,mCamera);
            mCamera.position.set(CharacterController.getInstance().getSelf().getPosition().x, CharacterController.getInstance().getSelf().getPosition().y+5, 0);
            mCamera.update();
            //render all du chara controller
            CharacterController.getInstance().renderAll(mBatch, mCamera);
            
           test2.render(mBatch, mCamera);
            //test3.render(mBatch, mCamera);
            
            mBatch.end();
         // Affiche les éléments physiques (pour débug)
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
            // TODO Auto-generated method stub
        }

        @Override
        public boolean keyDown(int arg0) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean keyTyped(char arg0) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean keyUp(int arg0) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean scrolled(int arg0) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean touchDragged(int arg0, int arg1, int arg2) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean touchMoved(int arg0, int arg1) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean touchDown(int x, int y, int pointerId, int button) {
        	
        	if(x<=(Gdx.graphics.getWidth()/2)){
        		CharacterController.getInstance().getSelf().setMoveDirection(Character.MOVE_LEFT);
        	}else{
        		CharacterController.getInstance().getSelf().setMoveDirection(Character.MOVE_RIGHT);
        	}
        	
        	
            return true;
        }

        @Override
        public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
        	CharacterController.getInstance().getSelf().setMoveDirection(Character.MOVE_NOT);
            return true;
        }
    }
}