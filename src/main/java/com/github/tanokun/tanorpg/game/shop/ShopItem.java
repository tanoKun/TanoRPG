package com.github.tanokun.tanorpg.game.shop;

import com.github.tanokun.tanorpg.game.item.CustomItem;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.menu.MenuManager;
import com.github.tanokun.tanorpg.util.ShortColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class ShopItem {
    private String owner;
    private CustomItem item;
    private long price;
    private String uuid;
    private Inventory inv;
    public ShopItem(CustomItem item, long price, String owner){
        this.item = item;
        this.price = price;
        this.uuid = UUID.randomUUID().toString();
        this.owner = owner;
        this.inv = Bukkit.createInventory(null, 27, "§6§l購入確認 " + "§7(ID: " + owner +")");
        ItemStack BSG = MenuManager.setItemColor(MenuManager.createItem(Material.STAINED_GLASS_PANE, "    ", 1, false),
                (short) 4);
        for (int i = 1; i < 27; i++) {
            inv.setItem(i - 1, BSG);
        }
        inv.setItem(11, MenuManager.createItem(Material.GOLD_INGOT, "§6§l値段: §b" + price, 1, false));
        ItemStack show = item.getItem();
        ItemMeta meta = show.getItemMeta();
        List<String> lore = meta.getLore();
        lore.add("  "); lore.add("§7" + uuid);
        meta.setLore(lore);
        show.setItemMeta(meta);
        inv.setItem(15, show);
        inv.setItem(18, MenuManager.setItemColor(MenuManager.createItem(Material.WOOL, "§c戻る", 1, true), ShortColor.RED.getColor()));
        inv.setItem(26, MenuManager.setItemColor(MenuManager.createItem(Material.WOOL, "§a購入する", 1, true), ShortColor.GREEN.getColor()));
    }

    public CustomItem getItem() {return item;}
    public String getUuid() {return uuid;}
    public long getPrice() {return price;}
    public void openCheck(Player player){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                "execute " + player.getName() + " ~ ~ ~ playsound minecraft:entity.shulker.open player @s ~ ~ ~ 10 1");
        inv.setItem(0, MenuManager.createItem(Material.EMERALD,
                "§6§l所持金: §b" + GamePlayerManager.getPlayer(player.getUniqueId()).getMoney(),
                1, false));
        player.openInventory(inv);
    }
}