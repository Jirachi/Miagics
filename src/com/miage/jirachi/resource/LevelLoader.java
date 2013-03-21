package com.miage.jirachi.resource;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.miage.jirachi.miagics.MainActivity;
import com.miage.jirachi.miagics.StaticSceneObject;

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
    
    /**
     * Charge un niveau en fonction du schema de niveau passe en parametre. Le schema de niveau peut
     * etre lu via la fonction loadScheme(String path) juste en-dessous.
     * @param scheme
     */
    public void loadLevel(Level scheme) {
    	// On charge le schema de niveau passé
    	// TODO: Effacer le niveau precedemment charge si il en existe un
    	
    	Iterator<LevelEntity> entities = scheme.getEntities().iterator();
    	
    	while (entities.hasNext()) {
    		LevelEntity entityTemplate = entities.next();
    		
    		if (entityTemplate.getType() == LevelEntity.TYPE_STATIC_GEOMETRY) {
    			// On crée une entité statique
    			// TODO: Stocker l'entité?
    			StaticSceneObject entity = new StaticSceneObject(entityTemplate.getProperty("ref_name"), entityTemplate.getProperty("resource"));
    			entity.setPosition(Float.parseFloat(entityTemplate.getProperty("pos_x")), -Float.parseFloat(entityTemplate.getProperty("pos_y")) - entity.getTexture().getHeight());
    			entity.setDensity(Float.parseFloat(entityTemplate.getProperty("density", "20.0")));
    			
    			if (entityTemplate.getProperty("isDynamic", "false").equals("false"))
    				entity.getPhysicsBody().setType(BodyType.StaticBody);
    			
    			MainActivity.mStage.addActor(entity);
    			
    			Log.d("LevelLoader", "Loaded entity at " + entityTemplate.getProperty("pos_x") + "," + entityTemplate.getProperty("pos_y"));
    		} else {
    			Log.e("LevelLoader", "Unhandled entity type: " + entityTemplate.getType());
    		}
    	}
  
    }
    
    
    /**
     * Charge un schema de niveau en fonction d'un fichier. Ces fichiers sont generes par Nyuu :3
     * @param path Chemin du fichier, relatif au dossier "levels"
     * @return Schema de niveau avec toutes les entites
     * @throws JSONException
     */
    public Level loadScheme(String path) throws JSONException {
        JSONObject json_root = new JSONObject(Gdx.files.internal("levels/" + path).readString());
        
        Level l = new Level();
        
        // Lecture des entites
        JSONObject json_entities = json_root.getJSONObject("entities");
        Iterator<?> it_entities = json_entities.keys();
        
        while (it_entities.hasNext()) {
            JSONObject json_entity = json_entities.getJSONObject((String)it_entities.next());
            JSONObject json_properties = json_entity.getJSONObject("properties");
            
            LevelEntity ent = new LevelEntity(json_entity.getInt("type"));
            
            Log.d("LevelLoader", "=== Loading entity ===");
            
            // Lecture des proprietes de l'entite
            Iterator<?> it_properties = json_properties.keys();
            while (it_properties.hasNext()) {
                String key = (String)it_properties.next();
                ent.setProperty(key, json_properties.getString(key));
                Log.d("LevelLoader", "==> " + key + " = " + json_properties.getString(key));
            }
            
            // Ajout de l'entite au level
            l.addEntity(ent);
            
            Log.d("LevelLoader", "=== Done loading entity ===");
        }
        
        return l;
    }
}
