package com.github.tanokun.tanorpg.game.player.status.buff;

public enum BuffType {
    ATK_UP_S("攻撃力上昇「小」", 10),
    ATK_UP_M("攻撃力上昇「中」", 20),
    ATK_UP_L("攻撃力上昇「大」", 30),

    SKILL_COOL_TIME_15("スキルCT減少「15%」", 0.15),
    SKILL_COOL_TIME_25("スキルCT減少「25%」", 0.25),


    NONE("none", 0),
    ;

    private String name;
    private double percent;

    BuffType(String s, double percent) {name = s; this.percent = percent;}
    public String getName() {return name;}
    public double getPercent() {return percent;}
}
