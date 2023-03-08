package com.github.tanokun.tanorpg.player;

import com.github.tanokun.tanorpg.game.item.ItemType;
import com.github.tanokun.tanorpg.player.status.StatusMap;
import com.github.tanokun.tanorpg.player.status.StatusType;
import com.github.tanokun.tanorpg.util.ItemUtilsKt;
import com.github.tanokun.tanorpg.util.SaveMarker;
import com.github.tanokun.tanorpg.util.io.Config;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class EquipmentMap implements SaveMarker<EquipmentMap> {
    private final HashMap<EquipmentType, ItemStack> equip = new HashMap<>();

    public EquipmentMap() {
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
        if (itemStack.getType() == Material.AIR) this.equip.put(equipType, itemStack);
        if (ItemUtilsKt.getItemData(itemStack) == null) return;
        this.equip.put(equipType, itemStack);
    }

    public HashMap<StatusType, Double> getStatus() {
        StatusMap statusMap = new StatusMap();
        this.equip.keySet().forEach(equip -> {
            if (this.equip.get(equip) == null || this.equip.get(equip).getType().equals(Material.AIR)) return;
            statusMap.addAllStatuses(ItemUtilsKt.getItemData(this.equip.get(equip)).getStatuses().getHasStatuses());
        });
        return statusMap.getHasStatuses();
    }

    @Override
    public void save(Config config, String key) {
        config.getConfig().set(key + "equipment.helmet", getEquip(EquipmentType.HELMET));
        config.getConfig().set(key + "equipment.chestplate", getEquip(EquipmentType.CHESTPLATE));
        config.getConfig().set(key + "equipment.leggings", getEquip(EquipmentType.LEGGINGS));
        config.getConfig().set(key + "equipment.boots", getEquip(EquipmentType.BOOTS));
        config.getConfig().set(key + "equipment.accessory", getEquip(EquipmentType.ACCESSORY));
        config.getConfig().set(key + "equipment.accessory2", getEquip(EquipmentType.ACCESSORY2));

        config.saveConfig();
    }

    @Override
    public EquipmentMap load(Config config, String key) {
        equip.put(EquipmentType.HELMET, (ItemStack) config.getConfig().get(key + "equipment.helmet"));
        equip.put(EquipmentType.CHESTPLATE, (ItemStack) config.getConfig().get(key + "equipment.chestplate"));
        equip.put(EquipmentType.LEGGINGS, (ItemStack) config.getConfig().get(key + "equipment.leggings"));
        equip.put(EquipmentType.BOOTS, (ItemStack) config.getConfig().get(key + "equipment.boots"));
        equip.put(EquipmentType.ACCESSORY, (ItemStack) config.getConfig().get(key + "equipment.accessory"));
        equip.put(EquipmentType.ACCESSORY2, (ItemStack) config.getConfig().get(key + "equipment.accessory2"));
        return this;
    }

    public double getStatus(StatusType status) {
        return getStatus().get(status) == null ? 0 : getStatus().get(status);
    }

    public enum EquipmentType {
        MAIN(-1, -1, -1, ItemType.NULL, ""),
        SUB(-1, -1, -1, ItemType.NULL, ""),
        HELMET(1, 1, 39, ItemType.EQUIPMENT, "ヘルメット"),
        CHESTPLATE(2, 1, 38, ItemType.EQUIPMENT, "チェストプレート"),
        LEGGINGS(3, 1, 37, ItemType.EQUIPMENT, "レギンス"),
        BOOTS(4, 1, 36, ItemType.EQUIPMENT, "ブーツ"),
        ACCESSORY(2, 0, -1, ItemType.ACCESSORY, "アクセサリー"),
        ACCESSORY2(2, 2, -1, ItemType.ACCESSORY, "アクセサリー"),
        ;

        private final ItemType itemType;
        private final int row;
        private final int column;
        private final int raw;
        private final String name;

        EquipmentType(int row, int column, int raw, ItemType itemType, String name) {
            this.row = row;
            this.column = column;
            this.raw = raw;
            this.itemType = itemType;
            this.name = name;
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

        public String getName() {
            return name;
        }

        public int getRaw() {
            return raw;
        }
    }
}

