package com.github.tanokun.tanorpg.game.entity.base;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.ActiveEntity;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.util.io.Config;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.metadata.FixedMetadataValue;

public class BaseWitherSkeleton extends ObjectEntity {

    public BaseWitherSkeleton(Config config) {
        super(config);
    }

    @Override
    public Entity spawn(Location location) {
        WitherSkeleton skeleton = (WitherSkeleton) location.getWorld().spawnEntity(location, EntityType.WITHER_SKELETON);
        setOptions(skeleton);
        ActiveEntity activeEntity = new ActiveEntity(this, skeleton);
        EntityManager.registerActiveEntity(activeEntity);
        return skeleton;
    }

    @Override
    public Entity setOptions(Entity entity) {
        WitherSkeleton skeleton = (WitherSkeleton) entity;
        skeleton.setCustomName(getName() + " §7[§dLv:§e" + getLEVEL() + "§7]");
        skeleton.setCustomNameVisible(true);
        skeleton.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(getSpeed());
        skeleton.setMetadata("TanoRPG_entity", new FixedMetadataValue(TanoRPG.getPlugin(), true));
        return entity;
    }
}
