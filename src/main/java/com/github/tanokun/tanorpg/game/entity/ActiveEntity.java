package com.github.tanokun.tanorpg.game.entity;

import com.github.tanokun.tanorpg.game.entity.base.ObjectEntity;
import org.bukkit.entity.Entity;

public class ActiveEntity {
    private final ObjectEntity objectEntity;

    private final Entity entity;

    public ActiveEntity(ObjectEntity objectEntity, Entity entity) {
        this.objectEntity = objectEntity;
        this.entity = entity;
    }

    public ObjectEntity getObjectEntity() {
        return objectEntity;
    }
}
