package com.github.tanokun.tanorpg.menu.mission;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.mission.Mission;
import com.github.tanokun.tanorpg.game.mission.listener.NpcClickListener;
import com.github.tanokun.tanorpg.game.player.status.Sidebar;
import com.github.tanokun.tanorpg.menu.Menu;
import com.github.tanokun.tanorpg.menu.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;


public class MissionCheck extends Menu {
    public MissionCheck() {
        super("§aMission>> §7ミッションを遂行しますか？", 1);
        setItem(2, MenuManager.createItem(Material.BLUE_WOOL, "§a遂行します", 1, true));
        setItem(6, MenuManager.createItem(Material.RED_WOOL, "§c遂行しません", 1, true));
    }

    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getType() == Material.AIR) return;
        if (!e.getClickedInventory().equals(e.getWhoClicked().getOpenInventory().getTopInventory()) && e.getView().getTitle().equals("§aMission>> §7ミッションを遂行しますか？")) return;
        Mission mission = NpcClickListener.meta_Mission.get(e.getWhoClicked().getUniqueId());
        if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§a遂行します")){
            new BukkitRunnable() {
                @Override
                public void run() {
                    NpcClickListener.flag_nowMissionEvent.add(e.getWhoClicked().getUniqueId());
                    try {
                        mission.startMission((Player) e.getWhoClicked());
                    } catch (Exception exception) {exception.printStackTrace();}
                    GamePlayerManager.getPlayer(e.getWhoClicked().getUniqueId()).setActive_mission_NPC_ID(mission.getNPC_ID());
                    GamePlayerManager.getPlayer(e.getWhoClicked().getUniqueId()).setActive_mission_NPC_Name(mission.getMissionName());
                    Sidebar.updateSidebar((Player) e.getWhoClicked());
                    NpcClickListener.flag_nowMissionEvent.remove(e.getWhoClicked().getUniqueId());
                    this.cancel();
                }
            }.runTaskAsynchronously(TanoRPG.getPlugin());
        } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§c遂行しません")){
            new BukkitRunnable() {
                @Override
                public void run() {
                    NpcClickListener.flag_nowMissionEvent.add(e.getWhoClicked().getUniqueId());
                    try {mission.cancelMission((Player) e.getWhoClicked());} catch (Exception exception) {exception.printStackTrace();}
                    NpcClickListener.flag_nowMissionEvent.remove(e.getWhoClicked().getUniqueId());
                    this.cancel();
                }
            }.runTaskAsynchronously(TanoRPG.getPlugin());
        }
        NpcClickListener.meta_Mission.remove(e.getWhoClicked().getUniqueId());
        e.getWhoClicked().closeInventory();
    }

    public void onClose(InventoryCloseEvent e) {
        if (NpcClickListener.meta_Mission.get(e.getPlayer().getUniqueId()) == null) return;
        else {e.getPlayer().sendMessage(TanoRPG.PX + "§c選択肢を選んでください");}
        Bukkit.getScheduler().runTask(TanoRPG.getPlugin(), () -> {
            openInv((Player) e.getPlayer());
        });
    }
}
