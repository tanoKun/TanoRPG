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
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
        loadMaterialItem(p);
        loadWeaponItem(p);
        loadMagicWeaponItem(p);
        loadEquipmentItem(p);
        loadRuneItem(p);
        loadAccessoryItem(p);
    }

    public void loadMaterialItem(Player p) {
        try {
            for (Config config : new Folder("items" + File.separator + "material", TanoRPG.getPlugin()).getFiles()) {
                for (String id : config.getConfig().getKeys(false)) {
                    String name = config.getConfig().getString(id + ".name", "unknown");
                    Material material = Material.valueOf(config.getConfig().getString(id + ".material", "BARRIER"));
                    List<String> lore = config.getConfig().getStringList(id + ".lore");
                    StatusMap statusMap = new StatusMap();
                    config.getConfig().getConfigurationSection(id + ".status").getKeys(false).forEach(text -> {
                        statusMap.addStatus(StatusType.valueOf(text), config.getConfig().getInt(id + ".status." + text, 0));
                    });
                    boolean glowing = config.getConfig().getBoolean(id + ".glowing", false);
                    long price = config.getConfig().getLong(id + ".price", 0);
                    ItemRarityType rarity = ItemRarityType.valueOf(config.getConfig().getString(id + ".rarity", "COMMON"));
                    Integer customModelData = config.getConfig().getInt(id + ".customModelData", 0);

                    ItemMaterial item = new ItemMaterial(id, material, name, lore, statusMap, glowing, rarity);
                    item.setPrice(price);
                    item.setCustomModelData(customModelData);
                    items.put(id, item);
                    itemIDs.add(id);
                }
            }
        }catch (Exception e){
            if (p != null){
                p.sendMessage(TanoRPG.PX + "§c素材系アイテムのコンフィグでエラーが発生しました。");
            } else {
              TanoRPG.getPlugin().getLogger().warning("素材系アイテムのコンフィグでエラーが発生しました。");
            }
        }

        if (p != null){
            p.sendMessage(TanoRPG.PX + "§a素材系アイテムのコンフィグをロードしました。");
        } else {
            TanoRPG.getPlugin().getLogger().warning("素材系アイテムのコンフィグをロードしました。");
        }
    }

    public void loadWeaponItem(Player p) {
        try {
            for (Config config : new Folder("items" + File.separator + "weapon", TanoRPG.getPlugin()).getFiles()) {
                for (String id : config.getConfig().getKeys(false)) {
                    String name = config.getConfig().getString(id + ".name", "unknown");
                    Material material = Material.valueOf(config.getConfig().getString(id + ".material", "BARRIER"));
                    List<String> lore = config.getConfig().getStringList(id + ".lore");
                    StatusMap statusMap = new StatusMap();
                    config.getConfig().getConfigurationSection(id + ".status").getKeys(false).forEach(text -> {
                        statusMap.addStatus(StatusType.valueOf(text), config.getConfig().getInt(id + ".status." + text, 0));
                    });
                    boolean glowing = config.getConfig().getBoolean(id + ".glowing", false);
                    long price = config.getConfig().getLong(id + ".price", 0);
                    ItemRarityType rarity = ItemRarityType.valueOf(config.getConfig().getString(id + ".rarity", "COMMON"));
                    Integer customModelData = config.getConfig().getInt(id + ".customModelData", 0);
                    int lvl = config.getConfig().getInt(id + ".lvl", 0);
                    int ct = config.getConfig().getInt(id + ".ct", 0);
                    List<SkillClass> proper = new ArrayList<>(); config.getConfig().getList(id + ".proper").stream().forEach(job ->
                            proper.add(SkillClass.valueOf((String) job)));
                    List<Integer> combos = new ArrayList<>(); config.getConfig().getList(id + ".combo").stream().forEach(i ->
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
        }catch (Exception e) {
            if (p != null) {
                p.sendMessage(TanoRPG.PX + "§c武器系アイテムのコンフィグでエラーが発生しました。");
            } else {
                TanoRPG.getPlugin().getLogger().warning("武器系アイテムのコンフィグでエラーが発生しました。");
            }
        }

        if (p != null){
            p.sendMessage(TanoRPG.PX + "§a武器系アイテムのコンフィグをロードしました。");
        } else {
            TanoRPG.getPlugin().getLogger().warning("武器系アイテムのコンフィグをロードしました。");
        }
    }

    public void loadMagicWeaponItem(Player p) {
        try {
            for (Config config : new Folder("items" + File.separator + "magicWeapon", TanoRPG.getPlugin()).getFiles()) {
                for (String id : config.getConfig().getKeys(false)) {
                    String name = config.getConfig().getString(id + ".name", "unknown");
                    Material material = Material.valueOf(config.getConfig().getString(id + ".material", "BARRIER"));
                    List<String> lore = config.getConfig().getStringList(id + ".lore");
                    StatusMap statusMap = new StatusMap();
                    config.getConfig().getConfigurationSection(id + ".status").getKeys(false).forEach(text -> {
                        statusMap.addStatus(StatusType.valueOf(text), config.getConfig().getInt(id + ".status." + text, 0));
                    });
                    boolean glowing = config.getConfig().getBoolean(id + ".glowing", false);
                    long price = config.getConfig().getLong(id + ".price", 0);
                    ItemRarityType rarity = ItemRarityType.valueOf(config.getConfig().getString(id + ".rarity", "COMMON"));
                    Integer customModelData = config.getConfig().getInt(id + ".customModelData", 0);
                    int lvl = config.getConfig().getInt(id + ".lvl", 0);
                    int ct = config.getConfig().getInt(id + ".ct", 0);
                    List<SkillClass> proper = new ArrayList<>(); config.getConfig().getList(id + ".proper").stream().forEach(job ->
                            proper.add(SkillClass.valueOf((String) job)));

                    ItemMagicWeapon item = new ItemMagicWeapon(id, material, name, lore, statusMap, glowing, rarity);
                    item.setPrice(price);
                    item.setCustomModelData(customModelData);
                    item.setCoolTime(ct);
                    item.setNecLevel(lvl);
                    item.setProper(proper);
                    items.put(id, item);
                    itemIDs.add(id);
                }
            }
        }catch (Exception e) {
            if (p != null) {
                p.sendMessage(TanoRPG.PX + "§c武器系アイテムのコンフィグでエラーが発生しました。");
            } else {
                TanoRPG.getPlugin().getLogger().warning("武器系アイテムのコンフィグでエラーが発生しました。");
            }
        }

        if (p != null){
            p.sendMessage(TanoRPG.PX + "§a魔法武器系アイテムのコンフィグをロードしました。");
        } else {
            TanoRPG.getPlugin().getLogger().warning("魔法武器系アイテムのコンフィグをロードしました。");
        }
    }

    public void loadEquipmentItem(Player p) {
        try {
            for (Config config : new Folder("items" + File.separator + "equip", TanoRPG.getPlugin()).getFiles()) {
                for (String id : config.getConfig().getKeys(false)) {
                    String name = config.getConfig().getString(id + ".name", "unknown");
                    Material material = Material.valueOf(config.getConfig().getString(id + ".material", "BARRIER"));
                    List<String> lore = config.getConfig().getStringList(id + ".lore");
                    EquipmentMap.EquipmentType equipmentType = null;

                    if (config.getConfig().isSet(id + ".equipType")){
                        equipmentType = EquipmentMap.EquipmentType.valueOf(config.getConfig().getString(id + ".equipType"));
                    } else {
                        EquipmentMap.EquipmentType.valueOf(config.getConfig().getString(id + ".material").split("_")[1]);
                    }

                    StatusMap statusMap = new StatusMap();
                    config.getConfig().getConfigurationSection(id + ".status").getKeys(false).forEach(text -> {
                        statusMap.addStatus(StatusType.valueOf(text), config.getConfig().getInt(id + ".status." + text, 0));
                    });
                    boolean glowing = config.getConfig().getBoolean(id + ".glowing", false);
                    long price = config.getConfig().getLong(id + ".price", 0);
                    ItemRarityType rarity = ItemRarityType.valueOf(config.getConfig().getString(id + ".rarity", "COMMON"));
                    Integer customModelData = config.getConfig().getInt(id + ".customModelData", 0);
                    int lvl = config.getConfig().getInt(id + ".lvl", 0);
                    List<SkillClass> proper = new ArrayList<>(); config.getConfig().getList(id + ".proper").stream().forEach(job ->
                            proper.add(SkillClass.valueOf((String) job)));
                    Color color = null;
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
        }catch (Exception e) {
            e.printStackTrace();
            if (p != null) {
                p.sendMessage(TanoRPG.PX + "§c装備系アイテムのコンフィグでエラーが発生しました。");
            } else {
                TanoRPG.getPlugin().getLogger().warning("装備系アイテムのコンフィグでエラーが発生しました。");
            }
        }

        if (p != null){
            p.sendMessage(TanoRPG.PX + "§a装備系アイテムのコンフィグをロードしました。");
        } else {
            TanoRPG.getPlugin().getLogger().warning("装備系アイテムのコンフィグをロードしました。");
        }
    }

    public void loadRuneItem(Player p) {
        try {
            for (Config config : new Folder("items" + File.separator + "rune", TanoRPG.getPlugin()).getFiles()) {
                for (String id : config.getConfig().getKeys(false)) {
                    String name = config.getConfig().getString(id + ".name", "unknown");
                    Material material = Material.valueOf(config.getConfig().getString(id + ".material", "BARRIER"));
                    List<String> lore = config.getConfig().getStringList(id + ".lore");
                    StatusMap statusMap = new StatusMap();
                    config.getConfig().getConfigurationSection(id + ".status").getKeys(false).forEach(text -> {
                        statusMap.addStatus(StatusType.valueOf(text), config.getConfig().getInt(id + ".status." + text, 0));
                    });
                    boolean glowing = config.getConfig().getBoolean(id + ".glowing", false);
                    long price = config.getConfig().getLong(id + ".price", 0);
                    ItemRarityType rarity = ItemRarityType.valueOf(config.getConfig().getString(id + ".rarity", "COMMON"));
                    Integer customModelData = config.getConfig().getInt(id + ".customModelData", 0);

                    ItemRune item = new ItemRune(id, material, name, lore, statusMap, glowing, rarity);
                    item.setPrice(price);
                    item.setCustomModelData(customModelData);
                    items.put(id, item);
                    itemIDs.add(id);
                }
            }
        }catch (Exception e){
            if (p != null){
                p.sendMessage(TanoRPG.PX + "§cルーン系アイテムのコンフィグでエラーが発生しました。");
            } else {
                TanoRPG.getPlugin().getLogger().warning("ルーン系アイテムのコンフィグでエラーが発生しました。");
            }
        }

        if (p != null){
            p.sendMessage(TanoRPG.PX + "§aルーン系アイテムのコンフィグをロードしました。");
        } else {
            TanoRPG.getPlugin().getLogger().warning("ルーン系アイテムのコンフィグをロードしました。");
        }
    }

    public void loadAccessoryItem(Player p) {
        try {
            for (Config config : new Folder("items" + File.separator + "accessory", TanoRPG.getPlugin()).getFiles()) {
                for (String id : config.getConfig().getKeys(false)) {
                    String name = config.getConfig().getString(id + ".name", "unknown");
                    Material material = Material.valueOf(config.getConfig().getString(id + ".material", "BARRIER"));
                    List<String> lore = config.getConfig().getStringList(id + ".lore");
                    StatusMap statusMap = new StatusMap();
                    config.getConfig().getConfigurationSection(id + ".status").getKeys(false).forEach(text -> {
                        statusMap.addStatus(StatusType.valueOf(text), config.getConfig().getInt(id + ".status." + text, 0));
                    });
                    boolean glowing = config.getConfig().getBoolean(id + ".glowing", false);
                    long price = config.getConfig().getLong(id + ".price", 0);
                    ItemRarityType rarity = ItemRarityType.valueOf(config.getConfig().getString(id + ".rarity", "COMMON"));
                    Integer customModelData = config.getConfig().getInt(id + ".customModelData", 0);

                    ItemAccessory item = new ItemAccessory(id, material, name, lore, statusMap, glowing, rarity);
                    item.setPrice(price);
                    item.setCustomModelData(customModelData);
                    items.put(id, item);
                    itemIDs.add(id);
                }
            }
        }catch (Exception e){
            if (p != null){
                p.sendMessage(TanoRPG.PX + "§cアクセサリー系アイテムのコンフィグでエラーが発生しました。");
            } else {
                TanoRPG.getPlugin().getLogger().warning("アクセサリー系アイテムのコンフィグでエラーが発生しました。");
            }
        }

        if (p != null){
            p.sendMessage(TanoRPG.PX + "§aアクセサリー系アイテムのコンフィグをロードしました。");
        } else {
            TanoRPG.getPlugin().getLogger().warning("アクセサリー系アイテムのコンフィグをロードしました。");
        }
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

    public int getAmount(Player player, ItemStack item) {
        int i = 0;
        for (ItemStack is : player.getInventory().getContents()) {
            if (is == null || is.getType().equals(Material.AIR)) continue;
            String is_name = (is.getItemMeta().getDisplayName() == null) ? "" : is.getItemMeta().getDisplayName();
            String item_name = (item.getItemMeta().getDisplayName() == null) ? "" : item.getItemMeta().getDisplayName();
            List<String> is_lore = (is.getItemMeta().getLore() != null) ? is.getItemMeta().getLore() : Arrays.asList("");
            List<String> item_lore = (item.getItemMeta().getLore() != null) ? item.getItemMeta().getLore() : Arrays.asList("");
            if (is_name.equals(item_name) && is_lore.equals(item_lore) && is.getType().equals(item.getType())) {
                i = i + is.getAmount();
            }
        }
        return i;
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
}
