package com.github.tanokun.tanorpg.player.quests.tasks.models

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.event.tanorpg.TanoRpgShopEvent
import com.github.tanokun.tanorpg.player.quests.QuestManager
import com.github.tanokun.tanorpg.player.quests.data.task.TaskIntData
import com.github.tanokun.tanorpg.player.quests.data.task.TaskPlayerData
import com.github.tanokun.tanorpg.player.quests.tasks.QuestIntTask
import com.github.tanokun.tanorpg.player.quests.utils.annotation.TaskData
import com.github.tanokun.tanorpg.util.io.Config
import com.github.tanokun.tanorpg.util.regex.RegexMatcher
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler

@TaskData("buyShop", "<Int:count> \"<String:id>\" \"<String:message>\"", "buyShop [count] [id] [message]", "[count] 個ショップで買い物をする")
open class QuestBuyShopTask(regexMatcher: RegexMatcher, parameters: String, config: Config): QuestIntTask(config, true) {
    var count: Int = 1

    init {
        regexMatcher.matchResult(parameters) {
            count = it.get("count", 1)
            this.message = it.get("message", "")
            this.id = it.get("id", "")
        }
    }

    override fun getMessage(taskData: TaskPlayerData<*>): String {
        taskData as TaskIntData
        var raw: String = message.replace("[nec]", count.toString())
        raw = raw.replace("[count]", taskData.getValue().toString())

        raw = if (taskData.getValue() >= count) "§a${raw}" else "§f${raw}"

        return raw
    }

    override fun isCleared(obj: Any): Boolean {
        if (obj is Int) return false
        return (obj as Int) >= count
    }

    override fun getData(config: Config, key: String): TaskIntData = TaskIntData(this, config.config.getInt(key, 0))

    override fun getData(): TaskIntData = TaskIntData(this, 0)

    override fun clear(taskPlayerData: TaskPlayerData<Int>, player: Player) {
        if (taskPlayerData.getValue() >= count && !taskPlayerData.flag) {
            taskPlayerData.flag = true
            player.sendMessage("${QuestManager.PX}タスクをクリアしました")
            TanoRPG.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 3, 0.6)
        }
    }

    @EventHandler
    open fun running(e: TanoRpgShopEvent) {
        if (e.member.questMap.activeQuest != "") e.member.questMap.quests[e.member.questMap.activeQuest]?.let { questPlayerData ->
            questPlayerData.indexTaskData
                .filter { it.task.javaClass == QuestBuyShopTask::class.java}
                .map { it as TaskIntData }
                .forEach { taskData ->
                    taskData.task as QuestBuyShopTask
                    taskData.setValue(taskData.getValue() + 1, e.player, questPlayerData)
                    if (taskData.task.excess && taskData.getValue() > taskData.task.count) taskData.setValue(taskData.task.count, e.player, questPlayerData)
                    TanoRPG.plugin.sidebarManager.updateSidebar(e.player, e.member)
                }
        }
    }
}