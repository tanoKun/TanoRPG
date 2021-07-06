package com.github.tanokun.tanorpg.game.entity.spawner;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.EntityDropItems;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.game.entity.EntityType;
import com.github.tanokun.tanorpg.game.entity.base.ObjectEntity;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.player.EquipmentMap;
import com.github.tanokun.tanorpg.player.status.StatusMap;
import com.github.tanokun.tanorpg.player.status.StatusType;
import com.github.tanokun.tanorpg.util.command.CommandContext;
import com.github.tanokun.tanorpg.util.io.Config;
import com.github.tanokun.tanorpg.util.io.Folder;
import com.github.tanokun.tanorpg.util.io.MapNode;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;

public class EntitySpawnerManager extends BukkitRunnable {
    private HashSet<EntitySpawner> spawners = new HashSet<>();

    @Override
    public void run() {
        for (EntitySpawner spawner : spawners){
            spawner.spawnEntities();
        }
    }

    public EntitySpawnerManager(Player player){
        loadEntity(player);
    }

    public void loadEntity(Player p) {
        try {
            for (Config config : new Folder("spawner", TanoRPG.getPlugin()).getFiles()) {
                for (String key : config.getConfig().getKeys(false)) {
                    Location spawnerLocation = null;
                    int spawnInRadius;
                    int playerInRadius;
                    int maxSpawnCount;
                    int oneTimeSpawnCount;
                    int nextSpawnTime;
                    int entityTeleportRadius;
                    ObjectEntity objectEntity = TanoRPG.getPlugin().getEntityManager().getEntity(config.getConfig().getString(key + ".entity", ""));
                    spawnerLocation = new Location(Bukkit.getWorld(config.getConfig().getString(key + ".location.world", "world")),
                            config.getConfig().getInt(key + ".location.x", 0),
                            config.getConfig().getInt(key + ".location.y", 0),
                            config.getConfig().getInt(key + ".location.z", 0));
                    spawnInRadius = config.getConfig().getInt(key + ".location.spawnInRadius", 0);
                    playerInRadius = config.getConfig().getInt(key + ".location.playerInRadius", 0);
                    maxSpawnCount = config.getConfig().getInt(key + ".location.maxSpawnCount", 0);
                    oneTimeSpawnCount = config.getConfig().getInt(key + ".location.oneTimeSpawnCount", 0);
                    nextSpawnTime = config.getConfig().getInt(key + ".location.nextSpawnTime", 0);
                    entityTeleportRadius = config.getConfig().getInt(key + ".location.entityTeleportRadius", 0);

                    spawners.add(new EntitySpawner(objectEntity,
                            spawnerLocation, spawnInRadius, playerInRadius, maxSpawnCount, oneTimeSpawnCount, nextSpawnTime,
                            entityTeleportRadius));
                }
            }
        }catch (Exception e){
            if (p != null){
                p.sendMessage(TanoRPG.PX + "§cエンティティスポナー系のコンフィグでエラーが発生しました。");
            } else {
                TanoRPG.getPlugin().getLogger().warning("エンティティスポナー系のコンフィグでエラーが発生しました。");
            }
        }

        if (p != null){
            p.sendMessage(TanoRPG.PX + "§aエンティティスポナー系のコンフィグをロードしました。");
        } else {
            TanoRPG.getPlugin().getLogger().warning("エンティティスポナー系のコンフィグをロードしました。");
        }
    }

}