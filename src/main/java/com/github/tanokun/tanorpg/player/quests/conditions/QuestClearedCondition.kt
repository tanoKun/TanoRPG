package com.github.tanokun.tanorpg.player.quests.conditions

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.player.quests.utils.annotation.ConditionData
import com.github.tanokun.tanorpg.util.io.Config
import org.bukkit.entity.Player

@ConditionData("clearedQuests")
class QuestClearedCondition(config: Config): QuestCondition() {
    private val names: List<String>

    init {
        names = config.config.getStringList("conditions.clearedQuests")
    }

    override fun apply(player: Player): Boolean {
        val member = TanoRPG.plugin.memberManager.getMember(player.uniqueId) ?: return false
        for (name in names) if (!member.questMap.clearQuests.containsKey(name)) return false
        return true
    }
}