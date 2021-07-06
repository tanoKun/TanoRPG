package com.github.tanokun.tanorpg.player.quest.action;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.util.command.CommandContext;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class QuestGiveItemAction implements Action {
    private ItemStack item;

    public QuestGiveItemAction(CommandContext cc){
        item = TanoRPG.getPlugin().getItemManager().getItem(cc.getArg(0, "")).init(Integer.valueOf(cc.getArg(1, "1")));
    }

    @Override
    public void execute(Member m) {
        Bukkit.getPlayer(m.getUuid()).getInventory().addItem(item);
    }
}
