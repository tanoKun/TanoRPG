package com.github.tanokun.tanorpg.player;

import com.github.tanokun.tanorpg.game.item.ItemType;
import com.github.tanokun.tanorpg.player.status.StatusMap;
import com.github.tanokun.tanorpg.player.status.StatusType;
import com.github.tanokun.tanorpg.util.ItemUtils;
import com.github.tanokun.tanorpg.util.SaveMarker;
import com.github.tanokun.tanorpg.util.io.Config;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class EquipmentMap implements SaveMarker<EquipmentMap> {
    private HashMap<EquipmentType, ItemStack> equip = new HashMap<>();

    public EquipmentMap(){
        equip.put(EquipmentType.MAIN, new ItemStack(Material.AIR));
        equip.put(EquipmentType.SUB, new ItemStack(Material.AIR));
        equip.put(EquipmentType.HELMET, new ItemStack(Material.AIR));
        equip.put(EquipmentType.CHESTPLATE, new ItemStack(Material.AIR));
        equip.put(EquipmentType.LEGGINGS, new ItemStack(Material.AIR));
        equip.put(EquipmentType.BOOTS, new ItemStack(Material.AIR));
        equip.put(EquipmentType.ACCESSORY, new ItemStack(Material.AIR));
        equip.put(EquipmentType.ACCESSORY2, new ItemStack(Material.AIR));
    }

    public ItemStack getEquip(EquipmentType equipType) {
        return equip.get(equipType);
    }

    public HashMap<EquipmentType, ItemStack> getEquip() {
        return equip;
    }

    public void setEquip(EquipmentType equipType, ItemStack itemStack) {
        this.equip.put(equipType, itemStack);
    }

    public HashMap<StatusType, Integer> getStatus(){
        StatusMap statusMap = new StatusMap();
        equip.keySet().forEach(equip -> {
            if (this.equip.get(equip) == null) return;
            if (this.equip.get(equip).getType().equals(Material.AIR)) return;
            statusMap.addAllStatus(ItemUtils.getItemData(this.equip.get(equip)).getStatuses().getHasStatuses());
        });
        return statusMap.getHasStatuses();
    }

    @Override
    public void save(Config config, String key) {
        config.getConfig().set(key + "equipment.helmet",     getEquip(EquipmentType.HELMET));
        config.getConfig().set(key + "equipment.chestplate", getEquip(EquipmentType.CHESTPLATE));
        config.getConfig().set(key + "equipment.leggings",   getEquip(EquipmentType.LEGGINGS));
        config.getConfig().set(key + "equipment.boots",      getEquip(EquipmentType.BOOTS));
        config.getConfig().set(key + "equipment.accessory",  getEquip(EquipmentType.ACCESSORY));
        config.getConfig().set(key + "equipment.accessory2", getEquip(EquipmentType.ACCESSORY2));

        config.saveConfig();
    }

    @Override
    public EquipmentMap load(Config config, String key) {
        Gson gson = new GsonBuilder().create();
        Type type = new TypeToken<Map<String, Object>>(){}.getType();

        equip.put(EquipmentType.HELMET,     (ItemStack) config.getConfig().get(key + "equipment.helmet"));
        equip.put(EquipmentType.CHESTPLATE, (ItemStack) config.getConfig().get(key + "equipment.chestplate"));
        equip.put(EquipmentType.LEGGINGS,   (ItemStack) config.getConfig().get(key + "equipment.leggings"));
        equip.put(EquipmentType.BOOTS,      (ItemStack) config.getConfig().get(key + "equipment.boots"));
        equip.put(EquipmentType.ACCESSORY,  (ItemStack) config.getConfig().get(key + "equipment.accessory"));
        equip.put(EquipmentType.ACCESSORY2, (ItemStack) config.getConfig().get(key + "equipment.accessory2"));
        return this;
    }

    public enum EquipmentType {
        MAIN(-1, -1, -1, ItemType.NULL, ""),
        SUB(-1, -1, -1, ItemType.NULL, ""),
        HELMET(1,1, 10, ItemType.EQUIPMENT, "HELMET"),
        CHESTPLATE(2,1, 19, ItemType.EQUIPMENT, "CHESTPLATE"),
        LEGGINGS(3,1, 28, ItemType.EQUIPMENT, "LEGGINGS"),
        BOOTS(4,1, 37, ItemType.EQUIPMENT, "BOOTS"),
        ACCESSORY(2, 0, 18, ItemType.ACCESSORY, ""),
        ACCESSORY2(2, 2, 20, ItemType.ACCESSORY, ""),
        ;

        private final ItemType itemType;
        private int row;
        private int column;
        private int raw;
        private String materialName;

        EquipmentType(int row, int column, int raw, ItemType itemType, String materialName){
            this.row = row;
            this.column = column;
            this.raw = raw;
            this.itemType = itemType;
            this.materialName = materialName;
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }

        public ItemType getItemType() {
            return itemType;
        }

        public String getMaterialName() {
            return materialName;
        }
    }
}

