package com.github.tanokun.tanorpg.player.quests.actions

import com.github.tanokun.tanorpg.player.Member
import com.github.tanokun.tanorpg.player.quests.utils.annotation.ActionData
import com.github.tanokun.tanorpg.util.regex.RegexMatcher

interface QuestAction {

    companion object {
        val actions = HashMap<String, Class<out QuestAction>>()

        fun getAction(name: String, parameters: String): QuestAction {
            val action = actions[name] ?: throw NullPointerException("actionが存在しません -> (${name})")
            val annotation = action.getAnnotation(ActionData::class.java) as ActionData

            val regex = RegexMatcher(annotation.parameters)

            return action.constructors[0].newInstance(regex, parameters) as QuestAction
        }
    }

    fun run(member: Member)
}