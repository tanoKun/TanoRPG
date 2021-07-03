package com.github.tanokun.tanorpg.player.quest.task;

import com.github.tanokun.tanorpg.util.command.CommandContext;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class Task<V> {
    protected V max;
    private String message;
    private final TaskType taskType;

    public Task(TaskType taskType1){
        this.taskType = taskType1;
    }

    public String getMessage() {return message;}

    abstract public String getMessage(boolean b);

    public V getMax() {
        return max;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setMessage(String message) {this.message = message;}

    public void setMax(V max) {
        this.max = max;
    }

    abstract public boolean isClearTask(V value);

    public abstract V castValue(Object o);
}
