package com.github.tanokun.tanorpg.game.entity;

import com.github.tanokun.tanorpg.game.entity.base.*;
import org.bukkit.entity.Entity;

public class ActiveEntity {
    private ObjectEntity objectEntity;
    private Entity activeEntity;

    public ActiveEntity(ObjectEntity e, Entity entity){
        objectEntity = e;
        activeEntity = entity;
    }

    public Entity getActiveEntity() {
        return activeEntity;
    }
    public ObjectEntity getObjectEntity() {
        return objectEntity;
    }

}
