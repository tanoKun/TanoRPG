package com.github.tanokun.tanorpg.command;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.command.register.Command;
import com.github.tanokun.tanorpg.game.mob.CustomEntityManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MobSpawnCommand extends Command {
    public MobSpawnCommand() {super("mobspawn");}

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
                sender.sendMessage(TanoRPG.PX + "§cCONSOLEからは実行できません");
                return true;
            }
            if (!(CustomEntityManager.isExists(args[0]))) {
                sender.sendMessage(TanoRPG.PX + "§bID「" + args[0] + "§b」" + "§cは存在しません");
                return true;
            }
            CustomEntityManager.getEntity(args[0]).spawnEntity(player.getLocation());
            sender.sendMessage(TanoRPG.PX + "§bID「" + args[0] + "§b」" + "§aを召喚しました");
        }
        return true;
    }
}
