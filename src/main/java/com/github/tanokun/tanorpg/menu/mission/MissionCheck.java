package com.github.tanokun.tanorpg.menu.mission;

import com.github.tanokun.api.smart_inv.inv.ClickableItem;
import com.github.tanokun.api.smart_inv.inv.SmartInventory;
import com.github.tanokun.api.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.api.smart_inv.inv.contents.InventoryProvider;
import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.mission.Mission;
import com.github.tanokun.tanorpg.game.mission.listener.NpcClickListener;
import com.github.tanokun.tanorpg.game.player.status.Sidebar;
import com.github.tanokun.tanorpg.menu.player.StatusMainMenu;
import com.github.tanokun.tanorpg.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;


public class MissionCheck implements InventoryProvider {

    private final Mission mission;

    public MissionCheck(Mission mission){
        this.mission = mission;
    }

    public static SmartInventory INVENTORY(Mission mission) {
        return SmartInventory.builder()
                .id("mission_check")
                .provider(new MissionCheck(mission))
                .size(1, 9)
                .title("§aMission>> §7ミッションを遂行しますか？")
                .closeable(false)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(0, 2, ClickableItem.of(ItemUtils.createItem(Material.BLUE_WOOL, "§a遂行します", 1, true), e -> {
            new BukkitRunnable() {
                @Override
                public void run() {
                    GamePlayerManager.flag_nowMissionEvent.add(e.getWhoClicked().getUniqueId());
                    try {
                        mission.startMission((Player) e.getWhoClicked());
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    GamePlayerManager.getPlayer(e.getWhoClicked().getUniqueId()).setActive_mission_NPC_ID(mission.getNPC_ID());
                    GamePlayerManager.getPlayer(e.getWhoClicked().getUniqueId()).setActive_mission_NPC_Name(mission.getMissionName());
                    Sidebar.updateSidebar((Player) e.getWhoClicked());
                    GamePlayerManager.flag_nowMissionEvent.remove(e.getWhoClicked().getUniqueId());
                    this.cancel();
                }
            }.runTaskAsynchronously(TanoRPG.getPlugin());
            contents.inventory().close(player);
        }));

        contents.set(0, 6, ClickableItem.of(ItemUtils.createItem(Material.RED_WOOL, "§c遂行しません", 1, true), e -> {
            new BukkitRunnable() {
                @Override
                public void run() {
                    GamePlayerManager.flag_nowMissionEvent.add(e.getWhoClicked().getUniqueId());
                    try {mission.cancelMission((Player) e.getWhoClicked());} catch (Exception exception) {exception.printStackTrace();}
                    GamePlayerManager.flag_nowMissionEvent.remove(e.getWhoClicked().getUniqueId());
                    this.cancel();
                }
            }.runTaskAsynchronously(TanoRPG.getPlugin());
            contents.inventory().close(player);
        }));
    }
}
