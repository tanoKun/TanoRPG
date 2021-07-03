package com.github.tanokun.tanorpg.player.quest.task;

import com.github.tanokun.tanorpg.player.quest.condition.Condition;

public enum TaskType {
    NONE(null),
    ;

    private Class<? extends Task> task;

    TaskType(Class<? extends Task> task) {
        this.task = task;
    }

    public Class<? extends Task> getTask() {
        return task;
    }
}
