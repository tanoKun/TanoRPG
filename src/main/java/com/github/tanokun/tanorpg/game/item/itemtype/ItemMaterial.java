package com.github.tanokun.tanorpg.game.item.itemtype;

import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.game.item.ItemType;
import com.github.tanokun.tanorpg.game.item.itemtype.base.Item;
import com.github.tanokun.tanorpg.game.player.status.Status;
import com.github.tanokun.tanorpg.game.player.status.StatusType;
import com.github.tanokun.tanorpg.util.Glowing;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemMaterial extends Item {
    private final ItemType itemType = ItemType.MATERIAL;

    public ItemMaterial(String id, Material material, String name, List<String> lores, List<Status> statuses, boolean glowing) {
        super(id, material, name, lores, statuses, glowing);
    }

    public ItemStack getItem() {
        List<String> statuses2 = new ArrayList<>();
        for(Status status : getStatuses()){
            if (status.getStatusType().equals(StatusType.NONE)) continue;
            if (status.getLevel() > 0){
                statuses2.add("§a" + status.getStatusType().getName() + " +" + status.getLevel());
            } else {
                statuses2.add("§c" + status.getStatusType().getName() + " " + status.getLevel());
            }
        }
        ItemStack is = new ItemStack(getMaterial());
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(getRarity().getColor() + getName());
        List<String> lore = new ArrayList<>();
        lore.add(ItemManager.LORE);
        for(String lore2 : getLore()){lore.add(lore2);}
        lore.add(ItemManager.FIRST_STATUS);
        for(String lore2 : statuses2){lore.add(lore2);}
        lore.add(" ");
        lore.add("§7Rarity: " + getRarity().getName());
        lore.add("§7Type: " + itemType.getName());
        lore.add(ItemManager.FINAL_STATUS);
        lore.add("§7ID: " + getId());
        im.setLore(lore);
        if (isGlowing()) {
            im.addEnchant(new Glowing(), 1, true);
        }
        if (getCustomModelData() != 0){
            im.setCustomModelData(getCustomModelData());
        }
        im.setUnbreakable(true);
        is.setItemMeta(im);
        return is;
    }
}
