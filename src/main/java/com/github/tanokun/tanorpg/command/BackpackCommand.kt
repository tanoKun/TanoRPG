package com.github.tanokun.tanorpg.command

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.TanoRPG.Companion.plugin
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.SuggestionInfo
import dev.jorel.commandapi.arguments.OfflinePlayerArgument
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BackpackCommand {
    init {
        registerBp()
        registerBpEdit()
    }

    private fun registerBp() {
        CommandAPICommand("bp")
            .withPermission(CommandPermission.fromString("tanorpg.command.bp"))
            .withArguments(OfflinePlayerArgument("target").replaceWithSafeSuggestions { suggestionInfo: SuggestionInfo? -> Bukkit.getOfflinePlayers() })
            .executesPlayer(PlayerCommandExecutor { sender: Player, args: Array<Any?> ->
                val target = args[0] as OfflinePlayer?
                if (target == null) {
                    sender.sendMessage("§cそのプレイヤーは存在しません")
                    return@PlayerCommandExecutor
                }
                val member = plugin.memberManager.loadData(target.uniqueId)
                if (member == null) {
                    sender.sendMessage("§cそのプレイヤーは存在しません")
                    return@PlayerCommandExecutor
                }
                member.backpackMenu.inv.open(sender)
            })
            .register()
        CommandAPICommand("bp")
            .withPermission(CommandPermission.fromString("tanorpg.command.bp"))
            .executesPlayer(PlayerCommandExecutor { sender: Player, args: Array<Any?>? ->
                val (_, _, _, _, _, _, _, _, _, backpackMenu) = plugin.memberManager.loadData(sender.uniqueId)
                backpackMenu.inv.open(sender)
            })
            .register()
    }

    private fun registerBpEdit() {
        val addPage = CommandAPICommand("addpage")
            .withArguments(OfflinePlayerArgument("target").replaceWithSafeSuggestions { suggestionInfo: SuggestionInfo? -> Bukkit.getOfflinePlayers() })
            .executes(CommandExecutor { sender: CommandSender, args: Array<Any?> ->
                val target = args[0] as OfflinePlayer?
                if (target == null) {
                    sender.sendMessage("§cそのプレイヤーは存在しません")
                    return@CommandExecutor
                }
                val member = plugin.memberManager.loadData(target.uniqueId)
                if (member == null) {
                    sender.sendMessage("§cそのプレイヤーは存在しません")
                    return@CommandExecutor
                }
                member.backpackMenu.addPage()
                sender.sendMessage(TanoRPG.PX + target.name + "のバックバックを1つ追加しました")
            })
        val removePage = CommandAPICommand("removepage")
            .withArguments(OfflinePlayerArgument("target").replaceWithSafeSuggestions { suggestionInfo: SuggestionInfo? -> Bukkit.getOfflinePlayers() })
            .executes(CommandExecutor { sender: CommandSender, args: Array<Any?> ->
                val target = args[0] as OfflinePlayer?
                if (target == null) {
                    sender.sendMessage("§cそのプレイヤーは存在しません")
                    return@CommandExecutor
                }
                val member = plugin.memberManager.loadData(target.uniqueId)
                if (member == null) {
                    sender.sendMessage("§cそのプレイヤーは存在しません")
                    return@CommandExecutor
                }
                member.backpackMenu.removePage()
                sender.sendMessage(TanoRPG.PX + target.name + "のバックバックを1つ追加しました")
            })
        CommandAPICommand("bpedit")
            .withPermission(CommandPermission.fromString("tanorpg.bpedit"))
            .withSubcommand(addPage)
            .withSubcommand(removePage)
            .register()
    }
}