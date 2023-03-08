package com.github.tanokun.tanorpg.game.item.type;

import com.github.tanokun.tanorpg.game.item.ItemRarityType;
import com.github.tanokun.tanorpg.game.item.ItemType;
import com.github.tanokun.tanorpg.game.item.status.ItemStatus;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class ItemRune extends ItemMaterial {
    public ItemRune(String id, Material material, String name, List<String> lore, ItemStatus statuses, boolean glowing, ItemRarityType itemRarityType) {
        super(id, material, name, lore, statuses, glowing, itemRarityType, new ArrayList<>());
    }

    public ItemType getItemType() {
        return ItemType.RUNE;
    }

}
