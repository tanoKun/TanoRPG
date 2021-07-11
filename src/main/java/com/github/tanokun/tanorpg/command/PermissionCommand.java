package com.github.tanokun.tanorpg.command;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.util.command.Command;
import com.github.tanokun.tanorpg.util.command.CommandContext;
import com.github.tanokun.tanorpg.util.command.CommandPermission;
import com.github.tanokun.tanorpg.util.command.TabComplete;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

public class PermissionCommand {

    @Command(
            parentName = "permission",
            name = "add",
            desc = ""
    )
    @CommandPermission(
            permission = "tanorpg.command.tg",
            perDefault = PermissionDefault.OP)
    public void addPermission(CommandSender sender, CommandContext commandContext) {
        Player target;
        if (commandContext.getArg(0, "").equals("")){
            sender.sendMessage(TanoRPG.PX + "§cパーミッション名を入力してください");
            return;
        }

        if (commandContext.getArg(1, "").equals("")){
            if (sender instanceof Player) {
                target = (Player) sender;
            } else {
                sender.sendMessage(TanoRPG.PX + "プレイヤー以外実行できません");
                return;
            }
        } else {
            target = Bukkit.getPlayer(commandContext.getArg(1, ""));
            if (target == null) {
                sender.sendMessage(TanoRPG.PX + "§cプレイヤー「" + commandContext.getArg(1, "") + "」は存在しません");
                return;
            }
        }

        TanoRPG.getPlugin().getMemberManager().getMember(target.getUniqueId()).getOpenPermissionMap().addPermission(commandContext.getArg(0, ""));
        sender.sendMessage(TanoRPG.PX + target.getName() + "に「" + commandContext.getArg(0, "") + "」を付与しました");

    }

    @TabComplete(
            parentName = "permission",
            name = "add"
    )
    public List<String> addPermissionTabComplete(CommandSender sender, CommandContext commandContext) {
        ArrayList<String> tc = new ArrayList<>();

        if (commandContext.getBaseArgs().length <= 2){
            String search = commandContext.getBaseArgs().length == 2 ? commandContext.getBaseArgs()[1] : "";
            TanoRPG.getPlugin().getDataManager().getPermissions().stream()
                    .filter(t -> t.startsWith(search))
                    .forEach(tc::add);

        }

        return tc;
    }

    @Command(
            parentName = "permission",
            name = "remove",
            desc = ""
    )
    @CommandPermission(
            permission = "tanorpg.command.tg",
            perDefault = PermissionDefault.OP)
    public void removePermission(CommandSender sender, CommandContext commandContext) {
        Player target;
        if (commandContext.getArg(0, "").equals("")){
            sender.sendMessage(TanoRPG.PX + "§cパーミッション名を入力してください");
            return;
        }

        if (commandContext.getArg(1, "").equals("")){
            if (sender instanceof Player) {
                target = (Player) sender;
            } else {
                sender.sendMessage(TanoRPG.PX + "プレイヤー以外実行できません");
                return;
            }
        } else {
            target = Bukkit.getPlayer(commandContext.getArg(1, ""));
            if (target == null) {
                sender.sendMessage(TanoRPG.PX + "§cプレイヤー「" + commandContext.getArg(1, "") + "」は存在しません");
                return;
            }
        }

        TanoRPG.getPlugin().getMemberManager().getMember(target.getUniqueId()).getOpenPermissionMap().removePermission(commandContext.getArg(0, ""));
        sender.sendMessage(TanoRPG.PX + target.getName() + "から「" + commandContext.getArg(0, "") + "」を剥奪しました");

    }

    @TabComplete(
            parentName = "permission",
            name = "remove"
    )
    public List<String> removePermissionTabComplete(CommandSender sender, CommandContext commandContext) {
        ArrayList<String> tc = new ArrayList<>();

        if (commandContext.getBaseArgs().length <= 2){
            String search = commandContext.getBaseArgs().length == 2 ? commandContext.getBaseArgs()[1] : "";
            TanoRPG.getPlugin().getDataManager().getPermissions().stream()
                    .filter(t -> t.startsWith(search))
                    .forEach(tc::add);

        }

        return tc;
    }
}
