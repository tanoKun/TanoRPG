package com.github.tanokun.tanorpg.player.quests.data.task

import com.github.tanokun.tanorpg.player.quests.tasks.QuestBooleanTask
import com.github.tanokun.tanorpg.util.io.Config

class TaskBooleanData(override val task: QuestBooleanTask, value: Boolean) : TaskPlayerData<Boolean>(task, value) {

    override fun save(config: Config, key: String) {
        config.config.set("$key.value", this.getValue())
        config.config.set("$key.flag", this.flag)
        config.saveConfig()
    }

    override fun load(config: Config, key: String): TaskPlayerData<Boolean> {
        this.setValue(config.config.getBoolean("$key.value", false))
        this.flag = config.config.getBoolean("$key.flag", false)
        return this
    }
}