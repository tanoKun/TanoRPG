package com.github.tanokun.tanorpg.player

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.game.item.drop.ChestData
import com.github.tanokun.tanorpg.util.SaveMarker
import com.github.tanokun.tanorpg.util.io.Coding
import com.github.tanokun.tanorpg.util.io.Config
import org.bukkit.Location
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


data class ChestMap(var playerChests: HashMap<Location, PlayerChest>) : SaveMarker<ChestMap> {

    fun isSet(location: Location): Boolean {
        return playerChests.containsKey(location)
    }

    override fun save(config: Config, key: String) {
        for(playerChest in playerChests.values) playerChest.save(config, "")
    }

    override fun load(config: Config, key: String): ChestMap {
        if (config.config.getConfigurationSection("chests") == null) return this;
        for (key in config.config.getConfigurationSection("chests")!!.getKeys(false)) {
            val l = config.config.getLocation("chests.${key}.location")
            val format = DateTimeFormatter.ofPattern("MM/dd HH:mm:ss")
            val chestData = TanoRPG.plugin.dropManager.getChest(l!!)
            val t = ZonedDateTime.parse(config.config.getString("chests.${key}.openedTime"), format)
            playerChests[l] = PlayerChest(chestData!!, t as ZonedDateTime)
        }
        return this
    }

    data class PlayerChest(var chestData: ChestData, var openedTime: ZonedDateTime) : SaveMarker<PlayerChest> {

        fun canReopen(): Boolean {
            if (chestData.reopenSecs <= 0) return false
            val now = LocalDateTime.now()
            val diffInSeconds = Duration.between(openedTime, now).toSeconds()
            return diffInSeconds >= chestData.reopenSecs
        }

        override fun save(config: Config, key: String) {
            config.config.run {
                val format = DateTimeFormatter.ofPattern("MM/dd HH:mm:ss")
                set("chests.${chestData.itemTable.name}${Coding.encode(chestData.location.x.toString())}.location", chestData.location)
                set("chests.${chestData.itemTable.name}${Coding.encode(chestData.location.x.toString())}.openedTime", openedTime.format(format))
            }
        }

        override fun load(config: Config, key: String): PlayerChest? {
            return null
        }
    }
}