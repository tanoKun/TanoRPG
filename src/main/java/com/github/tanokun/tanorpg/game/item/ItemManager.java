package com.github.tanokun.tanorpg.game.item;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.item.type.*;
import com.github.tanokun.tanorpg.game.item.type.base.ItemBase;
import com.github.tanokun.tanorpg.player.EquipmentMap;
import com.github.tanokun.tanorpg.player.skill.SkillClass;
import com.github.tanokun.tanorpg.player.status.KindOfStatusType;
import com.github.tanokun.tanorpg.player.status.StatusMap;
import com.github.tanokun.tanorpg.player.status.StatusType;
import com.github.tanokun.tanorpg.util.ItemUtils;
import com.github.tanokun.tanorpg.util.io.Config;
import com.github.tanokun.tanorpg.util.io.Folder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.*;

public class ItemManager {
    private final HashMap<String, ItemBase> items = new HashMap<>();

    private static final ArrayList<String> itemIDs = new ArrayList<>();
    public static final String LORE = "§e〇=-=-=-=-=§b説明§e=-=-=-=-=-〇";
    public static final String FIRST_STATUS = "§e〇=-=-=-§bステータス§e-=-=-=-〇";
    public static final String FINAL_STATUS = "§e〇=-=-=-=-=-=-=-=-=-=-=-〇";

    final public static String firstArmor = TanoRPG.getPlugin().getConfig().getString("first-job-armor");
    final public static String firstMagicWeapon = TanoRPG.getPlugin().getConfig().getString("first-job-mage-weapon");
    final public static String firstWeapon = TanoRPG.getPlugin().getConfig().getString("first-job-warrior-weapon");

    public ItemManager(Player p){
        if (p != null) p.sendMessage(TanoRPG.PX + "§bLoading item configs...");
        else Bukkit.getConsoleSender().sendMessage("[TanoRPG] §bLoading item configs...");
        loadMaterialItem(p);
        loadWeaponItem(p);
        loadMagicWeaponItem(p);
        loadEquipmentItem(p);
        loadRuneItem(p);
        loadAccessoryItem(p);
        if (p != null) p.sendMessage(TanoRPG.PX + " ");
        else Bukkit.getConsoleSender().sendMessage(" ");
    }

    public void loadMaterialItem(Player p) {
        String path = "";
        String filePath = "";
        HashSet<String> errors = new HashSet<>();
        errors.add("§a    Material item configs loaded without errors.");
        try {
            path = "items" + File.separator + "material";
            for (Config config : new Folder(path, TanoRPG.getPlugin()).getFiles()) {
                filePath = path + File.separator + config.getName() + File.separator;
                for (String id : config.getConfig().getKeys(false)) {

                    path = id + ".name";
                    String name = config.getConfig().getString(path, "unknown");

                    path = id + ".material";
                    Material material = Material.valueOf(config.getConfig().getString(path, "BARRIER"));

                    path = id + ".lore";
                    List<String> lore = config.getConfig().getStringList(id + ".lore");

                    path = id + ".status";
                    StatusMap statusMap = new StatusMap();
                    config.getConfig().getConfigurationSection(path).getKeys(false).forEach(text -> {
                        statusMap.addStatus(StatusType.valueOf(text), config.getConfig().getInt(id + ".status." + text, 0));
                    });

                    path = id + ".glowing";
                    boolean glowing = config.getConfig().getBoolean(path, false);

                    path = id + ".price";
                    long price = config.getConfig().getLong(path, 0);

                    path = id + ".rarity";
                    ItemRarityType rarity = ItemRarityType.valueOf(config.getConfig().getString(path, "COMMON"));

                    path = id + ".customModelData";
                    Integer customModelData = config.getConfig().getInt(path, 0);

                    ItemMaterial item = new ItemMaterial(id, material, name, lore, statusMap, glowing, rarity);
                    item.setPrice(price);
                    item.setCustomModelData(customModelData);
                    items.put(id, item);
                    itemIDs.add(id);
                }
            }
        } catch (Exception e){
            errors.remove("§a    Material item configs loaded without errors.");
            errors.add("§c    " + e.getMessage() + "§7" + "(Path: " + filePath + path + ")");
        }

        showErrors(errors, p);
    }

