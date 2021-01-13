package com.github.tanokun.tanorpg.game.player.status;

public class Status {
    private StatusType statusType;
    private double level;

    public Status(StatusType statusType, double lvl){this.statusType = statusType; this.level = lvl;}

    public double getLevel() {
        return level;
    }
    public StatusType getStatusType() {return statusType;}
    public void setLevel(double level) {this.level = level;}
    public void addLevel(double level) {this.level += level;}
}