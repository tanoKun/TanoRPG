package com.github.tanokun.tanorpg.player.quests.tasks.models

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.player.quests.QuestManager
import com.github.tanokun.tanorpg.player.quests.actions.QuestAction
import com.github.tanokun.tanorpg.player.quests.data.task.TaskBooleanData
import com.github.tanokun.tanorpg.player.quests.data.task.TaskPlayerData
import com.github.tanokun.tanorpg.player.quests.tasks.QuestBooleanTask
import com.github.tanokun.tanorpg.player.quests.utils.annotation.TaskData
import com.github.tanokun.tanorpg.util.io.Config
import com.github.tanokun.tanorpg.util.regex.RegexMatcher
import net.citizensnpcs.api.event.NPCRightClickEvent
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler

@TaskData("talkToNPC", "<Int:npcId> \"<String:id>\" \"<String:message>\"", "buyShop [count] [id] [message]", "[count] 個ショップで買い物をする")
class QuestTalkToNpcTask(regexMatcher: RegexMatcher, parameters: String, config: Config): QuestBooleanTask(config) {
    var npcId = 0

    var actions = ArrayList<QuestAction>()

    init {
        regexMatcher.matchResult(parameters) {
            npcId = it.get("npcId", 0)
            this.message = it.get("message", "")
            this.id = it.get("id", "")
        }

        config.config.getStringList("taskData.talkToNPC_${npcId}").forEach { raw: String ->
            val type = raw.split(" ")[0]
            actions.add(QuestAction.getAction(type, raw.replace("$type ", "")))
        }
    }

    override fun getMessage(taskData: TaskPlayerData<*>): String {
        taskData as TaskBooleanData
        var raw = message
        raw = if (taskData.getValue()) "§a${raw}" else "§f${raw}"

        return raw
    }

    override fun isCleared(obj: Any): Boolean {
        if (obj is Boolean) return false
        return (obj as Boolean)
    }

    override fun getData(config: Config, key: String): TaskBooleanData = TaskBooleanData(this, config.config.getBoolean(key, false))

    override fun getData(): TaskBooleanData = TaskBooleanData(this, false)

    override fun clear(taskPlayerData: TaskPlayerData<Boolean>, player: Player) {
        if (taskPlayerData.getValue() && !taskPlayerData.flag) {
            taskPlayerData.flag = true
            player.sendMessage("${QuestManager.PX}タスクをクリアしました")
            TanoRPG.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 3, 0.6)
        }
    }

    @EventHandler
    fun running(e: NPCRightClickEvent) {
        val member = TanoRPG.plugin.memberManager.getMember(e.clicker.uniqueId) ?: return
        if (member.questMap.activeQuest != "") member.questMap.quests[member.questMap.activeQuest]?.let { questPlayerData ->
            questPlayerData.indexTaskData
                .filter { it.task.javaClass == QuestTalkToNpcTask::class.java }
                .map { it as TaskBooleanData }
                .forEach { taskData ->
                    taskData.task as QuestTalkToNpcTask
                    if (taskData.task.npcId != e.npc.id || member.questMap.isAction || taskData.flag) return
                    member.questMap.isAction = true
                    Bukkit.getScheduler().runTaskAsynchronously(TanoRPG.plugin, Runnable {
                        taskData.task.actions.forEach { a ->
                            if (!member.player.isOnline) {
                                member.questMap.isAction = false
                                return@Runnable
                            }

                            a.run(member)

                        }

                        Bukkit.getScheduler().runTask(TanoRPG.plugin, Runnable {
                            taskData.setValue(true, e.clicker, questPlayerData)
                            TanoRPG.plugin.sidebarManager.updateSidebar(e.clicker, member)
                        })
                    })
                }
        }
    }
}