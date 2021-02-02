package com.github.tanokun.tanorpg.game.item;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayerJobType;
import com.github.tanokun.tanorpg.game.player.status.Status;
import com.github.tanokun.tanorpg.game.player.status.StatusType;
import com.github.tanokun.tanorpg.util.io.Config;
import com.github.tanokun.tanorpg.util.io.Folder;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CustomItemManager {
    private static Folder itemMaterial;
    private static Folder itemWeapon;
    private static Folder itemMagicWeapon;
    private static Folder itemEquipment;
    private static HashMap<String, CustomItem> customItems = new HashMap<>();
    private static List<String> customItemIDs = new ArrayList<>();
    public static final String LORE = "§e〇=-=-=-=-=§b説明§e=-=-=-=-=-〇";
    public static final String FIRST_STATUS = "§e〇=-=-=-§bステータス§e-=-=-=-〇";
    public static final String FINAL_STATUS = "§e〇=-=-=-=-=-=-=-=-=-=-=-=-〇";

    final public static String firstArmor = TanoRPG.getPlugin().getConfig().getString("first-job-armor");
    final public static String firstMagicWeapon = TanoRPG.getPlugin().getConfig().getString("first-job-mage-weapon");
    final public static String firstWeapon = TanoRPG.getPlugin().getConfig().getString("first-job-warrior-weapon");


    public static String loadCustomItemAll(){
        String message = ChatColor.GREEN + "All item config loaded without errors.";
        try {
            loadCustomItemMaterial();
        } catch (Exception e) {
            message = ChatColor.RED + e.getClass().getName() + ": " + e.getMessage() + ChatColor.GRAY + "(" + "Material" + ")";
        }

        try {
            loadCustomItemWeapon();
        } catch (Exception e){
            message = ChatColor.RED + e.getClass().getName() + ": " + e.getMessage() + ChatColor.GRAY + "(" + "Weapon" + ")";
        }

        try {
            loadCustomItemMagicWeapon();
        } catch (Exception e){
            message = ChatColor.RED + e.getClass().getName() + ": " + e.getMessage() + ChatColor.GRAY + "(" + "MagicWeapon" + ")";
        }

        try {
            loadCustomItemEquipment();
        } catch (Exception e){
            message = ChatColor.RED + e.getClass().getName() + ": " + e.getMessage() + ChatColor.GRAY + "(" + "Equipment" + ")";
        }
        return message;
    }
    public static void deleteCustomItems(){
        customItems = null;
        customItems = new HashMap<>();
    }
    public static void loadCustomItemMaterial(){
        itemMaterial = new Folder("items" + File.separator + "material", TanoRPG.getPlugin());
        for (Config config : itemMaterial.getFiles()) {
            for (String value : config.getConfig().getKeys(false)) {
                CustomItemType cit = CustomItemType.MATERIAL;
                String id = value;
                Material material = Material.valueOf((String) config.getConfig().get(value + ".material"));
                String name = config.getConfig().getString(value + ".name");
                ArrayList<String> lores = (ArrayList<String>) config.getConfig().getList(value + ".lore");
                ArrayList<Status> statuses = new ArrayList<>();
                for (String key : config.getConfig().getConfigurationSection(value + ".status").getKeys(false)) {
                    StatusType statusType = StatusType.valueOf(key);
                    if (statusType.getName().equals("")) {
                        break;
                    }
                    Status status2 = new Status(statusType, config.getConfig().getDouble(value + ".status." + key));
                    statuses.add(status2);
                }
                long price = config.getConfig().getLong(value + ".price");
                boolean glowing = config.getConfig().getBoolean(value + ".glowing");
                CustomItem customItem = new CustomItem(cit, id, material, name, lores, statuses, glowing);
                if (config.getConfig().isSet(value + ".customModelData")){
                    customItem.setCustomModelData(config.getConfig().getInt(value + ".customModelData"));
                }
                customItem.setRarity(CustomItemRarityType.valueOf((String) config.getConfig().get(value + ".rarity")));
                customItem.setPrice(price);
                customItems.put(value, customItem);
                customItemIDs.add(value);
            }
        }
    }
    public static void loadCustomItemWeapon() {
        itemWeapon = new Folder("items" + File.separator + "weapon", TanoRPG.getPlugin());
        for (Config config : itemWeapon.getFiles()) {
            for (String value : config.getConfig().getKeys(false)) {
                CustomItemType cit = CustomItemType.WEAPON;
                String id = value;
                Material material = Material.valueOf((String) config.getConfig().get(value + ".material"));
                String name = config.getConfig().getString(value + ".name");
                ArrayList<String> lores = (ArrayList<String>) config.getConfig().getList(value + ".lore");
                ArrayList<Status> statuses = new ArrayList<>();
                for (String key : config.getConfig().getConfigurationSection(value + ".status").getKeys(false)) {
                    StatusType statusType = StatusType.valueOf(key);
                    if (statusType.getName().equals("")) {
                        break;
                    }
                    Status status2 = new Status(statusType, config.getConfig().getDouble(value + ".status." + key));
                    statuses.add(status2);
                }
                ArrayList<GamePlayerJobType> jobs = new ArrayList<>();
                for (String job : (ArrayList<String>) config.getConfig().getList(value + ".proper")) {
                    jobs.add(GamePlayerJobType.valueOf(job));
                }
                long price = config.getConfig().getLong(value + ".price");
                long ct = config.getConfig().getLong(value + ".ct");
                boolean glowing = config.getConfig().getBoolean(value + ".glowing");
                CustomItem customItem = new CustomItem(cit, id, material, name, lores, statuses, glowing);
                customItem.setRarity(CustomItemRarityType.valueOf((String) config.getConfig().get(value + ".rarity")));
                if (config.getConfig().isSet(value + ".customModelData")){
                    customItem.setCustomModelData(config.getConfig().getInt(value + ".customModelData"));
                }
                customItem.setPrice(price);
                customItem.setProper(jobs);
                customItem.setCooltime(ct);
                customItem.setLvl(config.getConfig().getInt(value + ".lvl"));
                customItems.put(value, customItem);
                customItemIDs.add(value);
            }
        }
    }
    public static void loadCustomItemMagicWeapon() {
        itemMagicWeapon = new Folder("items" + File.separator + "magicWeapon", TanoRPG.getPlugin());
        for (Config config : itemMagicWeapon.getFiles()) {
            for (String value : config.getConfig().getKeys(false)) {
                CustomItemType cit = CustomItemType.MAGIC_WEAPON;
                String id = value;
                Material material = Material.valueOf((String) config.getConfig().get(value + ".material"));
                String name = config.getConfig().getString(value + ".name");
                ArrayList<String> lores = (ArrayList<String>) config.getConfig().getList(value + ".lore");
                ArrayList<Status> statuses = new ArrayList<>();
                for (String key : config.getConfig().getConfigurationSection(value + ".status").getKeys(false)) {
                    StatusType statusType = StatusType.valueOf(key);
                    if (statusType.getName().equals("")) {
                        break;
                    }
                    Status status2 = new Status(statusType, config.getConfig().getDouble(value + ".status." + key));
                    statuses.add(status2);
                }
                ArrayList<GamePlayerJobType> jobs = new ArrayList<>();
                for (String job : (ArrayList<String>) config.getConfig().getList(value + ".proper"))
                    jobs.add(GamePlayerJobType.valueOf(job));
                long price = config.getConfig().getLong(value + ".price");
                boolean glowing = config.getConfig().getBoolean(value + ".glowing");
                long ct = config.getConfig().getLong(value + ".ct");
                CustomItem customItem = new CustomItem(cit, id, material, name, lores, statuses, glowing);
                customItem.setRarity(CustomItemRarityType.valueOf((String) config.getConfig().get(value + ".rarity")));
                if (config.getConfig().isSet(value + ".customModelData")){
                    customItem.setCustomModelData(config.getConfig().getInt(value + ".customModelData"));
                }
                customItem.setPrice(price);
                customItem.setProper(jobs);
                customItem.setCooltime(ct);
                customItem.setLvl(config.getConfig().getInt(value + ".lvl"));
                customItems.put(value, customItem);
                customItemIDs.add(value);
            }
        }
    }
    public static void loadCustomItemEquipment(){
        itemEquipment = new Folder("items" + File.separator + "equip", TanoRPG.getPlugin());
        for (Config config : itemEquipment.getFiles()) {
            for (String value : config.getConfig().getKeys(false)) {
                CustomItemType cit = CustomItemType.EQUIPMENT;
                String id = value;
                Material material = Material.valueOf((String) config.getConfig().get(value + ".material"));
                String name = config.getConfig().getString(value + ".name");
                ArrayList<String> lores = (ArrayList<String>) config.getConfig().getList(value + ".lore");
                ArrayList<Status> statuses = new ArrayList<>();
                for (String key : config.getConfig().getConfigurationSection(value + ".status").getKeys(false)) {
                    StatusType statusType = StatusType.valueOf(key);
                    if (statusType.getName().equals("")) {
                        break;
                    }
                    Status status2 = new Status(statusType, config.getConfig().getDouble(value + ".status." + key));
                    statuses.add(status2);
                }
                Color color = null;
                if (material.toString().contains("LEATHER") && config.getConfig().get(value + ".color.RED") != null &&
                        config.getConfig().get(value + ".color.GREEN") != null &&
                        config.getConfig().get(value + ".color.BLUE") != null){
                    color = Color.fromRGB(config.getConfig().getInt(value + ".color.RED"),
                            config.getConfig().getInt(value + ".color.GREEN"),
                            config.getConfig().getInt(value + ".color.BLUE"));
                }
                ArrayList<GamePlayerJobType> jobs = new ArrayList<>();
                for (String job : (ArrayList<String>) config.getConfig().getList(value + ".proper"))
                    jobs.add(GamePlayerJobType.valueOf(job));
                long price = config.getConfig().getLong(value + ".price");
                boolean glowing = config.getConfig().getBoolean(value + ".glowing");
                CustomItem customItem = new CustomItem(cit, id, material, name, lores, statuses, glowing);
                customItem.setRarity(CustomItemRarityType.valueOf((String) config.getConfig().get(value + ".rarity")));
                if (config.getConfig().isSet(value + ".customModelData")){
                    customItem.setCustomModelData(config.getConfig().getInt(value + ".customModelData"));
                }
                customItem.setPrice(price);
                customItem.setProper(jobs);
                customItem.setLvl(config.getConfig().getInt(value + ".lvl"));
                if (color != null) customItem.setColor(color);
                customItems.put(value, customItem);
                customItemIDs.add(value);
            }
        }
    }
    public static CustomItem getCustomItem(String id){ return customItems.get(id);}
    public static boolean isExists(String id){
        return (customItems.get(id) != null);
    }
    public static String getID(ItemStack item){
        try {
            if (!item.getType().equals(Material.AIR) && item != null && item.getItemMeta().getLore() != null){
                String[] id = item.getItemMeta().getLore().get(item.getItemMeta().getLore().size() - 1).split(" ");
                if (id[1] == null) return "";
                return id[1];
            }
            return "";
        }catch (Exception e){
            return "";
        }
    }
    public static CustomItem getCustomItem(ItemStack item) {
        if (!item.getType().equals(Material.AIR) && item != null && item.getItemMeta().getLore() != null){
            String[] id = item.getItemMeta().getLore().get(item.getItemMeta().getLore().size() - 1).split(" ");
            return getCustomItem(id[1]);
        }
        return null;
    }
    public static int getAmount(Player player, ItemStack item){
        int i = 0;
        for (ItemStack is : player.getInventory().getContents()) {
            if (is == null || is.getType().equals(Material.AIR)) continue;
            String is_name = (is.getItemMeta().getDisplayName() == null) ? "" : is.getItemMeta().getDisplayName();
            String item_name = (item.getItemMeta().getDisplayName() == null) ? "" : item.getItemMeta().getDisplayName();
            List<String> is_lore = (is.getItemMeta().getLore() != null) ? is.getItemMeta().getLore() : Arrays.asList("");
            List<String> item_lore = (item.getItemMeta().getLore() != null) ? item.getItemMeta().getLore() : Arrays.asList("");
            if (is_name.equals(item_name) && is_lore.equals(item_lore) && is.getType().equals(item.getType())){
                i = i + is.getAmount();
            }
        }
        return i;
    }
    public static List<String> getItemIDs(){
        return customItemIDs;
    }
}
