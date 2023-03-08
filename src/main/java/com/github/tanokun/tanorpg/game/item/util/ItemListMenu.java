package com.github.tanokun.tanorpg.game.item.util;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.util.ItemUtilsKt;
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.Pagination;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.SlotIterator;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemListMenu implements InventoryProvider {

    public SmartInventory getInv() {
        return SmartInventory.builder()
                .closeable(true)
                .cancelable(true)
                .provider(this)
                .size(6, 9)
                .title("§d§lアイテムリスト")
                .id("ItemList")
                .update(false)
                .build();
    }

    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();
        ClickableItem[] items = new ClickableItem[TanoRPG.getPlugin().getItemManager().getItemIDs().size()];
        int i = 0;
        for (String id : TanoRPG.getPlugin().getItemManager().getItemIDs()) {
            ItemStack itemStack = TanoRPG.getPlugin().getItemManager().getItem(id).init(1, 0, true);
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<String> lore = itemMeta.getLore();
            lore.add("  ");
            lore.add("§d ID: " + id);
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            items[i] = ClickableItem.of(itemStack, e -> {
                TanoRPG.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
                ItemUtilsKt.addItem(player, TanoRPG.getPlugin().getItemManager().getItem(id).init(1, 0, true));
            });
            i++;
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(45);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

        arrow(player, contents);
    }

    private void arrow(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();

        if (pagination.getPage() == 0)
            contents.set(5, 0, ClickableItem.empty(new ItemStack(Material.AIR)));
        else
            contents.set(5, 0, ClickableItem.of(ItemUtilsKt.createItem(Material.SPECTRAL_ARROW,
                    pagination.getPage() + "ページに戻る", 1, true), e -> {
                contents.inventory().open(player, pagination.getPage() - 1);
                TanoRPG.playSound(player, Sound.ENTITY_SHULKER_OPEN, 10, 1);
            }));

        if (pagination.isLast())
            contents.set(5, 8, ClickableItem.empty(new ItemStack(Material.AIR)));
        else
            contents.set(5, 8, ClickableItem.of(ItemUtilsKt.createItem(Material.SPECTRAL_ARROW,
                    (pagination.getPage() + 2) + "ページに進む", 1, true), e -> {
                contents.inventory().open(player, pagination.getPage() + 1);
                TanoRPG.playSound(player, Sound.ENTITY_SHULKER_OPEN, 10, 1);
            }));
    }
}
