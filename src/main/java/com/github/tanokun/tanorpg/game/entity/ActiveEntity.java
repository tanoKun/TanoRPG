package com.github.tanokun.tanorpg.game.entity;

import com.github.tanokun.tanorpg.game.entity.base.ObjectEntity;
import com.github.tanokun.tanorpg.player.status.StatusType;
import org.bukkit.entity.Entity;

public class ActiveEntity {
    private final ObjectEntity objectEntity;

    private final Entity entity;

    private int hasHP;

    public ActiveEntity(ObjectEntity objectEntity, Entity entity) {
        this.objectEntity = objectEntity;
        this.entity = entity;
        this.hasHP = objectEntity.getStatusMap().getStatus(StatusType.HP);
    }

    public ObjectEntity getObjectEntity() {
        return objectEntity;
    }

    public int getHasHP() {
        return hasHP;
    }

    public int setHasHP(int hasHP) {
        this.hasHP = hasHP;
        return this.hasHP;
    }
}
