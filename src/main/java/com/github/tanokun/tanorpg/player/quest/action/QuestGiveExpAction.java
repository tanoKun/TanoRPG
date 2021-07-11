package com.github.tanokun.tanorpg.player.quest.action;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.util.command.CommandContext;
import org.bukkit.Bukkit;

public class QuestGiveExpAction implements Action {
    int exp;

    public QuestGiveExpAction(CommandContext cc){
        exp = Integer.valueOf(cc.getArg(0, "1"));
    }

    @Override
    public void execute(Member m) {
        Bukkit.getScheduler().runTask(TanoRPG.getPlugin(), () -> m.addHasEXP(exp));
    }
}
