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
import org.bukkit.entity.Skeleton;
import org.bukkit.metadata.FixedMetadataValue;

public class BaseSkeleton extends ObjectEntity {
    public BaseSkeleton(Config config) {
        super(config);
    }

    @Override
    public Entity spawn(Location location) {
        Skeleton skeleton = (Skeleton) location.getWorld().spawnEntity(location, EntityType.SKELETON);
        setOptions(skeleton);
        ActiveEntity activeEntity = new ActiveEntity(this, skeleton);
        EntityManager.registerActiveEntity(activeEntity);
        return skeleton;
    }

    @Override
    public Entity setOptions(Entity entity) {
        Skeleton skeleton = (Skeleton) entity;

        skeleton.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getHP());
        skeleton.setHealth(getHP());

        skeleton.setCustomName(getName() + " §7[§dLv:§e" + getLEVEL() + "§7] " + "§a❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘");
        if (!(getMainHand().equals(""))){skeleton.getEquipment().setItemInMainHand(ItemManager.getItem(getMainHand()).getItem());}
        if (!(getOffHand().equals(""))){skeleton.getEquipment().setItemInOffHand(ItemManager.getItem(getOffHand()).getItem());}
        if (!(getHelmet().equals(""))){skeleton.getEquipment().setHelmet(ItemManager.getItem(getHelmet()).getItem());}
        if (!(getChestPlate().equals(""))){skeleton.getEquipment().setChestplate(ItemManager.getItem(getChestPlate()).getItem());}
        if (!(getLeggings().equals(""))){skeleton.getEquipment().setLeggings(ItemManager.getItem(getLeggings()).getItem());}
        if (!(getBoots().equals(""))){skeleton.getEquipment().setBoots(ItemManager.getItem(getBoots()).getItem());}

        skeleton.setCustomNameVisible(true);
        skeleton.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(getSpeed());
        skeleton.setMetadata("TanoRPG_entity", new FixedMetadataValue(TanoRPG.getPlugin(), true));
        return entity;
    }
}
