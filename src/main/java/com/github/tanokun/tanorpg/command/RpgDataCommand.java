package com.github.tanokun.tanorpg.command;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.util.command.Command;
import com.github.tanokun.tanorpg.util.command.CommandContext;
import com.github.tanokun.tanorpg.util.command.CommandPermission;
import com.github.tanokun.tanorpg.util.command.TabComplete;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RpgDataCommand {
    List<String> t = Arrays.asList("guild-loc", "guild-region-name", "home-loc", "home-region-name", "respawn-loc");

    @Command(
            parentName = "rd",
            name = "set",
            desc = "色々設定"
    )
    @CommandPermission(

            permission = "tanorpg.command.rd",
            perDefault = PermissionDefault.OP
    )
    public void subSpawnLocation(CommandSender sender, CommandContext commandContext) {
        Player p = (Player) sender;
        if (commandContext.getArg(0, "").equals("guild-loc")) {
            double x = p.getLocation().getX();
            double y = p.getLocation().getY();
            double z = p.getLocation().getZ();
            TanoRPG.getPlugin().getDataManager().setGuildLoc(p.getLocation());
            sender.sendMessage(TanoRPG.PX + "ギルド位置を「x:" + Math.round(x) + ",y:" + Math.round(y) + ",z:" + Math.round(z) + "」に設定しました");
        }

        if (commandContext.getArg(0, "").equals("guild-region-name")) {
            if (commandContext.getArg(1, null) == null){
                sender.sendMessage(TanoRPG.PX + "§c名前を入力してください");
                return;
            }
            TanoRPG.getPlugin().getDataManager().setGuildRegionName(commandContext.getArg(1, null));
            sender.sendMessage(TanoRPG.PX + "ギルド領域名を「" + commandContext.getArg(1, null) + "」に設定しました");
        }

        if (commandContext.getArg(0, "").equals("home-loc")) {
            double x = p.getLocation().getX();
            double y = p.getLocation().getY();
            double z = p.getLocation().getZ();
            TanoRPG.getPlugin().getDataManager().setHomeLoc(p.getLocation());
            sender.sendMessage(TanoRPG.PX + "初期スポーン位置を「x:" + Math.round(x) + ",y:" + Math.round(y) + ",z:" + Math.round(z) + "」に設定しました");
        }

        if (commandContext.getArg(0, "").equals("home-region-name")) {
            if (commandContext.getArg(1, null) == null){
                sender.sendMessage(TanoRPG.PX + "§c名前を入力してください");
                return;
            }
            TanoRPG.getPlugin().getDataManager().setHomeRegionName(commandContext.getArg(1, null));
            sender.sendMessage(TanoRPG.PX + "初期スポーン領域名を「" + commandContext.getArg(1, null) + "」に設定しました");
        }

        if (commandContext.getArg(0, "").equals("respawn-loc")) {
            double x = p.getLocation().getX();
            double y = p.getLocation().getY();
            double z = p.getLocation().getZ();
            TanoRPG.getPlugin().getDataManager().setRespawnLoc(p.getLocation());
            sender.sendMessage(TanoRPG.PX + "リスポーン位置を「x:" + Math.round(x) + ",y:" + Math.round(y) + ",z:" + Math.round(z) + "」に設定しました");
        }
    }

    @TabComplete(
            parentName = "rd",
            name = "set"
    )
    public List<String> rdSetTabComplete(CommandSender sender, CommandContext commandContext) {
        ArrayList<String> tc = new ArrayList<>();

        if (commandContext.getBaseArgs().length <= 2) {
            String search = commandContext.getBaseArgs().length == 2 ? commandContext.getBaseArgs()[1] : "";
            t.stream()
                    .filter(t -> t.startsWith(search))
                    .forEach(tc::add);

        }
        return tc;
    }

}
