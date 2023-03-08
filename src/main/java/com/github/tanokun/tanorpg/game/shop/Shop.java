package com.github.tanokun.tanorpg.game.shop;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.util.ItemUtilsKt;
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;

public record Shop(String id, String name, ArrayList<ShopItem> items, int npcId, Permission permission) implements InventoryProvider {
    public Shop(String id, String name, ArrayList<ShopItem> items, int npcId, Permission permission) {
        this.id = id;
        this.name = name;
        this.items = items;
        this.npcId = npcId;
        this.permission = permission;
        if (Bukkit.getPluginManager().getPermission(permission.getName()) == null) Bukkit.getPluginManager().addPermission(this.permission);
    }

    public SmartInventory getInv() {
        return SmartInventory.builder()
                .id(id)
                .title("§6§lショップ「" + name + "§6§l」")
                .update(false)
                .provider(this)
                .size(5, 9)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(ItemUtilsKt.createItem(Material.YELLOW_STAINED_GLASS_PANE, " ", 1, false)));

        ArrayList<ShopItem> barrier = new ArrayList<>();

        items.stream().forEach(item -> {
            if (player.hasPermission(item.getPermission())) {
                contents.add(ClickableItem.of(item.getProduct(), e -> {
                    TanoRPG.playSound(player, Sound.ENTITY_SHULKER_OPEN, 3, 1);
                    item.getInv().open(player);
                }));
            } else {
                barrier.add(item);
            }
        });

        barrier.stream().forEach(item -> {
            contents.add(ClickableItem.of(ItemUtilsKt.createItem(Material.BARRIER, item.getProduct().getItemMeta().getDisplayName(), 1, false), e -> {
                TanoRPG.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 3, 1);
                player.sendMessage(TanoRPG.PX + "§cそのショップは開放されていません");
            }));
        });
    }

    public int getNpcId() {
        return npcId;
    }

    public String getId() {
        return id;
    }

    public ArrayList<ShopItem> getItems() {
        return items;
    }

    public Permission getPermission() {
        return permission;
    }

    public String getName() {
        return name;
    }
}
