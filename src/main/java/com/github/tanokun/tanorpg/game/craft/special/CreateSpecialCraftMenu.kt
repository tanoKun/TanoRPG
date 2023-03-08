package com.github.tanokun.tanorpg.game.craft.special

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.game.item.ItemType
import com.github.tanokun.tanorpg.game.item.drop.then
import com.github.tanokun.tanorpg.game.item.type.base.ItemData
import com.github.tanokun.tanorpg.player.EquipmentMap
import com.github.tanokun.tanorpg.player.menu.main.SelQuestMenu
import com.github.tanokun.tanorpg.player.menu.main.StatusPointMenu
import com.github.tanokun.tanorpg.player.menu.main.skill.SetSkillMenu
import com.github.tanokun.tanorpg.player.menu.util.AdminUtilMenu
import com.github.tanokun.tanorpg.player.status.KindOfStatusType
import com.github.tanokun.tanorpg.player.status.StatusType
import com.github.tanokun.tanorpg.util.createItem
import com.github.tanokun.tanorpg.util.getItemData
import com.github.tanokun.tanorpg.util.io.MapNode
import com.github.tanokun.tanorpg.util.item.ItemBuilder
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem
import com.github.tanokun.tanorpg.util.smart_inv.inv.InventoryListener
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.floor
import kotlin.math.roundToInt

class CreateSpecialCraftMenu(private val gui: String) : InventoryProvider {
    fun getInv(): SmartInventory {
        return SmartInventory.builder()
            .closeable(true)
            .provider(this)
            .size(6, 9)
            .title("§2§lCreateSpecialCraft")
            .id("CreateSpecialCraft")
            .update(false)
            .cancelable(false)
            .listener(InventoryListener(InventoryCloseEvent::class.java) {
                var gui = ""
                it.inventory.contents.forEach { item2 ->
                    val item = ((item2 == null) then ItemStack(Material.AIR)) ?: item2
                    gui += when (item.type){
                        Material.AIR -> "-"
                        Material.STONE -> "B"
                        Material.EMERALD -> "S"
                        Material.DIAMOND -> "P"
                        else -> "-"
                    }
                }

                Bukkit.getConsoleSender().sendMessage(gui)
            })
            .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        val barrier = ClickableItem.empty(ItemBuilder(Material.STONE).setDisplayName("§7障害物").setAmount(64).build())
        val buff = ClickableItem.empty(ItemBuilder(Material.EMERALD).setDisplayName("§aバフ").setAmount(64).build())
        val spawn = ClickableItem.empty(ItemBuilder(Material.DIAMOND).setDisplayName("§bスポーン位置").setAmount(64).build())
        if (gui != "") {
            val barrierLocation = ArrayList<MapNode<Int, Int>>()
            val buffLocation = ArrayList<MapNode<Int, Int>>()
            val spawnLocation = ArrayList<MapNode<Int, Int>>()
            var t = 0

            val array: ArrayList<String> = ArrayList()
            gui.split("").forEach { array.add(it) }

            array.removeAt(0)
            array.removeAt(54)

            for (row in 0..5) {
                for (column in 0..8) {
                    when (array[t]) {
                        "B" -> barrierLocation.add(MapNode(row, column))
                        "S" -> buffLocation.add(MapNode(row, column))
                        "P" -> spawnLocation.add(MapNode(row, column))
                    }
                    t++
                }
            }

            barrierLocation.forEach { contents[it.key, it.value] = barrier }
            buffLocation.forEach { contents[it.key, it.value] = buff }
            spawnLocation.forEach { contents[it.key, it.value] = spawn }

            return
        }
        contents[0, 0] = barrier
        contents[0, 1] = buff
        contents[0, 2] = spawn
    }
}