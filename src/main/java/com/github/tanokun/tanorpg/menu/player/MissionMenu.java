package com.github.tanokun.tanorpg.menu.player;

import com.github.tanokun.tanorpg.game.player.GamePlayer;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.mission.Mission;
import com.github.tanokun.tanorpg.game.player.mission.MissionManager;
import com.github.tanokun.tanorpg.game.player.mission.listener.EventKillListener;
import com.github.tanokun.tanorpg.game.player.mission.task.MissionTask;
import com.github.tanokun.tanorpg.game.player.skill.Skill;
import com.github.tanokun.tanorpg.game.player.skill.SkillManager;
import com.github.tanokun.tanorpg.game.player.status.Sidebar;
import com.github.tanokun.tanorpg.menu.Menu;
import com.github.tanokun.tanorpg.menu.MenuManager;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MissionMenu extends Menu {
    public MissionMenu(Player player) {
        super("§d§lPlayerStatus §7>> §aMissions", 3);
        if (player == null) return;
        GamePlayer gamePlayer = GamePlayerManager.getPlayer(player.getUniqueId());
        ItemStack item = MenuManager.createItem(Material.PURPLE_STAINED_GLASS_PANE, "  ", 1, false);
        int slot = 0;
        int slot2 = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 9; j++) {
                setItem(slot + slot2, item);
                slot2++;
            }
            slot2 = 0;
            slot = 18;
        }
        slot = 9;
        slot2 = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                setItem(slot + slot2, item);
                slot2++;
            }
            slot2 = 0;
            slot = 16;
        }

        slot = 11;

        for(Mission mission : MissionManager.getActiveMissions(player.getUniqueId())){
            String name = mission.getMissionName();
            List<String> lines = new ArrayList<>();
            for (MissionTask missionTask : mission.getMissionTasks()){
                lines.add("    " + missionTask.getMessage(player));
            }
            lines.add("  ");
            lines.add("§bクリックでアクティブミッションに選択");
            ItemStack missionItem = MenuManager.createItem(Material.BOOK, "§d" + name, lines, 1, true);
            setItem(slot, missionItem);
            slot++;
        }
    }

    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getType().equals(Material.PURPLE_STAINED_GLASS_PANE)) return;
        if (e.getCurrentItem().getType().equals(Material.AIR)) return;
        Player p = (Player)e.getWhoClicked();
        String name = e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§d", "");
        for(Mission mission : MissionManager.getActiveMissions(p.getUniqueId())){
            if (!mission.getMissionName().equals(name)) continue;
            GamePlayerManager.getPlayer(p.getUniqueId()).setActive_mission_NPC_ID(mission.getNPC_ID());
            p.sendMessage(EventKillListener.PX + "アクティブミッションを§d「" + name + "」§aに設定しました。");
            Sidebar.updateSidebar(p);
        }
    }

    public void onClose(InventoryCloseEvent e) {

    }
}
