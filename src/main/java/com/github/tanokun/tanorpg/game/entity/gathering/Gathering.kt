package com.github.tanokun.tanorpg.game.entity.gathering

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.game.item.drop.ItemTable
import com.github.tanokun.tanorpg.util.io.Config
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderInt
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack
import kotlin.math.pow
import kotlin.math.roundToInt

class Gathering(val id: String, val runGatherings: List<RunGatheringData>, private val spawnLocations: ArrayList<Location>,
                val itemTable: ItemTable, val tool: List<String>, val item: ItemStack, val time: Int, val config: Config, val gatheringFlags: GatheringFlags) {

    fun init() {
        val spawnerManager = TanoRPG.plugin.mythicMobs.spawnerManager

        for (i in 1..spawnLocations.size) {
            var r = false
            spawnerManager.getSpawnerByName("gathering_${id}_${i}")?.let {
                it.location = BukkitAdapter.adapt(spawnLocations[i - 1])
                it.activationRange = 10.0.pow(4.0).roundToInt()
                it.mobsPerSpawn = 1
                it.maxMobs = PlaceholderInt.of("1")
                it.spawnRadius = 1
                it.cooldownSeconds = time
                r = true
            }

            if (r) continue

            val spawner = spawnerManager.createSpawner("gathering_${id}_${i}", spawnLocations[i - 1], id)
            spawner.activationRange = 10.0.pow(4.0).roundToInt()
            spawner.mobsPerSpawn = 1
            spawner.maxMobs = PlaceholderInt.of("1")
            spawner.spawnRadius = 3
            spawner.cooldownSeconds = time
        }
    }

    fun getSpawnLocations(): ArrayList<Location> = spawnLocations
}

data class RunGatheringData(val item: ItemStack, val sound: Sound, val volume: Int, val pitch: Double, val nextTime: Int)

class GatheringFlags(private val flags: List<String>) {
    fun hasFlag(key: String): Boolean = flags.contains(key)
}