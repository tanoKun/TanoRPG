package com.github.tanokun.tanorpg.game.item.type.base;

import com.github.tanokun.tanorpg.game.item.ItemRarityType;
import com.github.tanokun.tanorpg.game.item.ItemType;
import com.github.tanokun.tanorpg.player.EquipmentMap;
import com.github.tanokun.tanorpg.player.skill.SkillClass;
import com.github.tanokun.tanorpg.player.status.StatusMap;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class ItemBase {
    private String id;
    private String displayName;
    private Material material;
    private List<String> lore;
    private StatusMap statuses;
    private boolean glowing;
    private long price = 0;
    private ItemRarityType rarity;
    private Integer customModelData = null;
    private List<SkillClass> proper = new ArrayList<>();
    private int necLevel = 0;
    private int maxDurabilityValue = 1000;
    private int coolTime = 0;
    private EquipmentMap.EquipmentType equipmentType = EquipmentMap.EquipmentType.MAIN;
    private Color color = Color.fromRGB(0, 0, 0);
    private List<Integer> combo = new ArrayList<>();
    private int reach = 0;

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getMaterial() {
        return material;
    }

    public List<String> getLore() {
        return lore;
    }

    public StatusMap getBasicStatuses() {
        return statuses;
    }

    public boolean isGlowing() {
        return glowing;
    }

    public long getPrice() {
        return price;
    }

    public ItemRarityType getRarity() {
        return rarity;
    }

    public Color getColor() {
        return color;
    }

    public Integer getCustomModelData() {
        return customModelData;
    }

    public List<SkillClass> getProper() {
        return proper;
    }

    public int getNecLevel() {
        return necLevel;
    }

    public int getMaxDurabilityValue() {
        return maxDurabilityValue;
    }

    public int getCoolTime() {
        return coolTime;
    }

    public EquipmentMap.EquipmentType getEquipmentType() {
        return equipmentType;
    }

    public List<Integer> getCombo() {
        return combo;
    }

    public int getReach() {
        return reach;
    }

    public abstract ItemType getItemType();

    public void setId(String id) {
        this.id = id;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public void setBasicStatuses(StatusMap statuses) {
        this.statuses = statuses;
    }

    public void setGlowing(boolean glowing) {
        this.glowing = glowing;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setRarity(ItemRarityType rarity) {
        this.rarity = rarity;
    }

    public void setProper(List<SkillClass> proper) {
        this.proper = proper;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setNecLevel(int necLevel) {
        this.necLevel = necLevel;
    }

    public void setMaxDurabilityValue(int maxDurabilityValue) {
        this.maxDurabilityValue = maxDurabilityValue;
    }

    public void setCoolTime(int coolTime) {
        this.coolTime = coolTime;
    }

    public void setCustomModelData(Integer customModelData) {
        this.customModelData = customModelData;
    }

    public void setEquipmentType(EquipmentMap.EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
    }

    public void setCombo(List<Integer> combo) {
        this.combo = combo;
    }

    public void setReach(int reach) {
        this.reach = reach;
    }

    public ItemBase(String id, Material material, String name, List<String> lore, StatusMap statuses, boolean glowing, ItemRarityType itemRarityType){
        this.id = id;
        this.material = material;
        this.displayName = name;
        this.lore = lore;
        this.glowing = glowing;
        this.statuses = statuses;
        this.rarity = itemRarityType;
    }

    public abstract ItemStack init(int count);

    public abstract ItemStack initData(ItemStack itemStack);
}
