package com.github.tanokun.tanorpg.game.entity.base;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.ActiveEntity;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.util.io.Config;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Husk;
import org.bukkit.metadata.FixedMetadataValue;

public class BaseGuardian extends ObjectEntity {
    public BaseGuardian(Config config) {
        super(config);
    }

    @Override
    public Entity spawn(Location location) {
        Guardian guardian = (Guardian) location.getWorld().spawnEntity(location, EntityType.GUARDIAN);
        setOptions(guardian);
        ActiveEntity activeEntity = new ActiveEntity(this, guardian);
        EntityManager.registerActiveEntity(activeEntity);
        return guardian;
    }

    @Override
    public Entity setOptions(Entity entity) {
        Guardian guardian = (Guardian) entity;
        guardian.setCustomName(getName() + " §7[§dLv:§e" + getLEVEL() + "§7]");
        guardian.setCustomNameVisible(true);
        guardian.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(getSpeed());
        guardian.setMetadata("TanoRPG_entity", new FixedMetadataValue(TanoRPG.getPlugin(), true));
        return entity;
    }
}
