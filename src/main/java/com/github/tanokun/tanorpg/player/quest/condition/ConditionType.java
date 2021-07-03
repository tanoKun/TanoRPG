package com.github.tanokun.tanorpg.player.quest.condition;

public enum ConditionType {
    LEVEL(QuestLevelCondition.class),
    SKILL_CLASS(QuestSkillClassCondition.class),
    CLEARED(QuestClearCondition.class),
    ;

    private Class<? extends Condition> condition;

    ConditionType(Class<? extends Condition> condition) {
        this.condition = condition;
    }

    public Class<? extends Condition> getCondition() {
        return condition;
    }
}
