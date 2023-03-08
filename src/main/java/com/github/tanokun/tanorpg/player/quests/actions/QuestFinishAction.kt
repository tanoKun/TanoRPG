package com.github.tanokun.tanorpg.player.quests.actions

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.player.Member
import com.github.tanokun.tanorpg.player.quests.QuestManager
import com.github.tanokun.tanorpg.player.quests.utils.annotation.ActionData
import com.github.tanokun.tanorpg.util.regex.RegexMatcher
import com.github.tanokun.tanorpg.util.variable.Variables
import org.bukkit.Bukkit
import org.bukkit.Sound
import java.time.LocalDateTime

@ActionData("finishQuest", "", "finishQuest", "強制的にクエストを終了します")
class QuestFinishAction(regexMatcher: RegexMatcher, parameters: String): QuestAction {

    override fun run(member: Member) {
        member.questMap.isAction = true
        member.questMap.quests[member.questMap.activeQuest]?.let { questPlayerData ->
            Bukkit.getScheduler().runTaskAsynchronously(TanoRPG.plugin, Runnable {
                questPlayerData.questModel.finishQuestActions.forEach { a ->
                    if (!member.player.isOnline) {
                        member.questMap.isAction = false
                        return@forEach
                    }

                    a.run(member)
                }

                Bukkit.getScheduler().runTask(TanoRPG.plugin, Runnable {
                    val now = LocalDateTime.now()
                    member.questMap.isAction = false
                    member.questMap.activeQuest = ""
                    questPlayerData.clearTime = now
                    member.questMap.clearQuests[questPlayerData.questModel.id] = now
                    member.questMap.quests.remove(questPlayerData.questModel.id)
                    member.player.compassTarget = TanoRPG.plugin.variables.getVariable(Variables.LOCATION_GUILD, Bukkit.getWorld("world")!!.spawnLocation)
                    member.player.sendMessage("${QuestManager.PX}§dクエスト「${questPlayerData.questModel.name}§d」を完全クリアしました！")

                    TanoRPG.plugin.sidebarManager.updateSidebar(member.player, member)
                    TanoRPG.playSound(member.player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 2.0)
                })
            })
        }
    }
}