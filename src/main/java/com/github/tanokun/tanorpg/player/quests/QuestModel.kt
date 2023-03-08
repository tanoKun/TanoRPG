package com.github.tanokun.tanorpg.player.quests

import com.github.tanokun.tanorpg.game.item.ItemRarityType
import com.github.tanokun.tanorpg.player.quests.actions.QuestAction
import com.github.tanokun.tanorpg.player.quests.conditions.QuestCondition
import com.github.tanokun.tanorpg.player.quests.data.QuestPlayerData

data class QuestModel(val id: String, val name: String, val npcID: Int, val lore: List<String>,
                      val result: List<String>, val difficulty: ItemRarityType,
                      val conditions: List<QuestCondition>,
                      val showQuestActions: List<QuestAction>,
                      val cancelQuestActions: List<QuestAction>,
                      val finishQuestActions: List<QuestAction>,
                      val questIndex: List<QuestIndex>,
                      val flags: List<String>) {

    fun getNewData(): QuestPlayerData {
        return QuestPlayerData(this, true)

    }
}