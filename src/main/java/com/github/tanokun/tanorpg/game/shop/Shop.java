package com.github.tanokun.tanorpg.game.shop;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.menu.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Shop {
    private ArrayList<ShopItem> items = new ArrayList<>();
    private String name;
    private String id;
    private Inventory inventory;

    public Shop(String name, String id, ArrayList<ShopItem> items){
        this.name = name;
        this.id = id;
        this.items = items;
        Inventory inventory = Bukkit.createInventory(null, 45, "§6§lShop: " + name + " §7(ID: " + id + ")");
        ItemStack BSG = MenuManager.createItem(Material.YELLOW_STAINED_GLASS_PANE, "    ", 1, false);

        for (int i = 1; i <= 18; i++){
            if (i <= 9){
                inventory.setItem(i - 1, BSG);
            } else {
                inventory.setItem(i + 26, BSG);
            }
        }
        for (int i = 1; i <= 7; i++){
            if (i <= 4){
                inventory.setItem(i * 9, BSG);
            } else {
                inventory.setItem(8 + (i - 4) * 9, BSG);
            }
        }
        int table = 0;
        int slot = 10;
        int i = 1;
        for (ShopItem item : items){
            if (i == 8){slot += 2; i = 1; table += 1;}
            if (table == 3) break;
            ItemStack show = item.getItem().getItem();
            ItemMeta meta = show.getItemMeta();
            List<String> lore = meta.getLore();
            lore.add("  "); lore.add("§7" + item.getUuid());
            meta.setLore(lore);
            show.setItemMeta(meta);
            inventory.setItem(slot, show);
            slot += 1;
            i += 1;
        }
        this.inventory = inventory;
    }
    public void openShop(Player player){
        TanoRPG.playSound(player, Sound.ENTITY_SHULKER_OPEN, 3, 1);
        player.openInventory(inventory);
        player.updateInventory();
    }
    public ShopItem getItem(String uuid) {
        try {
            for (ShopItem item : items){
                if (item.getUuid().equals(uuid)) return item;}
        }catch (Exception e){
            return null;
        }
        return null;
    }
}
