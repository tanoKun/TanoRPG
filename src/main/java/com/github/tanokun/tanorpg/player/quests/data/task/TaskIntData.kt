package com.github.tanokun.tanorpg.player.quests.data.task

import com.github.tanokun.tanorpg.player.quests.tasks.QuestIntTask
import com.github.tanokun.tanorpg.util.io.Config

class TaskIntData(override val task: QuestIntTask, value: Int) : TaskPlayerData<Int>(task, value) {

    override fun save(config: Config, key: String) {
        config.config.set("$key.value", this.getValue())
        config.config.set("$key.flag", this.flag)
        config.saveConfig()
    }

    override fun load(config: Config, key: String): TaskPlayerData<Int> {
        this.setValue(config.config.getInt("$key.value", 0))
        this.flag = config.config.getBoolean("$key.flag", false)
        return this
    }
}
