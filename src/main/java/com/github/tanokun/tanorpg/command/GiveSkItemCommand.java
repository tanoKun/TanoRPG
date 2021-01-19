package com.github.tanokun.tanorpg.command;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.command.register.Command;
import com.github.tanokun.tanorpg.game.player.skill.SkillManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveSkItemCommand extends Command {
    public GiveSkItemCommand() {
        super("giveskitem");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 1){
            if (SkillManager.isExists(args[0])){
                ((Player)sender).getInventory().addItem(SkillManager.getSkillItem(args[0]));
                sender.sendMessage(TanoRPG.PX + "「スキル習得書」を取得しました");
            }else {
                sender.sendMessage(TanoRPG.PX + "§c存在しないスキルです");
            }
        }else {
            sender.sendMessage(TanoRPG.PX + "§c引数がおかしいです");
            return true;
        }
        return true;
    }
}