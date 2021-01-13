package com.github.tanokun.tanorpg.game.player;

import com.github.tanokun.tanorpg.TanoRPG;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.UUID;

public class GamePlayerManager {
    private static HashMap<UUID, GamePlayer> players = new HashMap<>();
    public static GamePlayer loadData(UUID uuid){
        File data = new File(TanoRPG.getPlugin().getDataFolder() + File.separator + "player_database", uuid.toString() + ".json");
        Gson gson = new Gson();
        GamePlayer player;
        String json = null;
        if (data.exists()){
            try {json = new String(Files.readAllBytes(data.toPath()), Charset.defaultCharset());} catch (IOException e) {e.printStackTrace();}
            player = gson.fromJson(json, GamePlayer.class);
        } else {
            return null;
        }
        players.put(uuid, player);
        return player;
    }
    public static GamePlayer createData(UUID uuid, GamePlayerJobType job){
        GamePlayer player = null;
        player = new GamePlayer(uuid, job);
        players.put(uuid, player);
        return player;
    }
    public static void saveData(UUID uuid){
        if (players.get(uuid) != null) {
            File data = new File(TanoRPG.getPlugin().getDataFolder() + File.separator + "player_database", uuid.toString() + ".json");
            Gson gson = new Gson();
            String json = gson.toJson(players.get(uuid));
            BufferedWriter bw = null;
            data.delete();
            try {
                Files.newBufferedWriter(data.toPath(), Charset.defaultCharset());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bw = new BufferedWriter(new FileWriter(data));
                bw.write(json);
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void saveDataAll(){
        for (GamePlayer player : players.values())
        if (players.get(player.getUuid()) != null) {
            File data = new File(TanoRPG.getPlugin().getDataFolder() + File.separator + "player_database", player.getUuid().toString() + ".json");
            Gson gson = new Gson();
            String json = gson.toJson(player);
            BufferedWriter bw = null;
            try {
                Files.newBufferedWriter(data.toPath(), Charset.defaultCharset());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bw = new BufferedWriter(new FileWriter(data));
                bw.write(json);
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static GamePlayer getPlayer(UUID uuid){
        return players.get(uuid);
    }

    public static void removeData(UUID uuid) {
        players.remove(uuid);
    }
}
