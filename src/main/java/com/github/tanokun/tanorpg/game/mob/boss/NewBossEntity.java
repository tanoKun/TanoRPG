package com.github.tanokun.tanorpg.game.mob.boss;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.mob.CustomEntity;
import com.github.tanokun.tanorpg.game.mob.NewEntity;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Arrays;

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
        Location loc = getCreature().getLocation();
        for(Entity sound : TanoRPG.getNearbyEntities(loc, 30)){
            if (!(sound instanceof Player)) continue;
            Player player = (Player) sound;
            player.playSound(loc, Sound.ENTITY_WITHER_SPAWN, 3, 1);
        }
    }
}
