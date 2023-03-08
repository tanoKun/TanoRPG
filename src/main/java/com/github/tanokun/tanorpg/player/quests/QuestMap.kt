package com.github.tanokun.tanorpg.player.quests

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.player.quests.data.QuestPlayerData
import com.github.tanokun.tanorpg.util.SaveMarker
import com.github.tanokun.tanorpg.util.io.Config
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class QuestMap: SaveMarker<QuestMap> {
    val clearQuests = HashMap<String, LocalDateTime>()

    val quests = HashMap<String, QuestPlayerData>()

    var activeQuest: String = ""

    var isAction = false

    override fun save(config: Config, key: String) {
        config.config.set("activeQuest", activeQuest)

        val format = DateTimeFormatter.ofPattern("yyyy:MM/dd HH:mm")
        clearQuests.forEach {
            config.config.set("clearQuests.${it.key}", format.format(it.value))
        }

        config.config.set("questData", null)

        quests.forEach {
            it.value.save(config, "questData.${it.key}")
        }
        config.saveConfig()
    }

    override fun load(config: Config, key: String): QuestMap {
        val format = DateTimeFormatter.ofPattern("yyyy:MM/dd HH:mm")
        config.config.getConfigurationSection("clearQuests")?.let { s ->
            s.getKeys(false).forEach { id ->
                clearQuests[id] = LocalDateTime.parse(config.config.getString("clearQuests.${id}"), format)
            }
        }

        activeQuest = config.config.getString("activeQuest", "")!!

        config.config.getConfigurationSection("questData")?.let { s ->
            s.getKeys(false).forEach { id ->
                val questModel = TanoRPG.plugin.questManager.getQuest(id)
                questModel?.let { quests[id] = QuestPlayerData(it, false).load(config, "questData.${id}") }
            }
        }

        return this
    }
}