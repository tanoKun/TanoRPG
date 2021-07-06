package com.github.tanokun.tanorpg.event.tanorpg;

import com.github.tanokun.tanorpg.game.entity.base.ObjectEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class TanoRpgEntityKillEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    private final Entity entity;

    private final ObjectEntity objectEntity;

    public TanoRpgEntityKillEvent(Player who, Entity entity, ObjectEntity objectEntity) {
        super(who);
        this.entity = entity;
        this.objectEntity = objectEntity;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public Entity getEntity() {
        return entity;
    }

    public ObjectEntity getObjectEntity() {
        return objectEntity;
    }
}
