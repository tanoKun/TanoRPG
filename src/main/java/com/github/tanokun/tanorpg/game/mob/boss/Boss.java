package com.github.tanokun.tanorpg.game.mob.boss;

import com.github.tanokun.tanorpg.game.item.CustomItemManager;
import com.github.tanokun.tanorpg.game.mob.CustomEntity;
import com.github.tanokun.tanorpg.game.mob.CustomEntityManager;
import com.github.tanokun.tanorpg.game.mob.NewEntity;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;

abstract public class Boss extends CustomEntity {
    public Boss(String name, EntityType entityType, int LEVEL, int HP, long EXP) {
        super(name, entityType, LEVEL, HP, EXP);
    }

    @Override
    public Creature spawnEntity(Location location){
        Creature entity = (Creature) location.getWorld().spawnEntity(location, super.getEntityType());
        entity.setCustomName("§c" + super.getName() + " §7[§dLv:§e" + super.getLEVEL() + "§7]");
        if (!(super.getMainHand().equals(""))){entity.getEquipment().setItemInMainHand(CustomItemManager.getCustomItem(super.getMainHand()).getItem());}
        if (!(super.getOffHand().equals(""))){entity.getEquipment().setItemInOffHand(CustomItemManager.getCustomItem(super.getOffHand()).getItem());}
        if (!(super.getHelmet().equals(""))){entity.getEquipment().setHelmet(CustomItemManager.getCustomItem(super.getHelmet()).getItem());}
        if (!(super.getChestPlate().equals(""))){entity.getEquipment().setChestplate(CustomItemManager.getCustomItem(super.getChestPlate()).getItem());}
        if (!(super.getLeggings().equals(""))){entity.getEquipment().setLeggings(CustomItemManager.getCustomItem(super.getLeggings()).getItem());}
        if (!(super.getBoots().equals(""))){entity.getEquipment().setBoots(CustomItemManager.getCustomItem(super.getBoots()).getItem());}
        entity.setMaxHealth(super.getHP());
        entity.setHealth(super.getHP());

        NewBossEntity boss = new NewBossEntity(entity, this, this);
        CustomEntityManager.addNewEntity(boss);
        boss.start();
        return entity;
    }

}
