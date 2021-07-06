package com.github.tanokun.tanorpg.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.player.inv.MainMenu;
import com.github.tanokun.tanorpg.util.ItemUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class MainMenuListener implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent e){
        if (e.getWhoClicked().getGameMode() == GameMode.CREATIVE) return;
        if (e.getClickedInventory() == e.getWhoClicked().getOpenInventory().getTopInventory()) return;
        if (e.getSlot() == 8) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClick(PlayerInteractEvent e){
        if (e.getItem() == null) return;
        if (TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId()) == null) return;
        if (e.getItem().getItemMeta().getDisplayName().equals("§bMenu") &&
                e.getItem().getType().equals(Material.COMPASS)){
            e.setCancelled(true);
            MainMenu.getInv(e.getPlayer()).open(e.getPlayer());
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent e){
        e.getPlayer().getInventory().setItem(8, new ItemStack(Material.AIR));
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e){
        ItemStack item = e.getPlayer().getInventory().getItem(8);
        if (item != null) e.getPlayer().getLocation().getWorld().dropItem(e.getPlayer().getLocation(), item);

        e.getPlayer().getInventory().setItem(8, ItemUtils.createItem(
                Material.COMPASS, "§bMenu",
                Arrays.asList(new String[]{"§f初期アイテム", "§f自分の情報を見ることができる", "§fコンパスはクエストの場所を指し示してくれるぞ"}), 1, true));
    }
}
