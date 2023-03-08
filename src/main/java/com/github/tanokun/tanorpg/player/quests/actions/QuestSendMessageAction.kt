package com.github.tanokun.tanorpg.player.quests.actions

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.player.Member
import com.github.tanokun.tanorpg.player.quests.utils.annotation.ActionData
import com.github.tanokun.tanorpg.util.regex.RegexMatcher

@ActionData("send", "\"<String:text>\"", "send \"[text]\"", "[text]という文字列をplayerに送ります")
class QuestSendMessageAction(regexMatcher: RegexMatcher, parameters: String): QuestAction {
    private var text: String = ""

    init {
        regexMatcher.matchResult(parameters) {
            text = it.get("text", "")
        }
    }

    override fun run(member: Member) {
        var string = text
        string = string.replace("[player]", member.player.name)
        val member = TanoRPG.plugin.memberManager.getMember(member.player.uniqueId)
        if (member != null) string = string.replace("[player_class]", member.skillClass.name)

        member.player.sendMessage(string)
    }
}