package com.github.tanokun.tanorpg.player.quest.task;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.base.ObjectEntity;
import com.github.tanokun.tanorpg.util.command.CommandContext;
import com.github.tanokun.tanorpg.util.io.Config;

public class QuestEntityKillTask extends Task<Integer> {
    private int necCount;
    private ObjectEntity target;

    public QuestEntityKillTask(CommandContext commandContext, Config config) {
        super(TaskType.ENTITY_KILL);
        necCount = Integer.valueOf(commandContext.getArg(1, "1"));
        target = TanoRPG.getPlugin().getEntityManager().getEntity(commandContext.getArg(0, ""));
        setMessage("「" + target.getName() + "」を" + necCount + "体倒す");
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
