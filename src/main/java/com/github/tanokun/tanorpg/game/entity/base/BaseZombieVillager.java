package com.github.tanokun.tanorpg.game.entity.base;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.ActiveEntity;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.util.io.Config;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EntityZombieVillager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftVillagerZombie;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;

public class BaseZombieVillager extends ObjectEntity {

    public BaseZombieVillager(Config config) {
        super(config);
    }

    @Override
    public Entity spawn(Location location) {
        CraftWorld craftWorld = (CraftWorld) location.getWorld();
        ZombieVillager zombie = new CraftVillagerZombie((CraftServer) Bukkit.getServer(),
                new EntityZombieVillager(EntityTypes.ZOMBIE_VILLAGER, craftWorld.getHandle().getMinecraftWorld()));
        craftWorld.spawn(location, zombie.getClass());
        zombie.setBaby(false);
        setOptions(zombie);
        ActiveEntity activeEntity = new ActiveEntity(this, zombie);
        EntityManager.registerActiveEntity(activeEntity);
        return zombie;
    }

    @Override
    public Entity setOptions(Entity entity) {
        Zombie zombie = (Zombie) entity;

        zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getHP());
        zombie.setHealth(getHP());

        zombie.setCustomName(getName() + " §7[§dLv:§e" + getLEVEL() + "§7] " + "§a❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘");
        if (!(getMainHand().equals(""))){zombie.getEquipment().setItemInMainHand(ItemManager.getItem(getMainHand()).getItem());}
        if (!(getOffHand().equals(""))){zombie.getEquipment().setItemInOffHand(ItemManager.getItem(getOffHand()).getItem());}
        if (!(getHelmet().equals(""))){zombie.getEquipment().setHelmet(ItemManager.getItem(getHelmet()).getItem());}
        if (!(getChestPlate().equals(""))){zombie.getEquipment().setChestplate(ItemManager.getItem(getChestPlate()).getItem());}
        if (!(getLeggings().equals(""))){zombie.getEquipment().setLeggings(ItemManager.getItem(getLeggings()).getItem());}
        if (!(getBoots().equals(""))){zombie.getEquipment().setBoots(ItemManager.getItem(getBoots()).getItem());}

        zombie.setCustomNameVisible(true);
        zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(getSpeed());
        zombie.setMetadata("TanoRPG_entity", new FixedMetadataValue(TanoRPG.getPlugin(), true));
        return entity;
    }
}
