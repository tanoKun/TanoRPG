package com.github.tanokun.tanorpg.game.craft;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.util.io.Config;
import com.github.tanokun.tanorpg.util.io.Folder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CraftManager {
    private final HashMap<String, Craft> crafts = new HashMap<>();
    private final HashMap<Integer, String> npcIds = new HashMap<>();
    public CraftManager(Player p){
        loadCraft(p);
    }

    public void loadCraft(Player p) {
        String path = "";
        String filePath = "";
        HashSet<String> errors = new HashSet<>();
        errors.add("§a    Craft configs loaded without errors.");
        try {
            path = "crafts";
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
                    ArrayList<CraftItem> craftItems = new ArrayList<>();
                    for (String item : config.getConfig().getConfigurationSection(path).getKeys(false)){

                        path = id + ".items." + item + ".count";
                        int count = config.getConfig().getInt(path, 1);

                        path = id + ".items." + item;
                        ItemStack a = TanoRPG.getPlugin().getItemManager().getItem(item).init(count);

                        path = id + ".items." + item + ".price";
                        int price = config.getConfig().getInt(path, 0);

                        path = id + ".items." + item + ".permission";
                        boolean item_permission = config.getConfig().getBoolean(path, false);

                        path = id + ".items." + item + ".necI";
                        ArrayList<ItemStack> necI = new ArrayList<>();
                        if (config.getConfig().getConfigurationSection(path) != null) {
                            for (String necI_text : config.getConfig().getConfigurationSection(path).getKeys(false)) {
                                path = id + ".items." + item + ".necI." + necI_text;
                                int necI_count = config.getConfig().getInt(path, 1);
                                necI.add(TanoRPG.getPlugin().getItemManager().getItem(necI_text).init(necI_count));
                            }
                        }

                        path = id + ".items." + item + ".necT";
                        ArrayList<ItemStack> necT = new ArrayList<>();
                        if (config.getConfig().getConfigurationSection(path) != null) {
                            for (String necT_text : config.getConfig().getConfigurationSection(path).getKeys(false)) {
                                path = id + ".items." + item + ".necT." + necT_text;
                                int necI_count = config.getConfig().getInt(path, 1);
                                necT.add(TanoRPG.getPlugin().getItemManager().getItem(necT_text).init(necI_count));
                            }
                        }
                        //craftItems.add(new CraftItem(id, a, necI, necT, price, item_permission));
                    }
                    crafts.put(id, new Craft(id, name, craftItems, main_permission, npcId));
                    npcIds.put(npcId, id);
                }
            }
        } catch (Exception e){
            errors.remove("§a    Craft configs loaded without errors.");
            errors.add("§c    " + e.getMessage() + "§7" + "(Path: " + filePath + path + ")");
        }

        showErrors(errors, p);

    }

    public Craft getCraft(String id) {
        return crafts.get(id);
    }

    public String getCraftId(int npc) {
        return npcIds.get(npc);
    }

    public HashMap<Integer, String> getNpcIds() {
        return npcIds;
    }

    public HashMap<String, Craft> getCrafts() {
        return crafts;
    }

    public Set<String> getCraftIds(){
        return crafts.keySet();
    }

    private void showErrors(HashSet<String> errors, Player p) {
        if (p != null) {
            p.sendMessage(TanoRPG.PX + "§bLoading craft configs...");
            errors.stream().forEach(e -> p.sendMessage(e));
            p.sendMessage("  ");
        } else {
            Bukkit.getConsoleSender().sendMessage("[TanoRPG] §bLoading craft configs...");
            errors.stream().forEach(e -> Bukkit.getConsoleSender().sendMessage(e));
            Bukkit.getConsoleSender().sendMessage("  ");
        }
    }
}
