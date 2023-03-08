package com.github.tanokun.tanorpg.game.craft.special

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.game.craft.CraftRecipe
import com.github.tanokun.tanorpg.game.item.drop.then
import com.github.tanokun.tanorpg.game.item.status.generate.StatusGeneratorHandler
import com.github.tanokun.tanorpg.player.quests.actions.QuestAction.Companion.actions
import com.github.tanokun.tanorpg.player.status.StatusMap
import com.github.tanokun.tanorpg.player.status.StatusType
import com.github.tanokun.tanorpg.util.getPersistent
import com.github.tanokun.tanorpg.util.io.MapNode
import com.github.tanokun.tanorpg.util.item.ItemBuilder
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem
import com.github.tanokun.tanorpg.util.smart_inv.inv.InventoryListener
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import kotlin.math.floor


class SpecialCraft(val player: Player, var amount: Int, private val buffs: ArrayList<StatusGeneratorHandler>, private val board: Array<String>, val recipe: CraftRecipe): InventoryProvider {
    private var inventoryContents: InventoryContents? = null

    private var isActive = false

    val gotBuff = StatusMap()

    private var lastLocation = MapNode(0, 0)

    private var currentLocation = MapNode(0, 0)
    set(value) {
        lastLocation = currentLocation

        if (value.key > 5) value.key = value.key - 6
        if (value.value > 8) value.value = value.value - 9

        field = value

        inventoryContents?.let {
            if (!it.get(field.key, field.value).isEmpty) {
                if (it.get(field.key, field.value).get().item.type == Material.STONE) {
                    Bukkit.getScheduler().runTask(TanoRPG.plugin, Runnable { TanoRPG.playSound(player, Sound.BLOCK_CHEST_OPEN, 10, 1.0) })
                    return
                }
            }
            update(player, it)
        }
    }
    val inv: SmartInventory = SmartInventory.builder()
        .id("status")
        .title("§9§lステータス調整")
        .update(false)
        .provider(this)
        .closeable(false)
        .size(6, 9)
        .listener(InventoryListener(InventoryClickEvent::class.java) { e ->
            if (e.clickedInventory === player.openInventory.topInventory) return@InventoryListener

            if (this.amount == 0) {
                TanoRPG.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1.0)
                player.sendMessage(TanoRPG.PX + "§c行動回数が0になりました")
                return@InventoryListener
            }

            Bukkit.getScheduler().runTaskAsynchronously(TanoRPG.plugin, Runnable {
                val gson = Gson()
                val type = object : TypeToken<ArrayList<MapNode<Int, Int>>>() {}.type
                val json = e.currentItem?.itemMeta?.persistentDataContainer?.get(NamespacedKey(TanoRPG.plugin, "special"), PersistentDataType.STRING)

                val actions = gson.fromJson<ArrayList<MapNode<Int, Int>>>(json, type)

                if (isActive) return@Runnable
                isActive = true
                this.amount--

                actions.forEach { action ->
                    object : BukkitRunnable() {
                        var t = 0
                        override fun run() {
                            t++
                            TanoRPG.playSound(player, Sound.BLOCK_GRASS_FALL, 10, 1.0)
                            if (t == 20) cancel()
                        }
                    }.runTaskTimer(TanoRPG.plugin, 0, 1)
                    currentLocation = MapNode(currentLocation.key + action.key, currentLocation.value + action.value)
                    Thread.sleep(1000)
                }

                Thread.sleep(1000)
                isActive = false

            })
        })
        .listener(InventoryListener(InventoryCloseEvent::class.java) {
            Bukkit.getScheduler().runTaskLater(TanoRPG.plugin, Runnable {
                if (!isActive) {
                    isActive = true
                    CompleteSpecialCraft(this).inv.open(player)
                    isActive = false
                }
            }, 1)
        })
        .build()

    private val boards = ArrayList<Board>()

    init {
        board.forEach { boardData ->
            val barrierLocation = ArrayList<MapNode<Int, Int>>()
            val buffLocation = ArrayList<MapNode<Int, Int>>()
            val spawnLocation = ArrayList<MapNode<Int, Int>>()
            var t = 0

            val array: ArrayList<String> = ArrayList()
            boardData.split("").forEach { array.add(it) }

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

            boards.add(Board(barrierLocation, buffLocation, spawnLocation))
        }
    }


    override fun init(player: Player, contents: InventoryContents) {
        inventoryContents?.let { ic ->
            for (r in 0..5) {
                for (c in 0..8) {
                    if (!ic[r, c].isEmpty) contents[r, c] = ic[r, c].get()
                }
            }
        }

        if (inventoryContents != null) return

        inventoryContents = contents

        val random = Random()

        val barrier = ClickableItem.empty(ItemBuilder(Material.STONE)
            .setDisplayName("  ")
            .addAllItemFlags()
            .build())

        val lore = ArrayList<String>()
        lore.add(" §7獲得スキル: ")
        lore.addAll(TanoRPG.plugin.itemManager.fromEvolutionStatus(gotBuff).map { "   $it" })

        val spawn = ClickableItem.empty(ItemBuilder(Material.CHEST)
            .setDisplayName("§b§l残り行動回数 -> §7($amount)  ")
            .setArrayListLore(lore)
            .addAllItemFlags()
            .build())

        boards[random.nextInt(boards.size)].also { board ->
            board.barrierLocation.forEach {
                contents[it.key, it.value] = barrier
            }
            board.spawnLocation.shuffled()[0].also { contents[it.key, it.value] = spawn; currentLocation.key = it.key; currentLocation.value = it.value; }


            board.buffLocation.shuffled().also { list -> for (i in 0 until buffs.size) {
                list[i].also { val buff = buffs[i]
                    val main = buff.generate(0.0)
                    val value: Double = ((buff.statusType.end == "%") then floor(main * 100) / 100) ?: main
                    val name = ((value < 0) then "§c${buff.statusType.getName()} ${value}${buff.statusType.end}") ?: "§a${buff.statusType.getName()} +${value}${buff.statusType.end}"
                    contents[it.key, it.value] = ClickableItem.empty(
                        ItemBuilder(buff.statusType.material)
                            .setDisplayName(name)
                            .setArrayLore(buff.statusType.dec)
                            .addAllItemFlags()
                            .setPersistent("type", PersistentDataType.STRING, buff.statusType.toString())
                            .setPersistent("value", PersistentDataType.DOUBLE, value.toDouble())
                            .build()) }
            } }
        }
    }

    override fun update(player: Player, contents: InventoryContents) {
        TanoRPG.playSound(player, Sound.BLOCK_GRASS_BREAK, 10, 1.0)

            if (!contents[currentLocation.key, currentLocation.value].isEmpty) {
                val item = contents[currentLocation.key, currentLocation.value].get()
                if (getPersistent(item.item, "type", PersistentDataType.STRING) != null) {
                    val type = StatusType.valueOf(getPersistent(item.item, "type", PersistentDataType.STRING)!!)
                    val value = getPersistent(item.item, "value", PersistentDataType.DOUBLE)!!

                    Bukkit.getScheduler().runTask(TanoRPG.plugin, Runnable {

                        val lore = ArrayList<String>()
                        lore.add(" §7獲得スキル: ")
                        lore.addAll(TanoRPG.plugin.itemManager.fromEvolutionStatus(gotBuff).map { "   $it" })

                        val spawn = ClickableItem.empty(ItemBuilder(Material.CHEST)
                            .setDisplayName("§b§l残り行動回数 -> §7($amount)  ")
                            .setArrayListLore(lore)
                                .addAllItemFlags()
                            .build())

                        contents[lastLocation.key, lastLocation.value] = ClickableItem.empty(ItemStack(Material.AIR))
                        contents[currentLocation.key, currentLocation.value] = spawn
                    })

                    Bukkit.getScheduler().runTask(TanoRPG.plugin, Runnable { TanoRPG.playSound(player, Sound.BLOCK_CHEST_OPEN, 10, 1.0) })
                    Thread.sleep(1000)

                    Bukkit.getScheduler().runTask(TanoRPG.plugin, Runnable {
                        TanoRPG.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 10, 1.0)
                        TanoRPG.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 10, 1.0)
                        player.showTitle(Title.title(Component.text(""),
                            Component.text("                                                             " +
                                    "§6「${type.getName()}§6」を $value${type.end} 取得しました！")))

                        gotBuff.addStatus(type, value)
                    })
                    Thread.sleep(1200)
                }
            }

        Bukkit.getScheduler().runTask(TanoRPG.plugin, Runnable {

            val lore = ArrayList<String>()
            lore.add(" §7獲得スキル: ")
            lore.addAll(TanoRPG.plugin.itemManager.fromEvolutionStatus(gotBuff).map { "   $it" })

            val spawn = ClickableItem.empty(ItemBuilder(Material.CHEST)
                .setDisplayName("§b§l残り行動回数 -> §7($amount)  ")
                .setArrayListLore(lore)
                .addAllItemFlags()
                .build())

            contents[lastLocation.key, lastLocation.value] = ClickableItem.empty(ItemStack(Material.AIR))
            contents[currentLocation.key, currentLocation.value] = spawn
        })
    }

    data class Board(
        val barrierLocation: ArrayList<MapNode<Int, Int>>,
        val buffLocation: ArrayList<MapNode<Int, Int>>,
        val spawnLocation: ArrayList<MapNode<Int, Int>>,
    )

    data class SpecialData(
        var amount: Int,
        val buffs: ArrayList<StatusGeneratorHandler>,
        val board: Array<String>,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as SpecialData

            if (amount != other.amount) return false
            if (buffs != other.buffs) return false
            if (!board.contentEquals(other.board)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = amount
            result = 31 * result + buffs.hashCode()
            result = 31 * result + board.contentHashCode()
            return result
        }
    }
}

