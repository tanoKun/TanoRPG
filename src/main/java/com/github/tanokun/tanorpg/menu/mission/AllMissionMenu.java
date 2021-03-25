package com.github.tanokun.tanorpg.menu.mission;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.mission.Mission;
import com.github.tanokun.tanorpg.game.player.mission.MissionManager;
import com.github.tanokun.tanorpg.game.player.mission.listener.NpcClickListener;
import com.github.tanokun.tanorpg.game.player.mission.task.MissionTask;
import com.github.tanokun.tanorpg.menu.Menu;
import com.github.tanokun.tanorpg.menu.MenuManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class AllMissionMenu extends Menu {
    public AllMissionMenu(Player player){
        super("§aMission>> §7ミッション一覧", 1);
        if (player == null) return;
        int slot = 0;
        for(Mission mission : MissionManager.getMission(NpcClickListener.flag_selNPC_ID.get(player.getUniqueId()))) {
            if (MissionManager.isClear(player.getUniqueId(), mission)) continue;
            if (!MissionManager.isFit(GamePlayerManager.getPlayer(player.getUniqueId()), mission)) continue;
            String name = mission.getMissionName();
            List<String> lines = new ArrayList<>();
            for (MissionTask missionTask : mission.getMissionTasks()) {
                lines.add("    " + missionTask.getMessage(player));
            }
            ItemStack missionItem = MenuManager.createItem(Material.BOOK, "§d" + name, lines, 1, true);
            setItem(slot, missionItem);
            slot++;
        }
    }

    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getType().equals(Material.AIR)) return;
        Player p = (Player)e.getWhoClicked();
        String name = e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§d", "");


        if (MissionManager.getActiveMissions(e.getWhoClicked().getUniqueId()).size() >= 5){
            e.getWhoClicked().sendMessage(MissionManager.PX + "§c5個以上のミッションを同時に受けることはできません");
            e.getWhoClicked().closeInventory();
            return;
        }
        if (MissionManager.getActiveMissions(p.getUniqueId()).size() >= 5){
            p.sendMessage(MissionManager.PX + "§c5個以上のミッションを同時に受けることはできません");
            e.getWhoClicked().closeInventory();
            return;
        }

        Mission mission = MissionManager.getMission(NpcClickListener.flag_selNPC_ID.get(p.getUniqueId()), name);
        NpcClickListener.flag_nowMissionEvent.add(p.getUniqueId());

        e.getWhoClicked().closeInventory();

        new BukkitRunnable() {
            @Override
            public void run() {
                try {mission.showNPCMessages(p);} catch (Exception exception) {exception.printStackTrace();}
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        NpcClickListener.meta_Mission.put(p.getUniqueId(), mission);
                        new MissionCheck().openInv(p);
                        TanoRPG.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 1);
                        NpcClickListener.flag_nowMissionEvent.remove(p.getUniqueId());
                        NpcClickListener.flag_selNPC_ID.remove(p.getUniqueId());
                        this.cancel();
                    }
                }.runTask(TanoRPG.getPlugin());
                this.cancel();
            }
        }.runTaskAsynchronously(TanoRPG.getPlugin());

    }

    public void onClose(InventoryCloseEvent e) {}
}
