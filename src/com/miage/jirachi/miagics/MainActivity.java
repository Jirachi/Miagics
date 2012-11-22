package com.miage.jirachi.miagics;
import android.os.Bundle;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class MainActivity  extends AndroidApplication {
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

    Character test;
    SceneObject test2;
    SceneBackground backGTexture;
    AnimatedSceneObject test3;
    Camera mCamera;
    SpriteBatch mBatch;    

    public class GameClient implements ApplicationListener, InputProcessor {		
        @Override
        public void create() {
            test = new Character();
            test2 =  new StaticSceneObject("","data/test.png");
            //test3 = new AnimatedSceneObject("","animated/droid_from_android.png");
            backGTexture = new SceneBackground("data/background/decor1.png");
            
            //ajout ici des characters dans la liste (dans characontroller)
            CharacterController.getInstance().addCharacter(test);
            
            mCamera = new OrthographicCamera(28, 20);
            mCamera.update();
            mBatch = new SpriteBatch();
            
            // === TEST PHYSIQUE
            // On créé un sol
            PhysicsController.getInstance().createEdge(BodyType.StaticBody, -50, -10, 100, -10, 0);
           
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
            PhysicsController.getInstance().update();
            
            // Affiche les éléments physiques (pour débug)
            PhysicsController.getInstance().drawDebug(mCamera.combined);
        
            mBatch.begin();
          
            
            backGTexture.render(mBatch,mCamera);
            //render all du chara controller
            CharacterController.getInstance().renderAll(mBatch, mCamera);
            
            test2.render(mBatch, mCamera);
            //test3.render(mBatch, mCamera);
            
            mBatch.end();
            
            mCamera.position.set(test.getPosition().x, test.getPosition().y, 0);
            mCamera.update();
            
            
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
        		test.setMoveDirection(-1);
        	}else{
        		test.setMoveDirection(1);
        	}
        	
        	
            return false;
        }

        @Override
        public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
        	test.setMoveDirection(0);
            return false;
        }
    }
}