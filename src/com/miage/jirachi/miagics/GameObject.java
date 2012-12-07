package com.miage.jirachi.miagics;


public class GameObject extends AnimatedSceneObject {
    protected long mNetworkId;
    protected float mTargetX;
    protected float mTargetY;
    protected float mTargetTime;
    
    // Garder synchro avec serveur!
    public final static int PHYSICS_TYPE_STATIC = 1;
    public final static int PHYSICS_TYPE_DYNAMIC = 2;
    public final static int PHYSICS_TYPE_NO_COLLISION = 3;
    
    /**
     * 
     * @param networkId
     * @param resource
     */
    public GameObject(long networkId, String res) {
        super("__NETWORK_GAMEOBJECT_" + networkId, res);
        mNetworkId = networkId;
    }
    
    /**
     * Retourne l'ID réseau de l'objet
     */
    public long getNetworkId() {
        return mNetworkId;
    }
    
    /**
     * Deplace automatiquement l'objet a la position demandee
     * @note La physique n'est pas prise en compte.
     * @param x Position x cible
     * @param y Position y cible
     * @param time Duree en secondes
     */
    public void moveTo(float x, float y, float time) {
        mTargetX = x;
        mTargetY = y;
        mTargetTime = time;
    }
    
    @Override
    public void act(float timeDelta) {
        super.act(timeDelta);
        
        // TODO: MaJ position si moveTo
    }
}
