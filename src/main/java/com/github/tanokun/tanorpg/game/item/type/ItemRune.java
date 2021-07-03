package com.github.tanokun.tanorpg.game.item.type;

import com.github.tanokun.tanorpg.game.item.ItemRarityType;
import com.github.tanokun.tanorpg.game.item.ItemType;
import com.github.tanokun.tanorpg.player.status.StatusMap;
import org.bukkit.Material;

import java.util.List;

public class ItemRune extends ItemMaterial {
    public ItemRune(String id, Material material, String name, List<String> lore, StatusMap statuses, boolean glowing, ItemRarityType itemRarityType) {
        super(id, material, name, lore, statuses, glowing, itemRarityType);
    }

    public ItemType getItemType() {
        return ItemType.RUNE;
    }

}
