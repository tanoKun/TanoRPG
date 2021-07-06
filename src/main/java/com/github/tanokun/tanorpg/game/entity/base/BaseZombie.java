package com.github.tanokun.tanorpg.game.entity.base;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.ActiveEntity;
import com.github.tanokun.tanorpg.game.entity.EntityDropItems;
import com.github.tanokun.tanorpg.game.item.type.base.ItemData;
import com.github.tanokun.tanorpg.player.EquipmentMap;
import com.github.tanokun.tanorpg.player.status.StatusMap;
import com.github.tanokun.tanorpg.player.status.StatusType;
import com.github.tanokun.tanorpg.util.io.Config;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EntityZombie;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.metadata.FixedMetadataValue;

public class BaseZombie extends ObjectEntity {

    public BaseZombie(Config entityConfig, String name, StatusMap statusMap, EquipmentMap equipMap, EntityDropItems dropItems, int exp, int level) {
        super(entityConfig, name, statusMap, equipMap, dropItems, exp, level);
    }

    @Override
    public Entity spawn(Location location) {
        CraftWorld craftWorld = (CraftWorld) location.getWorld();
        Zombie entity = new CraftZombie((CraftServer) Bukkit.getServer(),
                new EntityZombie(EntityTypes.ZOMBIE, craftWorld.getHandle().getMinecraftWorld()));
        entity = craftWorld.spawn(location, entity.getClass());
        entity.setBaby(false);

        entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(
                entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() * (1 + (getStatusMap().getStatus(StatusType.SPEED) / 100)));

        entity.setCustomName(getName() + " §7[§dLv:§e" + getHasLevel() + "§7] " + "§a❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘");
        entity.setCustomNameVisible(true);
        entity.setMetadata("TanoRPG_entity", new FixedMetadataValue(TanoRPG.getPlugin(), new ActiveEntity(this, entity)));

        entity.getEquipment().setItemInMainHand(getEquipMap().getEquip(EquipmentMap.EquipmentType.MAIN));
        entity.getEquipment().setItemInOffHand(getEquipMap().getEquip(EquipmentMap.EquipmentType.SUB));
        entity.getEquipment().setHelmet(getEquipMap().getEquip(EquipmentMap.EquipmentType.HELMET));
        entity.getEquipment().setChestplate(getEquipMap().getEquip(EquipmentMap.EquipmentType.CHESTPLATE));
        entity.getEquipment().setLeggings(getEquipMap().getEquip(EquipmentMap.EquipmentType.LEGGINGS));
        entity.getEquipment().setBoots(getEquipMap().getEquip(EquipmentMap.EquipmentType.BOOTS));
        return entity;
    }
}