    public void loadWeaponItem(Player p) {
        String path = "";
        String filePath = "";
        HashSet<String> errors = new HashSet<>();
        errors.add("§a    Weapon item configs loaded without errors.");
        try {
            path = "items" + File.separator + "weapon";
            for (Config config : new Folder(path, TanoRPG.getPlugin()).getFiles()) {
                filePath = path + File.separator + config.getName() + File.separator;
                for (String id : config.getConfig().getKeys(false)) {

                    path = id + ".name";
                    String name = config.getConfig().getString(path, "unknown");

                    path = id + ".material";
                    Material material = Material.valueOf(config.getConfig().getString(path, "BARRIER"));

                    path = id + ".lore";
                    List<String> lore = config.getConfig().getStringList(id + ".lore");

                    path = id + ".status";
                    StatusMap statusMap = new StatusMap();
                    config.getConfig().getConfigurationSection(path).getKeys(false).forEach(text -> {
                        statusMap.addStatus(StatusType.valueOf(text), config.getConfig().getInt(id + ".status." + text, 0));
                    });

                    path = id + ".glowing";
                    boolean glowing = config.getConfig().getBoolean(path, false);

                    path = id + ".price";
                    long price = config.getConfig().getLong(path, 0);

                    path = id + ".rarity";
                    ItemRarityType rarity = ItemRarityType.valueOf(config.getConfig().getString(path, "COMMON"));

                    path = id + ".customModelData";
                    Integer customModelData = config.getConfig().getInt(path, 0);

                    path = id + ".lvl";
                    int lvl = config.getConfig().getInt(path, 0);

                    path = id + ".ct";
                    int ct = config.getConfig().getInt(path, 0);

                    path = id + ".proper";
                    List<SkillClass> proper = new ArrayList<>(); config.getConfig().getList(path).stream().forEach(job ->
                            proper.add(SkillClass.valueOf((String) job)));

                    path = id + ".combo";
                    List<Integer> combos = new ArrayList<>(); config.getConfig().getList(path).stream().forEach(i ->
                            combos.add(Integer.valueOf(String.valueOf(i))));

                    ItemWeapon item = new ItemWeapon(id, material, name, lore, statusMap, glowing, rarity);
                    item.setPrice(price);
                    item.setCustomModelData(customModelData);
                    item.setCoolTime(ct);
                    item.setNecLevel(lvl);
                    item.setProper(proper);
                    item.setCombo(combos);
                    items.put(id, item);
                    itemIDs.add(id);
                }
            }
        } catch (Exception e){
            errors.remove("§a    Weapon item configs loaded without errors.");
            errors.add("§c    " + e.getMessage() + "§7" + "(Path: " + filePath + path + ")");
        }

        showErrors(errors, p);
    }

