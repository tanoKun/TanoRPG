package com.github.tanokun.tanorpg.menu.player;


import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.game.player.GamePlayerJobType;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.status.Sidebar;
import com.github.tanokun.tanorpg.menu.Menu;
import com.github.tanokun.tanorpg.menu.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Arrays;

public class SetJobMenu extends Menu {
    public SetJobMenu(){
        super("§c§l職業選択 §7説明をよく読んで選択しよう！", 3);
        for (int i = 0; i < 27; i++) {
            setItem(i, MenuManager.createItem(Material.YELLOW_STAINED_GLASS_PANE, "    ", 1, false));
        }
        setItem(11, MenuManager.createItem(Material.DIAMOND_SWORD, "§c§lウォーリア",
                Arrays.asList("§f近接系のスキルが豊富で、", "§f正面の戦いに長けている", "§f遠距離攻撃は少し苦手")
                , 1, true));
        setItem(13, MenuManager.createItem(Material.STICK, "§5§lメイジ",
                Arrays.asList("§f遠距離系のスキルが豊富で、", "§fバランスよく戦える", "§f高い火力は持ってない")
                , 1, true));
        setItem(15, MenuManager.createItem(Material.ENDER_PEARL, "§d§lプリースト",
                Arrays.asList("§f支援系のスキルが豊富で、", "§f持久的に戦える", "§f戦略が大事だが重宝される")
                , 1, true));
        setItem(26, MenuManager.createItem(Material.REDSTONE_BLOCK, "§cログアウト", 1, false));
    }
    @Override
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() != null) {
            Player p = (Player) e.getWhoClicked();
            e.setCancelled(true);
            if (e.getCurrentItem().getType().equals(Material.YELLOW_STAINED_GLASS_PANE)) return;
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§cログアウト")) {
                p.kickPlayer("ログアウト");
                return;
            }
            TanoRPG.playSound((Player) e.getWhoClicked(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§c§lウォーリア")){
                GamePlayerManager.createData(p.getUniqueId(), GamePlayerJobType.WARRIOR);
                GamePlayerManager.saveData(p.getUniqueId());
                p.closeInventory();
                p.sendMessage(TanoRPG.PX + "職業を「ウォーリア」にしました！");
                p.getInventory().addItem(ItemManager.getItem(ItemManager.firstWeapon).getItem());
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§5§lメイジ")) {
                GamePlayerManager.createData(p.getUniqueId(), GamePlayerJobType.MAGE);
                GamePlayerManager.saveData(p.getUniqueId());
                p.closeInventory();
                p.sendMessage(TanoRPG.PX + "職業を「メイジ」にしました！");
                p.getInventory().addItem(ItemManager.getItem(ItemManager.firstMagicWeapon).getItem());
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§d§lプリースト")){
                GamePlayerManager.createData(p.getUniqueId(), GamePlayerJobType.PRIEST);
                GamePlayerManager.saveData(p.getUniqueId());
                p.closeInventory();
                p.sendMessage(TanoRPG.PX + "職業を「プリースト」にしました！");
                p.getInventory().addItem(ItemManager.getItem(ItemManager.firstMagicWeapon).getItem());
            }
            p.getInventory().addItem(ItemManager.getItem(ItemManager.firstArmor).getItem());
            Sidebar.setupSidebar(p);
        }
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        Bukkit.getScheduler().runTask(TanoRPG.getPlugin(), () -> {
            if (GamePlayerManager.getPlayer(e.getPlayer().getUniqueId()) == null){
                openInv((Player) e.getPlayer());
            }
        });
    }
}
