package com.github.tanokun.tanorpg.player.quests.tasks.models

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.event.tanorpg.TanoRpgCraftEvent
import com.github.tanokun.tanorpg.player.quests.data.task.TaskIntData
import com.github.tanokun.tanorpg.player.quests.utils.annotation.TaskData
import com.github.tanokun.tanorpg.util.getItemData
import com.github.tanokun.tanorpg.util.io.Config
import com.github.tanokun.tanorpg.util.regex.RegexMatcher
import org.bukkit.event.EventHandler


@TaskData("craftingId", "'<String:itemId>' <Int:count> \"<String:id>\" \"<String:message>\"", "craftingId [itemId] [count] [id] [message]", "[itemId]のアイテムを [count] 個クラフトする")
class QuestCraftingIdTask(regexMatcher: RegexMatcher, parameters: String, config: Config) : QuestCraftingTask(regexMatcher, parameters, config) {
    var itemId: String = ""
    init {
        regexMatcher.matchResult(parameters) {
            this.itemId = it.get("itemId", "")
            this.count = it.get("count", 1)
            this.message = it.get("message", "")
            this.id = it.get("id", "")
        }
    }

    @EventHandler
    override fun running(e: TanoRpgCraftEvent) {
        if (e.member.questMap.activeQuest != "") e.member.questMap.quests[e.member.questMap.activeQuest]?.let { questPlayerData ->
            questPlayerData.indexTaskData
                .filter { it.task.javaClass == QuestCraftingIdTask::class.java }
                .map { it as TaskIntData }
                .forEach { taskData ->
                    taskData.task as QuestCraftingIdTask
                    if (taskData.task.itemId != e.item.item.id) return
                    taskData.setValue(taskData.getValue() + 1, e.player, questPlayerData)
                    if (taskData.task.excess && taskData.getValue() > taskData.task.count) taskData.setValue(taskData.task.count, e.player, questPlayerData)
                    TanoRPG.plugin.sidebarManager.updateSidebar(e.player, e.member)
                }
        }
    }
}