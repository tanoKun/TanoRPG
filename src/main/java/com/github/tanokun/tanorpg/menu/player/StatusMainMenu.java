package com.github.tanokun.tanorpg.menu.player;

import com.github.tanokun.api.smart_inv.inv.ClickableItem;
import com.github.tanokun.api.smart_inv.inv.SmartInventory;
import com.github.tanokun.api.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.api.smart_inv.inv.contents.InventoryProvider;
import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.status.Status;
import com.github.tanokun.tanorpg.game.player.status.StatusType;
import com.github.tanokun.tanorpg.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatusMainMenu implements InventoryProvider {

    public static SmartInventory INVENTORY() {
        return SmartInventory.builder()
                .id("player_StatusMainMenu")
                .provider(new StatusMainMenu())
                .size(1, 9)
                .title("§d§lPlayerStatus")
                .closeable(true)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(0, 0, ClickableItem.of(ItemUtils.createItem(Material.NAME_TAG, "§6§lPlayerName: §b" + player.getName(),
                Arrays.asList("§d§l職業: §b" + GamePlayerManager.getPlayer(player.getUniqueId()).getJob().getName())
                , 1, false),
                e -> e.setCancelled(true)));

        ArrayList<Status> statuses = GamePlayerManager.getPlayer(player.getUniqueId()).getStatus();
        ArrayList<StatusType> basicTypes = StatusType.getBasicTypes();
        List<String> lore = new ArrayList<>();
        List<String> lore2 = new ArrayList<>();
        for (Status status : statuses) {
            if (basicTypes.contains(status.getStatusType())) {
                lore.add("§a " + status.getStatusType().getName() + ": +" + status.getLevel());
            } else {
                if (status.getLevel() == 0) continue;
                lore2.add("§a " + status.getStatusType().getName() + ": +" + status.getLevel());
            }
        }
        contents.set(0, 2, ClickableItem.of(
                ItemUtils.createItem(Material.IRON_SWORD, "§6§l基本ステータス:", lore, 1, false), e -> {
                    e.setCancelled(true);
                }));
        contents.set(0, 3, ClickableItem.of(
                ItemUtils.createItem(Material.DIAMOND_SWORD, "§6§l特殊ステータス:", lore2, 1, false), e -> {
                    e.setCancelled(true);
                }));

        contents.set(0, 5, ClickableItem.of(
                ItemUtils.createItem(Material.BLAZE_POWDER, "§c§lスキル", 1, true), e -> {
                    e.setCancelled(true);
                    StatusSkillMenu.INVENTORY().open(player);
                    TanoRPG.playSound(player, Sound.ENTITY_SHULKER_OPEN, 10, 1);
                }));

        contents.set(0, 7, ClickableItem.of(
                ItemUtils.createItem(Material.WRITABLE_BOOK, "§d§lミッション", 1, true), e -> {
                    e.setCancelled(true);
                    MissionMenu.INVENTORY().open(player);
                    TanoRPG.playSound(player, Sound.ENTITY_SHULKER_OPEN, 10, 1);
                }));
    }
}
