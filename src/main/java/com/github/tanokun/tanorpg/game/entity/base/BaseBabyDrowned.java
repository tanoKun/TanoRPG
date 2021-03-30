package com.github.tanokun.tanorpg.game.entity.base;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.ActiveEntity;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.util.io.Config;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;

public class BaseBabyDrowned extends ObjectEntity {
    public BaseBabyDrowned(Config config) {
        super(config);
    }

    @Override
    public Entity spawn(Location location) {
        Drowned drowned = (Drowned) location.getWorld().spawnEntity(location, EntityType.DROWNED);
        drowned.setBaby(true);
        setOptions(drowned);
        ActiveEntity activeEntity = new ActiveEntity(this, drowned);
        EntityManager.registerActiveEntity(activeEntity);
        return drowned;
    }

    @Override
    public Entity setOptions(Entity entity) {
        Drowned drowned = (Drowned) entity;

        drowned.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getHP());
        drowned.setHealth(getHP());

        drowned.setCustomName(getName() + " §7[§dLv:§e" + getLEVEL() + "§7] " + "§a❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘");
        if (!(getMainHand().equals(""))){drowned.getEquipment().setItemInMainHand(ItemManager.getItem(getMainHand()).getItem());}
        if (!(getOffHand().equals(""))){drowned.getEquipment().setItemInOffHand(ItemManager.getItem(getOffHand()).getItem());}
        if (!(getHelmet().equals(""))){drowned.getEquipment().setHelmet(ItemManager.getItem(getHelmet()).getItem());}
        if (!(getChestPlate().equals(""))){drowned.getEquipment().setChestplate(ItemManager.getItem(getChestPlate()).getItem());}
        if (!(getLeggings().equals(""))){drowned.getEquipment().setLeggings(ItemManager.getItem(getLeggings()).getItem());}
        if (!(getBoots().equals(""))){drowned.getEquipment().setBoots(ItemManager.getItem(getBoots()).getItem());}

        drowned.setCustomNameVisible(true);
        drowned.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(getSpeed());
        drowned.setMetadata("TanoRPG_entity", new FixedMetadataValue(TanoRPG.getPlugin(), true));
        return entity;
    }
}
