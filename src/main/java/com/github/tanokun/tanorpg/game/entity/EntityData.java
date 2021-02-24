package com.github.tanokun.tanorpg.game.entity;

import com.github.tanokun.tanorpg.game.item.CustomItemManager;
import com.github.tanokun.tanorpg.game.player.status.Status;
import com.github.tanokun.tanorpg.game.player.status.StatusType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import org.bukkit.entity.EntityType;

public class EntityData {
    private EntityDropItems dropItems;
    private String name;
    private EntityType entityType;
    private int HP;
    private int LEVEL;
    private long EXP;

    private String mainHand = "";
    private String offHand = "";
    private String helmet = "";
    private String chestPlate = "";
    private String leggings = "";
    private String boots = "";

    private HashMap<StatusType, Status> statuses = new HashMap<>();

    public EntityData(String name, EntityType entityType, int LEVEL, int HP, long EXP){
        this.name = name;
        this.entityType = entityType;
        this.HP = HP;
        this.LEVEL = LEVEL;
        this.EXP = EXP;
    }

    public void setDropItems(EntityDropItems dropItems) {this.dropItems = dropItems;}
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
        statuses.put(StatusType.DEF, new  Status(StatusType.DEF, i2));
        statuses.put(StatusType.MATK, new Status(StatusType.MATK, i3));
        statuses.put(StatusType.MDEF, new Status(StatusType.MDEF, i4));
        statuses.put(StatusType.AGI, new Status(StatusType.AGI, i5));
        statuses.put(StatusType.ING, new Status(StatusType.ING, i6));
        statuses.put(StatusType.INT, new Status(StatusType.INT, i7));
    }

    public EntityDropItems getDropItems() {return dropItems;}

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
        EntityManager.addEntity(new Entity(entity, this));
        return entity;
    }
    public void giveSpawnerEntity(Player player){
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                "give " + player.getName() + " spawner{" +
                        "display:{" +
                        "Name:'{" +
                        "\"text\":\"" + name +"\"" +
                        "}'" +
                        "},BlockEntityTag:{" +
                        "SpawnCount:1,SpawnRange:2,Delay:-20,MinSpawnDelay:150,MaxSpawnDelay:300,MaxNearbyEntities:10,RequiredPlayerRange:15,SpawnData:" +
                        "{id:\"minecraft:villager\",Tags:[\"entitymarker\"],CustomName:'{" +
                        "\"text\":\"" + name +"\"}'},SpawnPotentials:[{Weight:1,Entity:{" +
                        "id:\"minecraft:villager\",Tags:[\"entitymarker\"],CustomName:'{\"text\":\"" + name + "\"}'}}]}} 1"); }

    public String getMainHand() {return mainHand;}
    public String getOffHand() {return offHand;}
    public String getHelmet() {return helmet;}
    public String getChestPlate() {return chestPlate;}
    public String getLeggings() {return leggings;}
    public String getBoots() {return boots;}
}
