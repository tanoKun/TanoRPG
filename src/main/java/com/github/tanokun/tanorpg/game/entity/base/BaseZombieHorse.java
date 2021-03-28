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
import org.bukkit.entity.ZombieHorse;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class BaseZombieHorse extends ObjectEntity {

    private String horseType;

    private String horseColor;

    private boolean horseSaddled;

    private boolean horseTamed;

    public BaseZombieHorse(Config config) {
        super(config);
        this.horseType = config.getConfig().getString("Options.type");
        this.horseColor = config.getConfig().getString("Options.color", this.horseColor);
        this.horseSaddled = config.getConfig().getBoolean("Options.saddle", this.horseSaddled);
        this.horseTamed = config.getConfig().getBoolean("Options.tamed", this.horseTamed);
    }

    @Override
    public Entity spawn(Location location) {
        ZombieHorse entity = (ZombieHorse) location.getWorld().spawnEntity(location, EntityType.ZOMBIE_HORSE);
        setOptions(entity);
        ActiveEntity activeEntity = new ActiveEntity(this, entity);
        EntityManager.registerActiveEntity(activeEntity);
        return entity;
    }

    @Override
    public Entity setOptions(Entity entity) {
        ZombieHorse target = (ZombieHorse) entity;
        target.setCustomName(getName() + " §7[§dLv:§e" + getLEVEL() + "§7]");
        target.setCustomNameVisible(true);
        AbstractHorseInventory hi = target.getInventory();
        target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(getSpeed());
        target.setMetadata("TanoRPG_entity", new FixedMetadataValue(TanoRPG.getPlugin(), true));
        if (this.horseType != null)
            target.setVariant(Horse.Variant.valueOf(this.horseType.toUpperCase()));
        if (this.horseTamed)
            target.setTamed(true);
        if (this.horseSaddled)
            hi.setSaddle(new ItemStack(Material.SADDLE, 1));
        return entity;
    }
}
