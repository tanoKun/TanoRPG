package com.github.tanokun.tanorpg.player.quest.action;

import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.util.command.CommandContext;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class QuestSendMessageAction implements Action {

    private String message = "";

    public QuestSendMessageAction(CommandContext cc){
        cc.args.forEach(m -> message = message + " " + m);
    }

    @Override
    public void execute(Member m) {
    Player p = Bukkit.getPlayer(m.getUuid());
    String r = message;
        r = r.replace("[player]", p.getName());
                p.sendMessage(r);
                }
}
