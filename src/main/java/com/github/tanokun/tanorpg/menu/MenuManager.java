package com.github.tanokun.tanorpg.menu;

import com.github.tanokun.tanorpg.util.Glowing;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class MenuManager implements Listener {
    private static HashMap<String, Menu> menus = new HashMap<>();
    public static void registerMenu(Menu menu){menus.put(menu.getName(), menu);}
    public static Menu getMenu(String name){return menus.get(name);}

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if (menus.get(e.getView().getTitle()) != null){
            menus.get(e.getView().getTitle()).onClick(e);
        }
    }
    @EventHandler
    public void onClose(InventoryCloseEvent e){
        if (menus.get(e.getView().getTitle()) != null){
            menus.get(e.getView().getTitle()).onClose(e);
        }
    }
    public static ItemStack createItem(Material material, String name, int count, boolean glowing){
        ItemStack is = new ItemStack(material);
        is.setAmount(count);

        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        if (glowing == true) {im.addEnchant(new Glowing(80), 1, true);}
        is.setItemMeta(im);
        return is;
    }
    public static ItemStack createItem(Material material, String name, List<String> lore, int count, boolean glowing){
        ItemStack is = new ItemStack(material);
        is.setAmount(count);

        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        im.setLore(lore);
        if (glowing == true) {im.addEnchant(new Glowing(80), 1, true);}
        is.setItemMeta(im);
        return is;
    }
    public static ItemStack createItem(Material material, String name, int count, boolean glowing, DyeColor color){
        ItemStack is = new ItemStack(material, count, color.getWoolData());
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        if (glowing == true) {im.addEnchant(new Glowing(80), 1, true);}
        is.setItemMeta(im);
        return is;
    }
    public static ItemStack createItem(Material material, String name, List<String> lore, int count, boolean glowing, DyeColor color){
        ItemStack is = new ItemStack(material, count, color.getWoolData());

        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        im.setLore(lore);
        if (glowing == true) {im.addEnchant(new Glowing(80), 1, true);}
        is.setItemMeta(im);
        return is;
    }
    public static ItemStack setItemColor(ItemStack item, short i) {
        ItemMeta itemMeta = item.getItemMeta();
        item.setDurability(i);
        return item;
    }
}
