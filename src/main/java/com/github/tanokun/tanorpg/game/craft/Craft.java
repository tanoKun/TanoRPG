package com.github.tanokun.tanorpg.game.craft;

import com.github.tanokun.tanorpg.menu.MenuManager;
import com.github.tanokun.tanorpg.util.ShortColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Craft {
    private ArrayList<CraftItem> items;
    private String name;
    private String id;
    private Inventory inventory;
    public Craft(String name, String id, ArrayList<CraftItem> items) {
        this.name = name;
        this.id = id;
        this.items = items;
        Inventory inventory = Bukkit.createInventory(null, 45, "§b§lCraft: " + name + " §7(ID: " + id + ")");
        ItemStack BSG = MenuManager.createItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "    ", 1, false);
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
        for (CraftItem item : items){
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
    public void openCraft(Player player){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                "execute " + player.getName() + " ~ ~ ~ playsound minecraft:entity.shulker.open player @s ~ ~ ~ 10 1");
        player.openInventory(inventory);}
    public CraftItem getItem(String uuid) {
        try {
            for (CraftItem item : items){
                if (item.getUuid().equals(uuid)) return item;}
        }catch (Exception e){
            return null;
        }
        return null;
    }
}
