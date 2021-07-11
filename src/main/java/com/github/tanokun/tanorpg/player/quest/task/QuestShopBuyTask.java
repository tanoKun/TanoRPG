package com.github.tanokun.tanorpg.player.quest.task;

import com.github.tanokun.tanorpg.util.command.CommandContext;
import com.github.tanokun.tanorpg.util.io.Config;

public class QuestShopBuyTask extends Task<Integer> {
    private int necCount;

    public QuestShopBuyTask(CommandContext commandContext, Config config) {
        super(TaskType.SHOP);
        necCount = Integer.valueOf(commandContext.getArg(1, "1"));
        setMessage("買い物を" + necCount + "回する");
    }

    @Override
    public String getMessage(boolean b, TaskData<Integer> taskData) {
        return b ? "§a" + getMessage() + " §a[" + taskData.getValue() +"/" + necCount + "]" : "§f" + getMessage() + " §f[" + taskData.getValue() +"/" + necCount + "]";
    }

    @Override
    public boolean isClearTask(Integer value) {
        return value >= necCount;
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
