package com.github.tanokun.tanorpg.game.mob.boss;

import com.github.tanokun.tanorpg.game.mob.CustomEntity;
import com.github.tanokun.tanorpg.game.mob.NewEntity;
import org.bukkit.entity.Creature;

public class NewBossEntity extends NewEntity {
    private Boss boss;
    private boolean start = false;

    public NewBossEntity(Creature entity, CustomEntity customEntity, Boss boss) {
        super(entity, customEntity);
        this.boss = boss;
    }

    public void start() {
        if (start == true) return;
        start = true;
    }
}
