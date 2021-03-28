package com.github.tanokun.tanorpg.game.entity.base;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.ActiveEntity;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.util.io.Config;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Zombie;
import org.bukkit.metadata.FixedMetadataValue;

public class BaseBabyZombie extends ObjectEntity {

    public BaseBabyZombie(Config config) {
        super(config);
    }

    @Override
    public Entity spawn(Location location) {
        Zombie zombie = (Zombie) location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
        zombie.setBaby(true);
        setOptions(zombie);
        ActiveEntity activeEntity = new ActiveEntity(this, zombie);
        EntityManager.registerActiveEntity(activeEntity);
        return zombie;
    }

    @Override
    public Entity setOptions(Entity entity) {
        Zombie zombie = (Zombie) entity;
        zombie.setCustomName(getName() + " §7[§dLv:§e" + getLEVEL() + "§7]");
        zombie.setCustomNameVisible(true);
        zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(getSpeed());
        zombie.setMetadata("TanoRPG_entity", new FixedMetadataValue(TanoRPG.getPlugin(), true));
        return entity;
    }
}
