package com.github.tanokun.tanorpg.game.player.mission;


import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.mission.task.MissionTask;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public abstract class Mission {
    private final String missionName;
    private final HashMap<String, MissionTask> tasks = new HashMap<>();
    private final int NPC_ID;


    public Mission(String missionName, int npc_id){
        this.missionName = missionName;
        NPC_ID = npc_id;
    }

    public void addMissionTask(MissionTask mt){tasks.put(mt.getMessage(), mt);}

    public ArrayList<MissionTask> getMissionTasks() {return new ArrayList<>(tasks.values());}

    public int getNPC_ID() {return NPC_ID;}
    public String getMissionName() {return missionName;}

    public abstract void showNPCMessages(Player player) throws Exception;

    public abstract void startMission(Player player) throws Exception;
    public abstract void finishMission(Player player) throws Exception;
    public abstract void cancelMission(Player player) throws Exception;

    public boolean isActiveClear(UUID uuid){
        for(MissionTask missionTask : tasks.values()){
            if (!missionTask.isClearTask(uuid)) return false;
        }
        return true;
    }

    /*同期のAPI*/
    public void sendMessage(Player player, String message){
        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage(message);
            }
        }.runTask(TanoRPG.getPlugin());
    }
    public void playSound(Player player, Sound sound, int volume, double v2){
        new BukkitRunnable() {
            @Override
            public void run() {
                player.playSound(player.getLocation(), sound, volume, (float) v2);
            }
        }.runTask(TanoRPG.getPlugin());
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mission mission = (Mission) o;
        return NPC_ID == mission.NPC_ID && Objects.equals(missionName, mission.missionName) && Objects.equals(tasks, mission.tasks);
    }
    public int hashCode() {
        return Objects.hash(missionName, tasks, NPC_ID);
    }
}
