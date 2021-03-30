package com.github.tanokun.tanorpg.game.mission.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.event.tanorpg.CustomEntityKillEvent;
import com.github.tanokun.tanorpg.event.tanorpg.CustomShopEvent;
import com.github.tanokun.tanorpg.game.craft.Craft;
import com.github.tanokun.tanorpg.game.mission.Mission;
import com.github.tanokun.tanorpg.game.mission.MissionManager;
import com.github.tanokun.tanorpg.game.mission.task.CraftTask;
import com.github.tanokun.tanorpg.game.mission.task.EntityKillTask;
import com.github.tanokun.tanorpg.game.mission.task.MissionTask;
import com.github.tanokun.tanorpg.game.mission.task.ShoppingTask;
import com.github.tanokun.tanorpg.game.player.GameActionbar;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.status.Sidebar;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ShoppingEventListener implements Listener {

    @EventHandler
    public void onKill(CustomShopEvent e){
        for(Mission mission : MissionManager.getActiveMissions(e.getPlayer().getUniqueId())){
            if (!(GamePlayerManager.getPlayer(e.getPlayer().getUniqueId()).getActive_mission_NPC_ID() == mission.getNPC_ID())) return;
            for (MissionTask missionTask : mission.getMissionTasks()){
                if (!(missionTask instanceof ShoppingTask)) continue;
                ShoppingTask shoppingTask = (ShoppingTask) missionTask;
                if (shoppingTask.getItemId() == null){
                    shoppingTask.setValue(e.getPlayer().getUniqueId(), shoppingTask.getValue(e.getPlayer().getUniqueId()) + 1);
                    new GameActionbar(shoppingTask.getMessage(e.getPlayer())).showActionBar(e.getPlayer());
                    Sidebar.updateSidebar(e.getPlayer());
                    continue;
                }
                if (!shoppingTask.getItemId().equals(e.getItem().getId())) continue;
                shoppingTask.setValue(e.getPlayer().getUniqueId(), shoppingTask.getValue(e.getPlayer().getUniqueId()) + 1);
                new GameActionbar(shoppingTask.getMessage(e.getPlayer())).showActionBar(e.getPlayer());
                Sidebar.updateSidebar(e.getPlayer());
            }
            if (mission.isActiveClear(e.getPlayer().getUniqueId())){
                if (com.github.tanokun.tanorpg.game.mission.MissionManager.isClearFlag(e.getPlayer().getUniqueId())){return;}
                e.getPlayer().sendMessage(com.github.tanokun.tanorpg.game.mission.MissionManager.PX + "§aミッションを達成しました！ 報告しに行きましょう！§7「" + mission.getMissionName() + "」");
                TanoRPG.playSound(e.getPlayer(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 1);
                com.github.tanokun.tanorpg.game.mission.MissionManager.addClearFlag(e.getPlayer().getUniqueId());
                Sidebar.updateSidebar(e.getPlayer());
            }
        }
    }
}
