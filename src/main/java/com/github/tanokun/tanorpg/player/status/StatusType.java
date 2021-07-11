package com.github.tanokun.tanorpg.player.status;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum StatusType {
    NONE(null),
    ATK("基礎攻撃力", Material.IRON_SWORD, "§c"),
    MATK("基礎魔術力", Material.BLAZE_ROD, "§d"),
    DEF("基礎防御力", Material.IRON_CHESTPLATE, "§f"),
    CRITICAL_RATE("会心率", "%", Material.COMPASS, "§e"),
    CRITICAL("会心ダメージ", "%", Material.DIAMOND_SWORD, "§c§l"),
    SUS("自己理解率", "%", Material.BOOK, "§9"),
    HP("最大体力"),
    MP("最大マナ", Material.BLUE_DYE, "§b"),
    SPEED("スピードバフ", "%"),
    LUCKY_ITEM("ドロップ増加", "%")
    ;

    private String name;

    private String end = "";

    private Material material = Material.AIR;

    private String first = "";

    StatusType(String name){this.name = name;}

    StatusType(String name, Material material, String first){this.name = name; this.material = material; this.first = first;}

    StatusType(String name, String end){this.name = name; this.end = end;}

    StatusType(String name, String end, Material material, String first){this.name = name; this.end = end; this.material = material; this.first = first;}

    public String getName() {return name;}

    public String getEnd() {return end;}

    public Material getMaterial() {
        return material;
    }

    public String getFirst() {
        return first;
    }

    public static List<StatusType> getNotBasicStatus(){
        List<StatusType> list = new ArrayList<>(Arrays.asList(StatusType.values()));
        list.remove(StatusType.HP);
        list.remove(StatusType.MP);
        list.remove(StatusType.ATK);
        list.remove(StatusType.MATK);
        list.remove(StatusType.DEF);
        list.remove(StatusType.CRITICAL);
        list.remove(StatusType.CRITICAL_RATE);
        list.remove(StatusType.SUS);
        list.remove(StatusType.NONE);
        return list;
    }

    public static List<StatusType> getBasicStatus(){
        return Arrays.asList(
                StatusType.HP, StatusType.MP, StatusType.ATK, StatusType.MATK, StatusType.DEF, StatusType.CRITICAL_RATE, StatusType.CRITICAL, StatusType.SUS);
    }
}
