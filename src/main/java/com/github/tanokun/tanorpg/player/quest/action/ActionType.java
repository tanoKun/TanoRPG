package com.github.tanokun.tanorpg.player.quest.action;

public enum ActionType {
    SEND(QuestSendMessageAction.class),
    SOUND(QuestSoundAction.class),
    WAIT(QuestWaitAction.class),
    ;

    private Class<? extends Action> action;

    ActionType(Class<? extends Action> action) {
        this.action = action;
    }

    public Class<? extends Action> getAction() {
        return action;
    }
}
