package com.github.tanokun.tanorpg.player.menu.util;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.command.OpenInvCommand;
import com.github.tanokun.tanorpg.util.ItemUtilsKt;
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class AdminUtilMenu implements InventoryProvider {
    public SmartInventory getInv() {
        return SmartInventory.builder()
                .closeable(true)
                .provider(this)
                .size(1, 9)
                .title("§bAdminMenu")
                .id("AdminUtilMenu")
                .update(false)
                .build();
    }

    public void init(Player player, InventoryContents contents) {
        contents.set(0, 0, ClickableItem.of(ItemUtilsKt.createItem(Material.RAW_IRON, "§6§lアイテム", 1, true), e -> {
            TanoRPG.playSound(player, Sound.ENTITY_SHULKER_OPEN, 3, 1);
            OpenInvCommand.itemListMenu.getInv().open(player);
        }));
        contents.set(0, 1, ClickableItem.of(ItemUtilsKt.createItem(Material.ENDER_PEARL, "§a§lワープポイント", 1, false), e -> {

        }));
        contents.set(0, 2, ClickableItem.of(ItemUtilsKt.createItem(Material.ENDER_EYE, "§b§lアドミンポイント", 1, true), e -> {

        }));
        contents.set(0, 3, ClickableItem.of(ItemUtilsKt.createItem(Material.DIAMOND_AXE, "§7§lダンジョンポイント", 1, false), e -> {

        }));
        contents.set(0, 4, ClickableItem.of(ItemUtilsKt.createItem(Material.CREEPER_SPAWN_EGG, "§b§lMOB", 1, true), e -> {

        }));
        contents.set(0, 5, ClickableItem.of(ItemUtilsKt.createItem(Material.SPIDER_SPAWN_EGG, "§b§lBOSS", 1, true), e -> {

        }));
    }
}
