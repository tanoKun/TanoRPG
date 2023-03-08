package com.github.tanokun.tanorpg.command

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.TanoRPG.Companion.playSound
import com.github.tanokun.tanorpg.TanoRPG.Companion.plugin
import com.github.tanokun.tanorpg.game.item.util.ItemListMenu
import com.github.tanokun.tanorpg.game.sell.SellMenu
import com.github.tanokun.tanorpg.player.menu.SelSkillClassMenu
import com.github.tanokun.tanorpg.util.command.Command
import com.github.tanokun.tanorpg.util.command.CommandContext
import com.github.tanokun.tanorpg.util.command.CommandPermission
import com.github.tanokun.tanorpg.util.command.TabComplete
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionDefault

class OpenInvCommand {
    @Command(parentName = "open", name = "init", desc = "")
    @CommandPermission(permission = "tanorpg.command.open", perDefault = PermissionDefault.OP)
    fun openInit(sender: CommandSender?, commandContext: CommandContext?) {
        val p = sender as Player?
        SelSkillClassMenu().inv.open(p)
    }

    @Command(parentName = "open", name = "shop", desc = "")
    @CommandPermission(permission = "tanorpg.command.open", perDefault = PermissionDefault.OP)
    fun openShop(sender: CommandSender, commandContext: CommandContext) {
        val target: Player?
        if (commandContext.getArg(0, "") == "") {
            sender.sendMessage(TanoRPG.PX + "§cIDを入力してください")
            return
        }
        if (commandContext.getArg(1, "") == "") {
            target = if (sender is Player) {
                sender
            } else {
                sender.sendMessage(TanoRPG.PX + "プレイヤー以外実行できません")
                return
            }
        } else {
            target = Bukkit.getPlayer(commandContext.getArg(1, ""))
            if (target == null) {
                sender.sendMessage(TanoRPG.PX + "§cプレイヤー「" + commandContext.getArg(1, "") + "」は存在しません")
                return
            }
        }
        if (plugin.shopManager.getShop(commandContext.getArg(0, "")) == null) {
            sender.sendMessage(TanoRPG.PX + "§cID「" + commandContext.getArg(0, "") + "§c」" + "§cは存在しません")
            return
        }
        plugin.shopManager.getShop(commandContext.getArg(0, ""))!!.inv.open(target)
    }

    @TabComplete(parentName = "open", name = "shop")
    fun openShopTabComplete(sender: CommandSender?, commandContext: CommandContext): List<String> {
        val tc = ArrayList<String>()
        if (commandContext.baseArgs.size <= 2) {
            val search = if (commandContext.baseArgs.size == 2) commandContext.baseArgs[1] else ""
            plugin.shopManager.getShopIds().stream()
                .filter { t: String -> t.startsWith(search) }
                .forEach { e: String -> tc.add(e) }
        } else if (commandContext.baseArgs.size == 3) {
            val search = if (commandContext.baseArgs.size == 3) commandContext.baseArgs[2] else ""
            Bukkit.getOnlinePlayers().stream()
                .filter { t: Player -> t.name.startsWith(search) }
                .forEach { t: Player -> tc.add(t.name) }
        }
        return tc
    }

    @Command(parentName = "open", name = "craft", desc = "")
    @CommandPermission(permission = "tanorpg.command.open", perDefault = PermissionDefault.OP)
    fun openCraft(sender: CommandSender, commandContext: CommandContext) {
        val target: Player?
        if (commandContext.getArg(0, "") == "") {
            sender.sendMessage(TanoRPG.PX + "§cIDを入力してください")
            return
        }
        if (commandContext.getArg(1, "") == "") {
            target = if (sender is Player) {
                sender
            } else {
                sender.sendMessage(TanoRPG.PX + "プレイヤー以外実行できません")
                return
            }
        } else {
            target = Bukkit.getPlayer(commandContext.getArg(1, ""))
            if (target == null) {
                sender.sendMessage(TanoRPG.PX + "§cプレイヤー「" + commandContext.getArg(1, "") + "」は存在しません")
                return
            }
        }
        if (plugin.craftManager.getCraft(commandContext.getArg(0, "")) == null) {
            sender.sendMessage(TanoRPG.PX + "§cID「" + commandContext.getArg(0, "") + "§c」" + "§cは存在しません")
            return
        }
        plugin.craftManager.getCraft(commandContext.getArg(0, ""))!!.inv.open(target)
    }

