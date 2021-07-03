package com.github.tanokun.tanorpg.game.item.type.base;

import com.github.tanokun.tanorpg.game.item.ItemRarityType;
import com.github.tanokun.tanorpg.game.item.ItemRuneInventory;
import com.github.tanokun.tanorpg.game.item.ItemType;
import com.github.tanokun.tanorpg.player.EquipmentMap;
import com.github.tanokun.tanorpg.player.skill.SkillClass;
import com.github.tanokun.tanorpg.player.status.StatusMap;
import org.bukkit.Material;

import java.util.List;

public class ItemData {
    private String id;

    private String displayName;

    private ItemType itemType;

    private Material material;

    private List<String> lore;

    private boolean glowing;

    private long price;

    private Integer customModelData;

    private StatusMap statuses;

    private ItemRarityType rarity;

    private List<SkillClass> proper;

    private int coolTime;

    private int necLevel;

    private int maxDurabilityValue;

    private int durabilityValue;

    private int evolution;

    private ItemRuneInventory itemRuneInventory;

    private EquipmentMap.EquipmentType equipmentType;

    private List<Integer> combo;

    private int reach;

    public ItemData(ItemBase itemBase)  {
        this.itemType = itemBase.getItemType();
        this.id = itemBase.getId();
        this.displayName = itemBase.getDisplayName();
        this.material = itemBase.getMaterial();
        this.lore = itemBase.getLore();
        this.glowing = itemBase.isGlowing();
        this.price = itemBase.getPrice();
        this.customModelData = itemBase.getCustomModelData();
        this.statuses = itemBase.getBasicStatuses();
        this.rarity = itemBase.getRarity();
        this.proper = itemBase.getProper();
        this.coolTime = itemBase.getCoolTime();
        this.necLevel = itemBase.getNecLevel();
        this.maxDurabilityValue = itemBase.getMaxDurabilityValue();
        this.durabilityValue = maxDurabilityValue;
        this.evolution = 0;
        this.itemRuneInventory = new ItemRuneInventory();
        this.equipmentType = itemBase.getEquipmentType();
        this.combo = itemBase.getCombo();
        this.reach = itemBase.getReach();
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public Material getMaterial() {
        return material;
    }

    public List<String> getLore() {
        return lore;
    }

    public boolean isGlowing() {
        return glowing;
    }

    public long getPrice() {
        return price;
    }

    public Integer getCustomModelData() {
        return customModelData;
    }

    public StatusMap getStatuses() {
        return statuses;
    }

    public ItemRarityType getRarity() {
        return rarity;
    }

    public List<SkillClass> getProper() {
        return proper;
    }

    public int getCoolTime() {
        return coolTime;
    }

    public int getNecLevel() {
        return necLevel;
    }

    public List<Integer> getCombo() {
        return combo;
    }

    public int getMaxDurabilityValue() {
        return maxDurabilityValue;
    }

    public int getDurabilityValue() {
        return durabilityValue;
    }

    public int getEvolution() {
        return evolution;
    }

    public ItemRuneInventory getItemRuneInventory() {
        return itemRuneInventory;
    }

    public EquipmentMap.EquipmentType getEquipmentType() {
        return equipmentType;
    }

    public int getReach() {
        return reach;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public void setGlowing(boolean glowing) {
        this.glowing = glowing;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setCustomModelData(Integer customModelData) {
        this.customModelData = customModelData;
    }

    public void setStatuses(StatusMap statuses) {
        this.statuses = statuses;
    }

    public void setRarity(ItemRarityType rarity) {
        this.rarity = rarity;
    }

    public void setProper(List<SkillClass> proper) {
        this.proper = proper;
    }

    public void setCoolTime(int coolTime) {
        this.coolTime = coolTime;
    }

    public void setNecLevel(int necLevel) {
        this.necLevel = necLevel;
    }

    public void setMaxDurabilityValue(int maxDurabilityValue) {
        this.maxDurabilityValue = maxDurabilityValue;
    }

    public void setDurabilityValue(int durabilityValue) {
        this.durabilityValue = durabilityValue;
    }

    public void setEvolution(int evolution) {
        this.evolution = evolution;
    }

    public void setItemRuneInventory(ItemRuneInventory itemRuneInventory) {
        this.itemRuneInventory = itemRuneInventory;
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
}
