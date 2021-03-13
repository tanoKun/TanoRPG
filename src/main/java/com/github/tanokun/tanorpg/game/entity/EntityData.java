package com.github.tanokun.tanorpg.game.entity;

import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.game.player.status.Status;
import com.github.tanokun.tanorpg.game.player.status.StatusType;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;

import java.util.HashMap;

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

    public Creature spawnEntity(Location location) {
        Creature entity = (Creature) location.getWorld().spawnEntity(location, entityType);
        entity.setCustomName(name + " §7[§dLv:§e" + LEVEL + "§7] " + "§a❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘");
        if (!(mainHand.equals(""))){entity.getEquipment().setItemInMainHand(ItemManager.getItem(mainHand).getItem());}
        if (!(offHand.equals(""))){entity.getEquipment().setItemInOffHand(ItemManager.getItem(offHand).getItem());}
        if (!(helmet.equals(""))){entity.getEquipment().setHelmet(ItemManager.getItem(helmet).getItem());}
        if (!(chestPlate.equals(""))){entity.getEquipment().setChestplate(ItemManager.getItem(chestPlate).getItem());}
        if (!(leggings.equals(""))){entity.getEquipment().setLeggings(ItemManager.getItem(leggings).getItem());}
        if (!(boots.equals(""))){entity.getEquipment().setBoots(ItemManager.getItem(boots).getItem());}
        entity.setMaxHealth(HP);
        entity.setHealth(HP);
        EntityManager.addEntity(new EntityCreature(entity, this));
        return entity;
    }

    public String getMainHand() {return mainHand;}
    public String getOffHand() {return offHand;}
    public String getHelmet() {return helmet;}
    public String getChestPlate() {return chestPlate;}
    public String getLeggings() {return leggings;}
    public String getBoots() {return boots;}
}
