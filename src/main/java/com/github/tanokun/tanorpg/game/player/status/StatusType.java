package com.github.tanokun.tanorpg.game.player.status;

import java.util.ArrayList;
import java.util.Arrays;

public enum StatusType {
    NONE(""),
    ATK("物理攻撃力"),
    DEF("物理耐性"),
    MATK("魔撃力"),
    MDEF("魔耐性"),
    AGI("俊敏"),
    ING("巧妙"),
    INT("知性"),
    //特殊
    HP("最大体力"),
    MP("最大マナ"),
    CT_ATK("攻撃速度上昇"),
    CT_SKILL("スキル詠唱速度上昇"),
    ;

    private String name;
    StatusType(String name){this.name = name;}

    public String getName() {return name;}

    public static ArrayList<StatusType> getBasicTypes(){
        return new ArrayList<StatusType>(Arrays.asList(ATK, DEF, MATK, MDEF, AGI, ING, INT));
    }
}
