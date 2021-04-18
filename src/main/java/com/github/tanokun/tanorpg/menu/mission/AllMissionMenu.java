package com.github.tanokun.tanorpg.menu.mission;

import com.github.tanokun.api.smart_inv.inv.ClickableItem;
import com.github.tanokun.api.smart_inv.inv.SmartInventory;
import com.github.tanokun.api.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.api.smart_inv.inv.contents.InventoryProvider;
import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.mission.Mission;
import com.github.tanokun.tanorpg.game.mission.MissionManager;
import com.github.tanokun.tanorpg.game.mission.listener.NpcClickListener;
import com.github.tanokun.tanorpg.game.mission.task.MissionTask;
import com.github.tanokun.tanorpg.menu.player.StatusMainMenu;
import com.github.tanokun.tanorpg.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class AllMissionMenu implements InventoryProvider {

    private final int NPC_ID;

    public AllMissionMenu(int npc_id){
        this.NPC_ID = npc_id;
    }

    public static SmartInventory INVENTORY(int npc_id) {
        return SmartInventory.builder()
                .id("mission_menu")
                .provider(new AllMissionMenu(npc_id))
                .size(1, 9)
                .title("§aMission>> §7ミッション一覧")
                .closeable(true)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        int slot = 0;
        for (Mission mission : MissionManager.getMission(NPC_ID)) {
            if (MissionManager.isClear(player.getUniqueId(), mission)) continue;
            if (mission.isActiveClear(player.getUniqueId())) continue;
            if (!MissionManager.isFit(GamePlayerManager.getPlayer(player.getUniqueId()), mission)) continue;
            String name = mission.getMissionName();
            List<String> lines = new ArrayList<>();
            for (MissionTask missionTask : mission.getMissionTasks()) {
                lines.add("    " + missionTask.getMessage(player));
            }
            ItemStack missionItem = ItemUtils.createItem(Material.BOOK, "§d" + name, lines, 1, true);
            contents.add(ClickableItem.of(missionItem, e -> {
                if (MissionManager.getActiveMissions(e.getWhoClicked().getUniqueId()).size() >= 5){
                    e.getWhoClicked().sendMessage(MissionManager.PX + "§c5個以上のミッションを同時に受けることはできません");
                    e.getWhoClicked().closeInventory();
                    return;
                }
                if (MissionManager.getActiveMissions(player.getUniqueId()).size() >= 5){
                    player.sendMessage(MissionManager.PX + "§c5個以上のミッションを同時に受けることはできません");
                    e.getWhoClicked().closeInventory();
                    return;
                }

                GamePlayerManager.flag_nowMissionEvent.add(player.getUniqueId());

                e.getWhoClicked().closeInventory();

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {mission.showNPCMessages(player);} catch (Exception exception) {exception.printStackTrace();}
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                MissionCheck.INVENTORY(mission).open(player);
                                TanoRPG.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 1);
                                GamePlayerManager.flag_nowMissionEvent.remove(player.getUniqueId());
                                this.cancel();
                            }
                        }.runTask(TanoRPG.getPlugin());
                        this.cancel();
                    }
                }.runTaskAsynchronously(TanoRPG.getPlugin());
            }));
            slot++;
            }
        }
}
