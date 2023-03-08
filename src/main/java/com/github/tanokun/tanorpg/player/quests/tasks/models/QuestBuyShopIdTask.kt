package com.github.tanokun.tanorpg.player.quests.tasks.models

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.event.tanorpg.TanoRpgShopEvent
import com.github.tanokun.tanorpg.player.quests.data.task.TaskIntData
import com.github.tanokun.tanorpg.player.quests.utils.annotation.TaskData
import com.github.tanokun.tanorpg.util.getItemData
import com.github.tanokun.tanorpg.util.io.Config
import com.github.tanokun.tanorpg.util.regex.RegexMatcher
import org.bukkit.event.EventHandler

@TaskData("buyShopId", "'<String:itemId>' <Int:count> \"<String:id>\" \"<String:message>\"", "buyShopId [itemId] [count] [id] [message]", "[itemId]のアイテムを [count] 個ショップで買い物をする")
class QuestBuyShopIdTask(regexMatcher: RegexMatcher, parameters: String, config: Config) : QuestBuyShopTask(regexMatcher, parameters, config) {
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
    override fun running(e: TanoRpgShopEvent) {
        if (e.member.questMap.activeQuest != "") e.member.questMap.quests[e.member.questMap.activeQuest]?.let { questPlayerData ->
            questPlayerData.indexTaskData
                .filter { it.task.javaClass == QuestBuyShopIdTask::class.java }
                .map { it as TaskIntData }
                .forEach { taskData ->
                    taskData.task as QuestBuyShopIdTask
                    if (taskData.task.itemId != getItemData(e.item.product)!!.id) return
                    taskData.setValue(taskData.getValue() + 1, e.player, questPlayerData)
                    if (taskData.task.excess && taskData.getValue() > taskData.task.count) taskData.setValue(taskData.task.count, e.player, questPlayerData)
                    TanoRPG.plugin.sidebarManager.updateSidebar(e.player, e.member)
                }
        }
    }
}