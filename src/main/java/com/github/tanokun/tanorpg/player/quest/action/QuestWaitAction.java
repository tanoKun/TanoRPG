package com.github.tanokun.tanorpg.player.quest.action;

import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.util.command.CommandContext;

public class QuestWaitAction implements Action {
    private int millis;

    public QuestWaitAction(CommandContext cc){
        millis = Integer.valueOf(cc.getArg(0, "1")) * 50;
    }

    @Override
    public void execute(Member m) {
        try { Thread.sleep(millis); } catch (InterruptedException e) { e.printStackTrace(); }
    }
}
