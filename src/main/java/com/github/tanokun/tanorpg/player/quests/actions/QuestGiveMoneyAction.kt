package com.github.tanokun.tanorpg.player.quests.actions

import com.github.tanokun.tanorpg.player.Member
import com.github.tanokun.tanorpg.player.quests.utils.annotation.ActionData
import com.github.tanokun.tanorpg.util.regex.RegexMatcher

@ActionData("giveMoney", "<Int:money>", "giveMoney [money]", "playerに[money]セル渡します")
class QuestGiveMoneyAction(regexMatcher: RegexMatcher, parameters: String): QuestAction {
    private var money = 0

    init {
        regexMatcher.matchResult(parameters) {
            money = it.get("money", 0)
        }
    }

    override fun run(member: Member) {
        member.addMoney(money.toLong())
    }
}