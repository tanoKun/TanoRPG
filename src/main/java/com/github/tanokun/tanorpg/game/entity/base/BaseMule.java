package com.github.tanokun.tanorpg.game.entity.base;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.ActiveEntity;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.util.io.Config;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mule;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class BaseMule extends ObjectEntity {

    private boolean horseChest;

    private boolean horseSaddled;

    private boolean horseTamed;

    public BaseMule(Config config) {
        super(config);
        this.horseChest = config.getConfig().getBoolean("Options.chest", false);
        this.horseSaddled = config.getConfig().getBoolean("Options.saddle", false);
        this.horseTamed = config.getConfig().getBoolean("Options.tamed", false);
    }

    @Override
    public Entity spawn(Location location) {
        Mule entity = (Mule) location.getWorld().spawnEntity(location, EntityType.MULE);
        setOptions(entity);
        ActiveEntity activeEntity = new ActiveEntity(this, entity);
        EntityManager.registerActiveEntity(activeEntity);
        return entity;
    }

    @Override
    public Entity setOptions(Entity entity) {
        Mule target = (Mule) entity;
        AbstractHorseInventory hi = target.getInventory();
        target.setCustomName(getName() + " §7[§dLv:§e" + getLEVEL() + "§7]");
        target.setCustomNameVisible(true);
        if (this.horseTamed)
            target.setTamed(true);
        if (this.horseSaddled)
            hi.setSaddle(new ItemStack(Material.SADDLE, 1));
        if (this.horseChest)
            target.setCarryingChest(true);
        target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(getSpeed());
        target.setMetadata("TanoRPG_entity", new FixedMetadataValue(TanoRPG.getPlugin(), true));
        return entity;
    }
}
