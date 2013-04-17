package com.miage.jirachi.miagics;
import java.io.IOException;

import org.json.JSONException;

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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.miage.jirachi.resource.LevelLoader;

public class MainActivity  extends AndroidApplication {
    public static Stage mStage;

    private int mMoveTouchId;
    
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
    Button mJumpButton;
    Button mHitButton;
    Image mHealthBar;

    public class GameClient implements ApplicationListener, InputProcessor {		
        @Override
        public void create() {
        	// Early setup
        	mStage = new Stage(400, 240, true);
        	mCamera = mStage.getCamera();
        	Gdx.input.setInputProcessor(this);
        	
        	// =====================
        	// Setup the UI
        	
        	// Jump button
            TextureRegionDrawable texBtnJump1 = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/BoutonSaut.png")),0,0,64,64));
            TextureRegionDrawable texBtnJump2 = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/BoutonSaut2.png")),0,0,64,64));
            
            mJumpButton = new Button(texBtnJump1,texBtnJump2);
            mJumpButton.setWidth(50);
            mJumpButton.setHeight(50);
            mJumpButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    CharacterController.getInstance().getSelf().jump();
                }

            });
            
            // Hit button
            TextureRegionDrawable texBtnHit1 = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/BoutonFrappe.png")),0,0,64,64));
            TextureRegionDrawable texBtnHit2 = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/BoutonFrappe2.png")),0,0,64,64));
            
            mHitButton = new Button(texBtnHit1,texBtnHit2);
            mHitButton.setWidth(50);
            mHitButton.setHeight(50);
            mHitButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    CharacterController.getInstance().getSelf().fight();
                }

            });
            
            // Health bar
            TextureRegion mTextRBarre = new TextureRegion(new Texture(Gdx.files.internal("data/Life.png")), 0, 0, 32, 32);
            mHealthBar = new Image(mTextRBarre);
            mHealthBar.setWidth(150);
            mHealthBar.setHeight(30);
            
            // Add them all to the scene
            mStage.addActor(mJumpButton);
            mStage.addActor(mHitButton);
            mStage.addActor(mHealthBar);
            
            // Setup a background repeating texture
            backGTexture = new SceneBackground("data/background/decor1.png");
            mStage.addActor(backGTexture);
            
            
            // =======================
            // Load the test level !
            
            try {
				LevelLoader.getInstance().loadLevel(LevelLoader.getInstance().loadScheme("test1.scn"));
			} catch (JSONException e) {
				Log.e("Level load", e.getMessage());
			}

            // =======================
            // Connect to the server !
            try {
                //NetworkController.getInstance().connect("192.168.0.11", 37153);
                
                //NetworkController.getInstance().connect("friboks.ouverta.fr", 37153);
                NetworkController.getInstance().connect("xplod.clusterfuck.bbqdroid.org", 37153);
                
                NetworkController.getInstance().send(PacketMaker.makeBootMe());
            } catch (IOException e) {
                Log.e("Reseau", e.getMessage());
            }
        }

        @Override
        public void resize(int width, int height) {
            // TODO Auto-generated method stub
        }

        @Override
        public void render() {
            Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            
            // Mise a jour des elements reseau
            NetworkController.getInstance().update();
            
            // Mise à jour du monde physique
            PhysicsController.getInstance().update();
            
            // Mise a jour de la scène
            mStage.act(Gdx.graphics.getDeltaTime());
            
            // Centrage de la camera
            Player self = CharacterController.getInstance().getSelf();
            
            if (self != null) {
	            mCamera.position.set(self.getPosition().x, self.getPosition().y+5, 0);
	            mCamera.update();
	            
	            // Position du bouton
	            mJumpButton.setX(mCamera.position.x - mCamera.viewportWidth / 2.0f);
	            mJumpButton.setY(mCamera.position.y - mCamera.viewportHeight / 2.0f);
	            
	            mHitButton.setX(mCamera.position.x - mCamera.viewportWidth / 2.0f + 70);
	            mHitButton.setY(mCamera.position.y - mCamera.viewportHeight / 2.0f);
	            
	            mHealthBar.setX(mCamera.position.x + mCamera.viewportWidth/2 - 160);
	            mHealthBar.setY(mCamera.position.y - mCamera.viewportHeight/2 + 10);
	            mHealthBar.setScaleX(self.getHealth()/100.0f);
	            
	            mJumpButton.toFront();
	            mHitButton.toFront();
	            mHealthBar.toFront();
            }

            // Dessin de la scène
            mStage.draw();
            
            // Mise à jour post-act des personnages
            CharacterController.getInstance().update();
            
            // Affiche les éléments physiques (pour débug)
            // PhysicsController.getInstance().drawDebug(mCamera.combined);
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
        public boolean touchDown(int x, int y, int pointerId, int button) {
        	Player self = CharacterController.getInstance().getSelf();
        	if (self == null)
        		return true;
        	
            if (!mStage.touchDown(x,y,0,button)) {
                if(x<=(Gdx.graphics.getWidth()/2)){
                	self.setMoveDirection(Character.MOVE_LEFT);
                } else {
                	self.setMoveDirection(Character.MOVE_RIGHT);
                }
                
                mMoveTouchId = pointerId;
            }	

            return true;
        }

        @Override
        public boolean touchUp(int x, int y, int pointerId, int button) {
        	Player self = CharacterController.getInstance().getSelf();
        	if (self == null)
        		return true;
        	
            if (!mStage.touchUp(x, y, 0, button) && pointerId == mMoveTouchId) {
            	self.setMoveDirection(Character.MOVE_NOT);
            }
            return true;
        }

		@Override
		public boolean mouseMoved(int arg0, int arg1) {
			mStage.mouseMoved(arg0, arg1);
			return false;
		}
    }
}