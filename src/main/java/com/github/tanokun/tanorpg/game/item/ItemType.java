package com.github.tanokun.tanorpg.game.item;

public enum ItemType {
    MATERIAL("素材"),
    WEAPON("武器"),
    MAGIC_WEAPON("魔法武器"),
    EQUIPMENT("装備"),
    NULL("その他");

    private String name;

    private ItemType(String name){this.name = name;}
    public String getName() {return name;}
}
