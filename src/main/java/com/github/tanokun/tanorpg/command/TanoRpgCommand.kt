package com.github.tanokun.tanorpg.command

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.TanoRPG.Companion.plugin
import com.github.tanokun.tanorpg.game.craft.CraftManager
import com.github.tanokun.tanorpg.game.entity.EntityManager
import com.github.tanokun.tanorpg.game.entity.gathering.GatheringManager
import com.github.tanokun.tanorpg.game.item.ItemManager
import com.github.tanokun.tanorpg.game.item.drop.DropManager
import com.github.tanokun.tanorpg.game.shop.ShopManager
import com.github.tanokun.tanorpg.player.quests.QuestManager
import com.github.tanokun.tanorpg.util.addItem
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.arguments.MultiLiteralArgument
import dev.jorel.commandapi.arguments.PlayerArgument
import dev.jorel.commandapi.arguments.TextArgument
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.stream.Collectors

class TanoRpgCommand {
    init {
        registerTg()
    }

    private fun registerTg() {
        CommandAPICommand("tanorpg")
            .withAliases("tg", "t")
            .withPermission(CommandPermission.fromString("tanorpg.command.tg"))
            .withSubcommand(
                CommandAPICommand("item").withAliases("i")
                    .withArguments(TextArgument("itemid").replaceSuggestions { plugin.itemManager.itemIDs.toTypedArray()})
                    .executesPlayer(PlayerCommandExecutor { sender: Player, args: Array<Any> ->
                        val id = args[0] as String
                        if (plugin.itemManager.itemIDs.contains(id)) {
                            addItem(sender, plugin.itemManager.getItem(id)!!.init(1, 0.0, true))
                            sender.sendMessage(TanoRPG.PX + "§aに" + "§bID「" + id + "§b」" + "§aを1個渡しました")
                        }
                    })
            )
            .withSubcommand(
                CommandAPICommand("item").withAliases("i")
                    .withArguments(TextArgument("itemid").replaceSuggestions { plugin.itemManager.itemIDs.toTypedArray() })
                    .withArguments(IntegerArgument("amount"))
                    .withArguments(PlayerArgument("target"))
                    .withArguments(IntegerArgument("p"))
                    .executes(CommandExecutor { sender: CommandSender, args: Array<Any> ->
                        val p = args[3] as Int
                        val target = args[2] as Player
                        val amount = args[1] as Int
                        val id = args[0] as String
                        if (!plugin.itemManager.itemIDs.contains(id)) return@CommandExecutor
                        addItem(target, plugin.itemManager.getItem(id)!!.init(amount, p.toDouble(), false))
                        sender.sendMessage( "${TanoRPG.PX} §a${target.name} §aに§bID「${id}§b」§aを${amount}個渡しました")
                    })
            )
            .withSubcommand(
                CommandAPICommand("skill").withAliases("s")
                    .withArguments(TextArgument("skillid").replaceSuggestions { plugin.skillManager.skillNames.keys.stream()
                        .map { if (it!!.toByteArray(charset("SJIS")).size != it!!.length) "\"${it}\""; it }
                        .collect(Collectors.toList()).toTypedArray()
                    })
                    .withArguments(PlayerArgument("target"))
                    .executes(CommandExecutor { sender: CommandSender, args: Array<Any> ->
                        val target = args[1] as Player
                        val id = args[0] as String
                        if (!plugin.skillManager.skillNames.keys.contains(id)) return@CommandExecutor
                        target.inventory.addItem(plugin.skillManager.getSkillItem(id))
                        sender.sendMessage(
                            TanoRPG.PX + "§a" + target.name + "§aに" + "§bSkill「" + id
                                    + "§b」" + "§aを渡しました"
                        )
                    })
            )
            .withSubcommand(
                CommandAPICommand("reload").withAliases("r")
                    .withArguments(MultiLiteralArgument("item", "entity", "spawner", "shop", "craft", "quest", "drop", "all", "gathering"))
                    .executes(CommandExecutor { sender: CommandSender?, args: Array<Any> ->
                        val target = args[0] as String
                        if (target.equals("item", ignoreCase = true)) {
                            plugin.itemManager = ItemManager(if (sender is Player) sender else null)
                        } else if (target.equals("entity", ignoreCase = true)) {
                            Bukkit.dispatchCommand(sender!!, "mm reload")
                            Bukkit.getScheduler().runTaskLater(
                                plugin,
                                Runnable {
                                    plugin.entityManager = EntityManager(if (sender is Player) sender else null)
                                },
                                20
                            )
                        } else if (target.equals("shop", ignoreCase = true)) {
                            plugin.shopManager = ShopManager(if (sender is Player) sender else null)
                        } else if (target.equals("craft", ignoreCase = true)) {
                            plugin.craftManager = CraftManager(if (sender is Player) sender else null)
                        } else if (target.equals("quest", ignoreCase = true)) {
                            plugin.questManager = QuestManager(if (sender is Player) sender else null)
                        } else if (target.equals("drop", ignoreCase = true)) {
                            plugin.dropManager = DropManager(if (sender is Player) sender else null)
                        } else if (target.equals("gathering", ignoreCase = true)) {
                            plugin.setGatheringManager(GatheringManager(if (sender is Player) sender else null))
                        } else if (target.equals("all", ignoreCase = true)) {
                            Bukkit.dispatchCommand(sender!!, "mm reload")
                            plugin.itemManager = ItemManager(if (sender is Player) sender else null)
                            Bukkit.getScheduler().runTaskLater(plugin, Runnable { plugin.entityManager = EntityManager(if (sender is Player) sender else null) }, 20)
                            plugin.questManager = QuestManager(if (sender is Player) sender else null)
                            plugin.shopManager = ShopManager(if (sender is Player) sender else null)
                            plugin.craftManager = CraftManager(if (sender is Player) sender else null)
                            plugin.dropManager = DropManager(if (sender is Player) sender else null)
                            plugin.setGatheringManager(GatheringManager(if (sender is Player) sender else null))
                        }
                    })
            )
            .register()
    }
}