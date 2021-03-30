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
import org.bukkit.entity.Spider;
import org.bukkit.metadata.FixedMetadataValue;

public class BaseSpider extends ObjectEntity {

    public BaseSpider(Config config) {
        super(config);
    }

    @Override
    public Entity spawn(Location location) {
        Spider spider = (Spider) location.getWorld().spawnEntity(location, EntityType.SPIDER);
        setOptions(spider);
        ActiveEntity activeEntity = new ActiveEntity(this, spider);
        EntityManager.registerActiveEntity(activeEntity);
        return spider;
    }

    @Override
    public Entity setOptions(Entity entity) {
        Spider spider = (Spider) entity;

        spider.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getHP());
        spider.setHealth(getHP());

        spider.setCustomName(getName() + " §7[§dLv:§e" + getLEVEL() + "§7] " + "§a❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘");
        if (!(getMainHand().equals(""))){spider.getEquipment().setItemInMainHand(ItemManager.getItem(getMainHand()).getItem());}
        if (!(getOffHand().equals(""))){spider.getEquipment().setItemInOffHand(ItemManager.getItem(getOffHand()).getItem());}
        if (!(getHelmet().equals(""))){spider.getEquipment().setHelmet(ItemManager.getItem(getHelmet()).getItem());}
        if (!(getChestPlate().equals(""))){spider.getEquipment().setChestplate(ItemManager.getItem(getChestPlate()).getItem());}
        if (!(getLeggings().equals(""))){spider.getEquipment().setLeggings(ItemManager.getItem(getLeggings()).getItem());}
        if (!(getBoots().equals(""))){spider.getEquipment().setBoots(ItemManager.getItem(getBoots()).getItem());}

        spider.setCustomNameVisible(true);
        spider.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(getSpeed());
        spider.setMetadata("TanoRPG_entity", new FixedMetadataValue(TanoRPG.getPlugin(), true));
        return entity;
    }
}
