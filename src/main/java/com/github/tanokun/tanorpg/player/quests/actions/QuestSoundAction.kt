package com.github.tanokun.tanorpg.player.quests.actions

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.player.Member
import com.github.tanokun.tanorpg.player.quests.utils.annotation.ActionData
import com.github.tanokun.tanorpg.util.regex.RegexMatcher
import org.bukkit.Sound

@ActionData("sound", "<Sound:sound> <Int:volume> <Double:pitch>", "sound [Sound] [Volume] [Pitch]", "playerに音を流します")
class QuestSoundAction(regexMatcher: RegexMatcher, parameters: String): QuestAction {
    var sound: Sound = Sound.AMBIENT_CAVE
    var volume: Int = 1
    var pitch: Double = 1.0

    init {
        regexMatcher.matchResult(parameters) {
            sound = it.get("sound", Sound.AMBIENT_CAVE)
            volume = it.get("valume", 1)
            pitch = it.get("pitch", 1.0)
        }
    }

    override fun run(member: Member) = TanoRPG.playSound(member.player, sound, volume, pitch)
}