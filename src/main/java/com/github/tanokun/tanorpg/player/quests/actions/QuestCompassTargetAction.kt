package com.github.tanokun.tanorpg.player.quests.actions

import com.github.tanokun.tanorpg.player.Member
import com.github.tanokun.tanorpg.player.quests.utils.annotation.ActionData
import com.github.tanokun.tanorpg.util.regex.RegexMatcher
import org.bukkit.Bukkit
import org.bukkit.Location

@ActionData("compassTarget", "<BlockLocation:loc>", "compassTarget [location(world:x:y:z)]", "コンパスのターゲットを[location]に設定します")
class QuestCompassTargetAction(regexMatcher: RegexMatcher, parameters: String): QuestAction {
    var location: Location = Location(Bukkit.getWorld("world"),0.0, 0.0, 0.0)
    init {
        regexMatcher.matchResult(parameters) {
            location = it.get("loc", Location(Bukkit.getWorld("world"),0.0, 0.0, 0.0))
        }
    }

    override fun run(member: Member) { member.player.compassTarget = location }
}