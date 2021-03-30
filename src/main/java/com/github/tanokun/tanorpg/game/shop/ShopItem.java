package com.github.tanokun.tanorpg.game.shop;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.item.itemtype.base.Item;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.menu.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class ShopItem {
    private String owner;
    private Item item;
    private long price;
    private String uuid;
    private Inventory inv;
    public ShopItem(Item item, long price, String owner){
        this.item = item;
        this.price = price;
        this.uuid = UUID.randomUUID().toString();
        this.owner = owner;
        this.inv = Bukkit.createInventory(null, 27, "§6§l購入確認 " + "§7(ID: " + owner +")");
        ItemStack BSG = MenuManager.createItem(Material.YELLOW_STAINED_GLASS_PANE, "    ", 1, false);
        for (int i = 1; i < 27; i++) {
            inv.setItem(i - 1, BSG);
        }
        inv.setItem(11, MenuManager.createItem(Material.GOLD_INGOT, "§6§l値段: §b" + price + " " + TanoRPG.MONEY, 1, false));
        ItemStack show = item.getItem();
        ItemMeta meta = show.getItemMeta();
        List<String> lore = meta.getLore();
        lore.add("  "); lore.add("§7" + uuid);
        meta.setLore(lore);
        show.setItemMeta(meta);
        inv.setItem(15, show);
        inv.setItem(18, MenuManager.createItem(Material.RED_WOOL, "§c戻る", 1, true));
        inv.setItem(26, MenuManager.createItem(Material.GREEN_WOOL, "§a購入する", 1, true));
    }

    public Item getItem() {return item;}
    public String getUuid() {return uuid;}
    public long getPrice() {return price;}
    public void openCheck(Player player){
        TanoRPG.playSound(player, Sound.ENTITY_SHULKER_OPEN, 10, 1);
        inv.setItem(0, MenuManager.createItem(Material.EMERALD,
                "§6§l所持金: §b" + GamePlayerManager.getPlayer(player.getUniqueId()).getMoney(),
                1, false));
        player.openInventory(inv);
    }
}
