package com.github.tanokun.tanorpg.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class DeathEventListener implements Listener {
    @EventHandler
    public void onDeath(EntityDeathEvent e){
        e.getDrops().clear();
    }
}
