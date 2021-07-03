package com.github.tanokun.tanorpg.player.quest;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.player.quest.task.Task;
import com.github.tanokun.tanorpg.player.quest.task.TaskData;

import java.util.ArrayList;
        import java.util.UUID;

public class QuestData {
    private final int npcId;

    private final String name;

    private final ArrayList<TaskData> tasks;

    public QuestData(int npcId, String name, ArrayList<TaskData> tasks) {
        this.npcId = npcId;
        this.name = name;
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
}
