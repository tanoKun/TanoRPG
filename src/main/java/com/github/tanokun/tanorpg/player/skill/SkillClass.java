package com.github.tanokun.tanorpg.player.skill;

public enum SkillClass {

        HUNTER("ハンター", 0, ""),
    ARCHER("§c§lアーチャー", 100, 110, 35, 50, 10, 50, 3, 0, 10,  "§f遠距離型で攻撃力が高く、足が速い", "§fその代わりに、耐久性があまりない。"),

        GLADIATOR("グラディエーター", 0, ""),
    SOLDIER("§b§lソルジャー", 150, 80, 30, 0, 23, 10, 5, 0, 0, "§fバランス型で扱いやすい。"),
        KNIGHT("ナイト", 0, ""),

        WIZARD("ウィザード", 0, ""),
    MAGE("§d§lメイジ", 80, 150,10, 41, 9, 10, 5, 0, 0, "§f遠距離型で体力は低いが攻撃に特化している。"),
        ARC_MAGE("アークメイジ", 0, ""),

    PRIEST("§6§lプリースト", 90, 160,0, 28, 18, 30, 8, 0, 0, "§f支援型で重宝されるが、少し攻撃は苦手。");

    public final String NAME;

    public final String[] desc;

    public final int MAX_HP;
    public final int MAX_MP;

    public final int ATK;
    public final int MATK;
    public final int DEF;
    public final int Critical;
    public final int CriticalRate;
    public final int SUS;

    public final int walkSpeedPer;

    public final int count;

    SkillClass(String name, int maxHP, int maxMP, int ATK, int MATK, int DEF, int Critical, int CriticalRate, int SUS, int walkSpeedPer, String... desc){
        NAME = name;
        this.desc = desc;

        MAX_HP = maxHP;
        MAX_MP = maxMP;
        this.ATK = ATK;
        this.MATK = MATK;
        this.DEF = DEF;
        this.Critical = Critical;
        this.CriticalRate = CriticalRate;
        this.SUS = SUS;
        this.walkSpeedPer = walkSpeedPer;

        count = 1;
    }

    SkillClass(String name, int walkSpeedPer, String... desc){
        NAME = name;
        this.desc = desc;

        MAX_HP = 0;
        MAX_MP = 0;
        ATK = 0;
        MATK = 0;
        DEF = 0;
        Critical = 0;
        CriticalRate = 0;
        SUS = 0;
        this.walkSpeedPer = walkSpeedPer;

        count = 2;
    }
}
