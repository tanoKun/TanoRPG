package com.github.tanokun.tanorpg.game.craft;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.item.CustomItem;
import com.github.tanokun.tanorpg.menu.MenuManager;
import com.github.tanokun.tanorpg.util.ShortColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CraftItem {
    private CustomItem afterItem;
    private ArrayList<CustomItem> beforeItems = new ArrayList<>();
    private ArrayList<Integer> beforeItemsCount = new ArrayList<>();
    private long price;
    private String owner;
    private Inventory inv;
    private String uuid;
    public CraftItem(ArrayList<CustomItem> beforeItems, ArrayList<Integer> beforeItemsCount, CustomItem afterItem, long price, String owner){
        this.afterItem = afterItem;
        this.beforeItems = beforeItems;
        this.beforeItemsCount = beforeItemsCount;
        this.price = price;
        this.owner = owner;
        this.uuid = UUID.randomUUID().toString();
        inv = Bukkit.createInventory(null, 54, "§6§lクラフト確認 §7(ID: " + owner + ")");
        ItemStack BSG = MenuManager.createItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "    ", 1, false);
        for (int i = 1; i < 54; i++) {
            inv.setItem(i - 1, BSG);
        }
        ItemStack show = afterItem.getItem();
        ItemMeta meta = show.getItemMeta();
        List<String> lore = meta.getLore();
        lore.add("  "); lore.add("§7" + uuid);
        meta.setLore(lore);
        show.setItemMeta(meta);
        inv.setItem(34, show);
        inv.setItem(32, MenuManager.createItem(Material.GOLD_INGOT, "§6§l値段: " + price, 1, false));
        inv.setItem(53, MenuManager.createItem(Material.ANVIL, "§aクラフトする", 1, true));
        int slot = 19;
        int size = beforeItems.size();
        for (int i = 1; i < 10; i++) {
            if (size > i - 1){
                ItemStack item = beforeItems.get(i - 1).getItem();
                item.setAmount(beforeItemsCount.get(i - 1));
                inv.setItem(slot, item);
            } else {
                inv.setItem(slot, new ItemStack(Material.AIR));
            }
            if (i == 3){
                slot += 7;
            } else if (i == 6){
                slot += 7;
            } else {
                slot += 1;
            }
        }
    }

    public CustomItem getItem() {return afterItem;}
    public String getUuid() {return uuid;}
    public ArrayList<CustomItem> getBeforeItems(){return beforeItems;}
    public ArrayList<Integer> getBeforeItemsCount() {return beforeItemsCount;}

    public void openCheck(Player player) {
        TanoRPG.playSound(player, Sound.ENTITY_SHULKER_OPEN, 10, 1);
        player.openInventory(inv);
    }
    public long getPrice() {return price;}

    public CustomItem getAfterItem() {
        return afterItem;
    }
}
