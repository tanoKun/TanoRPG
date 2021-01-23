package com.github.tanokun.tanorpg.game.mob.boss.model;

import com.github.tanokun.tanorpg.game.mob.boss.Boss;
import org.bukkit.entity.EntityType;

public class GoblinBoss extends Boss {
    public GoblinBoss() {
        super("ゴブリントロルの親玉", EntityType.ZOMBIE_VILLAGER, 5, 300, 50);
        setStatuses(100, 40, 0, 40, 0, 0, 0);
    }
}
