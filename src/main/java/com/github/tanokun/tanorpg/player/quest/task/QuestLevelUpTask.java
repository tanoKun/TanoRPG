package com.github.tanokun.tanorpg.player.quest.task;

import com.github.tanokun.tanorpg.util.command.CommandContext;
import com.github.tanokun.tanorpg.util.io.Config;

public class QuestLevelUpTask extends Task<Integer> {
    private int necLevel;

    public QuestLevelUpTask(CommandContext commandContext, Config config) {
        super(TaskType.LEVEL_UP);
        necLevel = Integer.valueOf(commandContext.getArg(0, "1"));
        setMessage(necLevel + "Lv まで上げる");
    }

    @Override
    public String getMessage(boolean b, TaskData<Integer> taskData) {
        return b ? "§a" + getMessage() + " §a[" + taskData.getValue() +"/" + necLevel + "]" : "§f" + getMessage() + " §f[" + taskData.getValue() +"/" + necLevel + "]";
    }

    @Override
    public boolean isClearTask(Integer value) {
        return value >= necLevel;
    }

    @Override
    public Integer castValue(Object o) {
        return Integer.valueOf(String.valueOf(o));
    }

    @Override
    public TaskData<Integer> getTaskData() {
        return new TaskData<>(this, 0);
    }
}