    public void loadMagicWeaponItem(Player p) {
        String path = "";
        String filePath = "";
        HashSet<String> errors = new HashSet<>();
        errors.add("§a    MagicWeapon item configs loaded without errors.");
        try {
            path = "items" + File.separator + "magicWeapon";
            for (Config config : new Folder(path, TanoRPG.getPlugin()).getFiles()) {
                filePath = path + File.separator + config.getName() + File.separator;
                for (String id : config.getConfig().getKeys(false)) {

                    path = id + ".name";
                    String name = config.getConfig().getString(path, "unknown");

                    path = id + ".material";
                    Material material = Material.valueOf(config.getConfig().getString(path, "BARRIER"));

                    path = id + ".lore";
                    List<String> lore = config.getConfig().getStringList(id + ".lore");

                    path = id + ".status";
                    StatusMap statusMap = new StatusMap();
                    config.getConfig().getConfigurationSection(path).getKeys(false).forEach(text -> {
                        statusMap.addStatus(StatusType.valueOf(text), config.getConfig().getInt(id + ".status." + text, 0));
                    });

                    path = id + ".glowing";
                    boolean glowing = config.getConfig().getBoolean(path, false);

                    path = id + ".price";
                    long price = config.getConfig().getLong(path, 0);

                    path = id + ".rarity";
                    ItemRarityType rarity = ItemRarityType.valueOf(config.getConfig().getString(path, "COMMON"));

                    path = id + ".customModelData";
                    Integer customModelData = config.getConfig().getInt(path, 0);

                    path = id + ".lvl";
                    int lvl = config.getConfig().getInt(path, 0);

                    path = id + ".ct";
                    int ct = config.getConfig().getInt(path, 0);

                    path = id + ".proper";
                    List<SkillClass> proper = new ArrayList<>(); config.getConfig().getList(path).stream().forEach(job ->
                            proper.add(SkillClass.valueOf((String) job)));

                    path = id + ".combo";
                    List<Integer> combos = new ArrayList<>(); config.getConfig().getList(path).stream().forEach(i ->
                            combos.add(Integer.valueOf(String.valueOf(i))));

                    ItemMagicWeapon item = new ItemMagicWeapon(id, material, name, lore, statusMap, glowing, rarity);
                    item.setPrice(price);
                    item.setCustomModelData(customModelData);
                    item.setCoolTime(ct);
                    item.setNecLevel(lvl);
                    item.setProper(proper);
                    item.setCombo(combos);
                    items.put(id, item);
                    itemIDs.add(id);
                }
            }
        } catch (Exception e){
            errors.remove("§a    MagicWeapon item configs loaded without errors.");
            errors.add("§c    " + e.getMessage() + "§7" + "(Path: " + filePath + path + ")");
        }

        showErrors(errors, p);
    }

    public void loadEquipmentItem(Player p) {
        String path = "";
        String filePath = "";
        HashSet<String> errors = new HashSet<>();
        errors.add("§a    Equipment item configs loaded without errors.");
        try {
            path = "items" + File.separator + "equip";
            for (Config config : new Folder(path, TanoRPG.getPlugin()).getFiles()) {
                filePath = path + File.separator + config.getName() + File.separator;
                for (String id : config.getConfig().getKeys(false)) {
                    path = id + ".name";
                    String name = config.getConfig().getString(path, "unknown");

                    path = id + ".material";
                    Material material = Material.valueOf(config.getConfig().getString(path, "BARRIER"));

                    path = id + ".lore";
                    List<String> lore = config.getConfig().getStringList(id + ".lore");

                    path = id + ".status";
                    StatusMap statusMap = new StatusMap();
                    config.getConfig().getConfigurationSection(path).getKeys(false).forEach(text -> {
                        statusMap.addStatus(StatusType.valueOf(text), config.getConfig().getInt(id + ".status." + text, 0));
                    });

                    path = id + ".glowing";
                    boolean glowing = config.getConfig().getBoolean(path, false);

                    path = id + ".price";
                    long price = config.getConfig().getLong(path, 0);

                    path = id + ".rarity";
                    ItemRarityType rarity = ItemRarityType.valueOf(config.getConfig().getString(path, "COMMON"));

                    path = id + ".customModelData";
                    Integer customModelData = config.getConfig().getInt(path, 0);

                    path = id + ".lvl";
                    int lvl = config.getConfig().getInt(path, 0);

                    path = id + ".ct";
                    int ct = config.getConfig().getInt(path, 0);

                    path = id + ".proper";
                    List<SkillClass> proper = new ArrayList<>(); config.getConfig().getList(path).stream().forEach(job ->
                            proper.add(SkillClass.valueOf((String) job)));

                    EquipmentMap.EquipmentType equipmentType = null;
                    path = id + ".equipType";
                    if (config.getConfig().isSet(id + ".equipType")){
                        equipmentType = EquipmentMap.EquipmentType.valueOf(config.getConfig().getString(path));
                    } else {
                        EquipmentMap.EquipmentType.valueOf(config.getConfig().getString(id + ".material").split("_")[1]);
                    }

                    Color color = null;
                    path = id + ".color.*";
                    if (material.toString().contains("LEATHER") && config.getConfig().get(id + ".color.RED") != null &&
                            config.getConfig().get(id + ".color.GREEN") != null &&
                            config.getConfig().get(id + ".color.BLUE") != null) {
                        color = Color.fromRGB(
                                config.getConfig().getInt(id + ".color.RED"),
                                config.getConfig().getInt(id + ".color.GREEN"),
                                config.getConfig().getInt(id + ".color.BLUE"));
                    }

                    ItemEquipment item = new ItemEquipment(id, material, name, lore, statusMap, glowing, rarity);
                    item.setPrice(price);
                    item.setCustomModelData(customModelData);
                    item.setColor(color);
                    item.setNecLevel(lvl);
                    item.setProper(proper);
                    item.setEquipmentType(equipmentType);
                    items.put(id, item);
                    itemIDs.add(id);
                }
            }
        } catch (Exception e){
            errors.remove("§a    Equipment item configs loaded without errors.");
            errors.add("§c    " + e.getMessage() + "§7" + "(Path: " + filePath + path + ")");
        }

        showErrors(errors, p);
    }

