package com.github.tanokun.tanorpg.player.quests.data

import com.github.tanokun.tanorpg.player.quests.QuestModel
import com.github.tanokun.tanorpg.player.quests.data.task.TaskPlayerData
import com.github.tanokun.tanorpg.util.SaveMarker
import com.github.tanokun.tanorpg.util.io.Config
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class QuestPlayerData(val questModel: QuestModel, init: Boolean) : SaveMarker<QuestPlayerData> {

    var clearTime: LocalDateTime = LocalDateTime.now()

    var startTime: LocalDateTime = LocalDateTime.now()

    var index: Int = 0

    var indexTaskData: ArrayList<TaskPlayerData<Any>> = ArrayList()

    var flag: Boolean = false

    init {
        if (init) questModel.questIndex[index].tasks.forEach { indexTaskData.add(it.getData()) }
    }

    override fun load(config: Config, key: String): QuestPlayerData {
        val format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
        println(config.config.getString("${key}.startTime"))
        this.startTime = LocalDateTime.parse(config.config.getString("${key}.startTime"), format)
        this.clearTime = LocalDateTime.parse(config.config.getString("${key}.clearTime"), format)
        this.index = config.config.getInt("${key}.index", 0)
        this.flag = config.config.getBoolean("${key}.flag", false)
        questModel.questIndex[index].tasks.forEach {
            indexTaskData.add(it.getData().load(config, "${key}.taskData.${it.id}"))
        }

        return this
    }

    override fun save(config: Config, key: String) {
        val format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
        config.config.set("${key}.startTime", startTime.format(format))
        config.config.set("${key}.clearTime", clearTime.format(format))
        config.config.set("${key}.index", index)
        config.config.set("${key}.flag", flag)

        indexTaskData.forEach {
            it.save(config, "${key}.taskData.${it.task.id}")
        }

        config.saveConfig()
    }
}