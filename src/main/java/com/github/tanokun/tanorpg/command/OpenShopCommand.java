package com.github.tanokun.tanorpg.command;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.command.register.Command;
import com.github.tanokun.tanorpg.game.shop.ShopManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class OpenShopCommand extends Command {

    public OpenShopCommand(){super("openshop");}

    public boolean execute(CommandSender sender, String[] args) {
        if (args.length >= 2){
            if (!args[1].equals(TanoRPG.OPEN_KYE)) return true;
            if (ShopManager.getShop(args[0]) != null) {
                ShopManager.getShop(args[0]).openShop((Player) sender);
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
