package com.github.tanokun.tanorpg.game.shop;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.craft.Craft;
import com.github.tanokun.tanorpg.game.craft.CraftItem;
import com.github.tanokun.tanorpg.util.io.Config;
import com.github.tanokun.tanorpg.util.io.Folder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ShopManager {
    private final HashMap<String, Shop> shops = new HashMap<>();
    private final HashMap<Integer, String> npcIds = new HashMap<>();

    public ShopManager(Player p){
        loadShop(p);
    }

    public void loadShop(Player p) {
        String path = "";
        String filePath = "";
        HashSet<String> errors = new HashSet<>();
        errors.add("§a    Shop configs loaded without errors.");
        try {
            path = "shops";
            for (Config config : new Folder(path, TanoRPG.getPlugin()).getFiles()) {
                filePath = path + File.separator + config.getName() + File.separator;
                for (String id : config.getConfig().getKeys(false)) {
                    path = id + ".name";
                    String name = config.getConfig().getString(path, "unknown");

                    path = id + ".permission";
                    boolean main_permission = config.getConfig().getBoolean(path, false);

                    path = id + ".npcId";
                    int npcId = config.getConfig().getInt(path, 0);

                    path = id + ".items";
                    ArrayList<ShopItem> shopItems = new ArrayList<>();
                    for (String item : config.getConfig().getConfigurationSection(path).getKeys(false)){
                        path = id + ".items." + item;
                        ItemStack a = TanoRPG.getPlugin().getItemManager().getItem(item).init(1);

                        path = id + ".items." + item + ".price";
                        int price = config.getConfig().getInt(path, 0);

                        path = id + ".items." + item + ".permission";
                        boolean item_permission = config.getConfig().getBoolean(path, false);

                        shopItems.add(new ShopItem(id, item, a, price, item_permission));
                    }
                    shops.put(id, new Shop(id, name, shopItems, main_permission, npcId));
                    npcIds.put(npcId, id);
                }
            }
        } catch (Exception e){
            errors.remove("§a    Craft configs loaded without errors.");
            errors.add("§c    " + e.getMessage() + "§7" + "(Path: " + filePath + path + ")");
        }

        showErrors(errors, p);

    }

    public Shop getShop(String id) {
        return shops.get(id);
    }

    public String getShopId(int npc) {
        return npcIds.get(npc);
    }

    private void showErrors(HashSet<String> errors, Player p) {
        if (p != null) {
            p.sendMessage(TanoRPG.PX + "§bLoading shop configs...");
            errors.stream().forEach(e -> p.sendMessage(e));
            p.sendMessage("  ");
        } else {
            Bukkit.getConsoleSender().sendMessage("[TanoRPG] §bLoading shop configs...");
            errors.stream().forEach(e -> Bukkit.getConsoleSender().sendMessage(e));
            Bukkit.getConsoleSender().sendMessage("  ");
        }
    }
}
