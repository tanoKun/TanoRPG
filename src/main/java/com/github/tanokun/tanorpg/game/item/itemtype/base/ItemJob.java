package com.github.tanokun.tanorpg.game.item.itemtype.base;

import com.github.tanokun.tanorpg.game.player.GamePlayerJobType;

import java.util.ArrayList;

public interface ItemJob {
    ArrayList<GamePlayerJobType> getJobs();
    long getCoolTime();
    int getLvl();
    int getMaxDurabilityValue();

    void setJobs(ArrayList<GamePlayerJobType> jobs);
    void setCoolTime(long coolTime);
    void setLvl(int lvl);
    void setMaxDurabilityValue(int durabilityValue);
}
