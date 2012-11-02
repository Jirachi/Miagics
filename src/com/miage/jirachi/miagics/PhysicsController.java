package com.miage.jirachi.miagics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.World;

public class PhysicsController {
    private static PhysicsController mSingleton = null;
    
    private World mPhysicsWorld = null;
    private Box2DDebugRenderer mDebugRenderer = null;
    
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
        mDebugRenderer = new Box2DDebugRenderer();
    }
    
    /**
     * Returns the physics World
     * @return Physics World
     */
    public World getWorld() {
        return mPhysicsWorld;
    }
    
    /**
     * Met à jour la simulation physique
     */
    public void update() {
        mPhysicsWorld.step(Gdx.graphics.getDeltaTime(), 4, 4);
    }
    
    /**
     * Dessine une représentation des objets physiques (pour débugger)
     * @param camMatrix camera.combined
     */
    public void drawDebug(Matrix4 camMatrix) {
        mDebugRenderer.render(mPhysicsWorld, camMatrix);
    }
    
    /**
     * Créée une ligne de collision
     * @param type
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param density
     * @return
     */
    public Body createEdge(BodyType type, float x1, float y1, float x2, float y2, float density) {
        BodyDef def = new BodyDef();
        def.type = type;
        Body box = mPhysicsWorld.createBody(def);
 
        EdgeShape poly = new EdgeShape();
        poly.set(new Vector2(0, 0), new Vector2(x2 - x1, y2 - y1));
        box.createFixture(poly, density);
        box.setTransform(x1, y1, 0);
        poly.dispose();
 
        return box;
    }
}
