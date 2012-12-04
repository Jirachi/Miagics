package com.miage.jirachi.resource;

import java.util.HashMap;
import java.util.Map;

public class LevelEntity {
    protected int mType;
    protected Map<String, String> mProperties;
    
    public final static int TYPE_DUMMY = 0;
    public final static int TYPE_STATIC_GEOMETRY = 1;
    public final static int TYPE_ANIMATED_GEOMETRY = 2;
    
    LevelEntity(int type) {
        mType = type;
        mProperties = new HashMap<String, String>();
    }
    
    void setProperty(String key, String value) {
        mProperties.put(key, value);
    }
    
    String getProperty(String key) {
        return mProperties.get(key);
    }
    
    String getProperty(String key, String defaultvalue) {
        if (mProperties.containsKey(key))
        	return mProperties.get(key);
        else
        	return defaultvalue;
    }
    
    int getType() {
    	return mType;
    }
}
