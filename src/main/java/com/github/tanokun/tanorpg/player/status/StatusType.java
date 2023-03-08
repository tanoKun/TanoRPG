package com.github.tanokun.tanorpg.player.status;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum StatusType {
    NONE(null, null),
    ATK("基礎攻撃力", getDec("§7「ダメージ」に関係するステータスに強く影響します"), Material.IRON_SWORD, "§c§l"),
    STR("筋力", getDec("§7「物理的ダメージ」「貫通率」に関係するステータスに影響します"), Material.RAW_IRON, "§d§l"),
    INT("知性", getDec("§7「魔法ダメージ」に関係するステータスに影響します"), Material.AMETHYST_SHARD, "§d§l"),
    DEF("基礎防御力", getDec("§7「ダメージ」を防ぐステータスに強く影響します"), Material.IRON_CHESTPLATE, "§f§l"),
    CRITICAL_RATE("会心率", getDec("§7「筋力」と繋げることでより強くなります"), "%", Material.COMPASS, "§e§l"),
    CRITICAL("会心ダメージ", getDec("§7「会心ダメージ」の倍率を高めます"), "%", Material.DIAMOND_SWORD, "§c§l"),
    SUS("自己理解率", getDec("§7自分の体を理解することで「回避率」を上げます", "「攻撃速度」にも多少影響します"), "%", Material.BOOK, "§9§l"),
    HP("最大体力", getDec("§7死ににくくなります")),
    MP("最大マナ", getDec("§7多ければ多いほどスキルを連発できます"), Material.BLUE_DYE, "§b"),

    SPEED("スピードバフ", getDec("§7足が早くなります"), "%", Material.DIAMOND_BOOTS, ""),
    LUCKY_ITEM("ドロップ増加", getDec("§7レアなアイテムが出やすくなります"), "%", Material.BEACON, ""),
    HEAL_MP("MP回復率", getDec("§7「MP」の回復量が上がります"), "%", Material.AMETHYST_CLUSTER, ""),

    DEVELOP_PERCENT("ステータス上昇", getDec(""), "%", Material.EMERALD, ""),

    ;

    private String name;

    private String end = "";

    private String[] dec;

    private Material material = Material.AIR;

    private String first = "";

    StatusType(String name, String[] dec) {
        this.name = name;
        this.dec = dec;
    }

    StatusType(String name, String[] dec, Material material, String first) {
        this.name = name;
        this.dec = dec;
        this.material = material;
        this.first = first;
    }

    StatusType(String name, String[] dec, String end) {
        this.name = name;
        this.end = end;
        this.dec = dec;
    }

    StatusType(String name, String[] dec, String end, Material material, String first) {
        this.name = name;
        this.dec = dec;
        this.end = end;
        this.material = material;
        this.first = first;
    }

    public String getName() {
        return name;
    }

    public String getEnd() {
        return end;
    }

    public Material getMaterial() {
        return material;
    }

    public String getFirst() {
        return first;
    }

    public String[] getDec() {
        return dec;
    }

    public ArrayList<String> getDecArrayList() {
        return new ArrayList<>(List.of(dec));
    }

    public static List<StatusType> getNotBasicStatus() {
        List<StatusType> list = new ArrayList<>(Arrays.asList(StatusType.values()));
        list.remove(ATK);
        list.remove(STR);
        list.remove(INT);
        list.remove(DEF);
        list.remove(CRITICAL_RATE);
        list.remove(CRITICAL);
        list.remove(SUS);
        list.remove(HP);
        list.remove(MP);
        return list;
    }

    public static List<StatusType> getBasicStatus() {
        return Arrays.asList(ATK, STR, INT, DEF, CRITICAL_RATE, CRITICAL, SUS, HP, MP);
    }

    private static String[] getDec(String... dec) {
        return dec;
    }
}
