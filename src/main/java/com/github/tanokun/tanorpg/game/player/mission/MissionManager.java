package com.github.tanokun.tanorpg.game.player.mission;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.mission.task.MissionTask;
import com.github.tanokun.tanorpg.util.io.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class MissionManager {
    private static HashMap<Integer, Mission> NPC_ID_Missions = new HashMap<>();

    private static HashMap<UUID, ArrayList<Mission>> activeMissions = new HashMap<>();
    private static HashMap<UUID, ArrayList<Mission>> clearMissions = new HashMap<>();

    private static HashSet<UUID> player_clear_flag = new HashSet<>();

    public static void registerMission(Mission mission){NPC_ID_Missions.put(mission.getNPC_ID(), mission);}
    public static Mission getMission(int id){return NPC_ID_Missions.get(id);}

    public static void addActiveMission(UUID uuid, Mission mission){
        ArrayList<Mission> missions;
        if (!activeMissions.containsKey(uuid)){
            missions = new ArrayList<>();
            missions.add(mission);
            activeMissions.put(uuid, missions);
            return;
        } else {
            activeMissions.get(uuid).add(mission);
        }
    }
    public static ArrayList<Mission> getActiveMissions(UUID uuid) {
        return (activeMissions.get(uuid) == null) ? new ArrayList<>() : activeMissions.get(uuid);
    }
    public static boolean isActive(UUID uuid, Mission mission){
        if (!activeMissions.containsKey(uuid)) return false;
        return activeMissions.get(uuid).contains(mission);
    }

    public static void addClearMission(UUID uuid, Mission mission){
        ArrayList<Mission> missions;
        if (!clearMissions.containsKey(uuid)){
            missions = new ArrayList<>();
            missions.add(mission);
            removeClearFlag(uuid);
            clearMissions.put(uuid, missions);
            return;
        } else {
            clearMissions.get(uuid).add(mission);
        }
    }
    public static ArrayList<Mission> getClearMissions(UUID uuid) {
        return (clearMissions.get(uuid) == null) ? new ArrayList<>() : clearMissions.get(uuid);
    }
    public static boolean isClear(UUID uuid, Mission mission){
        if (!clearMissions.containsKey(uuid)) return false;
        return clearMissions.get(uuid).contains(mission);
    }

    public static void addClearFlag(UUID uuid){
        player_clear_flag.add(uuid);
    }
    public static void removeClearFlag(UUID uuid){
        player_clear_flag.remove(uuid);
    }
    public static boolean isClearFlag(UUID uuid){return player_clear_flag.contains(uuid);}

    public static void loadData(UUID uuid){
        Config config = new Config(uuid.toString() + ".yml", "player_database", TanoRPG.getPlugin());
        Set<String> ids = new HashSet<>();
        if (!config.getConfig().isSet("mission_data")) return;
        for (String id : config.getConfig().getConfigurationSection("mission_data").getKeys(false)){
            ids.add(id.replace("ID_", ""));
        }
        Mission mission;
        for (String id : ids){
            mission = getMission(Integer.parseInt(id));
            if (config.getConfig().getBoolean("mission_data.ID_" + id + ".clear")){
                addClearMission(uuid, mission);
                continue;
            }
            addActiveMission(uuid, mission);
            for (MissionTask missionTask : mission.getMissionTasks()){
                String value = config.getConfig().getString("mission_data.ID_" + id + "." + missionTask.getMessage() + ".value");
                missionTask.setValue(uuid, value);
            }
        }
    }

    public static void saveData(UUID uuid){
        if (activeMissions.get(uuid) == null) return;
        Config config = new Config(uuid.toString() + ".yml", "player_database", TanoRPG.getPlugin());
        for (Mission mission : activeMissions.get(uuid)){
            int id = mission.getNPC_ID();
            config.getConfig().set("mission_data.ID_" + id + ".clear", false);
            for(MissionTask missionTask : mission.getMissionTasks()){
                config.getConfig().set("mission_data.ID_" + id + "." + missionTask.getMessage() + ".value", missionTask.getValue(uuid));
            }
        }
        config.saveConfig();
        if (clearMissions.get(uuid) == null) return;
        for (Mission mission : clearMissions.get(uuid)) {
            int id = mission.getNPC_ID();
            config.getConfig().set("mission_data.ID_" + id + ".clear", true);
        }
        config.saveConfig();
    }
    public static void saveDataAll() {
        for(Player player : Bukkit.getOnlinePlayers()){
            saveData(player.getUniqueId());
        }
    }
}
