package com.github.tanokun.tanorpg.command;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.command.register.Command;
import com.github.tanokun.tanorpg.game.item.CustomItemManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class GiveItemCommand extends Command {
    public GiveItemCommand(){super("giveitem");}

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
            if (args.length == 0) {
                sender.sendMessage(TanoRPG.PX + "§cIDを入力してください");
                return true;
            }
        }
        if (args.length >= 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(TanoRPG.PX + "§cConsoleにアイテムを与えることはできません");
                return true;
            }
            if (!(CustomItemManager.isExists(args[0]))) {
                sender.sendMessage(TanoRPG.PX + "§bID「" + args[0] + "§b」" + "§cは存在しません");
                return true;
            }
            PlayerInventory inventory = player.getInventory();
            inventory.addItem(CustomItemManager.getCustomItem(args[0]).getItem());
            sender.sendMessage(TanoRPG.PX + "§a" + sender.getName() + "§aに" + "§bID「" + args[0] + "§b」" + "§aを渡しました");
        }
        return true;
    }
}