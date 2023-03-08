package com.github.tanokun.tanorpg.player.quests.actions

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.player.Member
import com.github.tanokun.tanorpg.player.quests.utils.annotation.ActionData
import com.github.tanokun.tanorpg.player.status.buff.BuffType
import com.github.tanokun.tanorpg.util.regex.RegexMatcher
import org.bukkit.Bukkit

@ActionData("giveBuff", "<String:buff> <Int:time>", "giveBuff [buff] [time]", "Buff効果を[time]秒付与します")
class QuestGiveBuffAction(regexMatcher: RegexMatcher, parameters: String): QuestAction {
    private var buffType: BuffType = BuffType.ATK_UP_L

    private var time: Int = 1

    init {
        regexMatcher.matchResult(parameters) {
            buffType = BuffType.valueOf(it.get("buff", ""))
            time = it.get("time", 1)
        }
    }

    override fun run(member: Member) {
        Bukkit.getScheduler().runTask(TanoRPG.plugin, Runnable { member.buffMap.addBuff(buffType, time, member.player) })
    }
}