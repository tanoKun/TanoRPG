package com.github.tanokun.tanorpg.game.item.type;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.game.item.ItemRarityType;
import com.github.tanokun.tanorpg.game.item.ItemType;
import com.github.tanokun.tanorpg.game.item.type.base.ItemBase;
import com.github.tanokun.tanorpg.game.item.type.base.ItemData;
import com.github.tanokun.tanorpg.player.status.StatusMap;
import com.github.tanokun.tanorpg.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemMaterial extends ItemBase {

    public ItemMaterial(String id, Material material, String name, List<String> lore, StatusMap statuses, boolean glowing, ItemRarityType itemRarityType) {
        super(id, material, name, lore, statuses, glowing, itemRarityType);
    }

    public ItemType getItemType() {
        return ItemType.MATERIAL;
    }

    public ItemStack init(int amount) {
        ItemStack result = new ItemStack(getMaterial());
        ItemMeta im = result.getItemMeta();

        List<String> statuses = TanoRPG.getPlugin().getItemManager().fromNormalStatus(getBasicStatuses());

        im.setDisplayName(getRarity().getColor() + getDisplayName());
        List<String> lore = new ArrayList<>();
        lore.add(ItemManager.LORE);
        getLore().stream().forEach(lore::add);
        lore.add(ItemManager.FIRST_STATUS);
        statuses.stream().forEach(lore::add);
        lore.add(" ");
        lore.add("§7レアリティ: " + getRarity().getName());
        lore.add("§7タイプ: " + getItemType().getName());
        lore.add(ItemManager.FINAL_STATUS);
        im.setLore(lore);

        if (isGlowing()) {im.addEnchant(Enchantment.MENDING, 1, true);}
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);

        if (getCustomModelData() != 0){
            im.setCustomModelData(getCustomModelData());
        }

        im.setUnbreakable(true);
        result.setItemMeta(im);
        result.setAmount(amount);

        return initData(result);
    }

    public ItemStack initData(ItemStack itemStack) {
        ItemUtils.setItemData(itemStack, new ItemData(this));
        return itemStack;
    }
}
