package com.github.tanokun.tanorpg.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.event.PlayerArmorEquipEvent;
import com.github.tanokun.tanorpg.player.Member;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class StopEventListener implements Listener {
    @EventHandler
    public void onEquip(PlayerArmorEquipEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getPlayer().isOp()) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.getPlayer().isOp()) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlock(EntityChangeBlockEvent e) {
        if (e.getEntity().isOp()) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onSplit(SlimeSplitEvent event) {
        event.setCount(0);
    }

    @EventHandler
    public void onRegainHealth(EntityRegainHealthEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerOpenInv(InventoryOpenEvent e) {
        if (e.getPlayer().isOp()) return;
        if (TanoRPG.getPlugin().getInventoryManager().getInventory((Player) e.getPlayer()) == null) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDoorOpen(PlayerInteractEvent e) {
        if ((e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            if (("" + e.getClickedBlock().getType()).contains("DOOR"))
                Bukkit.getScheduler().runTaskLater(TanoRPG.getPlugin(), () -> {
                    Door data = (Door) e.getClickedBlock().getBlockData();
                    data.setOpen(false);
                    e.getClickedBlock().setBlockData(data);
                }, 40);
        }
    }
}
