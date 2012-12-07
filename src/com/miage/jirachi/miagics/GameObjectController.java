package com.miage.jirachi.miagics;

import java.util.HashMap;
import java.util.Map;

public class GameObjectController {
    private static GameObjectController mSingleton = null;
    
    private Map<Long, GameObject> mObjects;
    
    public static GameObjectController getInstance() {
        if (mSingleton == null) {
            mSingleton = new GameObjectController();
        }
        
        return mSingleton;
    }
    
    /**
     * Constructeur par defaut
     */
    public GameObjectController() {
        mObjects = new HashMap<Long, GameObject>();
    }
    
    /**
     * Ajoute un objet gere par ce controleur
     * @param o L'objet
     */
    public void addObject(GameObject o) {
        mObjects.put(o.getNetworkId(), o);
        MainActivity.mStage.addActor(o);
    }
    
    public GameObject getGameObject(long id) {
        return mObjects.get(id);
    }
}
