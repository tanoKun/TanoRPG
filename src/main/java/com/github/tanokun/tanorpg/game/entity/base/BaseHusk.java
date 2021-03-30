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
import org.bukkit.entity.Husk;
import org.bukkit.metadata.FixedMetadataValue;

public class BaseHusk extends ObjectEntity {

    public BaseHusk(Config config) {
        super(config);
    }

    @Override
    public Entity spawn(Location location) {
        Husk husk = (Husk) location.getWorld().spawnEntity(location, EntityType.HUSK);
        husk.setBaby(false);
        setOptions(husk);
        ActiveEntity activeEntity = new ActiveEntity(this, husk);
        EntityManager.registerActiveEntity(activeEntity);
        return husk;
    }

    @Override
    public Entity setOptions(Entity entity) {
        Husk husk = (Husk) entity;

        husk.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getHP());
        husk.setHealth(getHP());

        husk.setCustomName(getName() + " §7[§dLv:§e" + getLEVEL() + "§7] " + "§a❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘");
        if (!(getMainHand().equals(""))){husk.getEquipment().setItemInMainHand(ItemManager.getItem(getMainHand()).getItem());}
        if (!(getOffHand().equals(""))){husk.getEquipment().setItemInOffHand(ItemManager.getItem(getOffHand()).getItem());}
        if (!(getHelmet().equals(""))){husk.getEquipment().setHelmet(ItemManager.getItem(getHelmet()).getItem());}
        if (!(getChestPlate().equals(""))){husk.getEquipment().setChestplate(ItemManager.getItem(getChestPlate()).getItem());}
        if (!(getLeggings().equals(""))){husk.getEquipment().setLeggings(ItemManager.getItem(getLeggings()).getItem());}
        if (!(getBoots().equals(""))){husk.getEquipment().setBoots(ItemManager.getItem(getBoots()).getItem());}

        husk.setCustomNameVisible(true);
        husk.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(getSpeed());
        husk.setMetadata("TanoRPG_entity", new FixedMetadataValue(TanoRPG.getPlugin(), true));
        return entity;
    }
}
