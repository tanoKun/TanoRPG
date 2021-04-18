package com.github.tanokun.api.smart_inv.inv;

import com.github.tanokun.api.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.api.smart_inv.util.Constants;
import com.google.common.collect.Iterables;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.function.Consumer;

public class ClickableItem
{
    public ItemStack basedItemStack;
    public Material material = Material.AIR;
    public int amount = 1;
    public int damage;
    public String displayName;
    public List<String> lore = new ArrayList<>();
    public Map<Enchantment, Integer> enchantments = new HashMap<>();
    public Set<ItemFlag> flags = new HashSet<>();
    private Consumer<ItemStack> raw = Constants.noOperation();

    private Consumer<ClickableItem> iconSettings;
    private Consumer<InventoryClickEvent> consumer;

    private ClickableItem(ItemStack item, Consumer<ClickableItem> settings, Consumer<InventoryClickEvent> consumer)
    {
        this.iconSettings = settings;
        this.basedItemStack = item;
        this.consumer = consumer;
        this.iconSettings.accept(this);
    }

    private ClickableItem(ItemStack item, Consumer<InventoryClickEvent> consumer)
    {
        this.basedItemStack = item;
        this.consumer = consumer;
    }

    private ClickableItem(Consumer<ClickableItem> settings, Consumer<InventoryClickEvent> consumer)
    {
        this.iconSettings = settings;
        this.consumer = consumer;
        this.iconSettings.accept(this);
    }

    public static ClickableItem empty(ItemStack item)
    {
        return of(item, e -> {});
    }

    public static ClickableItem empty(Consumer<ClickableItem> item)
    {
        return of(item, e -> {});
    }

    public static ClickableItem empty(ItemStack item, Consumer<ClickableItem> iconSettings)
    {
        return of(item, iconSettings, e -> {});
    }

    public static ClickableItem of(ItemStack item, Consumer<ClickableItem> settings, Consumer<InventoryClickEvent> consumer)
    {
        return new ClickableItem(item, settings, consumer);
    }

    public static ClickableItem of(ItemStack item, Consumer<InventoryClickEvent> consumer)
    {
        return new ClickableItem(item, consumer);
    }

    public static ClickableItem of(Consumer<ClickableItem> settings, Consumer<InventoryClickEvent> consumer)
    {
        return new ClickableItem(settings, consumer);
    }

    public void run(InventoryClickEvent e)
    {
        consumer.accept(e);
    }

    public ItemStack getItem()
    {
        if (basedItemStack == null) basedItemStack = apply();
        return basedItemStack;
    }

    public void raw(Consumer<ItemStack> settings)
    {
        raw = settings;
    }

    public ItemStack apply()
    {
        ItemStack item = new ItemStack(material);
        item.setAmount(amount);

        if (basedItemStack == null) item.setDurability((short) damage);

        ItemMeta meta = item.getItemMeta();
        if (meta != null)
        {
            meta.setDisplayName(displayName);
            meta.setLore(lore);
            enchantments.forEach((enchantment, level) -> meta.addEnchant(enchantment, level, true));
            meta.addItemFlags(Iterables.toArray(flags, ItemFlag.class));
            item.setItemMeta(meta);
        }

        raw.accept(item);
        return item;
    }

    public ItemStack toItemStack()
    {
        ItemStack item = basedItemStack != null ? basedItemStack : apply();
        return item;
    }
}