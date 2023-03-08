package com.github.tanokun.tanorpg.game.item.type;

import com.github.tanokun.tanorpg.game.item.ItemRarityType;
import com.github.tanokun.tanorpg.game.item.ItemType;
import com.github.tanokun.tanorpg.game.item.status.ItemStatus;
import com.github.tanokun.tanorpg.player.quests.actions.QuestGiveBuffAction;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class ItemPotion extends ItemMaterial {

    public ItemPotion(String id, Material material, String name, List<String> lore, ItemStatus statuses, boolean glowing, ItemRarityType itemRarityType, List<QuestGiveBuffAction> buff) {
        super(id, material, name, lore, statuses, glowing, itemRarityType, new ArrayList<>());
        setBuffs(buff);
    }

    @Override
    public ItemType getItemType() {
        return ItemType.POTION;
    }
}
