package com.miage.jirachi.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;


public class ResourceManager {
    private static ResourceManager mSingleton;
    Map<String, Resource> mResources;
    
    /**
     * Renvoie l'instance unique de la classe
     * @return Instance
     */
    public static ResourceManager getInstance() {
        if (mSingleton == null) {
            mSingleton = new ResourceManager();
        }
        
        return mSingleton;
    }
    
    /**
     * Constructeur par défaut
     */
    public ResourceManager() {
        
    }
    
    /**
     * Renvoie la ressource correpsondant au fichier spécifié. Le fichier sera chargé si il n'est 
     * pas déjà lu et chargé en mémoire.
     * @param filename Fichier .rs a lire
     * @return La ressource du fichier
     */
    public Resource getResource(String filename) {
        // Si on a déjà la ressource, on retourne ce qu'on sait déjà
        if (mResources.containsKey(filename)) {
            return mResources.get(filename);
        } else {
            // La ressource n'a jamais été demandée et n'est donc pas chargée,
            // on le fait.
            FileHandle handle = Gdx.files.internal(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(handle.read()));
            
            // On lit le fichier
            String line = null;
            String fileContents = "";
            try {
                while ((line = reader.readLine()) != null) {
                    fileContents += line;
                }
                
                reader.close();
            } catch (IOException e) {
                Log.e("MIAGICS", "ERROR READING FILE " + filename + " IN RESOURCE MANAGER! " + e.getMessage());
            }
            
            // On le parse et on récupère une resource
            try {
                Resource res = parseResource(fileContents);
                mResources.put(filename, res);
            
                return res;
            }
            catch (JSONException e) {
                Log.e("MIAGICS", "ERROR PARSING JSON OF FILE " + filename + " IN RESOURCE MANAGER! " + e.getMessage());
            }
        }
        
        // Si on arrive ici, c'est que quelque chose s'est vraiment mal passé.
        return null;
    }
    
    /**
     * Traite des données JSON de ressource
     * @param data Les données JSON
     * @return La ressource finale et parsée
     * @throws JSONException 
     */
    private Resource parseResource(String data) throws JSONException {
        JSONObject json_root = new JSONObject(data);
        Resource res = null;
        
        // On instancie le bon type de ressource
        if (json_root.getString("type").equals("animated")) {
            res = new ResourceAnimated();
        }
        else if (json_root.getString("type").equals("static")) {
            res = new Resource();
        }
        else {
            Log.e("MIAGICS", "UNKNOWN RESOURCE TYPE: " + json_root.getString("type"));
        }
        
        // Propriétés communes à tous les types de ressources
        res.file = json_root.getString("file");
        res.type = json_root.getString("type");
        
        // Propriétés spécifiques
        if (res instanceof ResourceAnimated) {
            JSONObject json_anim = json_root.getJSONObject("anim");
            ResourceAnimated ares = (ResourceAnimated)res;
            
            ares.columns = json_anim.getInt("columns");
            ares.lines = json_anim.getInt("lines");
            ares.width = json_anim.getInt("width");
            ares.height = json_anim.getInt("height");
            ares.reverse = json_anim.getBoolean("reverse");
            ares.speed = json_anim.getInt("speed");
            
            // Lecture des animations
            JSONArray json_anims = json_anim.getJSONArray("anims");
            ares.anims = new ArrayList<ResourceAnimated.AnimationDef>();
            
            for (int i = 0; i < json_anims.length(); i++) {
                JSONObject json_anims_entry = json_anims.getJSONObject(i);
                ResourceAnimated.AnimationDef animDef = new ResourceAnimated.AnimationDef();
                
                animDef.name = json_anims_entry.getString("name");
                animDef.start_col = json_anims_entry.getInt("start_col");
                animDef.end_col = json_anims_entry.getInt("end_col");
                animDef.start_line = json_anims_entry.getInt("start_line");
                animDef.end_line = json_anims_entry.getInt("end_line");
                
                ares.anims.add(animDef);
            }
        }
        
        return res;
    }
}
