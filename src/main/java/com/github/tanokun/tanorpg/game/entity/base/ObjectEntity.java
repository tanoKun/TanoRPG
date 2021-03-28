package com.github.tanokun.tanorpg.game.entity.base;

import com.github.tanokun.tanorpg.game.entity.EntityTypes;
import com.github.tanokun.tanorpg.game.entity.exception.TanoEntityException;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.game.player.status.Status;
import com.github.tanokun.tanorpg.game.player.status.StatusType;
import com.github.tanokun.tanorpg.util.io.Config;
import com.github.tanokun.tanorpg.util.io.MapNode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EntityEquipment;

import java.util.HashMap;
import java.util.Map;

public abstract class ObjectEntity {
    private Config entityConfig;

    private EntityEquipment equip;

    private EntityTypes entityTypes;
    private String name;
    private int HP;
    private int LEVEL;
    private long EXP;

    private double speed = 1;


    private Map<StatusType, Status> statuses = new HashMap<>();

    public abstract Entity spawn(Location location);
    public abstract Entity setOptions(Entity entity);

    public ObjectEntity(Config config) throws TanoEntityException {
        MapNode<String, Object> data = null;
        try {
            data = ItemManager.get("BaseOptions.DisplayName", config);
            if (data.getValue() != null) {name = (String) data.getValue();} else {throw new NullPointerException("エンティティ名が設定されていません");}
            try {
                data = ItemManager.get("BaseOptions.HP", config);
                if (data.getValue() != null) {HP = Integer.valueOf((String) data.getValue());} else {throw new NullPointerException("HPが設定されていません");}
                data = ItemManager.get("BaseOptions.LEVEL", config);
                if (data.getValue() != null) {LEVEL = Integer.valueOf((String) data.getValue());} else {throw new NullPointerException("LEVELが設定されていません");}
                data = ItemManager.get("BaseOptions.EXP", config);
                if (data.getValue() != null) {EXP = Integer.valueOf((String) data.getValue());} else {throw new NullPointerException("EXP売却値段が設定されていません");}
                data = ItemManager.get("BaseOptions.Speed", config);
                if (data.getValue() != null) {speed = Integer.valueOf((String) data.getValue());}
            } catch (NumberFormatException e){
                throw new NumberFormatException("「" + data.getValue() + "」は数字で入力して下さい");
            }
        }catch (Exception e){
            throw new TanoEntityException(e.getMessage(), data);
        }
    }

    public EntityTypes getEntityTypes() {
        return entityTypes;
    }
    public String getName() {
        return name;
    }
    public Config getEntityConfig() {
        return entityConfig;
    }
    public EntityEquipment getEquip() {
        return equip;
    }
    public int getHP() {
        return HP;
    }
    public int getLEVEL() {
        return LEVEL;
    }
    public long getEXP() {
        return EXP;
    }
    public Map<StatusType, Status> getStatuses() {
        return statuses;
    }
    public double getSpeed() {
        return 0.1 * speed;
    }

    public void setEntityTypes(EntityTypes entityTypes) {
        this.entityTypes = entityTypes;
    }
    public void setEntityConfig(Config entityConfig) {
        this.entityConfig = entityConfig;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEquip(EntityEquipment equip){
        this.equip = equip;
    }
    public void setHP(int HP) {
        this.HP = HP;
    }
    public void setLEVEL(int LEVEL) {
        this.LEVEL = LEVEL;
    }
    public void setEXP(long EXP) {
        this.EXP = EXP;
    }
    public void setStatuses(double i, double i2, double i3, double i4, double i5, double i6, double i7){
        statuses.put(StatusType.ATK, new Status(StatusType.ATK, i));
        statuses.put(StatusType.DEF, new  Status(StatusType.DEF, i2));
        statuses.put(StatusType.MATK, new Status(StatusType.MATK, i3));
        statuses.put(StatusType.MDEF, new Status(StatusType.MDEF, i4));
        statuses.put(StatusType.AGI, new Status(StatusType.AGI, i5));
        statuses.put(StatusType.ING, new Status(StatusType.ING, i6));
        statuses.put(StatusType.INT, new Status(StatusType.INT, i7));
    }
    public void setSpeed(double speed) {
        this.speed = speed;
    }
}