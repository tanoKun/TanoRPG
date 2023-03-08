package com.github.tanokun.tanorpg.game.entity.gathering

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.TanoRPG.Companion.plugin
import com.github.tanokun.tanorpg.game.item.drop.ItemTable
import com.github.tanokun.tanorpg.util.io.Config
import com.github.tanokun.tanorpg.util.regex.RegexMatcher
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.io.File

class GatheringManager(p: Player?) {
    val gatherings = HashMap<String, Gathering>()

    init {
        if (p != null) p.sendMessage(TanoRPG.PX + "§bLoading gathering configs...")
        else Bukkit.getConsoleSender().sendMessage("[TanoRPG] §bLoading gathering configs...")
        loadGathering(p, false, Config(plugin.mythicMobs,"Mobs" + File.separator + "gathering").file)
    }

    private fun loadGathering(p: Player?, b: Boolean, tempFile: File) {
        var path = ""
        var filePath = ""
        val errors = HashSet<String>()
        if (!b) errors.add("§a    Gathering configs loaded without errors.")
        val files = tempFile.listFiles()
        for (tmpFile in files) {
            if (tmpFile.isDirectory) {
                loadGathering(p, true, tmpFile)
            } else try {
                val config = Config(plugin, tmpFile, tmpFile.name)
                filePath = "..." + File.separator + tempFile.name + File.separator + config.fileName + File.separator
                for (id in config.config.getKeys(false)) {

                    path = "$id.gathering.dropTable"
                    var lootTable: ItemTable?
                    if (config.config.isSet(path)) {
                        lootTable = plugin.dropManager.getItemTable(config.config.getString(path)!!)
                        require(lootTable != null) { "ルートテーブルが存在しません" }
                    } else {
                        lootTable = ItemTable(id, 1, 1, ArrayList())
                    }

                    path = "$id.timer"
                    val timer = config.config.getInt(path, 1)

                    path = "$id.gathering.run"
                    val run = ArrayList<RunGatheringData>()
                    var r = RegexMatcher("<Material:material>:<Int:model> <Sound:sound>:<Int:volume>:<Double:pitch> <Int:nextTimeTick>")
                    val r2 = RegexMatcher("<Material:material>:<Int:model>")
                    var b = false
                    var item = ItemStack(Material.AIR)
                    for (string in config.config.getStringList(path)) {
                        if (!b) {
                            r2.matchResult(string) {
                                item = ItemStack(it.get("material", Material.AIR))
                                val itemMeta = item.itemMeta
                                itemMeta.setCustomModelData(it.get("model", 1))
                                item.itemMeta = itemMeta
                            }
                            b = true
                        }

                        r.matchResult(string) {
                            val sound: Sound = it.get("sound", Sound.BLOCK_GRASS_BREAK)
                            val volume: Int = it.get("volume", 1)
                            val pitch: Double = it.get("pitch", 1.0)
                            val nextTime: Int = it.get("nextTimeTick", 1)

                            val item = ItemStack(it.get("material", Material.AIR))
                            val itemMeta = item.itemMeta
                            itemMeta.setCustomModelData(it.get("model", 1))
                            item.itemMeta = itemMeta
                            run.add(RunGatheringData(item, sound, volume, pitch, nextTime))
                        }
                    }

                    path = "$id.gathering.locations"
                    val locs = ArrayList<Location>()
                    r = RegexMatcher("<World:world> <Double:x> <Double:y> <Double:z>")
                    for (string in config.config.getStringList(path)) {
                        r.matchResult(string) {
                            locs.add(Location(it.get("world", Bukkit.getWorld("world")), it.get("x", 1.0), it.get("y", 1.0), it.get("z", 1.0)))
                        }
                    }

                    path = "$id.gathering.tools"
                    val tools = config.config.getStringList(path)

                    path = "$id.gathering.flags"
                    val gatheringFlags = config.config.getStringList(path)

                    Gathering(id, run, locs, lootTable, tools, item, timer, config, GatheringFlags(gatheringFlags)).apply {
                        gatherings[id] = this
                        this.init()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                errors.remove("§a    Gathering configs loaded without errors.")
                errors.add("§c   " + e.message + "§7" + "(Path: " + filePath + path + ")")
            }
        }
        showErrors(errors, p)
    }

    private fun showErrors(errors: java.util.HashSet<String>, p: Player?) {
        if (p != null) errors.stream().forEach { e: String -> p.sendMessage(e)
        } else errors.stream().forEach { e: String -> Bukkit.getConsoleSender().sendMessage(e) }
    }
}