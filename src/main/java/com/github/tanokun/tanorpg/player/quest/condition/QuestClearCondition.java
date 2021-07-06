package com.github.tanokun.tanorpg.player.quest.condition;

import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.util.io.Config;

import java.util.ArrayList;
import java.util.HashSet;

public class QuestClearCondition implements Condition {
    private HashSet<String> quests = new HashSet<>();

    public QuestClearCondition(Config config){
        config.getConfig().getList("conditions.CLEARED", new ArrayList<String>()).stream()
                .forEach(s -> quests.add(String.valueOf(s)));
    }

    @Override
    public boolean execute(Member m) {

        for (String s : quests) {
            if (!m.getQuestMap().getClearQuestNames().contains(s)) return false;
        }

        return true;
    }
}
