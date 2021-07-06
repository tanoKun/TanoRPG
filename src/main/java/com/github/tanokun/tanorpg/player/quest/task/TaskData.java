package com.github.tanokun.tanorpg.player.quest.task;

import java.util.Objects;

public class TaskData<V> {
    private V value;

    private String name;

    private Task task;

    public TaskData(Task task){
        name = task.getMessage();
        this.task = task;
    }

    public TaskData(Task task, V value){
        name = task.getMessage();
        this.task = task;
        this.value = value;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public Task getTask() {
        return task;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskData<?> taskData = (TaskData<?>) o;
        return Objects.equals(value, taskData.value) && Objects.equals(name, taskData.name) && Objects.equals(task, taskData.task);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, name, task);
    }
}
