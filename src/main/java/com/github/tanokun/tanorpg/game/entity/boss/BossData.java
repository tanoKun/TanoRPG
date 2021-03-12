package com.github.tanokun.tanorpg.game.entity.boss;

import com.github.tanokun.tanorpg.game.entity.EntityData;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;

public class BossData extends EntityData {
    public BossData(String name, EntityType entityType, int LEVEL, int HP, long EXP) {super(name, entityType, LEVEL, HP, EXP);}

    public Creature spawnEntity(Location location) {throw new NullPointerException("This method doesn't exist");}
}