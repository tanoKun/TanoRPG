package com.github.tanokun.tanorpg.game.player;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.util.io.Config;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class GamePlayerManager {
    private static HashMap<UUID, GamePlayer> players = new HashMap<>();
    public static GamePlayer loadData(UUID uuid){
        Gson gson = new Gson();
        String json;
        GamePlayer player;

        Config config = new Config(uuid.toString() + ".yml", "player_database", TanoRPG.getPlugin());
        if (config.createExists()){
            json = config.getConfig().getString("JSON_DATA");
            player = gson.fromJson(json, GamePlayer.class);
        } else {return null;}
        players.put(uuid, player);
        return player;
    }
    public static GamePlayer createData(UUID uuid, GamePlayerJobType job){
        GamePlayer player;
        player = new GamePlayer(uuid, job);
        players.put(uuid, player);
        return player;
    }
    public static void saveData(UUID uuid){
        if (players.get(uuid) != null) {
            Config config = new Config(uuid.toString() + ".yml", "player_database", TanoRPG.getPlugin());
            Gson gson = new Gson();
            String json = gson.toJson(players.get(uuid));
            config.getConfig().set("JSON_DATA", json);
            config.saveConfig();
        }
    }
    public static void saveDataAll(){
        for(Player player : Bukkit.getOnlinePlayers()) {
            saveData(player.getUniqueId());
        }
    }
    public static GamePlayer getPlayer(UUID uuid){
        return players.get(uuid);
    }
    public static void removeData(UUID uuid) {
        players.remove(uuid);
    }
}
