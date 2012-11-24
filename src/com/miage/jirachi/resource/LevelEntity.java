package com.miage.jirachi.resource;

import java.util.Map;

public class LevelEntity {
    protected int mType;
    protected Map<String, String> mProperties;
    
    LevelEntity(int type) {
        mType = type;
    }
    
    void setProperty(String key, String value) {
        mProperties.put(key, value);
    }
    
    String getProperty(String key) {
        return mProperties.get(key);
    }
}
