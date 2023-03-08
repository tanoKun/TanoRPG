package com.github.tanokun.tanorpg.game.item;

public enum ItemType {
    MATERIAL("素材", "MATERIAL"),
    WEAPON("武器", "WEAPON"),
    MAGIC_WEAPON("魔法武器", "MAGIC_WEAPON"),
    PROJECTILE_WEAPON("飛び道具", "PROJECTILE_WEAPON"),
    EQUIPMENT("装備", "EQUIPMENT"),
    RUNE("ルーン", "RUNE"),
    ACCESSORY("アクセサリー", "ACCESSORY"),
    NULL("その他", "NULL"),
    POTION("ポーション", "POTION");

    private final String name;

    private final String type;

    ItemType(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}