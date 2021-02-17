package com.github.tanokun.tanorpg.command;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.command.register.Command;
import com.github.tanokun.tanorpg.game.item.CustomItemManager;
import com.github.tanokun.tanorpg.game.mob.CustomEntityManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GiveMobSpawnerCommand extends Command {
    public GiveMobSpawnerCommand() {
        super("givemobspawner");
    }

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
            CustomEntityManager.getEntity(args[0]).giveSpawnerEntity(player);
            sender.sendMessage(TanoRPG.PX + "§bID「" + args[0] + "§b」" + "§aのスポナーをgiveしました");
        }
        return true;
    }
    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 0){
            return CustomEntityManager.getEntityIDs();
        }
        if (!sender.isOp()) return null;
        List<String> texts = new ArrayList<>();
        for (String id : CustomEntityManager.getEntityIDs()){
            if (id.contains(args[0])) texts.add(id);
        }
        return texts;
    }
}
