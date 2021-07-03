package com.github.tanokun.tanorpg.player;

public class Attack {
    private int attackCombo = 0;

    private int nextAttackCombo = 0;

    private int lastAttackTicks = 0;

    private boolean attackWait = false;

    public int getAttackCombo() {
        return attackCombo;
    }

    public int getNextAttackCombo() {
        return nextAttackCombo;
    }

    public int getLastAttackTicks() {
        return lastAttackTicks;
    }

    public boolean isAttackWait() {
        return attackWait;
    }

    public void setAttackCombo(int attackCombo) {
        this.attackCombo = attackCombo;
    }

    public void setNextAttackCombo(int nextAttackCombo) {
        this.nextAttackCombo = nextAttackCombo;
    }

    public int nextAttackCombo() {
        this.attackCombo++;
        return attackCombo;
    }

    public void setLastAttackTicks(int lastAttackTicks) {
        this.lastAttackTicks = lastAttackTicks;
    }

    public int nextLastAttackTicks() {
        this.lastAttackTicks++;
        return this.lastAttackTicks;
    }

    public void setAttackWait(boolean attackWait) {
        this.attackWait = attackWait;
    }
}
