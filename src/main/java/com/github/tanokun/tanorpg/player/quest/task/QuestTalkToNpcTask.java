package com.github.tanokun.tanorpg.player.quest.task;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.base.ObjectEntity;
import com.github.tanokun.tanorpg.player.quest.action.Action;
import com.github.tanokun.tanorpg.player.quest.action.ActionType;
import com.github.tanokun.tanorpg.util.command.CommandContext;
import com.github.tanokun.tanorpg.util.io.Config;
import net.citizensnpcs.api.CitizensAPI;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class QuestTalkToNpcTask extends Task<Boolean> {
    private int npcId;
    private List<Action> actions = new ArrayList<>();

    public QuestTalkToNpcTask(CommandContext cc, Config config) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        super(TaskType.TALK_TO_NPC);
        npcId = Integer.valueOf(cc.getArg(0, "0"));
        setMessage(cc.getArg(1, npcId + "に話しかけに行く"));

        String path = "taskData.TALK_TO_NPC_" + npcId;
        for (String action : config.getConfig().getStringList(path)){
            cc.init(null, action.split(" "));
            actions.add(ActionType.valueOf(cc.args.remove(0)).getAction().getConstructor(CommandContext.class).newInstance(cc));
        }
    }

    @Override
    public String getMessage(boolean b, TaskData<Boolean> taskData) {
        return b ? "§a" + getMessage() : "§f" + getMessage();
    }

    @Override
    public boolean isClearTask(Boolean value) {
        return value;
    }

    @Override
    public Boolean castValue(Object o) {return Boolean.valueOf(String.valueOf(o)); }

    @Override
    public TaskData<Boolean> getTaskData() {
        return new TaskData<>(this, false);
    }

    public List<Action> getActions() {
        return actions;
    }

    public int getNpcId() {
        return npcId;
    }
}
