package com.github.tanokun.tanorpg.command;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.command.register.Command;
import com.github.tanokun.tanorpg.game.craft.CraftManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class OpenCraftCommand extends Command {

    public OpenCraftCommand(){super("opencraft");}

    public boolean execute(CommandSender sender, String[] args) {
        if (args.length >= 2){
            if (!args[1].equals(TanoRPG.OPEN_KYE)) return true;
            if (CraftManager.getCraft(args[0]) != null) {
                CraftManager.getCraft(args[0]).openCraft((Player) sender);
            } else {
                sender.sendMessage(TanoRPG.PX + "§cID: 「" + args[0] + "」は存在しません");
            }
        }
        return false;
    }
    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Arrays.asList(TanoRPG.OPEN_KYE);
    }
}
