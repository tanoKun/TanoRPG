package com.github.tanokun.tanorpg.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.player.menu.main.MainMenu;
import com.github.tanokun.tanorpg.util.ItemUtilsKt;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class MainMenuListener implements Listener {
    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClick(PlayerInteractEvent e) {
        try {
            if (e.getItem() == null) return;
            if (TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId()) == null) return;
            if (TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId()).getQuestMap().isAction()) return;
            if (e.getItem().getItemMeta().getDisplayName().equals("§bMenu") &&
                    e.getItem().getType().equals(Material.COMPASS)) {
                e.setCancelled(true);
                new MainMenu().getInv(e.getPlayer()).open(e.getPlayer());
            }
        } catch (Exception ignored) {}
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent e) {
        e.getPlayer().getInventory().setItem(8, new ItemStack(Material.AIR));
    }

    @EventHandler
    public void onOpen(InventoryInteractEvent e) {
        e.setCancelled(true);
    }


    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        ItemStack item = e.getPlayer().getInventory().getItem(8);
        if (item != null && !item.getItemMeta().getDisplayName().equals("§bMenu"))
            e.getPlayer().getLocation().getWorld().dropItem(e.getPlayer().getLocation(), item);

        e.getPlayer().getInventory().setItem(8, ItemUtilsKt.createItem(
                Material.COMPASS, "§bMenu",
                Arrays.asList("§f初期アイテム", "§f自分の情報を見ることができる", "§fコンパスはクエストの場所を指し示してくれるぞ"), 1, true));
    }

}
