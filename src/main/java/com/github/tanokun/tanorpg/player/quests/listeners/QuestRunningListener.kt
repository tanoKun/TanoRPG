package com.github.tanokun.tanorpg.player.quests.listeners

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.TanoRPG.Companion.plugin
import com.github.tanokun.tanorpg.player.Member
import com.github.tanokun.tanorpg.player.quests.QuestManager
import com.github.tanokun.tanorpg.player.quests.data.QuestPlayerData
import com.github.tanokun.tanorpg.player.quests.menus.QuestListMenu
import com.github.tanokun.tanorpg.player.quests.tasks.models.QuestTalkToNpcTask
import com.github.tanokun.tanorpg.util.variable.Variables
import net.citizensnpcs.api.event.NPCRightClickEvent
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.time.LocalDateTime


class QuestRunningListener: Listener {
    @EventHandler
    fun onTalking(e: NPCRightClickEvent) {
        TanoRPG.plugin.memberManager.getMember(e.clicker.uniqueId)?.let { member ->
            if (member.questMap.isAction || TanoRPG.plugin.questManager.getQuests(e.npc.id) == null) return

            member.questMap.quests[member.questMap.activeQuest]?.let { if (it.flag && it.questModel.npcID == e.npc.id) { nextTask(member, it); return } }

            for (quest in member.questMap.quests) {
                if (e.npc.id == quest.value.questModel.npcID) return
                if (quest.value.indexTaskData
                        .filter { it.task.javaClass == QuestTalkToNpcTask::class.java }
                        .map { it.task as QuestTalkToNpcTask }
                        .any { it.npcId == e.npc.id }
                ) return
            }

            QuestListMenu(member, e.npc.id).getInv().open(e.clicker)
        }
    }

    private fun nextTask(member: Member, questPlayerData: QuestPlayerData) {
        member.questMap.isAction = true
        if (questPlayerData.questModel.questIndex.size <= questPlayerData.index + 1) { questClear(member, questPlayerData); return }
        questPlayerData.index++
        questPlayerData.indexTaskData.clear()
        val questIndex = questPlayerData.questModel.questIndex[questPlayerData.index]
        questIndex.tasks.forEach { questPlayerData.indexTaskData.add(it.getData()) }
        questPlayerData.flag = false

        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            questIndex.actions.forEach { a ->
                    if (!member.player.isOnline) {
                        member.questMap.isAction = false
                        return@forEach
                    }

                    a.run(member)
                }

            Bukkit.getScheduler().runTask(plugin, Runnable {
                member.questMap.isAction = false
                member.questMap.activeQuest = questPlayerData.questModel.id
                plugin.sidebarManager.updateSidebar(member.player, member)
                TanoRPG.playSound(member.player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 2.0)
            })
        })

    }

    private fun questClear(member: Member, questPlayerData: QuestPlayerData) {
        member.questMap.isAction = true

        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            questPlayerData.questModel.finishQuestActions.forEach { a ->
                if (!member.player.isOnline) {
                    member.questMap.isAction = false
                    return@forEach
                }

                a.run(member)
            }

            Bukkit.getScheduler().runTask(plugin, Runnable {
                val now = LocalDateTime.now()
                member.questMap.isAction = false
                member.questMap.activeQuest = ""
                questPlayerData.clearTime = now
                member.questMap.clearQuests[questPlayerData.questModel.id] = now
                member.questMap.quests.remove(questPlayerData.questModel.id)
                member.player.compassTarget = plugin.variables.getVariable(Variables.LOCATION_GUILD, Bukkit.getWorld("world")!!.spawnLocation)
                member.player.sendMessage("${QuestManager.PX}§dクエスト「${questPlayerData.questModel.name}§d」を完全クリアしました！")

                plugin.sidebarManager.updateSidebar(member.player, member)
                TanoRPG.playSound(member.player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 2.0)
            })
        })
    }
}