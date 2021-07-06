package com.github.tanokun.tanorpg.game.entity.spawner;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.base.ObjectEntity;
import com.github.tanokun.tanorpg.util.EntityUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

public class EntitySpawner {
    private final ObjectEntity entity;

    private final Location spawnerLocation;
    private final int spawnInRadius;
    private final int playerInRadius;

    private final int maxSpawnCount;
    private final int oneTimeSpawnCount;

    private final int nextSpawnTime;
    private int nextSpawnTimeTemp = 0;

    private final int entityTeleportRadius;

    private final HashSet<Entity> activeEntities = new HashSet<>();

    public EntitySpawner(ObjectEntity entity, Location spawnerLocation,
                         int spawnInRadius, int playerInRadius, int maxSpawnCount, int oneTimeSpawnCount, int nextSpawnTime,
                         int entityTeleportRadius) {
        this.entity = entity;
        this.spawnerLocation = spawnerLocation;
        this.spawnInRadius = spawnInRadius;
        this.playerInRadius = playerInRadius;
        this.maxSpawnCount = maxSpawnCount;
        this.oneTimeSpawnCount = oneTimeSpawnCount;
        this.nextSpawnTime = nextSpawnTime;
        this.entityTeleportRadius = entityTeleportRadius;
    }


    public ObjectEntity getEntity() {return entity;}
    public Location getSpawnerLocation() {return spawnerLocation;}
    public int getSpawnInRadius() {return spawnInRadius;}
    public int getPlayerInRadius() {return playerInRadius;}
    public int getMaxSpawnCount() {return maxSpawnCount;}
    public int getOneTimeSpawnCount() {return oneTimeSpawnCount;}
    public int getNextSpawnTime() {return nextSpawnTime;}
    public int getNextSpawnTimeTemp() {return nextSpawnTimeTemp;}
    public int getEntityTeleportRadius() {return entityTeleportRadius;}
    public HashSet<Entity> getActiveEntities() {return activeEntities;}

    public void spawnEntities(){
        nextSpawnTimeTemp++;
        cleanActiveEntities();
        for (Entity entity : activeEntities){
            if (spawnerLocation.distance(entity.getLocation()) > entityTeleportRadius){
                entity.teleport(randomizeSpawnLocation(spawnerLocation, spawnInRadius));
            }
        }
        if (nextSpawnTimeTemp != nextSpawnTime) return;
        nextSpawnTimeTemp = 0;
        if (EntityUtils.getNearPlayers(spawnerLocation, playerInRadius).length == 0) return;
        if (activeEntities.size() >= maxSpawnCount) return;
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (int loop = 0; loop < oneTimeSpawnCount; loop++) {
                        Location location;
                        Material material;
                            if (activeEntities.size() >= maxSpawnCount) {cancel(); continue;}
                            location = randomizeSpawnLocation(spawnerLocation, spawnInRadius);
                            material = location.getBlock().getBlockData().getMaterial();
                            if (material.equals(Material.WATER) || material.equals(Material.AIR)) {
                                activeEntities.add(entity.spawn(location));
                            }
                    }
                    cancel();
                }
            }.runTask(TanoRPG.getPlugin());
        return;

    }

    public void cleanActiveEntities(){
        Iterator<Entity> iterable = activeEntities.iterator();
        while (iterable.hasNext()){
            if (iterable.next().isDead()) iterable.remove();
        }
    }

    public Location randomizeSpawnLocation(Location location, int r) {
        double x, z, sX = location.getX();
        double sY = location.getY();
        double sZ = location.getZ();
        do {
            x = randomRange(sX - r, sX + r);
            z = randomRange(sZ - r, sZ + r);
        } while (location.distance(new Location(location.getWorld(), x, sY, z)) > r);
        return new Location(location.getWorld(), x, sY, z);
    }

    public double randomRange(double arg0, double arg1) {
        double range = (arg0 < arg1) ? (arg1 - arg0) : (arg0 - arg1);
        if (range < 1.0D)
            return Math.floor(arg0) + 0.5D;
        double min = (arg0 < arg1) ? arg0 : arg1;
        return Math.floor(min + Math.random() * range) + 0.5D;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntitySpawner that = (EntitySpawner) o;
        return spawnInRadius == that.spawnInRadius && playerInRadius == that.playerInRadius && maxSpawnCount == that.maxSpawnCount && oneTimeSpawnCount == that.oneTimeSpawnCount && nextSpawnTime == that.nextSpawnTime && nextSpawnTimeTemp == that.nextSpawnTimeTemp && Objects.equals(entity, that.entity) && Objects.equals(spawnerLocation, that.spawnerLocation) && Objects.equals(activeEntities, that.activeEntities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity, spawnerLocation, spawnInRadius, playerInRadius, maxSpawnCount, oneTimeSpawnCount, nextSpawnTime, nextSpawnTimeTemp, activeEntities);
    }
}
