package com.github.tanokun.tanorpg.game.player.status.buff;

import org.bukkit.entity.Entity;

public class BuffSelf {
    private BuffType type;
    private Entity owner;
    private long seconds;
    long temp_seconds = 0;

    private boolean start = true;

    public BuffSelf(BuffType type, Entity owner, long seconds){
        this.type = type;
        this.owner = owner;
        this.seconds = seconds;
    }

    public BuffType getType() {return type;}
    public Entity getOwner() {return owner;}
    public long getSeconds() {return seconds;}

    public void start(){start = true;}
    public void stop(){start = false;}
}