    public void loadRuneItem(Player p) {
        String path = "";
        String filePath = "";
        HashSet<String> errors = new HashSet<>();
        errors.add("§a    Rune item configs loaded without errors.");
        try {
            path = "items" + File.separator + "rune";
            for (Config config : new Folder(path, TanoRPG.getPlugin()).getFiles()) {
                filePath = path + File.separator + config.getName() + File.separator;
                for (String id : config.getConfig().getKeys(false)) {

                    path = id + ".name";
                    String name = config.getConfig().getString(path, "unknown");

                    path = id + ".material";
                    Material material = Material.valueOf(config.getConfig().getString(path, "BARRIER"));

                    path = id + ".lore";
                    List<String> lore = config.getConfig().getStringList(id + ".lore");

                    path = id + ".status";
                    StatusMap statusMap = new StatusMap();
                    config.getConfig().getConfigurationSection(path).getKeys(false).forEach(text -> {
                        statusMap.addStatus(StatusType.valueOf(text), config.getConfig().getInt(id + ".status." + text, 0));
                    });

                    path = id + ".glowing";
                    boolean glowing = config.getConfig().getBoolean(path, false);

                    path = id + ".price";
                    long price = config.getConfig().getLong(path, 0);

                    path = id + ".rarity";
                    ItemRarityType rarity = ItemRarityType.valueOf(config.getConfig().getString(path, "COMMON"));

                    path = id + ".customModelData";
                    Integer customModelData = config.getConfig().getInt(path, 0);

                    ItemRune item = new ItemRune(id, material, name, lore, statusMap, glowing, rarity);
                    item.setPrice(price);
                    item.setCustomModelData(customModelData);
                    items.put(id, item);
                    itemIDs.add(id);
                }
            }
        } catch (Exception e){
            errors.remove("§a    Rune item configs loaded without errors.");
            errors.add("§c    " + e.getMessage() + "§7" + "(Path: " + filePath + path + ")");
        }

        showErrors(errors, p);
    }

