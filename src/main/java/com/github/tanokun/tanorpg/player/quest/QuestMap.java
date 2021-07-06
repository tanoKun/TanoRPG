package com.github.tanokun.tanorpg.player.quest;

import com.github.tanokun.tanorpg.TanoRPG;
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
    private ArrayList<QuestData> clearQuests = new ArrayList<>();

    private ArrayList<QuestData> quests = new ArrayList<>();

    private MapNode<String, Integer> activeQuest = new MapNode<>(null, 0);

    private boolean isAction = false;

    public ArrayList<QuestData> getClearQuests() {
        return clearQuests;
    }

    public ArrayList<String> getClearQuestNames() {
        ArrayList<String> names = new ArrayList<>();
        clearQuests.forEach(c -> names.add(c.getName()));
        return names;
    }

    public ArrayList<QuestData> getQuests() {
        return quests;
    }

    public QuestData getQuest(String title) {
        for (QuestData q : quests){
            if (q.getName().equals(title)) return q;
        }
        return null;
    }

    public QuestData getActiveQuest() {
        for (QuestData q : quests){
            if (q.getName().equals(activeQuest.getKey())) return q;
        }
        return null;
    }

    public void removeClearQuest(String title){
        clearQuests.remove(getQuest(title));
    }

    public void removeQuest(String title){
        quests.remove(getQuest(title));
    }

    public void addClearQuest(QuestData quest){
        clearQuests.add(quest);
    }

    public boolean addQuest(QuestData quest){
        if (quests.size() >= 5) return false;
        quests.add(quest);
        return true;
    }

    public void setActiveQuest(QuestData activeQuest) {
        this.activeQuest = new MapNode<>(activeQuest.getName(), activeQuest.getNpcId());
    }

    public void setActiveQuest(MapNode<String, Integer> mapNode) {
        this.activeQuest = mapNode;
    }

    public void setAction(boolean action) {
        isAction = action;
    }

    public boolean isAction() {
        return isAction;
    }

    @Override
    public void save(Config config, String key) {
        Gson gson = new Gson();
        config.getConfig().set(key + "quest.activeQuest", Coding.encode(gson.toJson(activeQuest)));
        for (QuestData quest : quests){
            config.getConfig().set(key + "quest.orderQuest." + quest.getName() + ".npcId", quest.getNpcId());
            for (TaskData task : quest.getTasks()){
                config.getConfig().set(key + "quest.orderQuest." + quest.getName() + ".task." + task.getName() + ".name", task.getName());
                config.getConfig().set(key + "quest.orderQuest." + quest.getName() + ".task." + task.getName() + ".type", task.getTask().getTaskType().name());
                config.getConfig().set(key + "quest.orderQuest." + quest.getName() + ".task." + task.getName() + ".value", task.getValue());
            }
        }

        for (QuestData quest : clearQuests){
            config.getConfig().set(key + "quest.clearQuest." + quest.getName() + ".npcId", quest.getNpcId());
            for (TaskData task : quest.getTasks()){
                config.getConfig().set(key + "quest.clearQuest." + quest.getName() + ".task." + task.getName() + ".name", task.getName());
                config.getConfig().set(key + "quest.clearQuest." + quest.getName() + ".task." + task.getName() + ".type", task.getTask().getTaskType().name());
                config.getConfig().set(key + "quest.clearQuest." + quest.getName() + ".task." + task.getName() + ".value", task.getValue());
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
            if (config.getConfig().getConfigurationSection(key + "quest.orderQuest") != null) {
                for (String name : config.getConfig().getConfigurationSection(key + "quest.orderQuest").getKeys(false)) {
                    int npcId = config.getConfig().getInt(key + "quest.orderQuest." + name + ".npcId");
                    ArrayList<TaskData> taskData = new ArrayList<>();

                    for (String taskName : config.getConfig().getConfigurationSection(key + "quest.orderQuest." + name + ".task").getKeys(false)) {
                        Task task = TanoRPG.getPlugin().getQuestManager().getQuest(npcId, name).getTask(config.getConfig().getString(key + "quest.orderQuest." + name + ".task." + taskName + ".name"));
                        taskData.add(new TaskData(task, task.castValue(config.getConfig().get(key + "quest.orderQuest." + name + ".task." + taskName + ".value"))));

                    }

                    quests.add(new QuestData(npcId, name, taskData));

                }
            }
            if (config.getConfig().getConfigurationSection(key + "quest.clearQuest") != null) {
                for (String name : config.getConfig().getConfigurationSection(key + "quest.clearQuest").getKeys(false)) {
                    int npcId = config.getConfig().getInt(key + "quest.clearQuest." + name + ".npcId");
                    ArrayList<TaskData> taskData = new ArrayList<>();

                    for (String taskName : config.getConfig().getConfigurationSection(key + "quest.clearQuest." + name + ".task").getKeys(false)) {
                        Task task = TanoRPG.getPlugin().getQuestManager().getQuest(npcId, name).getTask(config.getConfig().getString(key + "quest.clearQuest." + name + ".task." + taskName + ".name"));
                        taskData.add(new TaskData(task, task.castValue(config.getConfig().get(key + "quest.clearQuest." + name + ".task." + taskName + ".value"))));

                    }

                    clearQuests.add(new QuestData(npcId, name, taskData));

                }
            }
        } catch (Exception e){
            quests.addAll(backup);
            e.printStackTrace();
            return this;
        }

        return this;
    }
}
