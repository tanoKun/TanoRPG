package com.github.tanokun.tanorpg.player.quests.actions

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.player.Member
import com.github.tanokun.tanorpg.player.quests.utils.annotation.ActionData
import com.github.tanokun.tanorpg.util.regex.RegexMatcher
import org.bukkit.Bukkit

@ActionData("giveExp", "<Int:exp>", "giveExp [exp]", "playerに[exp] exp渡します")
class QuestGiveExpAction(regexMatcher: RegexMatcher, parameters: String): QuestAction {
    private var exp = 0

    init {
        regexMatcher.matchResult(parameters) {
            exp = it.get("exp", 0)
        }
    }

    override fun run(member: Member) {
        Bukkit.getScheduler().runTask(TanoRPG.plugin, Runnable { member.addHasEXP(exp.toLong()) })
    }
}