package com.github.tanokun.tanorpg.player.quest;

import com.github.tanokun.tanorpg.player.quest.task.Task;
import com.github.tanokun.tanorpg.player.quest.task.TaskData;
import com.github.tanokun.tanorpg.player.quest.task.TaskType;
import com.github.tanokun.tanorpg.util.SaveMarker;
import com.github.tanokun.tanorpg.util.io.Coding;
import com.github.tanokun.tanorpg.util.io.Config;
import com.github.tanokun.tanorpg.util.io.MapNode;
import com.google.gson.Gson;

import java.util.ArrayList;

public class QuestMap implements SaveMarker<QuestMap> {
    private ArrayList<QuestData> quests = new ArrayList<>();

    private MapNode<String, Integer> activeQuest = new MapNode<>(null, 0);

    private boolean isAction = false;

    public ArrayList<QuestData> getQuests() {
        return quests;
    }

    public QuestData getQuest(String title) {
        for (QuestData q : quests){
            if (q.getName().equals(title)) return q;
        }
        return null;
    }

    public void removeQuest(String title){
        quests.remove(getQuest(title));
    }

    public boolean addQuest(QuestData quest){
        if (quests.size() >= 5) return false;
        quests.add(quest);
        return true;
    }

    public QuestData getActiveQuest() {
        for (QuestData q : quests){
            if (q.getName().equals(activeQuest.getKey())) return q;
        }
        return null;
    }

    public void setActiveQuest(QuestData activeQuest) {
        this.activeQuest = new MapNode<>(activeQuest.getName(), activeQuest.getNpcId());
    }

    public void setAction(boolean action) {
        isAction = action;
    }

    public boolean isAction() {
        return isAction;
    }

    public boolean isClear(String name){
        return true;
    }

    @Override
    public void save(Config config, String key) {
        Gson gson = new Gson();
        config.getConfig().set(key + "quest.activeQuest", Coding.encode(gson.toJson(activeQuest)));
        for (QuestData quest : quests){
            config.getConfig().set(key + "quest.orderQuest." + quest.getName() + ".npcId", quest.getNpcId());
            for (TaskData task : quest.getTasks()){
                config.getConfig().set(key + "quest.orderQuest." + quest.getName() + ".task." + task.getName() + ".json", Coding.encode(gson.toJson(task.getTask())));
                config.getConfig().set(key + "quest.orderQuest." + quest.getName() + ".task." + task.getName() + ".type", task.getTask().getTaskType().name());
                config.getConfig().set(key + "quest.orderQuest." + quest.getName() + ".task." + task.getName() + ".value", task.getValue());
            }
        }
        config.saveConfig();
    }

    @Override
    public QuestMap load(Config config, String key) {
        ArrayList<QuestData> backup = new ArrayList<>(quests);
        quests.clear();

        Gson gson = new Gson();

        try {
            activeQuest = gson.fromJson(Coding.decode(config.getConfig().getString(key + "quest.activeQuest")), MapNode.class);
            for (String name : config.getConfig().getConfigurationSection(key + "quest.orderQuest").getKeys(false)) {
                int npcId = config.getConfig().getInt(key + "quest.orderQuest." + name + ".npcId");
                ArrayList<TaskData> taskData = new ArrayList<>();

                for (String taskName : config.getConfig().getConfigurationSection(key + "quest.orderQuest." + name + ".task").getKeys(false)){
                    Class<? extends Task> type = TaskType.valueOf(config.getConfig().getString(key + "quest.orderQuest." + name + ".task." + taskName + ".type")).getTask();
                    Task task = gson.fromJson(Coding.decode(config.getConfig().getString(key + "quest.orderQuest." + name + ".task." + taskName + ".json")), type);
                    taskData.add(new TaskData(task, task.castValue(config.getConfig().get(key + "quest.orderQuest." + name + ".task." + taskName + ".value"))));

                }

                quests.add(new QuestData(npcId, name, taskData));

            }
        } catch (Exception e){
            quests.addAll(backup);
            return this;
        }

        return this;
    }
}
