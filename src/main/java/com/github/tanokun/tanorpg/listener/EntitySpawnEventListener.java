package com.github.tanokun.tanorpg.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.mob.CustomEntityManager;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;

public class EntitySpawnEventListener implements Listener {
    private final static String Key = "SPAWNER.";
    public static final int spawnerCount = 2;
    public static HashMap<String, Integer> counts = new HashMap<>();
    @EventHandler
    public void onSpawn(SpawnerSpawnEvent e){
        Entity entity = e.getEntity();
        if (entity.getScoreboardTags().contains("entitymarker")) {
            String name = e.getEntity().getName();
            Location en_loc = e.getEntity().getLocation();
            Location spawner_loc = e.getSpawner().getLocation();
            String key2 = spawner_loc.getX() + "," + spawner_loc.getY() + "," + spawner_loc.getZ();
            Integer count = counts.get(Key + key2);
            if (count == null) count = 0;
            if (count >= spawnerCount) {
                return;
            }
            count += 1;
            counts.put(Key + key2, count);
            Creature creature = CustomEntityManager.getEntity(name).spawnEntity(en_loc);
            creature.setMetadata("custom_entity", new FixedMetadataValue(TanoRPG.getPlugin(), Key + key2));
            e.getEntity().remove();
        }
    }
}
