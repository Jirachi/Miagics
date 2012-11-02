package com.miage.jiarchi.miagics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class PhysicsController {
    private static PhysicsController mSingleton = null;
    
    private World mPhysicsWorld = null;
    
    // Instantiate and create unique class instance
    public static PhysicsController getInstance() {
        if (mSingleton == null) {
            mSingleton = new PhysicsController();
        }
        
        return mSingleton;
    }
    
    // Constructor
    private PhysicsController() {
        // Create physics world
        mPhysicsWorld = new World(new Vector2(0, -20), true);
    }
    
    /**
     * Returns the physics World
     * @return Physics World
     */
    public World getWorld() {
        return mPhysicsWorld;
    }
    
    public void update() {
        mPhysicsWorld.step(Gdx.graphics.getDeltaTime(), 4, 4);
    }
    
    
}
