package com.github.tanokun.tanorpg.game.mission;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayer;
import com.github.tanokun.tanorpg.game.mission.condition.ClearMissionCondition;
import com.github.tanokun.tanorpg.game.mission.condition.JobMissionCondition;
import com.github.tanokun.tanorpg.game.mission.condition.LvlMissionCondition;
import com.github.tanokun.tanorpg.game.mission.condition.MissionCondition;
import com.github.tanokun.tanorpg.game.mission.task.MissionTask;
import com.github.tanokun.tanorpg.util.io.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.annotation.Annotation;
import java.util.*;

public class MissionManager {

    public static final String PX = "§6[§a-｜ §b§lMission§a ｜-§6] §7=> §a";

    private static HashMap<Integer, Map<String, Mission>> NPC_ID_Missions = new HashMap<>();
    private static HashMap<UUID, LinkedList<Mission>> clearMissions = new HashMap<>();
    private static HashMap<UUID, LinkedList<Mission>> activeMissions = new HashMap<>();

    private static HashSet<UUID> player_clear_flag = new HashSet<>();
    public static void registerMission(Mission mission){
        HashMap<String, Mission> missions;
        if (!NPC_ID_Missions.containsKey(mission.getNPC_ID())){
            missions = new HashMap<>();
            missions.put(mission.getMissionName(), mission);
            NPC_ID_Missions.put(mission.getNPC_ID(), missions);
            return;
        } else {
            NPC_ID_Missions.get(mission.getNPC_ID()).put(mission.getMissionName(), mission);
        }
    }
    public static Mission getMission(int id, String name){
        if (NPC_ID_Missions.get(id) == null) return null;
        Iterator<Mission> missionIterator = getMission(id).iterator();
        while (missionIterator.hasNext()){
            Mission mission = missionIterator.next();
            if (mission.getMissionName().equals(name) && mission.getNPC_ID() == id){
                return mission;
            }
        }
        return null;
    }
    public static Collection<Mission> getMission(int id){
        return (NPC_ID_Missions.get(id) == null) ? new HashSet<>() : NPC_ID_Missions.get(id).values();
    }

    public static void addActiveMission(UUID uuid, Mission mission){
        LinkedList<Mission> missions;
        if (!activeMissions.containsKey(uuid)){
            missions = new LinkedList<>();
            missions.add(mission);
            activeMissions.put(uuid, missions);
            return;
        } else {
            activeMissions.get(uuid).add(mission);
        }
        System.out.println(activeMissions.get(uuid).size());
    }
    public static void removeActiveMission(UUID uuid, Mission mission){
        if (activeMissions.get(uuid) == null) return;
        Iterator<Mission> missionIterator = activeMissions.get(uuid).listIterator();
        while (missionIterator.hasNext()){
            Mission mission1 = missionIterator.next();
            if (mission1.equals(mission)) missionIterator.remove();
        }
    }
    public static LinkedList<Mission> getActiveMissions(UUID uuid) {
        return (activeMissions.get(uuid) == null) ? new LinkedList<>() : activeMissions.get(uuid);
    }
    public static boolean isActive(UUID uuid, Mission mission){
        return getActiveMissions(uuid).contains(mission);
    }

    public static void addClearMission(UUID uuid, Mission mission){
        LinkedList<Mission> missions;
        if (!clearMissions.containsKey(uuid)){
            missions = new LinkedList<>();
            missions.add(mission);
            removeClearFlag(uuid);
            removeActiveMission(uuid, mission);
            clearMissions.put(uuid, missions);
            return;
        } else {
            removeClearFlag(uuid);
            clearMissions.get(uuid).add(mission);
            removeActiveMission(uuid, mission);
        }
    }
    public static void removeClearMission(UUID uuid, Mission mission){
        if (clearMissions.get(uuid) == null) return;
        Iterator<Mission> missionIterator = clearMissions.get(uuid).listIterator();
        while (missionIterator.hasNext()){
            Mission mission1 = missionIterator.next();
            if (mission1.equals(mission)) missionIterator.remove();
        }
    }
    public static LinkedList<Mission> getClearMissions(UUID uuid) {
        return (clearMissions.get(uuid) == null) ? new LinkedList<>() : clearMissions.get(uuid);
    }
    public static boolean isClear(UUID uuid, Mission mission){
        return getClearMissions(uuid).contains(mission);
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
        for (String id : ids){
            for (Mission mission : getMission(Integer.parseInt(id))) {
                if (!config.getConfig().isSet("mission_data.ID_" + id + "."
                        + mission.getMissionName() + ".clear")) continue;
                if (config.getConfig().getBoolean("mission_data.ID_" + id + "."
                        + mission.getMissionName() + ".clear")) {
                    addClearMission(uuid, mission);
                    continue;
                }
                addActiveMission(uuid, mission);
                for (MissionTask missionTask : mission.getMissionTasks()) {
                    String value = config.getConfig().getString("mission_data.ID_" + id + "."
                            + mission.getMissionName() + "." + missionTask.getMessage() + ".value");
                    missionTask.setValue(uuid, value);
                }
            }
        }
    }

    public static void saveData(UUID uuid){
        if (activeMissions.get(uuid) == null) return;
        Config config = new Config(uuid.toString() + ".yml", "player_database", TanoRPG.getPlugin());
        Iterator<Mission> iterator = activeMissions.get(uuid).iterator();
        while (iterator.hasNext()){
            Mission mission = iterator.next();
            int id = mission.getNPC_ID();
            config.getConfig().set("mission_data.ID_" + id + "."
                    + mission.getMissionName() + ".clear", false);
            for(MissionTask missionTask : mission.getMissionTasks()){
                config.getConfig().set("mission_data.ID_" + id + "."
                        + mission.getMissionName() + "." + missionTask.getMessage() + ".value", missionTask.getValue(uuid));
                missionTask.removeValue(uuid);
            }
            iterator.remove();
            removeClearFlag(uuid);
        }
        config.saveConfig();

        if (clearMissions.get(uuid) == null) return;
        iterator = clearMissions.get(uuid).iterator();
        while (iterator.hasNext()){
            Mission mission = iterator.next();
            int id = mission.getNPC_ID();
            config.getConfig().set("mission_data.ID_" + id + "."
                    + mission.getMissionName() + ".clear", true);
            iterator.remove();
        }
        config.saveConfig();
    }
    public static void saveDataAll() {
        for(Player player : Bukkit.getOnlinePlayers()){
            saveData(player.getUniqueId());
        }
    }

    public static boolean isFit(GamePlayer player, Mission isMission) {
        if (!(isMission instanceof MissionCondition)) return true;
        Class<? extends Mission> clazz = isMission.getClass();
        for (Annotation annotation : clazz.getAnnotations()){
            if (annotation instanceof JobMissionCondition){
                JobMissionCondition jobMissionCondition = (JobMissionCondition) annotation;
                if (!Arrays.asList(jobMissionCondition.value()).contains(player.getJob())) return false;
            }
            if (annotation instanceof LvlMissionCondition){
                LvlMissionCondition jobMissionCondition = (LvlMissionCondition) annotation;
                if (jobMissionCondition.value() > player.getLEVEL()) return false;
            }
            if (annotation instanceof ClearMissionCondition){
                ClearMissionCondition clearMissionCondition = (ClearMissionCondition) annotation;
                ArrayList<String> names = new ArrayList<>();
                for (Mission mission : getClearMissions(player.getUuid())){
                    names.add(mission.getMissionName());
                }
                for (String missionName : clearMissionCondition.value()){
                    if (!names.contains(missionName)) return false;
                }
            }
        }
        return true;
    }
}
