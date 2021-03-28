package com.github.tanokun.tanorpg.game.entity.base;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.ActiveEntity;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.util.io.Config;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class BaseSkeletonHorse extends ObjectEntity {

    private boolean horseSaddled;

    private boolean horseTamed;

    public BaseSkeletonHorse(Config config) {
        super(config);
        this.horseSaddled = config.getConfig().getBoolean("Options.saddle", this.horseSaddled);
        this.horseTamed = config.getConfig().getBoolean("Options.tamed", this.horseTamed);
    }

    @Override
    public Entity spawn(Location location) {
        SkeletonHorse entity = (SkeletonHorse) location.getWorld().spawnEntity(location, EntityType.SKELETON_HORSE);
        setOptions(entity);
        ActiveEntity activeEntity = new ActiveEntity(this, entity);
        EntityManager.registerActiveEntity(activeEntity);
        return entity;
    }

    @Override
    public Entity setOptions(Entity entity) {
        SkeletonHorse target = (SkeletonHorse) entity;
        target.setCustomName(getName() + " §7[§dLv:§e" + getLEVEL() + "§7]");
        target.setCustomNameVisible(true);
        AbstractHorseInventory hi = target.getInventory();
        target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(getSpeed());
        target.setMetadata("TanoRPG_entity", new FixedMetadataValue(TanoRPG.getPlugin(), true));
        if (this.horseTamed)
            target.setTamed(true);
        if (this.horseSaddled)
            hi.setSaddle(new ItemStack(Material.SADDLE, 1, (short) 0));
        return entity;
    }
}
