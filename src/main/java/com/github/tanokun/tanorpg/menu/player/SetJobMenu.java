package com.github.tanokun.tanorpg.menu.player;


import com.github.tanokun.api.smart_inv.inv.ClickableItem;
import com.github.tanokun.api.smart_inv.inv.SmartInventory;
import com.github.tanokun.api.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.api.smart_inv.inv.contents.InventoryProvider;
import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.game.player.GamePlayerJobType;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.status.Sidebar;
import com.github.tanokun.tanorpg.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Arrays;

public class SetJobMenu implements InventoryProvider {

    public static SmartInventory INVENTORY() {
        return SmartInventory.builder()
                .id("player_SetJobMenu")
                .provider(new StatusMainMenu())
                .size(3, 9)
                .title("§c§l職業選択 §7説明をよく読んで選択しよう！")
                .closeable(false)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(ItemUtils.createItem(Material.YELLOW_STAINED_GLASS_PANE, "    ", 1, false)));

        contents.set(1, 2, ClickableItem.of(ItemUtils.createItem(Material.DIAMOND_SWORD, "§c§lウォーリア",
                Arrays.asList("§f近接系のスキルが豊富で、", "§f正面の戦いに長けている", "§f遠距離攻撃は少し苦手")
                , 1, true), e -> {
            TanoRPG.playSound((Player) e.getWhoClicked(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§c§lウォーリア")) {
                GamePlayerManager.createData(player.getUniqueId(), GamePlayerJobType.WARRIOR);
                GamePlayerManager.saveData(player.getUniqueId());
                player.sendMessage(TanoRPG.PX + "職業を「ウォーリア」にしました！");
                player.getInventory().addItem(ItemManager.getItem(ItemManager.firstWeapon).getItem());
                player.getInventory().addItem(ItemManager.getItem(ItemManager.firstArmor).getItem());
                contents.inventory().close(player);
            }
        }));

        contents.set(1, 4, ClickableItem.of(ItemUtils.createItem(Material.STICK, "§5§lメイジ",
                Arrays.asList("§f遠距離系のスキルが豊富で、", "§fバランスよく戦える", "§f高い火力は持ってない")
                , 1, true), e -> {
            TanoRPG.playSound((Player) e.getWhoClicked(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§5§lメイジ")) {
                GamePlayerManager.createData(player.getUniqueId(), GamePlayerJobType.MAGE);
                GamePlayerManager.saveData(player.getUniqueId());
                player.sendMessage(TanoRPG.PX + "職業を「メイジ」にしました！");
                player.getInventory().addItem(ItemManager.getItem(ItemManager.firstMagicWeapon).getItem());
                player.getInventory().addItem(ItemManager.getItem(ItemManager.firstArmor).getItem());
                contents.inventory().close(player);
            }
        }));

        contents.set(1, 4, ClickableItem.of(ItemUtils.createItem(Material.ENDER_PEARL, "§d§lプリースト",
                 Arrays.asList("§f支援系のスキルが豊富で、", "§f持久的に戦える", "§f戦略が大事だが重宝される")
                 , 1, true), e -> {
            TanoRPG.playSound((Player) e.getWhoClicked(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§d§lプリースト")) {
                GamePlayerManager.createData(player.getUniqueId(), GamePlayerJobType.PRIEST);
                GamePlayerManager.saveData(player.getUniqueId());
                player.sendMessage(TanoRPG.PX + "職業を「プリースト」にしました！");
                player.getInventory().addItem(ItemManager.getItem(ItemManager.firstMagicWeapon).getItem());
                player.getInventory().addItem(ItemManager.getItem(ItemManager.firstArmor).getItem());
                contents.inventory().close(player);
            }
        }));

        contents.set(2, 8, ClickableItem.of(ItemUtils.createItem(Material.REDSTONE_BLOCK, "§cログアウト", 1, false), e -> {
            player.kickPlayer("ログアウト");
        }));
    }
}
