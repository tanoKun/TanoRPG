package com.github.tanokun.tanorpg.player.quest.task;

public enum TaskType {
    NONE(null),
    ENTITY_KILL(QuestEntityKillTask.class),
    LEVEL_UP(QuestLevelUpTask.class),
    CRAFT(QuestCraftTask.class),
    SHOP(QuestShopBuyTask.class),
    TALK_TO_NPC(QuestTalkToNpcTask.class),
    ;

    private Class<? extends Task> task;

    TaskType(Class<? extends Task> task) {
        this.task = task;
    }

    public Class<? extends Task> getTask() {
        return task;
    }
}
