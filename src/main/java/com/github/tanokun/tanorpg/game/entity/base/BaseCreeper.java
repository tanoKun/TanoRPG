package com.github.tanokun.tanorpg.game.entity.base;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.ActiveEntity;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.util.io.Config;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;

public class BaseCreeper extends ObjectEntity {
    private int explosionRadius;
    private int explosionFuse;

    private boolean powered;

    public BaseCreeper(Config config) {
        super(config);
        this.powered = config.getConfig().getBoolean("Options.powered", false);
        this.explosionFuse = config.getConfig().getInt("Options.fuseTicks", -1);
        this.explosionRadius = config.getConfig().getInt("Options.explosionRadius", -1);
    }

    @Override
    public Entity spawn(Location location) {
        Creeper entity = (Creeper) location.getWorld().spawnEntity(location, EntityType.CREEPER);
        setOptions(entity);
        ActiveEntity activeEntity = new ActiveEntity(this, entity);
        EntityManager.registerActiveEntity(activeEntity);
        return entity;
    }

    @Override
    public Entity setOptions(Entity entity) {
        Creeper target = (Creeper) entity;

        target.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getHP());
        target.setHealth(getHP());

        target.setCustomName(getName() + " §7[§dLv:§e" + getLEVEL() + "§7] " + "§a❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘");
        target.setCustomNameVisible(true);
        target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(getSpeed());
        if (this.powered)
            target.setPowered(true);
        if (this.explosionFuse >= 0)
            target.setMaxFuseTicks(this.explosionFuse);
        if (this.explosionRadius >= 0)
            target.setExplosionRadius(this.explosionRadius);
        target.setMetadata("TanoRPG_entity", new FixedMetadataValue(TanoRPG.getPlugin(), true));
        return entity;
    }
}
