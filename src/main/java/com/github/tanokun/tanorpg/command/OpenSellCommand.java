package com.github.tanokun.tanorpg.command;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.command.register.Command;
import com.github.tanokun.tanorpg.game.shop.sell.Sell;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenSellCommand extends Command {

    public OpenSellCommand(){super("opensell");}

    public boolean execute(CommandSender sender, String[] args) {
        if (args.length >= 2) {
            if (!args[1].equals(TanoRPG.OPEN_KYE)) return true;
            Sell.openSell((Player) sender);
        }
    return true;
    }
}
