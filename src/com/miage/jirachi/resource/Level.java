package com.miage.jirachi.resource;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private List<LevelEntity> mEntities;
    
    public Level() {
        mEntities = new ArrayList<LevelEntity>();
    }
    
    public void addEntity(LevelEntity entity) {
        mEntities.add(entity);
    }
    
    public List<LevelEntity> getEntities() {
        return mEntities;
    }
}
