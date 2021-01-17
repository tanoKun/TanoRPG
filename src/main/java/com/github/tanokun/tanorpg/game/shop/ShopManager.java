package com.github.tanokun.tanorpg.game.shop;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.item.CustomItem;
import com.github.tanokun.tanorpg.game.item.CustomItemManager;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.menu.MenuManager;
import com.github.tanokun.tanorpg.util.io.Config;
import com.github.tanokun.tanorpg.util.io.Folder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class ShopManager implements Listener {
    private static HashMap<String, Shop> shops = new HashMap<>();
    private static Folder shopsFolder;
    public static String loadShops(){
        String message = ChatColor.GREEN + "All shop config loaded without errors.";
        try {
            shopsFolder = new Folder("shop", TanoRPG.getPlugin());
            for (Config config : shopsFolder.getFiles()) {
                for (String value : config.getConfig().getKeys(false)) {
                    String id = value;
                    String name = (String) config.getConfig().get(value + ".name");
                    ArrayList<ShopItem> items = new ArrayList<>();
                    for (String key : config.getConfig().getConfigurationSection(value + ".items").getKeys(false)) {
                        CustomItem customItem = CustomItemManager.getCustomItem(key);
                        ShopItem item = new ShopItem(customItem, config.getConfig().getLong(value + ".items." + key), id);
                        items.add(item);
                    }
                    shops.put(id, new Shop(name, id, items));
                }
            }
        }catch (Exception e){
            message = ChatColor.RED + e.getClass().getName() + ": " + e.getMessage() + " §7(Shop)";
        }
        return message;
    }
    public static void deleteShops(){
        shops = new HashMap<>();
    }
    public static Shop getShop(String id) {return shops.get(id);}
    public static boolean isExists(String id){
        return shops.get(id) != null ? true : false;
    }
    @EventHandler
    public void onClick(InventoryClickEvent e){
        if (e.getView().getTitle().contains("§6§lShop: ")){
            String[] name = e.getView().getTitle().split(" ");
            String id = name[3].replace(")", "");
            if (isExists(id) == false) return;
            e.setCancelled(true);
            if (!e.getCurrentItem().getType().equals(Material.AIR) && !(e.getCurrentItem() == null)){
                Shop shop = getShop(id);
                if (e.getCurrentItem().getItemMeta().getLore() == null) return;
                String uuid = e.getCurrentItem().getItemMeta().getLore().get(e.getCurrentItem().getItemMeta().getLore().size() - 1).replace("§7", "");
                ShopItem item = shop.getItem(uuid);
                if (item != null){
                    item.openCheck((Player) e.getWhoClicked());
                }
            }
        }
        if (e.getView().getTitle().contains("§6§l購入確認")){
            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;
            String[] name = e.getView().getTitle().split(" ");
            String id = name[2].replace(")", "");
            Shop shop = getShop(id);
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§a購入する")){
                ItemStack item = e.getWhoClicked().getOpenInventory().getItem(15);
                String uuid = item.getItemMeta().getLore().get(item.getItemMeta().getLore().size() - 1).replace("§7", "");
                if (GamePlayerManager.getPlayer(e.getWhoClicked().getUniqueId()).getMoney() >= shop.getItem(uuid).getPrice()){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                            "execute " + e.getWhoClicked().getName() + " ~ ~ ~ playsound entity.experience_orb.pickup player @s ~ ~ ~ 10 1");
                    GamePlayerManager.getPlayer(e.getWhoClicked().getUniqueId()).removeMoney(shop.getItem(uuid).getPrice());
                    e.getWhoClicked().sendMessage(TanoRPG.PX + "購入しました！");
                    e.getWhoClicked().getInventory().addItem(shop.getItem(uuid).getItem().getItem());
                    e.getWhoClicked().getOpenInventory().setItem(0, MenuManager.createItem(Material.EMERALD,
                            "§6§l所持金: §b" + GamePlayerManager.getPlayer(e.getWhoClicked().getUniqueId()).getMoney(),
                            1, false));
                } else {
                    e.getWhoClicked().closeInventory();
                    e.getWhoClicked().sendMessage(TanoRPG.PX + "§cお金が足りません");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                            "execute " + e.getWhoClicked().getName() + " ~ ~ ~ /playsound block.note.bass player @s ~ ~ ~ 10 1");
                }
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§c戻る")){
                shop.openShop((Player) e.getWhoClicked());
            }
        }
    }
}
