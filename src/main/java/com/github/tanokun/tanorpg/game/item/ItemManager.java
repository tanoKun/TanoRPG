package com.github.tanokun.tanorpg.game.item;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.item.itemtype.ItemEquipment;
import com.github.tanokun.tanorpg.game.item.itemtype.ItemMagicWeapon;
import com.github.tanokun.tanorpg.game.item.itemtype.ItemMaterial;
import com.github.tanokun.tanorpg.game.item.itemtype.ItemWeapon;
import com.github.tanokun.tanorpg.game.item.itemtype.base.Item;
import com.github.tanokun.tanorpg.game.player.GamePlayerJobType;
import com.github.tanokun.tanorpg.game.player.status.Status;
import com.github.tanokun.tanorpg.game.player.status.StatusType;
import com.github.tanokun.tanorpg.util.io.Config;
import com.github.tanokun.tanorpg.util.io.Folder;
import com.github.tanokun.tanorpg.util.io.MapNode;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class ItemManager {
    private static final HashMap<String, Item> items = new HashMap<>();

    private static final List<String> itemIDs = new ArrayList<>();
    public static final String LORE = "§e〇=-=-=-=-=§b説明§e=-=-=-=-=-〇";
    public static final String FIRST_STATUS = "§e〇=-=-=-§bステータス§e-=-=-=-〇";
    public static final String FINAL_STATUS = "§e〇=-=-=-=-=-=-=-=-=-=-=-=-〇";

    final public static String firstArmor = TanoRPG.getPlugin().getConfig().getString("first-job-armor");
    final public static String firstMagicWeapon = TanoRPG.getPlugin().getConfig().getString("first-job-mage-weapon");
    final public static String firstWeapon = TanoRPG.getPlugin().getConfig().getString("first-job-warrior-weapon");

    public static HashSet<String> loadMaterialItem(){
        HashSet<String> errors = new HashSet<>();

        for (Config config : new Folder("items" + File.separator + "material", TanoRPG.getPlugin()).getFiles()) {
            for (String value : config.getConfig().getKeys(false)) {
                boolean successFull = true;
                MapNode<String, Object> data = null;

                String id = value;
                String name = null;
                Material material = null;
                List<String> lore = new ArrayList<>();
                List<Status> statuses = new ArrayList<>();
                boolean glowing = false;
                int price = 0;
                ItemRarityType rarity = ItemRarityType.COMMON;
                Integer customModelData = 0;
                try {
                    data = get(value + ".name", config);
                    if (data.getValue() != null) {name = (String) data.getValue();} else {throw new NullPointerException("アイテム名が設定されていません");}

                    data = get(value + ".material", config);
                    if (data.getValue() != null) {
                        try {
                            material = Material.valueOf((String) data.getValue());
                        }catch (IllegalArgumentException e){
                            throw new IllegalArgumentException("マテリアル「" + data.getValue() + "」は存在しません");
                        }
                    } else {throw new NullPointerException("マテリアルが設定されていません");}

                    data = getList(value + ".lore", config);
                    if (data.getValue() != null) {lore = (List<String>) data.getValue();} else {throw new NullPointerException("説明文が設定されていません");}

                    data = new MapNode<>(value + "/status", config.getConfig().getConfigurationSection(value + ".status").getKeys(false));
                    if (data.getValue() != null) {
                        for (String key : (LinkedHashSet<String>) data.getValue()) {
                            try {
                                StatusType statusType = StatusType.valueOf(key);
                                data = get(value + ".status." + key, config);
                                Status status2 = new Status(statusType, Integer.valueOf((String) data.getValue()));
                                statuses.add(status2);
                            }catch (NumberFormatException e){
                                throw new NumberFormatException("ステータスレベル「" + data.getValue() + "」は数字で入力して下さい");
                            }catch (IllegalArgumentException e){
                                throw new IllegalArgumentException("ステータス「" + key + "」は存在しません");
                            }
                        }
                    } else {
                        throw new NullPointerException("ステータスが設定されていません");
                    }

                    data = get(value + ".glowing", config);
                    if (data.getValue() != null) {
                        if (!data.getValue().equals("true") && !data.getValue().equals("false")){
                            throw new IllegalArgumentException("Glowingは「true or false」で入力してください");
                        }
                        glowing = Boolean.valueOf((String) data.getValue());
                    }

                    try {
                        data = get(value + ".price", config);
                        if (data.getValue() != null) {
                            price = Integer.valueOf((String) data.getValue());
                        } else {
                            throw new NullPointerException("売却値段が設定されていません");
                        }

                        data = get(value + ".customModelData", config);
                        if (data.getValue() != null) {
                            customModelData = Integer.valueOf((String) data.getValue());
                        }
                    } catch (NumberFormatException e){
                        throw new NumberFormatException("「" + data.getValue() + "」は数字で入力して下さい");
                    }

                    data = get(value + ".rarity", config);
                    if (data.getValue() != null) {
                        try {
                            rarity = ItemRarityType.valueOf((String) data.getValue());
                        }catch (IllegalArgumentException e){
                            throw new IllegalArgumentException("レアリティ「" + data.getValue() + "」は存在しません");
                        }
                    } else {throw new NullPointerException("レアリティが設定されていません");}
                } catch (Exception e) {
                    successFull = false;
                    errors.add(ChatColor.RED + e.getMessage() + ChatColor.GRAY + "(Path: " + config.getName() + "/" + data.getKey() + ")");
                }
                if (successFull){
                    ItemMaterial item = new ItemMaterial(id, material, name, lore, statuses, glowing);
                    item.setPrice(price);
                    item.setRarity(rarity);
                    item.setCustomModelData(customModelData);
                    items.put(value, item);
                    itemIDs.add(value);
                    errors.add("§aMaterial item config loaded without errors.");
                }
            }
        }

        return errors;
    }
    public static HashSet<String> loadWeaponItem(){
        HashSet<String> errors = new HashSet<>();

        for (Config config : new Folder("items" + File.separator + "weapon", TanoRPG.getPlugin()).getFiles()) {
            for (String value : config.getConfig().getKeys(false)) {
                boolean successFull = true;
                MapNode<String, Object> data = null;

                String id = value;
                String name = null;
                Material material = null;
                List<String> lore = new ArrayList<>();
                List<Status> statuses = new ArrayList<>();
                boolean glowing = false;
                int price = 0;
                ItemRarityType rarity = ItemRarityType.COMMON;
                Integer customModelData = 0;

                long ct = 20;
                int lvl = 1;
                ArrayList<GamePlayerJobType> jobs = new ArrayList<>();

                try {
                    data = get(value + ".name", config);
                    if (data.getValue() != null) {name = (String) data.getValue();} else {throw new NullPointerException("アイテム名が設定されていません");}

                    data = get(value + ".material", config);
                    if (data.getValue() != null) {
                        try {
                            material = Material.valueOf((String) data.getValue());
                        }catch (IllegalArgumentException e){
                            throw new IllegalArgumentException("マテリアル「" + data.getValue() + "」は存在しません");
                        }
                    } else {throw new NullPointerException("マテリアルが設定されていません");}

                    data = getList(value + ".lore", config);
                    if (data.getValue() != null) {lore = (List<String>) data.getValue();} else {throw new NullPointerException("説明文が設定されていません");}

                    data = new MapNode<>(value + "/status", config.getConfig().getConfigurationSection(value + ".status").getKeys(false));
                    if (data.getValue() != null) {
                        for (String key : (LinkedHashSet<String>) data.getValue()) {
                            try {
                                StatusType statusType = StatusType.valueOf(key);
                                data = get(value + ".status." + key, config);
                                Status status2 = new Status(statusType, Integer.valueOf((String) data.getValue()));
                                statuses.add(status2);
                            }catch (NumberFormatException e){
                                throw new NumberFormatException("ステータスレベル「" + data.getValue() + "」は数字で入力して下さい");
                            }catch (IllegalArgumentException e){
                                throw new IllegalArgumentException("ステータス「" + key + "」は存在しません");
                            }
                        }
                    } else {
                        throw new NullPointerException("ステータスが設定されていません");
                    }

                    data = get(value + ".glowing", config);
                    if (data.getValue() != null) {
                        if (!data.getValue().equals("true") && !data.getValue().equals("false")){
                            throw new IllegalArgumentException("Glowingは「true or false」で入力してください");
                        }
                        glowing = Boolean.getBoolean((String) data.getValue());
                    }

                    try {
                        data = get(value + ".price", config);
                        if (data.getValue() != null) {
                            price = Integer.valueOf((String) data.getValue());
                        } else {
                            throw new NullPointerException("売却値段が設定されていません");
                        }

                        data = get(value + ".customModelData", config);
                        if (data.getValue() != null) {
                            customModelData = Integer.valueOf((String) data.getValue());
                        }

                        data = get(value + ".ct", config);
                        if (data.getValue() != null) {
                            ct = Integer.valueOf((String) data.getValue());
                        }

                        data = get(value + ".lvl", config);
                        if (data.getValue() != null) {
                            lvl = Integer.valueOf((String) data.getValue());
                        }
                    } catch (NumberFormatException e){
                        throw new NumberFormatException("「" + data.getValue() + "」は数字で入力して下さい");
                    }

                    data = getList(value + ".proper", config);
                    if (data.getValue() != null) {
                        for (String proper : (List<String>) data.getValue()) {
                            try {
                                jobs.add(GamePlayerJobType.valueOf(proper));
                            } catch (IllegalArgumentException e) {
                                throw new NumberFormatException("職業「" + proper + "」は存在しません");
                            }
                        }
                    } else {throw new NullPointerException("対応職業が設定されていません");}

                    data = get(value + ".rarity", config);
                    if (data.getValue() != null) {
                        try {
                            rarity = ItemRarityType.valueOf((String) data.getValue());
                        }catch (IllegalArgumentException e){
                            throw new IllegalArgumentException("レアリティ「" + data.getValue() + "」は存在しません");
                        }
                    } else {throw new NullPointerException("レアリティが設定されていません");}


                } catch (Exception e) {
                    successFull = false;
                    errors.add(ChatColor.RED + e.getMessage() + ChatColor.GRAY + "(Path: " + config.getName() + "/" + data.getKey() + ")");
                }
                if (successFull){
                    ItemWeapon item = new ItemWeapon(id, material, name, lore, statuses, glowing);
                    item.setPrice(price);
                    item.setRarity(rarity);
                    item.setCustomModelData(customModelData);
                    item.setCoolTime(ct);
                    item.setLvl(lvl);
                    item.setJobs(jobs);
                    items.put(value, item);
                    itemIDs.add(value);
                    errors.add("§aWeapon item config loaded without errors.");
                }
            }
        }

        return errors;
    }
    public static HashSet<String> loadMagicWeaponItem(){
        HashSet<String> errors = new HashSet<>();

        for (Config config : new Folder("items" + File.separator + "magicWeapon", TanoRPG.getPlugin()).getFiles()) {
            for (String value : config.getConfig().getKeys(false)) {
                boolean successFull = true;
                MapNode<String, Object> data = null;

                String id = value;
                String name = null;
                Material material = null;
                List<String> lore = new ArrayList<>();
                List<Status> statuses = new ArrayList<>();
                boolean glowing = false;
                int price = 0;
                ItemRarityType rarity = ItemRarityType.COMMON;
                Integer customModelData = 0;

                long ct = 20;
                int lvl = 1;
                ArrayList<GamePlayerJobType> jobs = new ArrayList<>();

                try {
                    data = get(value + ".name", config);
                    if (data.getValue() != null) {name = (String) data.getValue();} else {throw new NullPointerException("アイテム名が設定されていません");}

                    data = get(value + ".material", config);
                    if (data.getValue() != null) {
                        try {
                            material = Material.valueOf((String) data.getValue());
                        }catch (IllegalArgumentException e){
                            throw new IllegalArgumentException("マテリアル「" + data.getValue() + "」は存在しません");
                        }
                    } else {throw new NullPointerException("マテリアルが設定されていません");}

                    data = getList(value + ".lore", config);
                    if (data.getValue() != null) {lore = (List<String>) data.getValue();} else {throw new NullPointerException("説明文が設定されていません");}

                    data = new MapNode<>(value + "/status", config.getConfig().getConfigurationSection(value + ".status").getKeys(false));
                    if (data.getValue() != null) {
                        for (String key : (LinkedHashSet<String>) data.getValue()) {
                            try {
                                StatusType statusType = StatusType.valueOf(key);
                                data = get(value + ".status." + key, config);
                                Status status2 = new Status(statusType, Integer.valueOf((String) data.getValue()));
                                statuses.add(status2);
                            }catch (NumberFormatException e){
                                throw new NumberFormatException("ステータスレベル「" + data.getValue() + "」は数字で入力して下さい");
                            }catch (IllegalArgumentException e){
                                throw new IllegalArgumentException("ステータス「" + key + "」は存在しません");
                            }
                        }
                    } else {
                        throw new NullPointerException("ステータスが設定されていません");
                    }

                    data = get(value + ".glowing", config);
                    if (data.getValue() != null) {
                        if (!data.getValue().equals("true") && !data.getValue().equals("false")){
                            throw new IllegalArgumentException("Glowingは「true or false」で入力してください");
                        }
                        glowing = Boolean.getBoolean((String) data.getValue());
                    }

                    try {
                        data = get(value + ".price", config);
                        if (data.getValue() != null) {
                            price = Integer.valueOf((String) data.getValue());
                        } else {
                            throw new NullPointerException("売却値段が設定されていません");
                        }

                        data = get(value + ".customModelData", config);
                        if (data.getValue() != null) {
                            customModelData = Integer.valueOf((String) data.getValue());
                        }

                        data = get(value + ".ct", config);
                        if (data.getValue() != null) {
                            ct = Integer.valueOf((String) data.getValue());
                        }

                        data = get(value + ".lvl", config);
                        if (data.getValue() != null) {
                            lvl = Integer.valueOf((String) data.getValue());
                        }
                    } catch (NumberFormatException e){
                        throw new NumberFormatException("「" + data.getValue() + "」は数字で入力して下さい");
                    }

                    data = getList(value + ".proper", config);
                    if (data.getValue() != null) {
                        for (String proper : (List<String>) data.getValue()) {
                            try {
                                jobs.add(GamePlayerJobType.valueOf(proper));
                            } catch (IllegalArgumentException e) {
                                throw new NumberFormatException("職業「" + proper + "」は存在しません");
                            }
                        }
                    } else {throw new NullPointerException("対応職業が設定されていません");}

                    data = get(value + ".rarity", config);
                    if (data.getValue() != null) {
                        try {
                            rarity = ItemRarityType.valueOf((String) data.getValue());
                        }catch (IllegalArgumentException e){
                            throw new IllegalArgumentException("レアリティ「" + data.getValue() + "」は存在しません");
                        }
                    } else {throw new NullPointerException("レアリティが設定されていません");}


                } catch (Exception e) {
                    successFull = false;
                    errors.add(ChatColor.RED + e.getMessage() + ChatColor.GRAY + "(Path: " + config.getName() + "/" + data.getKey() + ")");
                }
                if (successFull){
                    ItemMagicWeapon item = new ItemMagicWeapon(id, material, name, lore, statuses, glowing);
                    item.setPrice(price);
                    item.setRarity(rarity);
                    item.setCustomModelData(customModelData);
                    item.setCoolTime(ct);
                    item.setLvl((int) lvl);
                    item.setJobs(jobs);
                    items.put(value, item);
                    itemIDs.add(value);
                    errors.add("§aMagicWeapon item config loaded without errors.");
                }
            }
        }

        return errors;
    }
    public static HashSet<String> loadEquipmentItem(){
        HashSet<String> errors = new HashSet<>();

        for (Config config : new Folder("items" + File.separator + "equip", TanoRPG.getPlugin()).getFiles()) {
            for (String value : config.getConfig().getKeys(false)) {
                boolean successFull = true;
                MapNode<String, Object> data = null;

                String id = value;
                String name = null;
                Material material = null;
                List<String> lore = new ArrayList<>();
                List<Status> statuses = new ArrayList<>();
                boolean glowing = false;
                int price = 0;
                ItemRarityType rarity = ItemRarityType.COMMON;
                Integer customModelData = 0;

                int lvl = 1;
                ArrayList<GamePlayerJobType> jobs = new ArrayList<>();

                Color color = null;

                try {
                    data = get(value + ".name", config);
                    if (data.getValue() != null) {name = (String) data.getValue();} else {throw new NullPointerException("アイテム名が設定されていません");}

                    data = get(value + ".material", config);
                    if (data.getValue() != null) {
                        try {
                            material = Material.valueOf((String) data.getValue());
                        }catch (IllegalArgumentException e){
                            throw new IllegalArgumentException("マテリアル「" + data.getValue() + "」は存在しません");
                        }
                    } else {throw new NullPointerException("マテリアルが設定されていません");}

                    data = getList(value + ".lore", config);
                    if (data.getValue() != null) {lore = (List<String>) data.getValue();} else {throw new NullPointerException("説明文が設定されていません");}

                    data = new MapNode<>(value + "/status", config.getConfig().getConfigurationSection(value + ".status").getKeys(false));
                    if (data.getValue() != null) {
                        for (String key : (LinkedHashSet<String>) data.getValue()) {
                            try {
                                StatusType statusType = StatusType.valueOf(key);
                                data = get(value + ".status." + key, config);
                                Status status2 = new Status(statusType, Integer.valueOf((String) data.getValue()));
                                statuses.add(status2);
                            }catch (NumberFormatException e){
                                throw new NumberFormatException("ステータスレベル「" + data.getValue() + "」は数字で入力して下さい");
                            }catch (IllegalArgumentException e){
                                throw new IllegalArgumentException("ステータス「" + key + "」は存在しません");
                            }
                        }
                    } else {
                        throw new NullPointerException("ステータスが設定されていません");
                    }

                    data = get(value + ".glowing", config);
                    if (data.getValue() != null) {
                        if (!data.getValue().equals("true") && !data.getValue().equals("false")){
                            throw new IllegalArgumentException("Glowingは「true or false」で入力してください");
                        }
                        glowing = Boolean.getBoolean((String) data.getValue());
                    }

                    try {
                        data = get(value + ".price", config);
                        if (data.getValue() != null) {
                            price = Integer.valueOf((String) data.getValue());
                        } else {
                            throw new NullPointerException("売却値段が設定されていません");
                        }

                        data = get(value + ".customModelData", config);
                        if (data.getValue() != null) {
                            customModelData = Integer.valueOf((String) data.getValue());
                        }

                        data = get(value + ".lvl", config);
                        if (data.getValue() != null) {
                            lvl = Integer.valueOf((String) data.getValue());
                        }
                    } catch (NumberFormatException e){
                        throw new NumberFormatException("「" + data.getValue() + "」は数字で入力して下さい");
                    }

                    data = getList(value + ".proper", config);
                    if (data.getValue() != null) {
                        for (String proper : (List<String>) data.getValue()) {
                            try {
                                jobs.add(GamePlayerJobType.valueOf(proper));
                            } catch (IllegalArgumentException e) {
                                throw new NumberFormatException("職業「" + proper + "」は存在しません");
                            }
                        }
                    } else {throw new NullPointerException("対応職業が設定されていません");}

                    data = get(value + ".rarity", config);
                    if (data.getValue() != null) {
                        try {
                            rarity = ItemRarityType.valueOf((String) data.getValue());
                        }catch (IllegalArgumentException e){
                            throw new IllegalArgumentException("レアリティ「" + data.getValue() + "」は存在しません");
                        }
                    } else {throw new NullPointerException("レアリティが設定されていません");}

                    if (material.toString().contains("LEATHER") && config.getConfig().get(value + ".color.RED") != null &&
                            config.getConfig().get(value + ".color.GREEN") != null &&
                            config.getConfig().get(value + ".color.BLUE") != null){
                        try {
                            data = get(value + ".color.RED", config);
                            int r = Integer.valueOf((String) data.getValue());
                            data = get(value + ".color.GREEN", config);
                            int g = Integer.valueOf((String) data.getValue());
                            data = get(value + ".color.BLUE", config);
                            int b = Integer.valueOf((String) data.getValue());
                            color = Color.fromRGB(r, g, b);
                        }catch (NumberFormatException e) {
                            throw new NullPointerException("RGBは数字で入力してください");
                        }catch (IllegalArgumentException e){
                            throw new IllegalArgumentException("RGBは「0~255」の間です");
                        }
                    }

                } catch (Exception e) {
                    successFull = false;
                    errors.add(ChatColor.RED + e.getMessage() + ChatColor.GRAY + "(Path: " + config.getName() + "/" + data.getKey() + ")");
                }
                if (successFull){
                    ItemEquipment item = new ItemEquipment(id, material, name, lore, statuses, glowing);
                    item.setPrice(price);
                    item.setRarity(rarity);
                    item.setCustomModelData(customModelData);
                    item.setLvl(lvl);
                    item.setJobs(jobs);
                    item.setColor(color);
                    items.put(value, item);
                    itemIDs.add(value);
                    errors.add("§aEquipment item config loaded without errors.");
                }
            }
        }

        return errors;
    }

    public static MapNode<String, Object> get(String path, Config config){
        try {
            if (!config.getConfig().isSet(path)) throw new NullPointerException();
            return new MapNode<>(path.replace(".", "/"), String.valueOf(config.getConfig().getString(path)));
        }catch (Exception e) {
            return new MapNode<>(path.replace(".", "/"), null);
        }
    }
    private static MapNode<String, Object> getList(String path, Config config){
        try {
            return new MapNode<>(path.replace(".", "/"), config.getConfig().getStringList(path));
        }catch (Exception e) {
            return new MapNode<>(path.replace(".", "/"), null);
        }
    }

    public static Item getItem(String id){ return items.get(id);}
    public static boolean isExists(String id){
        return (items.get(id) != null);
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
    public static Item getItem(ItemStack item) {
        if (!item.getType().equals(Material.AIR) && item != null && item.getItemMeta().getLore() != null){
            String[] id = item.getItemMeta().getLore().get(item.getItemMeta().getLore().size() - 1).split(" ");
            return getItem(id[1]);
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
        return itemIDs;
    }

    public static void deleteItems() {
        items.clear();
        itemIDs.clear();
    }
}
