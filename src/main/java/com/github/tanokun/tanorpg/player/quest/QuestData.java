package com.github.tanokun.tanorpg.player.quest;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.player.quest.task.TaskData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class QuestData {
    private final int npcId;

    private final String name;

    private final ArrayList<TaskData> tasks;

    private boolean isClearTasks = false;

    private LocalDateTime clearTime;

    public QuestData(int npcId, String name, ArrayList<TaskData> tasks) {
        this.npcId = npcId;
        this.name = name;
        this.tasks = tasks;
    }

    public QuestData(Quest quest) {
        this.npcId = quest.getNpcID();
        this.name = quest.getName();
        ArrayList<TaskData> tasks = new ArrayList<>(); quest.getTasks().forEach(task -> tasks.add(task.getTaskData()));
        this.tasks = tasks;
    }

    public int getNpcId() {
        return npcId;
    }

    public ArrayList<TaskData> getTasks() {
        return tasks;
    }

    public TaskData getTaskData(String name){
        for (TaskData t : tasks){
            if (t.getName().equals(name)) return t;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public Quest getQuest(){
        return TanoRPG.getPlugin().getQuestManager().getQuest(npcId, name);
    }

    public LocalDateTime getClearTime() {
        return clearTime;
    }

    public boolean isClear(){
        for (TaskData taskData : tasks){
            if (!taskData.getTask().isClearTask(taskData.getValue())) return false;
        }
        return true;
    }

    public void setClearTasks(boolean clearTasks) {
        isClearTasks = clearTasks;
    }

    public boolean isClearTasks() {
        return isClearTasks;
    }

    public void setClearTime(LocalDateTime clearTime) {
        this.clearTime = clearTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestData questData = (QuestData) o;
        return npcId == questData.npcId && isClearTasks == questData.isClearTasks && Objects.equals(name, questData.name) && Objects.equals(tasks, questData.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(npcId, name, tasks, isClearTasks);
    }
}
