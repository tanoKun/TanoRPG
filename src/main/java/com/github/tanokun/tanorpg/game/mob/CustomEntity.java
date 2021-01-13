package com.github.tanokun.tanorpg.game.mob;

import com.github.tanokun.tanorpg.game.item.CustomItemManager;
import com.github.tanokun.tanorpg.game.player.status.Status;
import com.github.tanokun.tanorpg.game.player.status.StatusType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CustomEntity {
    private CustomEntityDropItems dropItems;
    private String name;
    private EntityType entityType;
    private int HP = 20;
    private int LEVEL = 1;
    private long EXP = 0;

    private String mainHand = "";
    private String offHand = "";
    private String helmet = "";
    private String chestPlate = "";
    private String leggings = "";
    private String boots = "";

    private HashMap<StatusType, Status> statuses = new HashMap<>();
    private double ATK = 0;
    private double DEF = 0;
    private double MATK = 0;
    private double MDEF = 0;
    private double AGI = 0;
    private double ING = 0;
    private double INT = 0;

    public CustomEntity(String name, EntityType entityType, int LEVEL, int HP, long EXP){
        this.name = name;
        this.entityType = entityType;
        this.HP = HP;
        this.LEVEL = LEVEL;
        this.EXP = EXP;
    }

    public void setDropItems(CustomEntityDropItems dropItems) {this.dropItems = dropItems;}
    public void setArmors(String mainHand, String offHand, String helmet, String chestPlate, String leggings, String boots) {
        this.mainHand = mainHand;
        this.offHand = offHand;
        this.helmet = helmet;
        this.chestPlate = chestPlate;
        this.leggings = leggings;
        this.boots = boots;
    }

    public String getName() {return name;}
    public EntityType getEntityType() {return entityType;}
    public int getHP() {return HP;}
    public int getLEVEL() {return LEVEL;}
    public long getEXP() {return EXP;}
    public double getATK() {return statuses.get(StatusType.ATK).getLevel();}
    public double getDEF() {return statuses.get(StatusType.DEF).getLevel();}
    public double getMATK() {return statuses.get(StatusType.MATK).getLevel();}
    public double getMDEF() {return statuses.get(StatusType.MDEF).getLevel();}
    public double getAGI() {return statuses.get(StatusType.AGI).getLevel();}
    public double getING() {return statuses.get(StatusType.ING).getLevel();}
    public double getINT() {return statuses.get(StatusType.INT).getLevel();}

    public void setStatuses(double i, double i2, double i3, double i4, double i5, double i6, double i7){
        statuses.put(StatusType.ATK, new Status(StatusType.ATK, i));
        statuses.put(StatusType.DEF, new Status(StatusType.DEF, i2));
        statuses.put(StatusType.MATK, new Status(StatusType.MATK, i3));
        statuses.put(StatusType.MDEF, new Status(StatusType.MDEF, i4));
        statuses.put(StatusType.AGI, new Status(StatusType.AGI, i5));
        statuses.put(StatusType.ING, new Status(StatusType.ING, i6));
        statuses.put(StatusType.INT, new Status(StatusType.INT, i7));
    }

    public CustomEntityDropItems getDropItems() {return dropItems;}

    public Creature spawnEntity(Location location){
        Creature entity = (Creature) location.getWorld().spawnEntity(location, entityType);
        entity.setCustomName(name + " §7[§dLv:§e" + LEVEL + "§7]");
        if (!(mainHand.equals(""))){entity.getEquipment().setItemInMainHand(CustomItemManager.getCustomItem(mainHand).getItem());}
        if (!(offHand.equals(""))){entity.getEquipment().setItemInOffHand(CustomItemManager.getCustomItem(offHand).getItem());}
        if (!(helmet.equals(""))){entity.getEquipment().setHelmet(CustomItemManager.getCustomItem(helmet).getItem());}
        if (!(chestPlate.equals(""))){entity.getEquipment().setChestplate(CustomItemManager.getCustomItem(chestPlate).getItem());}
        if (!(leggings.equals(""))){entity.getEquipment().setLeggings(CustomItemManager.getCustomItem(leggings).getItem());}
        if (!(boots.equals(""))){entity.getEquipment().setBoots(CustomItemManager.getCustomItem(boots).getItem());}
        entity.setMaxHealth(HP);
        entity.setHealth(HP);
        return entity;
    }
    public void giveSpawnerEntity(Player player){
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"give " + player.getName() + " minecraft:mob_spawner 1 0 {\"display\":{\"Name\":\"" + getName() + "\"},\"BlockEntityTag\":{\"id\":\"MobSpawner\",\"SpawnCount\":1s,\"SpawnRange\":2s,\"MaxNearbyEntities\":16s,\"Delay\":-1s,\"MinSpawnDelay\":150s,\"MaxSpawnDelay\":300s,\"RequiredPlayerRange\":15s,\"SpawnPotentials\":[{\"Weight\":1,\"Entity\":{\"CustomName\":\"" + name + "\",\"CustomNameVisible\":1,\"Tags\":[\"entitymarker\"],\"id\":\"villager\"}}]}}");
    }

    public static boolean chance(double percent) {
        double count = percent / 100;
        if (Math.random() <= count) {
            return true;
        }
        return false;
    }
}
