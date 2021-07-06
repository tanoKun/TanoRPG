package com.github.tanokun.tanorpg.util;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.item.type.base.ItemData;
import com.github.tanokun.tanorpg.player.Member;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;

public class ItemUtils implements Listener {
    private static Gson gson = new Gson();
    public static ItemStack createItem(Material material, String name, int count, boolean glowing){
        ItemStack is = new ItemStack(material);
        is.setAmount(count);

        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        if (glowing) {im.addEnchant(Enchantment.MENDING, 1, true);}
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack createItem(Material material, String name, List<String> lore, int count, boolean glowing){
        ItemStack is = new ItemStack(material);
        is.setAmount(count);

        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        im.setLore(lore);
        if (glowing) {im.addEnchant(Enchantment.MENDING, 1, true);}
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack createItem(Material material, String name, int count, boolean glowing, int c){
        ItemStack is = new ItemStack(material);
        is.setAmount(count);

        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        if (glowing) {im.addEnchant(Enchantment.MENDING, 1, true);}
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
        im.setCustomModelData(c);
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack createItem(Material material, String name, List<String> lore, int count, boolean glowing, int c){
        ItemStack is = new ItemStack(material);
        is.setAmount(count);

        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        im.setLore(lore);
        if (glowing) {im.addEnchant(Enchantment.MENDING, 1, true);}
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
        im.setCustomModelData(c);
        is.setItemMeta(im);
        return is;
    }



    public static <T, Z> void setPersistent(ItemStack itemStack, String key, PersistentDataType<T, Z> persistentDataType, Z value) {
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer test = meta.getPersistentDataContainer();
        test.set(new NamespacedKey(TanoRPG.getPlugin(), key), persistentDataType, value);
        itemStack.setItemMeta(meta);
    }

    public static <T, Z> Z getPersistent(ItemStack itemStack, String key, PersistentDataType<T, Z> persistentDataType){
        if (itemStack.getType().equals(Material.AIR)) return null;
        if (itemStack.getItemMeta().getPersistentDataContainer().isEmpty()) return null;
        return itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(TanoRPG.getPlugin(), key), persistentDataType);
    }

    public static ItemData getItemData(ItemStack itemStack){
        if (getPersistent(itemStack, "data", PersistentDataType.STRING) == null) return null;
        return gson.fromJson(getPersistent(itemStack, "data", PersistentDataType.STRING), new TypeToken<ItemData>(){}.getType());
    }

    public static void setItemData(ItemStack itemStack, ItemData itemData){
        setPersistent(itemStack, "data", PersistentDataType.STRING, gson.toJson(itemData, new TypeToken<ItemData>(){}.getType()));
    }

    public static boolean isTrueSkillClass(ItemData itemData, Member member, Player player){
        if (!itemData.getProper().contains(member.getSkillClass())) {
            TanoRPG.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 3, 1);
            player.sendMessage(TanoRPG.PX + "§c職業が対応していません");
            return false;
        }
        return true;
    }

    public static boolean isTrueLevel(ItemData itemData, Member member, Player player){
        if (itemData.getNecLevel() > member.getHasLevel().getValue()) {
            TanoRPG.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 3, 1);
            player.sendMessage(TanoRPG.PX + "§cレベルが足りません");
            return false;
        }
        return true;
    }


    public static int getAmount(Player player, ItemStack item) {
        int i = 0;
        for (ItemStack is : player.getInventory().getContents()) {
            if (is == null || is.getType().equals(Material.AIR)) continue;
            String is_name = (is.getItemMeta().getDisplayName() == null) ? "" : is.getItemMeta().getDisplayName();
            String item_name = (item.getItemMeta().getDisplayName() == null) ? "" : item.getItemMeta().getDisplayName();
            List<String> is_lore = (is.getItemMeta().getLore() != null) ? is.getItemMeta().getLore() : Arrays.asList("");
            if (is_name.equals(item_name) && is.getType().equals(item.getType())) {
                i = i + is.getAmount();
            }
        }
        return i;
    }

    public static ItemStack getSameItem(Player player, ItemStack item) {
        for (ItemStack is : player.getInventory().getContents()) {
            if (is == null || is.getType().equals(Material.AIR)) continue;
            String is_name = (is.getItemMeta().getDisplayName() == null) ? "" : is.getItemMeta().getDisplayName();
            String item_name = (item.getItemMeta().getDisplayName() == null) ? "" : item.getItemMeta().getDisplayName();
            List<String> is_lore = (is.getItemMeta().getLore() != null) ? is.getItemMeta().getLore() : Arrays.asList("");
            if (is_name.equals(item_name) && is.getType().equals(item.getType())) return is;
        }
        return null;
    }
}