    @TabComplete(parentName = "open", name = "craft")
    fun openCraftTabComplete(sender: CommandSender?, commandContext: CommandContext): List<String> {
        val tc = ArrayList<String>()
        if (commandContext.baseArgs.size <= 2) {
            val search = if (commandContext.baseArgs.size == 2) commandContext.baseArgs[1] else ""
            plugin.craftManager.getCraftIds().stream()
                .filter { t: String -> t.startsWith(search) }
                .forEach { e: String -> tc.add(e) }
        } else if (commandContext.baseArgs.size == 3) {
            val search = if (commandContext.baseArgs.size == 3) commandContext.baseArgs[2] else ""
            Bukkit.getOnlinePlayers().stream()
                .filter { t: Player -> t.name.startsWith(search) }
                .forEach { t: Player -> tc.add(t.name) }
        }
        return tc
    }

    @Command(parentName = "open", name = "sell", desc = "")
    @CommandPermission(permission = "tanorpg.command.open", perDefault = PermissionDefault.OP)
    fun openSell(sender: CommandSender, commandContext: CommandContext) {
        val target: Player?
        if (commandContext.getArg(0, "") == "") {
            target = if (sender is Player) {
                sender
            } else {
                sender.sendMessage(TanoRPG.PX + "プレイヤー以外実行できません")
                return
            }
        } else {
            target = Bukkit.getPlayer(commandContext.getArg(0, ""))
            if (target == null) {
                sender.sendMessage(TanoRPG.PX + "§cプレイヤー「" + commandContext.getArg(0, "") + "」は存在しません")
                return
            }
        }
        playSound((sender as Player), Sound.ENTITY_SHULKER_OPEN, 3, 1.0)
        SellMenu(plugin.memberManager.getMember(sender.uniqueId)).inv.open(sender)
    }

    @TabComplete(parentName = "open", name = "sell")
    fun openSellTabComplete(sender: CommandSender?, commandContext: CommandContext): List<String> {
        val tc = ArrayList<String>()
        if (commandContext.baseArgs.size <= 2) {
            val search = if (commandContext.baseArgs.size == 2) commandContext.baseArgs[1] else ""
            Bukkit.getOnlinePlayers().stream()
                .filter { t: Player -> t.name.startsWith(search) }
                .forEach { t: Player -> tc.add(t.name) }
        }
        return tc
    }

    @Command(parentName = "open", name = "drop", desc = "")
    @CommandPermission(permission = "tanorpg.command.open", perDefault = PermissionDefault.OP)
    fun openItemTable(sender: CommandSender, commandContext: CommandContext) {
        val target: Player?
        if (commandContext.getArg(0, "") == "") {
            sender.sendMessage(TanoRPG.PX + "§cIDを入力してください")
            return
        }
        if (commandContext.getArg(1, "") == "") {
            target = if (sender is Player) {
                sender
            } else {
                sender.sendMessage(TanoRPG.PX + "プレイヤー以外実行できません")
                return
            }
        } else {
            target = Bukkit.getPlayer(commandContext.getArg(1, ""))
            if (target == null) {
                sender.sendMessage(TanoRPG.PX + "§cプレイヤー「" + commandContext.getArg(1, "") + "」は存在しません")
                return
            }
        }
        if (plugin.dropManager.getItemTable(commandContext.getArg(0, "")) == null) {
            sender.sendMessage(TanoRPG.PX + "§cID「" + commandContext.getArg(0, "") + "§c」" + "§cは存在しません")
            return
        }
        target.openInventory(
            plugin.dropManager.getItemTable(commandContext.getArg(0, ""))!!
                .generate(1)
        )
    }

    @TabComplete(parentName = "open", name = "drop")
    fun openItemTableTabComplete(sender: CommandSender?, commandContext: CommandContext): List<String> {
        val tc = ArrayList<String>()
        if (commandContext.baseArgs.size <= 2) {
            val search = if (commandContext.baseArgs.size == 2) commandContext.baseArgs[1] else ""
            plugin.dropManager.itemTables.keys.stream()
                .filter { t: String -> t.startsWith(search) }
                .forEach { e: String -> tc.add(e) }
        } else if (commandContext.baseArgs.size == 3) {
            val search = if (commandContext.baseArgs.size == 3) commandContext.baseArgs[2] else ""
            Bukkit.getOnlinePlayers().stream()
                .filter { t: Player -> t.name.startsWith(search) }
                .forEach { t: Player -> tc.add(t.name) }
        }
        return tc
    }

    @Command(parentName = "open", name = "items", desc = "")
    @CommandPermission(permission = "tanorpg.command.open", perDefault = PermissionDefault.OP)
    fun openItemList(sender: CommandSender, commandContext: CommandContext?) {
        if (sender !is Player) {
            sender.sendMessage(TanoRPG.PX + "プレイヤー以外実行できません")
            return
        }
        itemListMenu.inv.open(sender)
    }

    companion object {
        @JvmField
        var itemListMenu = ItemListMenu()
    }
}