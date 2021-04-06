package com.github.tanokun.tanorpg.game.entity.spawner;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.util.io.Config;
import com.github.tanokun.tanorpg.util.io.Folder;
import com.github.tanokun.tanorpg.util.io.MapNode;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;

public class EntitySpawnerManager extends BukkitRunnable {
    private HashSet<EntitySpawner> spawners = new HashSet<>();

    public void registerSpawner(EntitySpawner spawner){
        spawners.add(spawner);
    }

    @Override
    public void run() {
        for (EntitySpawner spawner : spawners){
            spawner.spawnEntities();
        }
    }

    public HashSet<String> loadSpawner(){
        for (EntitySpawner spawner : spawners){
            for (Entity entity : spawner.getActiveEntities()){
                entity.remove();
            }
        }
        spawners.clear();

        HashSet<String> errors = new HashSet<>();

        boolean allError = false;

        for (Config config : new Folder("spawner", TanoRPG.getPlugin()).getFiles()) {
            for (String key : config.getConfig().getKeys(false)) {
                boolean successFull = true;
                MapNode<String, Object> data = null;

                String entityName = null;
                int x, y, z;
                Location spawnerLocation = null;
                int spawnInRadius = 0;
                int playerInRadius = 0;
                int maxSpawnCount = 0;
                int oneTimeSpawnCount = 0;
                int nextSpawnTime = 0;
                int entityTeleportRadius = 25;

                try {
                    data = ItemManager.get(key + ".entity", config);
                    if (data.getValue() != null) {
                        entityName = (String) data.getValue();
                        if (EntityManager.getBaseEntity(entityName) == null)
                            throw new NullPointerException("エンティティ名「" + entityName + "」は存在しません");
                    } else {
                        throw new NullPointerException("エンティティIDが設定されていません");
                    }

                    try {
                        data = ItemManager.get(key + ".location.x", config);
                        if (data.getValue() != null) {
                            x = Integer.valueOf((String) data.getValue());
                        } else {
                            throw new NullPointerException("X座標の値を入力してください");
                        }

                        data = ItemManager.get(key + ".location.y", config);
                        if (data.getValue() != null) {
                            y = Integer.valueOf((String) data.getValue());
                        } else {
                            throw new NullPointerException("Y座標の値を入力してください");
                        }

                        data = ItemManager.get(key + ".location.z", config);
                        if (data.getValue() != null) {
                            z = Integer.valueOf((String) data.getValue());
                        } else {
                            throw new NullPointerException("Z座標の値を入力してください");
                        }
                        spawnerLocation = new Location(Bukkit.getWorld("world"), x, y, z);
                    } catch (NumberFormatException e) {
                        throw new NumberFormatException("座標の値は数字で入力してください");
                    }

                    data = ItemManager.get(key + ".spawnInRadius", config);
                    if (data.getValue() != null) {
                        if (!StringUtils.isNumeric((String) data.getValue()))
                            throw new NumberFormatException("スポーン範囲は数字で入力してください");
                        spawnInRadius = Integer.valueOf((String) data.getValue());
                    } else {
                        throw new NullPointerException("スポーン範囲が設定されていません");
                    }

                    data = ItemManager.get(key + ".playerInRadius", config);
                    if (data.getValue() != null) {
                        if (!StringUtils.isNumeric((String) data.getValue()))
                            throw new NumberFormatException("検知範囲は数字で入力してください");
                        playerInRadius = Integer.valueOf((String) data.getValue());
                    } else {
                        throw new NullPointerException("検知範囲が設定されていません");
                    }

                    data = ItemManager.get(key + ".maxSpawnCount", config);
                    if (data.getValue() != null) {
                        if (!StringUtils.isNumeric((String) data.getValue()))
                            throw new NumberFormatException("最大エンティティ数は数字で入力してください");
                        maxSpawnCount = Integer.valueOf((String) data.getValue());
                    } else {
                        throw new NullPointerException("最大エンティティ数が設定されていません");
                    }

                    data = ItemManager.get(key + ".oneTimeSpawnCount", config);
                    if (data.getValue() != null) {
                        if (!StringUtils.isNumeric((String) data.getValue()))
                            throw new NumberFormatException("スポーンエンティティ数は数字で入力してください");
                        oneTimeSpawnCount = Integer.valueOf((String) data.getValue());
                    } else {
                        throw new NullPointerException("スポーンエンティティ数が設定されていません");
                    }

                    data = ItemManager.get(key + ".nextSpawnTime", config);
                    if (data.getValue() != null) {
                        if (!StringUtils.isNumeric((String) data.getValue()))
                            throw new NumberFormatException("スポーン間隔時間は数字で入力してください");
                        nextSpawnTime = Integer.valueOf((String) data.getValue());

                    } else {
                        throw new NullPointerException("スポーン間隔時間が設定されていません");
                    }

                    data = ItemManager.get(key + ".entityTeleportRadius", config);
                    if (data.getValue() != null) {
                        if (!StringUtils.isNumeric((String) data.getValue()))
                            throw new NumberFormatException("テレポート範囲は数字で入力してください");
                        entityTeleportRadius = Integer.valueOf((String) data.getValue());
                    } else {

                    }

                } catch (Exception e) {
                    successFull = false;
                    allError = true;
                    errors.add(ChatColor.RED + e.getMessage() + ChatColor.GRAY + "(Path: " + config.getName() + "/" + data.getKey() + ")");
                }

                if (successFull) {
                    TanoRPG.getEntitySpawnerManager().registerSpawner(new EntitySpawner(EntityManager.getBaseEntity(entityName),
                            spawnerLocation, spawnInRadius, playerInRadius, maxSpawnCount, oneTimeSpawnCount, nextSpawnTime,
                            entityTeleportRadius));
                    errors.add("§aEntitySpawners loaded without errors.");
                }
            }
        }
        if (allError) errors.remove("§aEntitySpawners loaded without errors.");
        return errors;
    }
}