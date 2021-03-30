package com.github.tanokun.tanorpg.menu.player;

import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.status.Status;
import com.github.tanokun.tanorpg.game.player.status.StatusType;
import com.github.tanokun.tanorpg.menu.Menu;
import com.github.tanokun.tanorpg.menu.MenuManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatusMainMenu extends Menu {
    public StatusMainMenu(Player player) {
        super("§d§lPlayerStatus", 1);
        if (player == null) return;
        setItem(0, MenuManager.createItem(Material.NAME_TAG, "§6§lPlayerName: §b" + player.getName(),
                Arrays.asList("§d§l職業: §b" + GamePlayerManager.getPlayer(player.getUniqueId()).getJob().getName())
                , 1, false));
        ArrayList<Status> statuses = GamePlayerManager.getPlayer(player.getUniqueId()).getStatus();
        ArrayList<StatusType> basicTypes = StatusType.getBasicTypes();
        List<String> lore = new ArrayList<>();
        List<String> lore2 = new ArrayList<>();
        for(Status status : statuses){
            if (basicTypes.contains(status.getStatusType())){
                lore.add("§a " + status.getStatusType().getName() + ": +" + status.getLevel());
            } else {
                if (status.getLevel() == 0) continue;
                lore2.add("§a " + status.getStatusType().getName() + ": +" + status.getLevel());
            }
        }
        setItem(2, MenuManager.createItem(Material.IRON_SWORD, "§6§l基本ステータス:", lore, 1, false));
        setItem(3, MenuManager.createItem(Material.DIAMOND_SWORD, "§6§l特殊ステータス:", lore2, 1, true));
        setItem(5, MenuManager.createItem(Material.BLAZE_POWDER, "§c§lスキル", 1, true));
        setItem(7, MenuManager.createItem(Material.WRITABLE_BOOK, "§d§lミッション", 1, true));
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getType().equals(Material.AIR)) return;
        if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§c§lスキル")) new StatusSkillMenu((Player) e.getWhoClicked()).openInv((Player) e.getWhoClicked());
        if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§d§lミッション")) new MissionMenu((Player) e.getWhoClicked()).openInv((Player) e.getWhoClicked());
    }
    public void onClose(InventoryCloseEvent e) {}
}
