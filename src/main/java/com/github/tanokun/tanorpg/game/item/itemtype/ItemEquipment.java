package com.github.tanokun.tanorpg.game.item.itemtype;

import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.game.item.ItemType;
import com.github.tanokun.tanorpg.game.item.itemtype.base.Item;
import com.github.tanokun.tanorpg.game.item.itemtype.base.ItemJob;
import com.github.tanokun.tanorpg.game.player.GamePlayerJobType;
import com.github.tanokun.tanorpg.game.player.status.Status;
import com.github.tanokun.tanorpg.util.Glowing;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemEquipment extends Item implements ItemJob {
    private final ItemType itemType = ItemType.EQUIPMENT;

    private ArrayList<GamePlayerJobType> jobs;
    private int lvl = 0;
    private Color color;

    public ItemEquipment(String id, Material material, String name, List<String> lores, List<Status> statuses, boolean glowing) {
        super(id, material, name, lores, statuses, glowing);
    }

    public ArrayList<GamePlayerJobType> getJobs() {return jobs;}
    public long getCoolTime() {return 0;}
    public int getLvl() {return lvl;}
    public Color getColor() {return color;}

    public void setJobs(ArrayList<GamePlayerJobType> jobs) {this.jobs = jobs;}
    public void setCoolTime(long coolTime) {}
    public void setLvl(int lvl) {this.lvl = lvl;}
    public void setColor(Color color) {this.color = color;}

    @Override
    public ItemStack getItem() {
        List<String> statuses2 = new ArrayList<>();
        for(Status status : getStatuses()){
            if (status.getLevel() > 0){
                statuses2.add("§a" + status.getStatusType().getName() + " +" + status.getLevel());
            } else {
                statuses2.add("§c" + status.getStatusType().getName() + " " + status.getLevel());
            }
        }
        ItemStack is = new ItemStack(getMaterial());
        if (getMaterial().toString().contains("LEATHER")) {
            if (color != null) {
                LeatherArmorMeta meta = (LeatherArmorMeta) is.getItemMeta();
                meta.setColor(color);
                is.setItemMeta(meta);
            }
        }
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(getRarity().getColor() + getName());
        List<String> lore = new ArrayList<>();
        lore.add(ItemManager.LORE);
        for(String lore2 : getLore()){lore.add(lore2);}
        lore.add(ItemManager.FIRST_STATUS);
        for(String lore2 : statuses2){lore.add(lore2);}
        lore.add(" ");
        String proper = null;
        for (GamePlayerJobType job : jobs){
            if (proper == null){proper = job.getName();}
            else{proper = proper + ", " + job.getName();}
        }
        lore.add("§7Lv: " + lvl);
        lore.add("§7Proper: " + proper);
        lore.add("§7Rarity: " + getRarity().getName());
        lore.add("§7Type: " + itemType.getName());
        lore.add(ItemManager.FINAL_STATUS);
        lore.add("§7ID: " + getId());
        im.setLore(lore);
        if (isGlowing()) {
            im.addEnchant(new Glowing(), 1, true);
        }
        if (getCustomModelData() != 0){
            im.setCustomModelData(getCustomModelData());
        }
        im.setUnbreakable(true);
        is.setItemMeta(im);
        return is;
    }
}
