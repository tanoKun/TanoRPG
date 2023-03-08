package com.github.tanokun.tanorpg.player.status;

public enum KindOfStatusType {
    NORMAL("§f§l◆ §r"),
    RUNE("§b§l◆ §r"),
    EVOLUTION("§d§l◆ §r");

    private final String star;

    KindOfStatusType(String s) {
        star = s;
    }

    public String getStar() {
        return star;
    }

    @Override
    public String toString() {
        return star;
    }
}
