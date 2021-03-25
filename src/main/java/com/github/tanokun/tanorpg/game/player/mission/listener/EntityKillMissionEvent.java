package com.github.tanokun.tanorpg.game.player.mission.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.event.tanorpg.CustomEntityKillEvent;
import com.github.tanokun.tanorpg.game.player.GameActionbar;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.mission.Mission;
import com.github.tanokun.tanorpg.game.player.mission.task.EntityKillTask;
import com.github.tanokun.tanorpg.game.player.mission.task.MissionTask;
import com.github.tanokun.tanorpg.game.player.status.Sidebar;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EntityKillMissionEvent implements Listener {

    @EventHandler
    public void onKill(CustomEntityKillEvent e){
        for(Mission mission : com.github.tanokun.tanorpg.game.player.mission.MissionManager.getActiveMissions(e.getAttacker().getUniqueId())){
            if (!(GamePlayerManager.getPlayer(e.getAttacker().getUniqueId()).getActive_mission_NPC_ID() == mission.getNPC_ID())) return;
            for (MissionTask missionTask : mission.getMissionTasks()){
                if (!(missionTask instanceof EntityKillTask)) continue;
                EntityKillTask killTask = (EntityKillTask) missionTask;
                if (!killTask.getTarget().equals(e.getEntityData())) continue;
                killTask.setValue(e.getAttacker().getUniqueId(), killTask.getValue(e.getAttacker().getUniqueId()) + 1);
                new GameActionbar(killTask.getMessage(e.getAttacker())).showActionBar(e.getAttacker());
                Sidebar.updateSidebar(e.getAttacker());
            }
            if (mission.isActiveClear(e.getAttacker().getUniqueId())){
                if (com.github.tanokun.tanorpg.game.player.mission.MissionManager.isClearFlag(e.getAttacker().getUniqueId())){return;}
                e.getAttacker().sendMessage(com.github.tanokun.tanorpg.game.player.mission.MissionManager.PX + "§aミッションを達成しました！ 報告しに行きましょう！§7「" + mission.getMissionName() + "」");
                TanoRPG.playSound(e.getAttacker(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 1);
                com.github.tanokun.tanorpg.game.player.mission.MissionManager.addClearFlag(e.getAttacker().getUniqueId());
                Sidebar.updateSidebar(e.getAttacker());
            }
        }
    }
}
