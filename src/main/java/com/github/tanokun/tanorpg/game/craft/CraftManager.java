package com.github.tanokun.tanorpg.game.craft;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.event.tanorpg.CustomCraftEvent;
import com.github.tanokun.tanorpg.event.tanorpg.CustomEntityKillEvent;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.game.item.itemtype.base.Item;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.util.io.Config;
import com.github.tanokun.tanorpg.util.io.Folder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class CraftManager implements Listener {
    private static HashMap<String, Craft> crafts = new HashMap<>();
    private static Folder craftsFolder;
    public static String loadCrafts(){
        String message = ChatColor.GREEN + "All craft config loaded without errors.";
        try {
            craftsFolder = new Folder("craft", TanoRPG.getPlugin());
            for (Config config : craftsFolder.getFiles()) {
                for (String value : config.getConfig().getKeys(false)) {
                    String id = value;
                    String name = (String) config.getConfig().get(value + ".name");
                    ArrayList<CraftItem> craftItem = new ArrayList<>();
                    for (String afterItem : config.getConfig().getConfigurationSection(value + ".items").getKeys(false)) {
                        long price = config.getConfig().getLong(value + ".items." + afterItem + ".price");
                        ArrayList<Item> before = new ArrayList<>();
                        ArrayList<Integer> beforeCount = new ArrayList<>();
                        Item after = ItemManager.getItem(afterItem);
                        for (String beforeItem : config.getConfig().getConfigurationSection(value + ".items." + afterItem + ".beforeItems").
                                getKeys(false)) {
                            before.add(ItemManager.getItem(beforeItem));
                            beforeCount.add(config.getConfig().getInt(value + ".items." + afterItem + ".beforeItems." + beforeItem));
                        }
                        craftItem.add(new CraftItem(before, beforeCount, after, price, id));
                    }
                    crafts.put(id, new Craft(name, id, craftItem));
                }
            }
        }catch (Exception e){
            message = ChatColor.RED + e.getClass().getName() + ": " + e.getMessage() + " §7(Craft)";
        }
        return message;
    }
    public static void deleteCrafts(){
        crafts = new HashMap<>();
    }
    public static Craft getCraft(String id) {return crafts.get(id);}
    public static boolean isExists(String id){
        return crafts.get(id) != null ? true : false;
    }
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().contains("§b§lCraft: ")) {
            e.setCancelled(true);
            String[] name = e.getView().getTitle().split(" ");
            String id = name[3].replace(")", "");
            if (!e.getCurrentItem().getType().equals(Material.AIR) && !(e.getCurrentItem() == null)) {
                Craft craft = getCraft(id);
                if (e.getCurrentItem().getItemMeta().getLore() == null) return;
                String uuid = e.getCurrentItem().getItemMeta().getLore().get(e.getCurrentItem().getItemMeta().getLore().size() - 1).replace("§7", "");
                CraftItem item = craft.getItem(uuid);
                if (item != null) {
                    item.openCheck((Player) e.getWhoClicked());
                }
            }
        }
        if (e.getView().getTitle().contains("§6§lクラフト確認 §7(ID: ")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;
            String[] name = e.getView().getTitle().split(" ");
            String id = name[2].replace(")", "");
            Craft craft = getCraft(id);
            ItemStack item = e.getWhoClicked().getOpenInventory().getItem(34);
            String uuid = item.getItemMeta().getLore().get(item.getItemMeta().getLore().size() - 1).replace("§7", "");
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§aクラフトする")){
                if (e.getWhoClicked().hasMetadata("craft")){
                    e.getWhoClicked().sendMessage(TanoRPG.PX + "クラフトが終わっていません");
                    return;
                }
                int size = craft.getItem(uuid).getBeforeItems().size();
                HashMap<String, Integer> amount = new HashMap<>();
                for (int i = 1; i < 10; i++) {
                    if (size <= i - 1) break;
                    CraftItem craftItem = craft.getItem(uuid);
                    if (amount.get(craftItem.getBeforeItems().get(i - 1).getId()) != null){
                        amount.put(craftItem.getBeforeItems().get(i - 1).getId(), amount.get(craftItem.getBeforeItems().get(i - 1).getId())
                                + craftItem.getBeforeItemsCount().get(i - 1));
                    } else {
                        amount.put(craftItem.getBeforeItems().get(i - 1).getId(), craftItem.getBeforeItemsCount().get(i - 1));
                    }
                }
                for (Item item2 : craft.getItem(uuid).getBeforeItems()) {
                    if (!(ItemManager.getAmount((Player) e.getWhoClicked(), item2.getItem()) >= amount.get(item2.getId()))) {
                        TanoRPG.playSound((Player)e.getWhoClicked(), Sound.BLOCK_NOTE_BLOCK_BASS, 10, 1);
                        e.getWhoClicked().sendMessage(TanoRPG.PX + "§c素材が足りません");
                        e.getWhoClicked().closeInventory();
                        return;
                    }
                }
                if (GamePlayerManager.getPlayer(e.getWhoClicked().getUniqueId()).getMoney() < craft.getItem(uuid).getPrice()){
                    e.getWhoClicked().sendMessage(TanoRPG.PX + "§cお金が足りません");
                    return;
                }
                e.getWhoClicked().setMetadata("craft", new FixedMetadataValue(TanoRPG.getPlugin(), true));
                new BukkitRunnable(){
                    public void run() {
                        int size = craft.getItem(uuid).getBeforeItems().size();
                        for (int i = 1; i < 10; i++) {
                            if (size <= i - 1) break;
                            ItemStack item = craft.getItem(uuid).getBeforeItems().get(i - 1).getItem();
                            item.setAmount(craft.getItem(uuid).getBeforeItemsCount().get(i - 1));
                            e.getWhoClicked().getInventory().removeItem(item);
                        }
                        GamePlayerManager.getPlayer(e.getWhoClicked().getUniqueId()).removeMoney(craft.getItem(uuid).getPrice());
                        TanoRPG.playSound((Player)e.getWhoClicked(), Sound.BLOCK_ANVIL_DESTROY, 10, 1);
                        try {Thread.sleep(750);} catch (InterruptedException e) {e.printStackTrace();}
                        e.getWhoClicked().sendMessage(TanoRPG.PX + "クラフトが完了しました");
                        new BukkitRunnable(){

                            @Override
                            public void run() {
                                Bukkit.getServer().getPluginManager().callEvent(new CustomCraftEvent(((Player) e.getWhoClicked()).getPlayer(),
                                        craft.getItem(uuid).getAfterItem(), craft));
                            }
                        }.runTask(TanoRPG.getPlugin());
                        e.getWhoClicked().getInventory().addItem(craft.getItem(uuid).getAfterItem().getItem());
                        e.getWhoClicked().removeMetadata("craft", TanoRPG.getPlugin());
                    }
                }.runTaskAsynchronously(TanoRPG.getPlugin());
            }
        }
    }
}
