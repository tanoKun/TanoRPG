package com.github.tanokun.tanorpg.command

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.TanoRPG.Companion.playSound
import com.github.tanokun.tanorpg.TanoRPG.Companion.plugin
import com.github.tanokun.tanorpg.game.craft.CraftRecipe
import com.github.tanokun.tanorpg.game.craft.special.CreateSpecialCraftMenu
import com.github.tanokun.tanorpg.game.craft.special.SpecialCraft
import com.github.tanokun.tanorpg.game.item.ItemType
import com.github.tanokun.tanorpg.game.item.status.generate.IntGenerator
import com.github.tanokun.tanorpg.game.item.status.generate.StatusGeneratorHandler
import com.github.tanokun.tanorpg.game.item.type.base.ItemData
import com.github.tanokun.tanorpg.player.EquipmentMap
import com.github.tanokun.tanorpg.player.menu.main.MainMenu
import com.github.tanokun.tanorpg.player.menu.main.SelQuestMenu
import com.github.tanokun.tanorpg.player.menu.main.StatusPointMenu
import com.github.tanokun.tanorpg.player.menu.main.skill.SetSkillMenu
import com.github.tanokun.tanorpg.player.menu.util.AdminUtilMenu
import com.github.tanokun.tanorpg.player.status.KindOfStatusType
import com.github.tanokun.tanorpg.player.status.StatusType
import com.github.tanokun.tanorpg.util.createItem
import com.github.tanokun.tanorpg.util.getItemData
import com.github.tanokun.tanorpg.util.item.ItemBuilder
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem
import com.github.tanokun.tanorpg.util.smart_inv.inv.InventoryListener
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider
import com.playerislands.thirdparty.Connection
import com.playerislands.thirdparty.Spigot
import com.sk89q.worldedit.command.argument.Arguments
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.arguments.TextArgument
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import net.minecraft.data.Main
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.v1_17_R1.CraftServer
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.lang.reflect.Field
import java.util.*
import kotlin.collections.ArrayList


class OtherCommand : Listener {

    init {
        registerSetMotd()
        registerTest()
        registerCreateSpecialCraft()
    }

    /* /test 0.15 -9.5 -9.5 */
    private fun registerTest() {
        CommandAPICommand("test")
            .executesPlayer(PlayerCommandExecutor { player: Player, arg: Array<Any> ->
                //val list = ArrayList<StatusGeneratorHandler>()
                //list.add(IntGenerator("100.0%{-10~10}", StatusType.DEF))
                //SpecialCraft(player, 3, list,
                //"P---------S----------------------------------B--------").inv.open(player)

                //com.github.tanokun.tanorpg.command.MainMenu().getInv(player).open(player)
            })
            .register()
    }

    private fun registerCreateSpecialCraft() {
        CommandAPICommand("createSpecialCraft")
            .executesPlayer(PlayerCommandExecutor { player: Player, arg: Array<Any> ->
                CreateSpecialCraftMenu("").getInv().open(player)
            })
            .register()

        CommandAPICommand("createSpecialCraft")
            .withArguments(TextArgument("gui"))
            .executesPlayer(PlayerCommandExecutor { player: Player, arg: Array<Any> ->
                CreateSpecialCraftMenu(arg[0] as String).getInv().open(player)
            })
            .register()
    }

    private fun registerSetMotd() {
        CommandAPICommand("motd")
            .withPermission(CommandPermission.fromString("tanorpg.command.motd"))
            .withArguments(TextArgument("motd"))
            .executes(CommandExecutor { _: CommandSender, objects: Array<Any> ->
                var motd = objects[0] as String
                motd = motd.replace("&0", "§0")
                motd = motd.replace("&1", "§1")
                motd = motd.replace("&2", "§2")
                motd = motd.replace("&3", "§3")
                motd = motd.replace("&4", "§4")
                motd = motd.replace("&5", "§5")
                motd = motd.replace("&7", "§7")
                motd = motd.replace("&8", "§8")
                motd = motd.replace("&9", "§9")
                motd = motd.replace("&a", "§a")
                motd = motd.replace("&b", "§b")
                motd = motd.replace("&c", "§c")
                motd = motd.replace("&d", "§d")
                motd = motd.replace("&e", "§e")
                motd = motd.replace("&f", "§f")
                motd = motd.replace("&k", "§k")
                motd = motd.replace("&l", "§l")
                motd = motd.replace("&m", "§m")
                motd = motd.replace("&n", "§n")
                motd = motd.replace("&o", "§o")
                motd = motd.replace("&r", "§r")
                motd = motd.replace("&n", "\n")
                motd = motd.replace("$", "\\")
                (Bukkit.getServer() as CraftServer).server.motd = motd
                val spigot = Spigot.INSTANCE
                var field: Field? = null
                try {
                    field = spigot.javaClass.getDeclaredField("connection")
                } catch (e: NoSuchFieldException) {
                    e.printStackTrace()
                }
                field!!.isAccessible = true
                var connection: Connection? = null
                try {
                    connection = field[spigot] as Connection
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
                connection!!.sendMotd(motd)
            })
            .register()
    }
}

class MainMenu : InventoryProvider {
    fun getInv(player: Player): SmartInventory {
        val member = plugin.memberManager.getMember(player.uniqueId)

        return SmartInventory.builder()
            .closeable(true)
            .provider(this)
            .size(6, 9)
            .title("test")
            .id("test")
            .update(false)
            .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        val member = plugin.memberManager.getMember(player.uniqueId)
        val barriers = 15
        val items = 5

        val random = Random()

        val barrier = ClickableItem.empty(ItemBuilder(Material.STONE)
            .setDisplayName("  ")
            .addAllItemFlags()
            .build())

        val item = ClickableItem.empty(ItemBuilder(Material.EMERALD)
            .setDisplayName("")
            .addAllItemFlags()
            .build())

        for (i in 0..barriers) {
            val row = random.nextInt(6)
            val column = random.nextInt(9)

            if (contents[row, column].isEmpty) contents[row, column] = barrier
        }

        for (i in 0..items) {
            val row = random.nextInt(6)
            val column = random.nextInt(9)

            if (contents[row, column].isEmpty) contents[row, column] = item
        }
    }
}