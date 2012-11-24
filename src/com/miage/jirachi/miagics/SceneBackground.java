package com.miage.jirachi.miagics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class SceneBackground extends Image {
    protected Texture mBackgroundTexture;
    protected Vector2 mPosBGLeft;
    protected Vector2 mPosBGRight;
    protected Vector3 mProjectionL;
    protected Vector3 mProjectionR;
    protected float mProfondeur;

    public SceneBackground(String path) {
        mBackgroundTexture = new Texture(Gdx.files.internal(path));
        mPosBGRight = new Vector2(0,-200.5f);
        mPosBGLeft = new Vector2(-mBackgroundTexture.getWidth(),-200.5f);
        mProfondeur = 0;
        
        mProjectionL = new Vector3();
        mProjectionR = new Vector3();
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        project(mProjectionR.set(mPosBGRight.x, mPosBGRight.y,0), batch.getProjectionMatrix());
        project(mProjectionL.set(mPosBGLeft.x, mPosBGLeft.y,0), batch.getProjectionMatrix());
        
        if (mProjectionL.x>0) {
            mPosBGRight.x=mPosBGLeft.x;
            mPosBGLeft.x=mPosBGRight.x-mBackgroundTexture.getWidth();
        }
        
        if (mProjectionR.x < 0) {
            mPosBGRight.x=mPosBGLeft.x;
            mPosBGLeft.x=mPosBGRight.x+mBackgroundTexture.getWidth();
        }
        
        // TODO: Mise a l'echelle d'un fond (voir fonction draw de Image.java, https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/scenes/scene2d/ui/Image.java)
        batch.draw(mBackgroundTexture, mPosBGLeft.x, mPosBGLeft.y, mBackgroundTexture.getWidth(), mBackgroundTexture.getHeight());
        batch.draw(mBackgroundTexture, mPosBGRight.x, mPosBGRight.y, mBackgroundTexture.getWidth(), mBackgroundTexture.getHeight());
    }
    
    public void project (Vector3 vec, Matrix4 combined) {
        project(vec, combined, 0, 0);
    }

    public void project (Vector3 vec, Matrix4 combined, float viewportX, float viewportY) {
        vec.prj(combined);
        vec.x = Gdx.graphics.getWidth() * (vec.x + 1) / 2 + viewportX;
        vec.y =  Gdx.graphics.getHeight() * (vec.y + 1) / 2 + viewportY;
        vec.z = (vec.z + 1) / 2;
    }
}
