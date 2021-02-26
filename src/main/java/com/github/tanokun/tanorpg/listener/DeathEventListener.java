package com.github.tanokun.tanorpg.listener;

import com.github.tanokun.tanorpg.game.entity.EntityManager;
import org.bukkit.entity.Creature;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class DeathEventListener implements Listener {
    @EventHandler
    public void onDeath(EntityDeathEvent e){
        if (e.getEntity() instanceof Creature) {
            e.getDrops().clear();
            EntityManager.removeEntity((Creature) e.getEntity());
        }
    }
}
