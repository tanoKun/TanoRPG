package com.github.tanokun.tanorpg.event.tanorpg;

import com.github.tanokun.tanorpg.game.entity.EntityData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Contract;

public class CustomEntityKillEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final EntityData entityData;
    private final Player attacker;
    private final Entity entity;

    public CustomEntityKillEvent(EntityData entityData, Player attacker, Entity entity) {
        this.entityData = entityData;
        this.attacker = attacker;
        this.entity = entity;
    }

    @Contract(pure = true)
    public static HandlerList getHandlerList() {
        return handlers;
    }
    public HandlerList getHandlers() {
        return handlers;
    }

    public EntityData getEntityData() {return entityData;}
    public Player getAttacker() {return attacker;}
    public Entity getEntity() {return entity;}
}
