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
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class BaseHorse extends ObjectEntity {

    private String horseStyle;

    private String horseType;

    private String horseColor;

    private String horseArmor;

    private boolean horseChest;

    private boolean horseSaddled;

    private boolean horseTamed;

    public BaseHorse(Config config) {
        super(config);
        this.horseArmor = config.getConfig().getString("Options.armor");
        this.horseChest = config.getConfig().getBoolean("Options.carryingChest", false);
        this.horseStyle = config.getConfig().getString("Options.style");
        this.horseType = config.getConfig().getString("Options.type");
        this.horseColor = config.getConfig().getString("Options.color", this.horseColor);
        this.horseSaddled = config.getConfig().getBoolean("Options.saddle", this.horseSaddled);
        this.horseTamed = config.getConfig().getBoolean("Options.tamed", this.horseTamed);
    }

    @Override
    public Entity spawn(Location location) {
        Horse entity = (Horse) location.getWorld().spawnEntity(location, EntityType.HORSE);
        setOptions(entity);
        ActiveEntity activeEntity = new ActiveEntity(this, entity);
        EntityManager.registerActiveEntity(activeEntity);
        return entity;
    }

    @Override
    public Entity setOptions(Entity entity) {
        Horse target = (Horse) entity;

        target.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getHP());
        target.setHealth(getHP());

        target.setCustomName(getName() + " §7[§dLv:§e" + getLEVEL() + "§7] "  + "§a❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘");
        target.setCustomNameVisible(true);
        HorseInventory hi = target.getInventory();
        target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(getSpeed());
        target.setMetadata("TanoRPG_entity", new FixedMetadataValue(TanoRPG.getPlugin(), true));
        if (this.horseStyle != null)
            target.setStyle(Horse.Style.valueOf(this.horseStyle.toUpperCase()));
        if (this.horseType != null)
            target.setVariant(Horse.Variant.valueOf(this.horseType.toUpperCase()));
        if (this.horseColor != null)
            target.setColor(Horse.Color.valueOf(this.horseColor.toUpperCase()));
        if (this.horseTamed)
            target.setTamed(true);
        if (this.horseSaddled)
            hi.setSaddle(new ItemStack(Material.SADDLE, 1, (short)0));
        if (this.horseArmor != null) {
            switch (this.horseArmor) {
                case "diamond":
                    hi.setArmor(new ItemStack(Material.DIAMOND_HORSE_ARMOR, 1));
                    return target;
                case "gold":
                case "golden":
                    hi.setArmor(new ItemStack(Material.GOLDEN_HORSE_ARMOR, 1));
                    return target;
            }
            hi.setArmor(new ItemStack(Material.IRON_HORSE_ARMOR, 1));
        }
        return entity;
    }
}
