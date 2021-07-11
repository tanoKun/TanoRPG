package com.github.tanokun.tanorpg.player.quest;

import com.github.tanokun.tanorpg.game.item.ItemRarityType;
import com.github.tanokun.tanorpg.player.quest.action.Action;
import com.github.tanokun.tanorpg.player.quest.condition.Condition;
import com.github.tanokun.tanorpg.player.quest.task.Task;

import java.util.List;

public class Quest {
    private final String name;

    private final int npcID;

    private final List<String> lore;

    private final List<String> result;

    private final ItemRarityType difficulty;

    private final List<Condition> conditions;

    private final List<Task> tasks;

    private final List<Action> showQuestActions;

    private final List<Action> startQuestActions;

    private final List<Action> finishQuestActions;

    private final List<Action> cancelQuestActions;

    private final boolean IfCantToDoQuestShow;

    private final long minutes;

    public Quest(String name, int npcID, List<String> lore, List<String> result, ItemRarityType difficulty, List<Condition> conditions, List<Task> tasks,
                 List<Action> showQuestActions, List<Action> startQuestActions, List<Action> finishQuestActions, List<Action> cancelQuestActions, boolean ifCantToDoQuestShow, long minutes){
        this.name = name;
        this.npcID = npcID;
        this.lore = lore;
        this.result = result;
        this.difficulty = difficulty;
        this.conditions = conditions;
        this.tasks = tasks;
        this.showQuestActions = showQuestActions;
        this.startQuestActions = startQuestActions;
        this.finishQuestActions = finishQuestActions;
        this.cancelQuestActions = cancelQuestActions;
        IfCantToDoQuestShow = ifCantToDoQuestShow;
        this.minutes = minutes;
    }

    public String getName() {
        return name;
    }

    public int getNpcID() {
        return npcID;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Task getTask(String title){
        for (Task task : tasks){
            if (task.getMessage().equals(title)) return task;
        }
        return null;
    }

    public List<Action> getShowQuestActions() {
        return showQuestActions;
    }

    public List<Action> getStartQuestActions() {
        return startQuestActions;
    }

    public List<Action> getFinishQuestActions() {
        return finishQuestActions;
    }

    public List<Action> getCancelQuestActions() {
        return cancelQuestActions;
    }

    public ItemRarityType getDifficulty() {
        return difficulty;
    }

    public List<String> getLore() {
        return lore;
    }

    public List<String> getResult() {
        return result;
    }

    public boolean isCantToDoQuestShow() {
        return IfCantToDoQuestShow;
    }

    public long getMinutes() {
        return minutes;
    }
}
