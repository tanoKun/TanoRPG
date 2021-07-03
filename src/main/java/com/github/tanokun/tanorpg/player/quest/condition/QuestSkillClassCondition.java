package com.github.tanokun.tanorpg.player.quest.condition;

import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.player.skill.SkillClass;
import com.github.tanokun.tanorpg.util.io.Config;

import java.util.ArrayList;
import java.util.HashSet;

public class QuestSkillClassCondition implements Condition {
    private HashSet<SkillClass> skillClasses = new HashSet<>();

    public QuestSkillClassCondition(Config config){
        config.getConfig().getList("conditions.SKILL_CLASS", new ArrayList<String>()).stream()
                .forEach(s -> skillClasses.add(SkillClass.valueOf(String.valueOf(s))));
    }

    @Override
    public boolean execute(Member m) {
        return (skillClasses.contains(m.getSkillClass()));
    }
}
