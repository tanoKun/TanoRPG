package com.github.tanokun.tanorpg.game.player;

import com.github.tanokun.tanorpg.game.player.status.Status;
import com.github.tanokun.tanorpg.game.player.status.StatusType;

import java.util.HashMap;

public enum GamePlayerJobType {
    MAGE("メイジ", 0, 50, 0, 0, 10, 10, 0, 0, 3),
    WARRIOR("ウォーリア", 20, 0, 10, 10, 0, 0, 0, 0, 3),
    PRIEST("プリースト", 0, 50, 0, 0, 10, 10, 0, 0, 3);
    private String name;
    private HashMap<StatusType, Status> statuses = new HashMap<>();
    private int HP;
    private int MP;
    private GamePlayerJobType(String name, int HP, int MP, int ATK, int DEF, int MATK, int MDEF, int AGI, int ING, int INT){
        this.name = name;
        this.HP = HP;
        this.MP = MP;
        statuses.put(StatusType.ATK, new Status(StatusType.ATK, ATK));
        statuses.put(StatusType.DEF, new Status(StatusType.DEF, DEF));
        statuses.put(StatusType.MATK, new Status(StatusType.MATK, MATK));
        statuses.put(StatusType.MDEF, new Status(StatusType.MDEF, MDEF));
        statuses.put(StatusType.AGI, new Status(StatusType.AGI, AGI));
        statuses.put(StatusType.ING, new Status(StatusType.ING, ING));
        statuses.put(StatusType.INT, new Status(StatusType.INT, INT));
    }

    public int getHP() {
        return HP;
    }
    public int getMP() {
        return MP;
    }

    public HashMap<StatusType, Status> getStatuses() {return statuses;}

    public String getName() {return name;}
}