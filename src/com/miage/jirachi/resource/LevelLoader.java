package com.miage.jirachi.resource;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;

public class LevelLoader {
    private static LevelLoader mSingleton = null;
    
    public static LevelLoader getInstance() {
        if (mSingleton == null) {
            mSingleton = new LevelLoader();
        }
        
        return mSingleton;
    }
    
    private LevelLoader() {
        
    }
    
    public Level loadScheme(String path) throws JSONException {
        JSONObject json_root = new JSONObject(Gdx.files.internal(path).readString());
        
        Level l = new Level();
        
        // Lecture des entites
        JSONObject json_entities = json_root.getJSONObject("entities");
        Iterator<?> it_entities = json_entities.keys();
        
        while (it_entities.hasNext()) {
            JSONObject json_entity = json_entities.getJSONObject((String)it_entities.next());
            JSONObject json_properties = json_entity.getJSONObject("properties");
            
            LevelEntity ent = new LevelEntity(json_entity.getInt("type"));
            
            // Lecture des proprietes de l'entite
            Iterator<?> it_properties = json_properties.keys();
            while (it_properties.hasNext()) {
                String key = (String)it_properties.next();
                ent.setProperty(key, json_properties.getString(key));
            }
            
            // Ajout de l'entite au level
            l.addEntity(ent);
        }
        
        return l;
    }
}
