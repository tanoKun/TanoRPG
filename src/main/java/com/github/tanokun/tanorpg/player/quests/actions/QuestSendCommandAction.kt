package com.github.tanokun.tanorpg.player.quests.actions

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.player.Member
import com.github.tanokun.tanorpg.player.quests.utils.annotation.ActionData
import com.github.tanokun.tanorpg.util.regex.RegexMatcher
import org.bukkit.Bukkit

@ActionData("sendCommand", "\"<String:command>\" <String:sender>", "send \"[text]\"", "[text]という文字列をplayerに送ります")
class QuestSendCommandAction(regexMatcher: RegexMatcher, parameters: String): QuestAction {
    private var command: String = ""

    private var sender = ""

    init {
        regexMatcher.matchResult(parameters) {
            command = it.get("command", "")
            sender = it.get("sender", "")
            if (sender != "console" && sender != "player") throw IllegalArgumentException("senderは「console」か「player」と記入してください")
        }
    }

    override fun run(m: Member) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(TanoRPG.plugin) {
            if (sender == "console") Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                command.replace("[player]", m.player.name)
            ) else if (sender == "player") Bukkit.dispatchCommand(
                m.player,
                command.replace("[player]", m.player.name)
            )
        }
    }
}