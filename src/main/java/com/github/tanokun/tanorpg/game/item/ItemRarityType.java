package com.github.tanokun.tanorpg.game.item;


public enum ItemRarityType {
    COMMON("§fCommon", "§f", 255, 255, 255),
    UN_COMMON("§8unCommon", "§8", 110, 110, 110),
    RARE("§eRare", "§e", 255, 255, 100),
    SUPER_RARE("§6SuperRare", "§6", 255, 180, 0),
    LEGEND("§5Legend", "§5", 255, 0, 255);

    private String name;
    private String color;
    private int red;
    private int green;
    private int blue;

    private ItemRarityType(String name, String color, int r, int g, int b){
        this.name = name;
        this.color = color;
        red = r;
        green = g;
        blue = b;
    }
    public String getName() {return name;}
    public String getColor() {return color;}

    public int getRed() {return red;}
    public int getGreen() {return green;}
    public int getBlue() {return blue;}
}
