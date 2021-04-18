package com.github.tanokun.api.smart_inv.inv.contents;


import com.github.tanokun.api.smart_inv.inv.SmartInventory;
import com.github.tanokun.tanorpg.TanoRPG;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public interface InventoryProvider {
    
    void init(Player player, InventoryContents contents);
    default void update(Player player, InventoryContents contents){}
}