    public void loadAccessoryItem(Player p) {
        String path = "";
        String filePath = "";
        HashSet<String> errors = new HashSet<>();
        errors.add("§a    Accessory item configs loaded without errors.");
        try {
            path = "items" + File.separator + "accessory";
            for (Config config : new Folder(path, TanoRPG.getPlugin()).getFiles()) {
                filePath = path + File.separator + config.getName() + File.separator;
                for (String id : config.getConfig().getKeys(false)) {

                    path = id + ".name";
                    String name = config.getConfig().getString(path, "unknown");

                    path = id + ".material";
                    Material material = Material.valueOf(config.getConfig().getString(path, "BARRIER"));

                    path = id + ".lore";
                    List<String> lore = config.getConfig().getStringList(id + ".lore");

                    path = id + ".status";
                    StatusMap statusMap = new StatusMap();
                    config.getConfig().getConfigurationSection(path).getKeys(false).forEach(text -> {
                        statusMap.addStatus(StatusType.valueOf(text), config.getConfig().getInt(id + ".status." + text, 0));
                    });

                    path = id + ".glowing";
                    boolean glowing = config.getConfig().getBoolean(path, false);

                    path = id + ".price";
                    long price = config.getConfig().getLong(path, 0);

                    path = id + ".rarity";
                    ItemRarityType rarity = ItemRarityType.valueOf(config.getConfig().getString(path, "COMMON"));

                    path = id + ".customModelData";
                    Integer customModelData = config.getConfig().getInt(path, 0);

                    ItemRune item = new ItemRune(id, material, name, lore, statusMap, glowing, rarity);
                    item.setPrice(price);
                    item.setCustomModelData(customModelData);
                    items.put(id, item);
                    itemIDs.add(id);
                }
            }
        } catch (Exception e){
            errors.remove("§a    Accessory item configs loaded without errors.");
            errors.add("§c    " + e.getMessage() + "§7" + "(Path: " + filePath + path + ")");
        }

        showErrors(errors, p);
    }

    public ItemBase getItem(String id) {
        return items.get(id);
    }

    public boolean isExists(String id) {
        return (items.get(id) != null);
    }

    public String getID(ItemStack item) {
        return ItemUtils.getPersistent(item, "id", PersistentDataType.STRING) == null
                ? "" : ItemUtils.getPersistent(item, "id", PersistentDataType.STRING);
    }

    public ItemBase getItem(ItemStack item) {
        if (!item.getType().equals(Material.AIR) && item != null && item.getItemMeta().getLore() != null) {
            String[] id = item.getItemMeta().getLore().get(item.getItemMeta().getLore().size() - 1).split(" ");
            return getItem(id[1]);
        }
        return null;
    }

    public ArrayList<String> getItemIDs() {
        return itemIDs;
    }

    public List<String> fromNormalStatus(StatusMap statusMap){
        List<String> statuses = new ArrayList<>();
        for(StatusType status : statusMap.getHasStatuses().keySet()){
            if (status.equals(StatusType.NONE)) continue;
            if (statusMap.getHasStatuses().get(status) > 0){
                statuses.add(KindOfStatusType.NORMAL + "§a" + status.getName() + " +" + statusMap.getHasStatuses().get(status));
            } else {
                statuses.add(KindOfStatusType.NORMAL + "§c" + status.getName() + " -" + statusMap.getHasStatuses().get(status));
            }
        }
        return statuses;
    }

    public List<String> fromEvolutionStatus(StatusMap statusMap){
        List<String> statuses = new ArrayList<>();
        for(StatusType status : statusMap.getHasStatuses().keySet()){
            if (status.equals(StatusType.NONE)) continue;
            if (statusMap.getHasStatuses().get(status) > 0){
                statuses.add(KindOfStatusType.EVOLUTION + "§a" + status.getName() + " +" + statusMap.getHasStatuses().get(status));
            } else {
                statuses.add(KindOfStatusType.EVOLUTION + "§c" + status.getName() + " -" + statusMap.getHasStatuses().get(status));
            }
        }
        return statuses;
    }

    public List<String> fromRuneStatus(StatusMap statusMap){
        List<String> statuses = new ArrayList<>();
        for(StatusType status : statusMap.getHasStatuses().keySet()){
            if (status.equals(StatusType.NONE)) continue;
            if (statusMap.getHasStatuses().get(status) > 0){
                statuses.add(KindOfStatusType.RUNE + "§a" + status.getName() + " +" + statusMap.getHasStatuses().get(status));
            } else {
                statuses.add(KindOfStatusType.RUNE + "§c" + status.getName() + " -" + statusMap.getHasStatuses().get(status));
            }
        }
        return statuses;
    }

    public void deleteItems() {
        items.clear();
        itemIDs.clear();
    }

    private void showErrors(HashSet<String> errors, Player p){
        if (p != null) errors.stream().forEach(e -> p.sendMessage( e));
        else errors.stream().forEach(e -> Bukkit.getConsoleSender().sendMessage(e));
    }
}
