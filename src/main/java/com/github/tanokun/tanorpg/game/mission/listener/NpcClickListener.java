package com.github.tanokun.tanorpg.game.mission.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayer;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.mission.Mission;
import com.github.tanokun.tanorpg.game.mission.MissionManager;
import com.github.tanokun.tanorpg.menu.mission.AllMissionMenu;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class NpcClickListener implements Listener {

    @EventHandler
    public void onClick(NPCRightClickEvent e){
        Player player = e.getClicker();
        GamePlayer gamePlayer = GamePlayerManager.getPlayer(e.getClicker().getUniqueId());
        NPC npc = e.getNPC();
        int id = npc.getId();
        if (MissionManager.getMission(id).isEmpty()) return;

        if (GamePlayerManager.flag_nowMissionEvent.contains(e.getClicker().getUniqueId())) return;

        for (Mission activeMission : MissionManager.getActiveMissions(player.getUniqueId())) {
            if (!(activeMission.getNPC_ID() == id)) continue;
            if (activeMission.isActiveClear(player.getUniqueId())) {
                Mission finalMission = activeMission;
                GamePlayerManager.flag_nowMissionEvent.add(e.getClicker().getUniqueId());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            finalMission.finishMission(player);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        GamePlayerManager.flag_nowMissionEvent.remove(e.getClicker().getUniqueId());
                        this.cancel();
                        return;
                    }
                }.runTaskAsynchronously(TanoRPG.getPlugin());
            }
        }

        if (MissionManager.getMission(id).contains(MissionManager.getMission(
                gamePlayer.getActive_mission_NPC_ID(), gamePlayer.getActive_mission_NPC_Name()))) return;
        AllMissionMenu.INVENTORY(id).open(player);
    }
}
