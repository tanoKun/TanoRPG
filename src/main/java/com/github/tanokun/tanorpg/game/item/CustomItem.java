package com.github.tanokun.tanorpg.game.item;

import com.github.tanokun.tanorpg.game.player.GamePlayerJobType;
import com.github.tanokun.tanorpg.game.player.status.Status;
import com.github.tanokun.tanorpg.util.Glowing;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomItem {
    private CustomItemType cit = CustomItemType.NULL;
    private String id = "null";
    private Material material = Material.STONE;
    private String name = "null";
    private ArrayList<String> lores = new ArrayList<>(Arrays.asList(" "));
    private ArrayList<Status> statuses;
    private boolean glowing = false;
    private long price = 0;
    private ArrayList<GamePlayerJobType> jobs;
    private long cooltime = 0;
    private CustomItemRarityType rarity;
    private Color color;
    private int lvl = 0;
    private Integer customModelData = null;

    public CustomItem(CustomItemType cit, String id, Material material, String name, ArrayList<String> lores, ArrayList<Status> statuses, boolean glowing){
        this.cit = cit;
        this.id = id;
        this.material = material;
        this.name = name;
        this.lores = lores;
        this.glowing = glowing;
        this.statuses = statuses;
    }
    public boolean setProper(ArrayList<GamePlayerJobType> jobs){
        if (this.cit == CustomItemType.MATERIAL){return false;}
            this.jobs = jobs;
        return true;
    }
    public void setPrice(long price){this.price = price;}
    public void setCooltime(long cooltime) {this.cooltime = cooltime;}
    public void setRarity(CustomItemRarityType rarity) {this.rarity = rarity;}
    public void setColor(Color color) {this.color = color;}
    public void setLvl(int lvl) {this.lvl = lvl;}
    public void setCustomModelData(Integer customModelData) {this.customModelData = customModelData;}

    public CustomItemType getCit() {return cit;}
    public String getId() {return id;}
    public Material getMaterial() {return material;}
    public ArrayList<String> getLores() {return lores;}
    public ArrayList<Status> getStatuses() {return statuses;}
    public String getName() {return name;}
    public ArrayList<GamePlayerJobType> getJobs() {return jobs;}
    public long getCooltime() {return cooltime;}
    public CustomItemRarityType getRarity() {return rarity;}
    public long getPrice() {return price;}
    public int getLvl() {return lvl;}

    public ItemStack getItem(){
        List<String> statuses2 = new ArrayList<>();
        for(Status status : getStatuses()){
            if (status.getLevel() > 0){
                statuses2.add("§a" + status.getStatusType().getName() + " +" + status.getLevel());
            } else {
                statuses2.add("§c" + status.getStatusType().getName() + " " + status.getLevel());
            }
        }
        ItemStack is = new ItemStack(material);
        if (material.toString().contains("LEATHER")) {
            if (color != null) {
                LeatherArmorMeta meta = (LeatherArmorMeta) is.getItemMeta();
                meta.setColor(color);
                is.setItemMeta(meta);
            }
        }
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(rarity.getColor() + name);
        List<String> lore = new ArrayList<>();
        lore.add(CustomItemManager.LORE);
        for(String lore2 : lores){lore.add(lore2);}
        lore.add(CustomItemManager.FIRST_STATUS);
        for(String lore2 : statuses2){lore.add(lore2);}
        lore.add(" ");
        if (!(cit.equals(CustomItemType.MATERIAL) && !(cit.equals(CustomItemType.NULL)))){
            String proper = null;
            for (GamePlayerJobType job : jobs){
                if (proper == null){proper = job.getName();}
                else{proper = proper + ", " + job.getName();}
            }
            lore.add("§7Lv: " + lvl);
            lore.add("§7Cooltime: " + cooltime);
            lore.add("§7Proper: " + proper);}
        lore.add("§7Rarity: " + rarity.getName());
        lore.add("§7Type: " + cit.getName());
        lore.add(CustomItemManager.FINAL_STATUS);
        lore.add("§7ID: " + id);
        im.setLore(lore);
        if (glowing == true) {
            im.addEnchant(new Glowing(), 1, true);
        }
        if (customModelData != null){
            im.setCustomModelData(customModelData);
        }
        im.setUnbreakable(true);
        is.setItemMeta(im);
        return is;
    }
}
