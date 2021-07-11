package com.github.tanokun.tanorpg.game.entity;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class EntityDropItems {
    public HashMap<ItemStack, Double> drops = new HashMap<>();

    public void addDrop(ItemStack item, double chance) {
        drops.put(item, chance);
    }

    public HashMap<ItemStack, Double> getDrops() {
        return drops;
    }

    public void chanceGive(Player player, double chanceUp){
        for (ItemStack item : drops.keySet()){
            double chance = drops.get(item) * ((chanceUp / 100) + 1);
            if (chance(chance)) {
                player.getInventory().addItem(item);
            }
        }
    }
    private boolean chance(double chance) {
        chance = chance / 100;
        if (Math.random() <= chance) {
            return true;
        }
        return false;
    }

}
