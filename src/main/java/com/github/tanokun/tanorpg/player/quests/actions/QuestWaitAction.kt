package com.github.tanokun.tanorpg.player.quests.actions

import com.github.tanokun.tanorpg.player.Member
import com.github.tanokun.tanorpg.player.quests.utils.annotation.ActionData
import com.github.tanokun.tanorpg.util.regex.RegexMatcher

@ActionData("wait", "<Int:tick>", "wait [tick]", "[tick] だけ一時停止します")
class QuestWaitAction(regexMatcher: RegexMatcher, parameters: String): QuestAction {
    var tick: Int = 0
    init {
        regexMatcher.matchResult(parameters) {
            tick = it.get("tick", 0)
        }
    }

    override fun run(member: Member) {
        Thread.sleep((tick * 50).toLong())
    }
}