package com.github.tanokun.tanorpg.game.entity;

import org.bukkit.entity.Creature;

public class Entity {
    private Creature creature;
    private EntityData entityData;

    public Entity(Creature creature, EntityData entityData){
        this.creature = creature;
        this.entityData = entityData;
    }

    public Creature getCreature() {return creature;}

    public EntityData getEntityData() {return entityData;}
}
