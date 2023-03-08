package com.github.tanokun.tanorpg.game.entity.boss;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.game.entity.ObjectEntity;
import com.github.tanokun.tanorpg.game.item.drop.ItemTable;
import com.github.tanokun.tanorpg.util.io.MapNode;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class BossEntity {

    private ObjectEntity objectEntity;

    private Date spawnTime = null;

    private Location clear;

    private Location teleport;

    private Location spawn;

    private BossActiveEntity bossActiveEntity;

    private ItemTable chestTable;

    public BossEntity(ObjectEntity o, Location clear, Location teleport, Location spawn, ItemTable chestTable) {
        objectEntity = o;
        this.clear = clear;
        this.teleport = teleport;
        this.spawn = spawn;
        this.chestTable = chestTable;

    }

    public ObjectEntity getObjectEntity() {
        return objectEntity;
    }

    public Date getSpawnTime() {
        return spawnTime;
    }

    public Location getClear() {
        return clear;
    }

    public Location getTeleport() {
        return teleport;
    }

    public Location getSpawn() {
        return spawn;
    }

    public BossActiveEntity getBossActiveEntity() {
        return bossActiveEntity;
    }

    public ItemTable getChestTable() {
        return chestTable;
    }

    public void setObjectEntity(ObjectEntity objectEntity) {
        this.objectEntity = objectEntity;
    }

    public void setSpawnTime(LocalDateTime now) {
        if (now == null) {
            this.spawnTime = null;
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        DateTimeFormatter byString = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        try {
            this.spawnTime = format.parse(now.format(byString));
        } catch (ParseException e2) {
            e2.printStackTrace();
        }
    }

    public void setClear(Location clear) {
        this.clear = clear;
    }

    public void setTeleport(Location teleport) {
        this.teleport = teleport;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public void setBossActiveEntity(BossActiveEntity bossActiveEntity) {
        this.bossActiveEntity = bossActiveEntity;
    }

    public void setChestTable(ItemTable chestTable) {
        this.chestTable = chestTable;
    }
    public Entity spawn() {
        setSpawnTime(LocalDateTime.now());

        Entity entity = TanoRPG.getPlugin().getMythicMobs().getMobManager().getMythicMob(objectEntity.getId()).spawn(
                        new AbstractLocation(new BukkitWorld(spawn.getWorld()), spawn.getX(), spawn.getY(), spawn.getZ(), spawn.getYaw(), spawn.getPitch()), 1)
                .getEntity().getBukkitEntity();
        this.setBossActiveEntity(new BossActiveEntity(this, entity));
        entity.setMetadata(EntityManager.ENTITY, new FixedMetadataValue(TanoRPG.getPlugin(), this.getBossActiveEntity()));

        return entity;
    }

    public void stop() {
        setSpawnTime(null);
        bossActiveEntity.getEntity().remove();
    }

    public void die() {
        MapNode<Integer, Integer> loc = new MapNode<>(spawn.getBlockX(), spawn.getBlockZ());

        spawn.getWorld().getBlockAt(spawn).setType(Material.CHEST);

        Bukkit.getScheduler().runTaskLater(TanoRPG.getPlugin(), () -> {
            bossActiveEntity.getJoin().forEach(player -> player.sendMessage(TanoRPG.PX + "10秒後にテレポートします"));
        }, 100);

        Bukkit.getScheduler().runTaskLater(TanoRPG.getPlugin(), () -> {
            spawn.getWorld().getBlockAt(spawn).setType(Material.AIR);
            bossActiveEntity.getJoin().forEach(player -> {
                player.setMetadata("boss", new FixedMetadataValue(TanoRPG.getPlugin(), true));
                player.teleport(clear);
                TanoRPG.getPlugin().getMemberManager().getMember(player.getUniqueId()).getChestMap().getPlayerChests().remove(loc);
            });
        }, 300);

        Bukkit.getScheduler().runTaskLater(TanoRPG.getPlugin(), () -> {
            setBossActiveEntity(null);
            setSpawnTime(null);
        }, 305);
    }
}
