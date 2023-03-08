package com.github.tanokun.tanorpg.player.quests.conditions

import com.github.tanokun.tanorpg.util.io.Config
import org.bukkit.entity.Player

abstract class QuestCondition {
    companion object {
        val conditions = HashMap<String, Class<out QuestCondition>>()

        fun getCondition(name: String, config: Config): QuestCondition {
            val action = conditions[name] ?: throw NullPointerException("conditionが存在しません -> (${name})")
            return action.constructors[0].newInstance(config) as QuestCondition
        }
    }

    abstract fun apply(player: Player): Boolean
}