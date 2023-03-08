package com.github.tanokun.tanorpg.util.smart_inv.inv.contents;


import org.bukkit.entity.Player;

public interface InventoryProvider {

    void init(Player player, InventoryContents contents);

    default void update(Player player, InventoryContents contents) {
    }
}