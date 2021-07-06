package com.github.tanokun.tanorpg.player.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum StatusType {
    NONE(null),
    ATK("基礎攻撃力"),
    MATK("基礎魔術力"),
    DEF("基礎防御力"),
    CRITICAL("会心ダメージ", "%"),
    CRITICAL_RATE("会心率", "%"),
    SUS("自己理解率", "%"),
    HP("最大体力"),
    MP("最大マナ"),
    SPEED("スピードバフ", "%"),
    LUCKY_ITEM("ドロップ増加", "%")
    ;

    private String name;

    private String end = "";

    StatusType(String name){this.name = name;}

    StatusType(String name, String end){this.name = name; this.end = end;}

    public String getName() {return name;}

    public String getEnd() {return end;}

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
                StatusType.HP, StatusType.MP, StatusType.ATK, StatusType.MATK, StatusType.DEF, StatusType.CRITICAL, StatusType.CRITICAL_RATE, StatusType.SUS);
    }
}
