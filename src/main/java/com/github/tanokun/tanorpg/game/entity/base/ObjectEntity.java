package com.github.tanokun.tanorpg.game.entity.base;

import com.github.tanokun.tanorpg.game.entity.EntityDropItems;
import com.github.tanokun.tanorpg.player.EquipmentMap;
import com.github.tanokun.tanorpg.player.status.StatusMap;
import com.github.tanokun.tanorpg.util.io.Config;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public abstract class ObjectEntity {
    private final Config entityConfig;

    private final String name;

    private final StatusMap statusMap;

    private final EquipmentMap equipMap;

    private final EntityDropItems dropItems;

    private int exp;

    private int hasLevel;

    public ObjectEntity(Config entityConfig, String name, StatusMap statusMap, EquipmentMap equipMap, EntityDropItems dropItems, int exp, int level) {
        this.entityConfig = entityConfig;
        this.name = name;
        this.statusMap = statusMap;
        this.equipMap = equipMap;
        this.dropItems = dropItems;
        this.exp = exp;
        this.hasLevel = level;

    }

    public Config getEntityConfig() {
        return entityConfig;
    }

    public String getName() {
        return name;
    }

    public StatusMap getStatusMap() {
        return statusMap;
    }

    public EquipmentMap getEquipMap() {
        return equipMap;
    }

    public EntityDropItems getDropItems() {
        return dropItems;
    }

    public int getHasLevel() {
        return hasLevel;
    }

    public int getExp() {
        return exp;
    }

    public void setHasLevel(int hasLevel) {
        this.hasLevel = hasLevel;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public abstract Entity spawn(Location location);
}
