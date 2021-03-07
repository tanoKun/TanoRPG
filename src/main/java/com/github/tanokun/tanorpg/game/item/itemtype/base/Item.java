package com.github.tanokun.tanorpg.game.item.itemtype.base;

import com.github.tanokun.tanorpg.game.item.ItemRarityType;
import com.github.tanokun.tanorpg.game.item.ItemType;
import com.github.tanokun.tanorpg.game.player.status.Status;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class Item {
    private String id;
    private String name;
    private Material material;
    private List<String> lore;
    private List<Status> statuses;
    private boolean glowing;
    private long price = 0;
    private ItemRarityType rarity;
    private Integer customModelData = null;

    private final ItemType itemType = ItemType.NULL;

    public String getId() {return id;}
    public String getName() {return name;}
    public Material getMaterial() {return material;}
    public List<String> getLore() {return lore;}
    public List<Status> getStatuses() {return statuses;}
    public boolean isGlowing() {return glowing;}
    public long getPrice() {return price;}
    public ItemRarityType getRarity() {return rarity;}
    public Integer getCustomModelData() {return customModelData;}
    public ItemType getItemType() {return itemType;}

    public void setId(String id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setMaterial(Material material) {this.material = material;}
    public void setLore(List<String> lore) {this.lore = lore;}
    public void setStatuses(List<Status> statuses) {this.statuses = statuses;}
    public void setGlowing(boolean glowing) {this.glowing = glowing;}
    public void setPrice(long price) {this.price = price;}
    public void setRarity(ItemRarityType rarity) {this.rarity = rarity;}
    public void setCustomModelData(Integer customModelData) {this.customModelData = customModelData;}

    public Item(String id, Material material, String name, List<String> lores, List<Status> statuses, boolean glowing){
        this.id = id;
        this.material = material;
        this.name = name;
        this.lore = lores;
        this.glowing = glowing;
        this.statuses = statuses;
    }

    abstract public ItemStack getItem();
}
