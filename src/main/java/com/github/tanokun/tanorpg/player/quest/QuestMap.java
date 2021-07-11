package com.github.tanokun.tanorpg.player.quest;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.player.quest.task.Task;
import com.github.tanokun.tanorpg.player.quest.task.TaskData;
import com.github.tanokun.tanorpg.util.SaveMarker;
import com.github.tanokun.tanorpg.util.io.Coding;
import com.github.tanokun.tanorpg.util.io.Config;
import com.github.tanokun.tanorpg.util.io.MapNode;
import com.google.gson.Gson;
import org.apache.commons.lang.time.DateUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class QuestMap implements SaveMarker<QuestMap> {
    private HashMap<String, QuestData> clearQuests = new HashMap<>();

    private HashMap<String, QuestData> quests = new HashMap<>();

    private MapNode<String, Integer> activeQuest = new MapNode<>(null, 0);

    private boolean isAction = false;

    public ArrayList<QuestData> getClearQuests() {
        return new ArrayList<>(clearQuests.values());
    }

    public ArrayList<String> getClearQuestNames() {
        ArrayList<String> names = new ArrayList<>();
        clearQuests.values().forEach(c -> names.add(c.getName()));
        return names;
    }

    public ArrayList<QuestData> getQuests() {
        return new ArrayList<>(quests.values());
    }

    public QuestData getQuest(String title) {
        for (QuestData q : quests.values()){
            if (q.getName().equals(title)) return q;
        }
        return null;
    }

    public QuestData getActiveQuest() {
        for (QuestData q : quests.values()){
            if (q.getName().equals(activeQuest.getKey())) return q;
        }
        return null;
    }

    public QuestData getClearQuest(String title) {
        for (QuestData q : clearQuests.values()){
            if (q.getName().equals(title)) return q;
        }
        return null;
    }

    public void removeClearQuest(String title){
        System.out.println(clearQuests.remove(title));
    }

    public void removeQuest(String title){
        quests.remove(title);
    }

    public void addClearQuest(QuestData quest){
        quest.setClearTime(LocalDateTime.now());
        clearQuests.put(quest.getName(), quest);
    }

    public boolean addQuest(QuestData quest){
        if (quests.size() >= 5) return false;
        quests.put(quest.getName(), quest);
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
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        Gson gson = new Gson();
        config.getConfig().set(key + "quest.activeQuest", Coding.encode(gson.toJson(activeQuest)));
        for (QuestData quest : quests.values()){
            config.getConfig().set(key + "quest.orderQuest." + quest.getName() + ".npcId", quest.getNpcId());
            for (TaskData task : quest.getTasks()){
                config.getConfig().set(key + "quest.orderQuest." + quest.getName() + ".task." + task.getName() + ".name", task.getName());
                config.getConfig().set(key + "quest.orderQuest." + quest.getName() + ".task." + task.getName() + ".type", task.getTask().getTaskType().name());
                config.getConfig().set(key + "quest.orderQuest." + quest.getName() + ".task." + task.getName() + ".value", task.getValue());
            }
        }

        config.getConfig().set(key + "quest.clearQuest", null);
        for (QuestData quest : clearQuests.values()){
            config.getConfig().set(key + "quest.clearQuest." + quest.getName() + ".npcId", quest.getNpcId());
            config.getConfig().set(key + "quest.clearQuest." + quest.getName() + ".time", quest.getClearTime().format(format));
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
        ArrayList<QuestData> backup = new ArrayList<>(quests.values());
        quests.clear();

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        Gson gson = new Gson();

        activeQuest = gson.fromJson(Coding.decode(config.getConfig().getString(key + "quest.activeQuest")), MapNode.class);
        if (config.getConfig().getConfigurationSection(key + "quest.orderQuest") != null) {
            for (String name : config.getConfig().getConfigurationSection(key + "quest.orderQuest").getKeys(false)) {
                int npcId = config.getConfig().getInt(key + "quest.orderQuest." + name + ".npcId");
                ArrayList<TaskData> taskData = new ArrayList<>();
                for (String taskName : config.getConfig().getConfigurationSection(key + "quest.orderQuest." + name + ".task").getKeys(false)) {
                    Task task = TanoRPG.getPlugin().getQuestManager().getQuest(npcId, name).getTask(config.getConfig().getString(key + "quest.orderQuest." + name + ".task." + taskName + ".name"));
                    taskData.add(new TaskData(task, task.castValue(config.getConfig().get(key + "quest.orderQuest." + name + ".task." + taskName + ".value"))));
                }
                QuestData data = new QuestData(npcId, name, taskData);
                quests.put(data.getName(), data);
            }
        }

        if (config.getConfig().getConfigurationSection(key + "quest.clearQuest") != null) {
            for (String name : config.getConfig().getConfigurationSection(key + "quest.clearQuest").getKeys(false)) {
                int npcId = config.getConfig().getInt(key + "quest.clearQuest." + name + ".npcId");
                LocalDateTime localDateTime = LocalDateTime.parse(config.getConfig().getString(key + "quest.clearQuest." + name + ".time"), format);
                ArrayList<TaskData> taskData = new ArrayList<>();
                for (String taskName : config.getConfig().getConfigurationSection(key + "quest.clearQuest." + name + ".task").getKeys(false)) {
                    Task task = TanoRPG.getPlugin().getQuestManager().getQuest(npcId, name).getTask(config.getConfig().getString(key + "quest.clearQuest." + name + ".task." + taskName + ".name"));
                    taskData.add(new TaskData(task, task.castValue(config.getConfig().get(key + "quest.clearQuest." + name + ".task." + taskName + ".value"))));
                }
                QuestData questData = new QuestData(npcId, name, taskData);
                questData.setClearTime(localDateTime);
                clearQuests.put(questData.getName(), questData);
            }
        }

        return this;
    }
}
