package com.github.tanokun.tanorpg.command;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.command.register.Command;
import com.github.tanokun.tanorpg.game.shop.sell.Sell;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class OpenSellCommand extends Command {

    public OpenSellCommand(){super("opensell");}

    public boolean execute(CommandSender sender, String[] args) {
        if (args.length >= 1) {
            if (!args[0].equals(TanoRPG.OPEN_KYE)) return true;
            Sell.INVENTORY().open((Player) sender);
        }
    return true;
    }
    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (!sender.isOp()) return null;
        return Arrays.asList(TanoRPG.OPEN_KYE);
    }
}
