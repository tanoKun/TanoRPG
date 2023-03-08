package com.github.tanokun.tanorpg.player.quests.conditions

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.player.quests.utils.annotation.ConditionData
import com.github.tanokun.tanorpg.util.io.Config
import org.bukkit.entity.Player

@ConditionData("level")
class QuestLevelCondition(config: Config): QuestCondition() {
    private val level: Int

    init {
        level = config.config.getInt("conditions.level", 1)
    }

    override fun apply(player: Player): Boolean {
        val member = TanoRPG.plugin.memberManager.getMember(player.uniqueId) ?: return false
        return member.level.value >= level
    }
}