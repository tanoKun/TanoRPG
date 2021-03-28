package com.github.tanokun.tanorpg.game.entity.base;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.ActiveEntity;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.util.io.Config;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.metadata.FixedMetadataValue;

public class BaseDrowned extends ObjectEntity {

    public BaseDrowned(Config config) {
        super(config);
    }

    @Override
    public Entity spawn(Location location) {
        Drowned drowned = (Drowned) location.getWorld().spawnEntity(location, EntityType.DROWNED);
        drowned.setBaby(false);
        setOptions(drowned);
        ActiveEntity activeEntity = new ActiveEntity(this, drowned);
        EntityManager.registerActiveEntity(activeEntity);
        return drowned;
    }

    @Override
    public Entity setOptions(Entity entity) {
        Drowned drowned = (Drowned) entity;
        drowned.setCustomName(getName() + " §7[§dLv:§e" + getLEVEL() + "§7]");
        drowned.setCustomNameVisible(true);
        drowned.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(getSpeed());
        drowned.setMetadata("TanoRPG_entity", new FixedMetadataValue(TanoRPG.getPlugin(), true));
        return entity;
    }
}
