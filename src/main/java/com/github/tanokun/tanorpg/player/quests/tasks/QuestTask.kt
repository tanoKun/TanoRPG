package com.github.tanokun.tanorpg.player.quests.tasks

import com.github.tanokun.tanorpg.player.quests.data.task.TaskPlayerData
import com.github.tanokun.tanorpg.player.quests.utils.annotation.TaskData
import com.github.tanokun.tanorpg.util.io.Config
import com.github.tanokun.tanorpg.util.regex.RegexMatcher
import org.bukkit.entity.Player
import org.bukkit.event.Listener

abstract class QuestTask<T>(val config: Config): Listener {
    var message: String = ""
    var id: String = ""

    companion object {
        val tasks = HashMap<String, Class<out QuestTask<Any>>>()

        fun getTask(name: String, parameters: String, config: Config): QuestTask<Any> {
            val task = tasks[name] ?: throw NullPointerException("taskが存在しません -> (${name})")
            val annotation = task.getAnnotation(TaskData::class.java)

            val regex = RegexMatcher(annotation.parameters)

            return task.constructors[0].newInstance(regex, parameters, config) as QuestTask<Any>
        }
    }

    abstract fun getMessage(taskData: TaskPlayerData<*>): String

    abstract fun isCleared(obj: Any): Boolean

    abstract fun getData(): TaskPlayerData<T>

    abstract fun getData(config: Config, key: String): TaskPlayerData<T>

    abstract fun clear(taskPlayerData: TaskPlayerData<T>, player: Player)
}