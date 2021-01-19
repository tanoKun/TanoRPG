package com.github.tanokun.tanorpg.game.mob;

import org.bukkit.entity.Creature;

public class NewEntity {
    private Creature entity;
    private CustomEntity customEntity;

    public NewEntity(Creature entity, CustomEntity customEntity){
        this.entity = entity;
        this.customEntity = customEntity;
    }

    public Creature getCreature() {return entity;}
    public CustomEntity getCustomEntity() {return customEntity;}
}
