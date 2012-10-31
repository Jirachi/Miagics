package com.miage.jiarchi.miagics;
import android.os.Bundle;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

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
    }

    Character test;
    SceneObject test2;
    Camera mCamera;
    SpriteBatch mBatch;
    World mPhysicsWorld;
    
    
    public class GameClient implements ApplicationListener, InputProcessor {		
        @Override
        public void create() {
            test = new Character();
            test2 =  new StaticSceneObject("","animated/fox.png");
            mCamera = new OrthographicCamera(28, 20);
            mCamera.update();
            mPhysicsWorld = new World(new Vector2(0, -20), true);
            mBatch = new SpriteBatch();
        }

        @Override
        public void resize(int width, int height) {
            // TODO Auto-generated method stub
        }

        @Override
        public void render() {
        	
        	
            Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            mCamera.position.set(0, 0, 0);
            mCamera.update();
            
            mBatch.begin();
            test.render(mBatch, mCamera);
            mBatch.end();
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

            return false;
        }

        @Override
        public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {

            return false;
        }
    }
}