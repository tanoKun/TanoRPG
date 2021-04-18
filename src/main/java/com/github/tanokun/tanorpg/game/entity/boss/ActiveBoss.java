package com.github.tanokun.tanorpg.game.entity.boss;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.ActiveEntity;
import com.github.tanokun.tanorpg.game.entity.base.ObjectEntity;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class ActiveBoss extends ActiveEntity {
    public ActiveBoss(ObjectEntity e, Entity entity) {
        super(e, entity);
        entity.setCustomName("§c" + e.getName() + " §7[§dLv:§e" + e.getLEVEL() + "§7] " + "§a❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘");
        entity.setMetadata("TanoRPG_entity_boss", new FixedMetadataValue(TanoRPG.getPlugin(), true));
        TanoRPG.playSound((Player[]) Bukkit.getOnlinePlayers().toArray(), Sound.ENTITY_WITHER_SPAWN, 3, 1);
    }
}
