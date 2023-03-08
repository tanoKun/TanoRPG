package com.github.tanokun.tanorpg.game.item.type;

import com.github.tanokun.tanorpg.game.item.ItemRarityType;
import com.github.tanokun.tanorpg.game.item.ItemType;
import com.github.tanokun.tanorpg.game.item.status.ItemStatus;
import com.github.tanokun.tanorpg.player.EquipmentMap;
import org.bukkit.Material;

import java.util.List;

public class ItemAccessory extends ItemEquipment {

    public ItemAccessory(String id, Material material, String name, List<String> lore, ItemStatus statuses, boolean glowing, ItemRarityType itemRarityType) {
        super(id, material, name, lore, statuses, glowing, itemRarityType);
        setEquipmentType(EquipmentMap.EquipmentType.ACCESSORY);
    }

    public ItemType getItemType() {
        return ItemType.ACCESSORY;
    }
}
