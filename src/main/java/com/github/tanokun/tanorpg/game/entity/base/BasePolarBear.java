package com.github.tanokun.tanorpg.game.entity.base;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.ActiveEntity;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.util.io.Config;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PolarBear;
import org.bukkit.metadata.FixedMetadataValue;

public class BasePolarBear extends ObjectEntity {

    public BasePolarBear(Config config) {
        super(config);
    }

    @Override
    public Entity spawn(Location location) {
        PolarBear entity = (PolarBear) location.getWorld().spawnEntity(location, EntityType.POLAR_BEAR);
        setOptions(entity);
        ActiveEntity activeEntity = new ActiveEntity(this, entity);
        EntityManager.registerActiveEntity(activeEntity);
        return entity;
    }

    @Override
    public Entity setOptions(Entity entity) {
        PolarBear target = (PolarBear) entity;

        target.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getHP());
        target.setHealth(getHP());

        target.setCustomName(getName() + " §7[§dLv:§e" + getLEVEL() + "§7] " + "§a❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘");
        if (!(getMainHand().equals(""))){target.getEquipment().setItemInMainHand(ItemManager.getItem(getMainHand()).getItem());}
        if (!(getOffHand().equals(""))){target.getEquipment().setItemInOffHand(ItemManager.getItem(getOffHand()).getItem());}
        if (!(getHelmet().equals(""))){target.getEquipment().setHelmet(ItemManager.getItem(getHelmet()).getItem());}
        if (!(getChestPlate().equals(""))){target.getEquipment().setChestplate(ItemManager.getItem(getChestPlate()).getItem());}
        if (!(getLeggings().equals(""))){target.getEquipment().setLeggings(ItemManager.getItem(getLeggings()).getItem());}
        if (!(getBoots().equals(""))){target.getEquipment().setBoots(ItemManager.getItem(getBoots()).getItem());}

        target.setCustomNameVisible(true);
        target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(getSpeed());
        target.setMetadata("TanoRPG_entity", new FixedMetadataValue(TanoRPG.getPlugin(), true));
        return entity;
    }
}
