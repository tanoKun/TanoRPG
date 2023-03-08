package com.github.tanokun.tanorpg.game.item;


public enum ItemRarityType {
    COMMON("§fCommon", "§f", 255, 255, 255, "COMMON"),
    UN_COMMON("§8unCommon", "§8", 110, 110, 110, "UN_COMMON"),
    RARE("§eRare", "§e", 255, 255, 100, "RARE"),
    SUPER_RARE("§6SuperRare", "§6", 255, 180, 0, "SUPER_RARE"),
    LEGEND("§5Legend", "§5", 255, 0, 255, "LEGEND");

    private String name;
    private final String type;
    private String color;
    private int red;
    private int green;
    private int blue;

    private ItemRarityType(String name, String color, int r, int g, int b, String type) {
        this.name = name;
        this.type = type;
        this.color = color;
        red = r;
        green = g;
        blue = b;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getType() {
        return type;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }
}