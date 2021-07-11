package com.github.tanokun.tanorpg.command;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.sell.SellMenu;
import com.github.tanokun.tanorpg.player.inv.SelSkillClassMenu;
import com.github.tanokun.tanorpg.util.command.Command;
import com.github.tanokun.tanorpg.util.command.CommandContext;
import com.github.tanokun.tanorpg.util.command.CommandPermission;
import com.github.tanokun.tanorpg.util.command.TabComplete;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

public class OpenInvCommand {

    @Command(
            parentName = "open",
            name = "init",
            desc = ""
    )
    @CommandPermission(
            permission = "tanorpg.command.open",
            perDefault = PermissionDefault.OP)
    public void openInit(CommandSender sender, CommandContext commandContext) {
        Player p = (Player) sender;
        new SelSkillClassMenu().getInv().open(p);
    }

    @Command(
            parentName = "open",
            name = "shop",
            desc = ""
    )
    @CommandPermission(
            permission = "tanorpg.command.open",
            perDefault = PermissionDefault.OP)
    public void openShop(CommandSender sender, CommandContext commandContext) {
        Player target;

        if (commandContext.getArg(0, "").equals("")){
            sender.sendMessage(TanoRPG.PX + "§cIDを入力してください");
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

        if (TanoRPG.getPlugin().getShopManager().getShop(commandContext.getArg(0, "")) == null) {
            sender.sendMessage(TanoRPG.PX + "§cID「" + commandContext.getArg(0, "") + "§c」" + "§cは存在しません");
            return;
        }

        TanoRPG.getPlugin().getShopManager().getShop(commandContext.getArg(0, "")).getInv().open(target);
    }

    @TabComplete(
            parentName = "open",
            name = "shop"
    )
    public List<String> openShopTabComplete(CommandSender sender, CommandContext commandContext) {
        ArrayList<String> tc = new ArrayList<>();

        if (commandContext.getBaseArgs().length <= 2){
            String search = commandContext.getBaseArgs().length == 2 ? commandContext.getBaseArgs()[1] : "";
            TanoRPG.getPlugin().getShopManager().getShopIds().stream()
                    .filter(t -> t.startsWith(search))
                    .forEach(tc::add);

        } else if (commandContext.getBaseArgs().length == 3){
            String search = commandContext.getBaseArgs().length == 3 ? commandContext.getBaseArgs()[2] : "";
            Bukkit.getOnlinePlayers().stream()
                    .filter(t -> t.getName().startsWith(search))
                    .forEach(t -> tc.add(t.getName()));
        }
        return tc;
    }

    @Command(
            parentName = "open",
            name = "craft",
            desc = ""
    )
    @CommandPermission(
            permission = "tanorpg.command.open",
            perDefault = PermissionDefault.OP)
    public void openCraft(CommandSender sender, CommandContext commandContext) {
        Player target;

        if (commandContext.getArg(0, "").equals("")){
            sender.sendMessage(TanoRPG.PX + "§cIDを入力してください");
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

        if (TanoRPG.getPlugin().getCraftManager().getCraft(commandContext.getArg(0, "")) == null) {
            sender.sendMessage(TanoRPG.PX + "§cID「" + commandContext.getArg(0, "") + "§c」" + "§cは存在しません");
            return;
        }

        TanoRPG.getPlugin().getCraftManager().getCraft(commandContext.getArg(0, "")).getInv().open(target);
    }

    @TabComplete(
            parentName = "open",
            name = "craft"
    )
    public List<String> openCraftTabComplete(CommandSender sender, CommandContext commandContext) {
        ArrayList<String> tc = new ArrayList<>();

        if (commandContext.getBaseArgs().length <= 2){
            String search = commandContext.getBaseArgs().length == 2 ? commandContext.getBaseArgs()[1] : "";
            TanoRPG.getPlugin().getCraftManager().getCraftIds().stream()
                    .filter(t -> t.startsWith(search))
                    .forEach(tc::add);

        } else if (commandContext.getBaseArgs().length == 3){
            String search = commandContext.getBaseArgs().length == 3 ? commandContext.getBaseArgs()[2] : "";
            Bukkit.getOnlinePlayers().stream()
                    .filter(t -> t.getName().startsWith(search))
                    .forEach(t -> tc.add(t.getName()));
        }
        return tc;
    }

    @Command(
            parentName = "open",
            name = "sell",
            desc = ""
    )
    @CommandPermission(
            permission = "tanorpg.command.open",
            perDefault = PermissionDefault.OP)
    public void openSell(CommandSender sender, CommandContext commandContext) {
        Player target;

        if (commandContext.getArg(0, "").equals("")){
            if (sender instanceof Player) {
                target = (Player) sender;
            } else {
                sender.sendMessage(TanoRPG.PX + "プレイヤー以外実行できません");
                return;
            }
        } else {
            target = Bukkit.getPlayer(commandContext.getArg(0, ""));
            if (target == null) {
                sender.sendMessage(TanoRPG.PX + "§cプレイヤー「" + commandContext.getArg(0, "") + "」は存在しません");
                return;
            }
        }

        TanoRPG.playSound((Player) sender, Sound.ENTITY_SHULKER_OPEN, 3, 1);
        new SellMenu(TanoRPG.getPlugin().getMemberManager().getMember(((Player)sender).getUniqueId())).getInv().open(((Player) sender));
    }

    @TabComplete(
            parentName = "open",
            name = "sell"
    )
    public List<String> openSellTabComplete(CommandSender sender, CommandContext commandContext) {
        ArrayList<String> tc = new ArrayList<>();

        if (commandContext.getBaseArgs().length <= 2){
            String search = commandContext.getBaseArgs().length == 2 ? commandContext.getBaseArgs()[1] : "";
            Bukkit.getOnlinePlayers().stream()
                    .filter(t -> t.getName().startsWith(search))
                    .forEach(t -> tc.add(t.getName()));
            }
        return tc;
    }

    @Command(
            parentName = "open",
            name = "rune",
            desc = ""
    )
    @CommandPermission(
            permission = "tanorpg.command.open",
            perDefault = PermissionDefault.OP)
    public void openRune(CommandSender sender, CommandContext commandContext) {
        Player target;

        if (commandContext.getArg(0, "").equals("")){
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

        TanoRPG.playSound((Player) sender, Sound.ENTITY_SHULKER_OPEN, 3, 1);
    }
}
