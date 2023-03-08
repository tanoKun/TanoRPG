package com.github.tanokun.tanorpg.game.item.drop

import com.github.tanokun.tanorpg.DataManager
import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.TanoRPG.Companion.plugin
import com.github.tanokun.tanorpg.util.command.CommandContext
import com.github.tanokun.tanorpg.util.io.Folder
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import java.io.File

class DropManager(p: Player?) {
    val itemTables = HashMap<String, ItemTable>()
    private val chests = HashMap<Location, ChestData>()

    init {
        if (p != null) p.sendMessage(TanoRPG.PX + "§bLoading itemTable configs...") else Bukkit.getConsoleSender()
            .sendMessage("[TanoRPG] §bLoading itemTable configs...")
        load(p)
        if (p != null) p.sendMessage(TanoRPG.PX + " ") else Bukkit.getConsoleSender().sendMessage(" ")
    }

    fun load(p: Player?) {
        var path = ""
        var filePath = ""
        val errors = HashSet<String>()
        errors.add("§a    ItemTable configs loaded without errors.")
        try {
            path = "itemTables"
            for (config in Folder(plugin, path).getFiles()) {
                filePath = path + File.separator + config.fileName + File.separator
                for (id in config.config.getKeys(false)) {
                    val dropItems = ArrayList<DropItem>()
                    path = "$id.name"
                    val name = config.config.getString(path, "")!!
                    path = "$id.max"
                    val max = config.config.getInt(path, 3)
                    path = "$id.min"
                    val min = config.config.getInt(path, 1)
                    require(max > 0) { "maxは0以下にできません" }
                    require(max >= min) { "maxをmin未満にすることはできません" }
                    path = "$id.item"
                    for (string in config.config.getStringList(path)) {
                        val data = string.split(" ").toTypedArray()
                        val pra = data[1].toDouble()
                        requireNotNull(plugin.itemManager.getItem(data[0])) { "Itemが存在しません -> " + data[0] }
                        val to = IntArray(2)
                        to[0] = data[2].split("~").toTypedArray()[0].toInt()
                        to[1] = data[2].split("~").toTypedArray()[1].toInt()
                        require(to[0] <= to[1]) { "アイテム個数の設定が不正です 「x~y」という形にしてください" }
                        dropItems.add(DropItem(to[0], to[1], plugin.itemManager.getItem(data[0])!!, pra))
                    }
                    val itemTable = ItemTable(name, min, max, dropItems)
                    itemTables[id] = itemTable
                    path = "$id.chest"
                    if (config.config.isSet(path)) {
                        val pas = config.config.getStringList(path)
                        for (text in pas) {
                            val commandContext = CommandContext()
                            commandContext.init(null, text.split(" ").toTypedArray())
                            val location = DataManager.getLocationNoYawAndPitch(
                                commandContext.getString(0, "world")
                                        + " " + commandContext.getString(1, "0")
                                        + " " + commandContext.getString(2, "0")
                                        + " " + commandContext.getString(3, "0")
                            )
                            val s = commandContext.getFlagInteger("s", 0).toLong()
                            val m = commandContext.getFlagInteger("m", 0) * 60L
                            val h = commandContext.getFlagInteger("h", 0) * 3600L
                            val d = commandContext.getFlagInteger("d", 0) * 86400L
                            chests[location] = ChestData(itemTable, location, s + m + h + d)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            errors.remove("§a    ItemTable configs loaded without errors.")
            errors.add("§c    " + e.message + "§7" + "(Path: " + filePath + path + ")")
        }
        showErrors(errors, p)
    }

    private fun showErrors(errors: HashSet<String>, p: Player?) {
        if (p != null) errors.stream().forEach { e: String? ->
            p.sendMessage(
                e!!
            )
        } else errors.stream().forEach { e: String? -> Bukkit.getConsoleSender().sendMessage(e!!) }
    }

    fun getItemTable(id: String): ItemTable? {
        return itemTables[id]
    }

    fun getChest(location: Location): ChestData? {
        location.pitch = 0f
        location.yaw = 0f
        return chests[location]
    }
}