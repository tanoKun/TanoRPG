package com.github.tanokun.tanorpg.util;
import com.github.tanokun.tanorpg.TanoRPG;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class Glowing extends Enchantment {

    public Glowing() {
        super(new NamespacedKey(TanoRPG.getPlugin(), "glowing"));
    }
    @Override
    public boolean canEnchantItem(ItemStack arg0) {
        return false;
    }
    @Override
    public boolean conflictsWith(Enchantment arg0) {
        return false;
    }
    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }
    @Override
    public boolean isTreasure() {
        return false;
    }
    @Override
    public boolean isCursed() {
        return false;
    }
    @Override
    public int getMaxLevel() {
        return 0;
    }
    @Override
    public String getName() {
        return null;
    }
    @Override
    public int getStartLevel() {
        return 0;
    }

}
