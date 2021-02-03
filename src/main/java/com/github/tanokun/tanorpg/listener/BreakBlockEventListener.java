package com.github.tanokun.tanorpg.listener;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakBlockEventListener implements Listener {
    @EventHandler
    public void onBreak(BlockBreakEvent e){
        if (e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)){
            e.setCancelled(true);
        }
    }
}
