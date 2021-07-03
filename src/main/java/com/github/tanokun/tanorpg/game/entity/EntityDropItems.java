package com.github.tanokun.tanorpg.game.entity;

import com.github.tanokun.tanorpg.game.item.type.base.ItemBase;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import scala.Int;

import java.util.ArrayList;
import java.util.HashMap;

public class EntityDropItems {
    public HashMap<ItemStack, Integer> drops = new HashMap<>();

    public void addDrop(ItemStack item, int chance) {
        drops.put(item, chance);
    }

    public HashMap<ItemStack, Integer> getDrops() {
        return drops;
    }

    public void chanceGive(Player player, int chanceUp){
        for (ItemStack item : drops.keySet()){
            int chance = drops.get(item) * ((chanceUp / 100) + 1);
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
