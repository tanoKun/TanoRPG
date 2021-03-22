package com.github.tanokun.tanorpg.game.player.mission.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.mission.Mission;
import com.github.tanokun.tanorpg.game.player.mission.MissionManager;
import com.github.tanokun.tanorpg.menu.mission.MissionCheck;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class NpcClickListener implements Listener {
    public static final HashMap<UUID, Mission> meta = new HashMap<>();
    public static final HashMap<UUID, Mission> meta2 = new HashMap<>();
    public static final HashSet<UUID> flag = new HashSet<>();

    @EventHandler
    public void onClick(NPCRightClickEvent e){
        Player player = e.getClicker();
        NPC npc = e.getNPC();
        int id = npc.getId();
        if (MissionManager.getMission(id) == null) return;
        Mission mission = MissionManager.getMission(id);
        if (MissionManager.isClear(player.getUniqueId(), mission)) return;
        if (flag.contains(e.getClicker().getUniqueId())){
            e.getClicker().sendMessage(EventKillListener.PX + "§cミッションイベントが進行中です");
            return;
        }
        if (MissionManager.isActive(player.getUniqueId(), mission)) {
            if (mission.isActiveClear(e.getClicker().getUniqueId())){
                flag.add(e.getClicker().getUniqueId());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {mission.finishMission(player);} catch (Exception exception) {exception.printStackTrace();}
                        flag.remove(e.getClicker().getUniqueId());
                        this.cancel();
                    }
                }.runTaskAsynchronously(TanoRPG.getPlugin());
            }
        } else {
            if (MissionManager.getActiveMissions(e.getClicker().getUniqueId()).size() >= 5){
                e.getClicker().sendMessage(EventKillListener.PX + "§c5個以上のミッションを同時に受けることはできません");
            }
            flag.add(e.getClicker().getUniqueId());
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {mission.showNPCMessages(player);} catch (Exception exception) {exception.printStackTrace();}
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            meta.put(player.getUniqueId(), mission);
                            new MissionCheck().openInv(e.getClicker());
                            TanoRPG.playSound(e.getClicker(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 1);
                            this.cancel();
                        }
                    }.runTask(TanoRPG.getPlugin());
                    flag.remove(e.getClicker().getUniqueId());
                    this.cancel();
                }
            }.runTaskAsynchronously(TanoRPG.getPlugin());
        }
    }
}
