package com.miage.jiarchi.miagics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Character {
    private Texture mTexture;
    
    public Character() {
        mTexture = new Texture(Gdx.files.internal("gfx/pechouchoux.png"));
        // mBody = new body
    }
    
    public void render(SpriteBatch batch, Camera cam) {
        batch.draw(mTexture, 10, 20, 110, 130);
    }
}
