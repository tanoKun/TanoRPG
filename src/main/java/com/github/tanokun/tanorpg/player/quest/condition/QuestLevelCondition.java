package com.github.tanokun.tanorpg.player.quest.condition;

import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.util.io.Config;

public class QuestLevelCondition implements Condition {
    private int level;

    public QuestLevelCondition(Config config){
        this.level = config.getConfig().getInt("conditions.LEVEL", 0);
    }

    @Override
    public boolean execute(Member m) {
        return m.getHasLevel().getValue() >= level;
    }
}
