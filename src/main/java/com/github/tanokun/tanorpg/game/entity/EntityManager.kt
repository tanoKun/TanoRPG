package com.github.tanokun.tanorpg.game.entity

import com.github.tanokun.tanorpg.DataManager
import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.TanoRPG.Companion.plugin
import com.github.tanokun.tanorpg.game.entity.boss.BossEntity
import com.github.tanokun.tanorpg.game.entity.boss.BossManager
import com.github.tanokun.tanorpg.game.item.drop.ItemTable
import com.github.tanokun.tanorpg.player.status.StatusMap
import com.github.tanokun.tanorpg.player.status.StatusType
import com.github.tanokun.tanorpg.util.io.Config
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.io.File

class EntityManager(player: Player?) {
    val bossManager = BossManager()
    val entities = HashMap<String, ObjectEntity>()
    private val ids = HashMap<String, String>()

    init {
        if (player != null) player.sendMessage(TanoRPG.PX + "§bLoading mob configs...")
        else Bukkit.getConsoleSender().sendMessage("[TanoRPG] §bLoading mob configs...")
        loadEntity(player, false, Config( plugin.mythicMobs, "Mobs").file)
    }

    private fun loadEntity(p: Player?, b: Boolean, tempFile: File) {
        var path = ""
        var filePath = ""
        val errors = HashSet<String>()
        if (!b) errors.add("§a    Mobs configs loaded without errors.")
        val files = tempFile.listFiles()
        for (tmpFile in files) {
            if (tmpFile.isDirectory) {
                loadEntity(p, true, tmpFile)
            } else try {
                val config = Config(plugin, tmpFile, tmpFile.name)
                filePath = "..." + File.separator + tempFile.name + File.separator + config.fileName + File.separator
                for (id in config.config.getKeys(false)) {
                    if (!config.config.isSet("$id.TanoRPG_level")) continue
                    path = "$id.Display"
                    val name = config.config.getString(path, "unknown")!!
                    path = "$id.TanoRPG_level"
                    val level = config.config.getInt(path, 0)
                    path = "$id.TanoRPG_exp"
                    val exp = config.config.getInt(path, 0)
                    val statusMap = StatusMap()
                    path = "$id.TanoRPG_statuses"
                    for (text in config.config.getConfigurationSection(path)!!.getKeys(false)) {
                        path = "$id.TanoRPG_statuses.$text"
                        statusMap.addStatus(StatusType.valueOf(text), config.config.getDouble(path, 0.0))
                        path = "$id.TanoRPG_statuses"
                    }
                    path = "$id.TanoRPG_drops"
                    var lootTable: ItemTable?
                    if (config.config.isSet(path)) {
                        lootTable = plugin.dropManager.getItemTable(config.config.getString(path)!!)
                        requireNotNull(lootTable) { "ルートテーブルが存在しません" }
                    } else {
                        lootTable = ItemTable(name, 1, 1, ArrayList())
                    }
                    val objectEntity = ObjectEntity(id, name, exp, level, statusMap, lootTable)
                    if (config.config.isSet("$id.TanoRPG_Boss.spawnLoc")) {
                        path = "$id.TanoRPG_Boss.spawnLoc"
                        val spawn = DataManager.getLocation(config, path)
                        path = "$id.TanoRPG_Boss.teleportLoc"
                        val teleport = DataManager.getLocation(config, path)
                        path = "$id.TanoRPG_Boss.clearLoc"
                        val clear = DataManager.getLocation(config, path)
                        path = "$id.TanoRPG_Boss.chest"
                        val itemTable = plugin.dropManager.getItemTable(config.config.getString(path)!!)
                        val bossEntity = BossEntity(objectEntity, clear, teleport, spawn, itemTable)
                        bossManager.register(id, bossEntity)
                    }
                    ids[name] = id
                    entities[id] = objectEntity
                }
            } catch (e: Exception) {
                errors.remove("§a    Mobs configs loaded without errors.")
                errors.add("§c    " + e.message + "§7" + "(Path: " + filePath + path + ")")
            }
        }
        showErrors(errors, p)
    }


    private fun showErrors(errors: HashSet<String>, p: Player?) {
        if (p != null) errors.stream().forEach { e: String -> p.sendMessage(e)
        } else errors.stream().forEach { e: String -> Bukkit.getConsoleSender().sendMessage(e) }
    }

    fun getID(name: String): String? {
        return ids[name]
    }

    fun getEntity(name: String): ObjectEntity? {
        return entities[name]
    }

    companion object {
        var AI = "TanoRPG_AI"
        var DISPLAY_NAME = "TanoRPG_displayName"
        @JvmField
        var ENTITY = "TanoRPG_entity"
    }
}