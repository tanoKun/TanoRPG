package com.github.tanokun.tanorpg.player.quest.action;

import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.util.command.CommandContext;

public class QuestGiveMoneyAction implements Action {
    private int money;

    public QuestGiveMoneyAction(CommandContext cc){
        money = Integer.valueOf(cc.getArg(0, "1"));
    }

    @Override
    public void execute(Member m) {
        m.addMoney(money);
    }
}
