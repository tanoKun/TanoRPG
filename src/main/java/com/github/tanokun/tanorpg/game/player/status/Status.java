package com.github.tanokun.tanorpg.game.player.status;

public class Status {
    private StatusType statusType;
    private int level;

    public Status(StatusType statusType, int lvl){this.statusType = statusType; this.level = lvl;}

    public int getLevel() {
        return level;
    }
    public StatusType getStatusType() {return statusType;}
    public void setLevel(int level) {this.level = level;}
    public void addLevel(int level) {this.level += level;}
}