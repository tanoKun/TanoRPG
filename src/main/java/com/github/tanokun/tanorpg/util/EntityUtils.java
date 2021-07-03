package com.github.tanokun.tanorpg.util;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.ActiveEntity;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashSet;
import java.util.NoSuchElementException;

public class EntityUtils {

    public static Entity[] getNearbyEntities(Location l, double radius) {
        double chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        HashSet<Entity> radiusEntities = new HashSet< Entity >();
        try {
            for (double chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
                for (double chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
                    int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
                    for (Entity e: new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities()) {
                        if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock())
                            radiusEntities.add(e);
                    }
                }
            }
        }catch (NoSuchElementException | NullPointerException e){
            return radiusEntities.toArray(new Entity[radiusEntities.size()]);
        }
        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }

    public static Entity[] getNearbyPlayers(Location l, double radius) {
        double chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        HashSet <Entity> radiusEntities = new HashSet< Entity >();
        try {
            for (double chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
                for (double chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
                    int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
                    for (Entity e: new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities()) {
                        if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock() && e instanceof Player)
                            radiusEntities.add(e);
                    }
                }
            }
        }catch (NoSuchElementException | NullPointerException e){
            return radiusEntities.toArray(new Entity[radiusEntities.size()]);
        }
        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }

    public static ActiveEntity getActiveEntity(Entity entity){
        if (!entity.hasMetadata("TanoRPG_entity")) return null;
        return (ActiveEntity) entity.getMetadata("TanoRPG_entity").get(0).value();
    }

    public static boolean isActiveEntity(Entity entity){
        return entity.hasMetadata("TanoRPG_entity");
    }

    public static void setActiveEntity(Entity entity, ActiveEntity activeEntity){
        entity.setMetadata("TanoRPG_entity", new FixedMetadataValue(TanoRPG.getPlugin(), activeEntity));
    }
}
