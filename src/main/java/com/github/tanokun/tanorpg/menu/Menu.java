package com.github.tanokun.tanorpg.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class Menu {
    private String name;
    private Inventory inv;
    public Menu(String name, int i){
        this.name = name;
        inv = Bukkit.createInventory(null, i*9, name);
    }
    public void setItem(int i, ItemStack is){inv.setItem(i, is);}
    public Inventory getInv() {return inv;}
    public String getName() {return name;}
    public void openInv(Player player){player.openInventory(inv); player.updateInventory();}
    abstract public void onClick(InventoryClickEvent e);
    public abstract void onClose(InventoryCloseEvent e);
}
