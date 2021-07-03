package com.github.tanokun.tanorpg.player.quest.task;

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
}
