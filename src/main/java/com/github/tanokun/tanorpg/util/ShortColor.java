package com.github.tanokun.tanorpg.util;

public enum ShortColor {
    WHITE(0),
    ORANGE(1),
    MAGENTA(2),
    LIGHT_BLUE(3),
    YELLOW(4),
    LIME(5),
    PINK(6),
    GRAY(7),
    SILVER(8),
    CYAN(9),
    PURPLE(10),
    BLUE(11),
    BROWN(12),
    GREEN(13),
    RED(14),
    BLACK(15);

    private short i;
    private ShortColor(int i) {this.i = (short) i;}

    public short getColor() {return i;}
}
