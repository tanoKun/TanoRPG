package com.github.tanokun.tanorpg.menu.player;

import com.github.tanokun.api.smart_inv.inv.ClickableItem;
import com.github.tanokun.api.smart_inv.inv.SmartInventory;
import com.github.tanokun.api.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.api.smart_inv.inv.contents.InventoryProvider;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.mission.Mission;
import com.github.tanokun.tanorpg.game.mission.MissionManager;
import com.github.tanokun.tanorpg.game.mission.task.MissionTask;
import com.github.tanokun.tanorpg.game.player.status.Sidebar;
import com.github.tanokun.tanorpg.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MissionMenu implements InventoryProvider {

    public static SmartInventory INVENTORY() {
        return SmartInventory.builder()
                .id("player_StatusMissionMenu")
                .provider(new MissionMenu())
                .size(3, 9)
                .title("§d§lPlayerStatus §7>> §aMissions")
                .closeable(true)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(ItemUtils.createItem(Material.PURPLE_STAINED_GLASS_PANE, "  ", 1, false)));
        contents.set(1, 1, ClickableItem.empty(ItemUtils.createItem(Material.PURPLE_STAINED_GLASS_PANE, "  ", 1, false)));
        contents.set(1, 7, ClickableItem.empty(ItemUtils.createItem(Material.PURPLE_STAINED_GLASS_PANE, "  ", 1, false)));

        int slot = 2;

        for(Mission mission : MissionManager.getActiveMissions(player.getUniqueId())){
            String name = mission.getMissionName();
            List<String> lines = new ArrayList<>();
            for (MissionTask missionTask : mission.getMissionTasks()){
                lines.add("    " + missionTask.getMessage(player));
            }
            lines.add("  ");
            lines.add("§bクリックでアクティブミッションに選択");
            ItemStack missionItem = ItemUtils.createItem(Material.BOOK, "§d" + name, lines, 1, true);
            ClickableItem.of(missionItem, e -> {
                GamePlayerManager.getPlayer(player.getUniqueId()).setActive_mission_NPC_ID(mission.getNPC_ID());
                GamePlayerManager.getPlayer(player.getUniqueId()).setActive_mission_NPC_Name(name);
                player.sendMessage(MissionManager.PX + "アクティブミッションを§d「" + name + "」§aに設定しました。");
                Sidebar.updateSidebar(player);
            });
        }
    }
}
