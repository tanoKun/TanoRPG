package com.github.tanokun.tanorpg.player.quests

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.TanoRPG.Companion.plugin
import com.github.tanokun.tanorpg.game.item.ItemRarityType
import com.github.tanokun.tanorpg.player.quests.actions.QuestAction
import com.github.tanokun.tanorpg.player.quests.conditions.QuestCondition
import com.github.tanokun.tanorpg.player.quests.tasks.QuestTask
import com.github.tanokun.tanorpg.util.io.Config
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.io.File
import java.util.function.Consumer


class QuestManager(p: Player?) {
    companion object {
        const val PX = "§6[§a-｜ §b§lQuest§a ｜-§6] §7=> "
    }

    private var quests = HashMap<Int, ArrayList<QuestModel>>()

    private var questsByID = HashMap<String, QuestModel>()

    init {
        val errors = HashSet<String>()
        if (p != null) p.sendMessage(TanoRPG.PX + "§bLoading quest configs...") else Bukkit.getConsoleSender()
            .sendMessage("[TanoRPG] §bLoading quest configs...")
        loadQuest(Config(plugin, "quests").file, errors)
        showErrors(errors, p)
        if (p != null) p.sendMessage(TanoRPG.PX + " ") else Bukkit.getConsoleSender().sendMessage(" ")
    }

    private fun loadQuest(tempFile: File, errors: HashSet<String>) {
        var path = "quests"
        var filePath = ""
        val files = tempFile.listFiles()
        for (tmpFile in files ?: arrayOfNulls(0)) {
            if (tmpFile.isDirectory) {
                loadQuest(tmpFile, errors)
            } else {
                try {
                    val config = Config(plugin, tmpFile, tmpFile.name)
                    filePath = "..." + File.separator + tempFile.name + File.separator + config.fileName + File.separator
                    path = "options.name"
                    val name = config.config.getString(path, "unknown")!!

                    path = "options.id"
                    val id = config.config.getString(path, "unknown")!!

                    path = "options.npc"
                    val npcId = config.config.getInt(path, 0)

                    path = "options.difficulty"
                    val difficulty = ItemRarityType.valueOf(config.config.getString(path, "COMMON")!!)

                    path = "options.lore"
                    val lore: MutableList<String> = ArrayList()
                    config.config.getStringList(path).forEach(Consumer { t: String -> lore.add("§f$t")})

                    path = "options.result"
                    val result: MutableList<String> = ArrayList()
                    config.config.getStringList(path).forEach(Consumer { t: String -> result.add("§f * $t") })

                    path = "options.flags"
                    val flags: MutableList<String> = config.config.getStringList(path)

                    /*
                    val cantToDoQuestShow = config.config.getBoolean("options.cantToDoQuestShow", false)
                    val minutes = config.config.getLong("options.minutes", 0)

                    path = "conditions"
                    val conditions: MutableList<com.github.tanokun.tanorpg.player.quest.condition.Condition> =
                        ArrayList<com.github.tanokun.tanorpg.player.quest.condition.Condition>()
                    for (condition in config.config.getConfigurationSection(path)!!.getKeys(false)) {
                        conditions.add(
                            ConditionType.valueOf(condition!!).condition.getConstructor(Config::class.java)
                                .newInstance(config)
                        )
                    }

                     */

                    path = "conditions"
                    val conditions = ArrayList<QuestCondition>()
                    config.config.getConfigurationSection(path)?.let {
                        it.getKeys(false).forEach { id ->
                            conditions.add(QuestCondition.getCondition(id, config))
                        }
                    }


                    path = "showQuestActions"
                    val showQuestActions: MutableList<QuestAction> = ArrayList()
                    config.config.getStringList(path).forEach { raw: String ->
                        val type = raw.split(" ")[0]
                        showQuestActions.add(QuestAction.getAction(type, raw.replace("$type ", "")))
                    }

                    path = "finishQuestActions"
                    val finishQuestActions: MutableList<QuestAction> = ArrayList()
                    config.config.getStringList(path).forEach { raw: String ->
                        val type = raw.split(" ")[0]
                        finishQuestActions.add(QuestAction.getAction(type, raw.replace("$type ", "")))
                    }

                    path = "cancelQuestActions"
                    val cancelQuestActions: MutableList<QuestAction> = ArrayList()
                    config.config.getStringList(path).forEach { raw: String ->
                        val type = raw.split(" ")[0]
                        cancelQuestActions.add(QuestAction.getAction(type, raw.replace("$type ", "")))
                    }

                    val questIndex: MutableList<QuestIndex> = ArrayList()
                    for (key in config.config.getKeys(false)) {
                        if (!key.contains("index_")) continue
                        path = "$key.actions"
                        val indexAction: MutableList<QuestAction> = ArrayList()
                        for (S_Index in config.config.getStringList(path)) {
                            val type = S_Index.split(" ")[0]
                            indexAction.add(QuestAction.getAction(type, S_Index.replace("$type ", "")))
                        }

                        path = "$key.tasks"
                        val indexTasks: MutableList<QuestTask<Any>> = ArrayList()
                        for (raw in config.config.getStringList(path)) {
                            val type = raw.split(" ")[0]
                            val task: QuestTask<Any> = QuestTask.getTask(type, raw.replace("$type ", ""), config)
                            indexTasks.add(task)
                        }
                        questIndex.add(QuestIndex(indexTasks, indexAction))
                    }
                    val quests = if (quests[npcId] == null) ArrayList() else quests[npcId]!!
                    val quest = QuestModel(id, name, npcId, lore, result, difficulty, conditions, showQuestActions, cancelQuestActions, finishQuestActions, questIndex, flags)
                    quests.add(quest)
                    this.quests[npcId] = quests
                    this.questsByID[id] = quest
                } catch (e: Exception) {
                    errors.add("§c    " + e.message + "§7" + "(Path: " + filePath + path + ")")
                }
            }
        }
    }

    private fun showErrors(errors: HashSet<String>, p: Player?) {
        if (errors.size == 0) errors.add("§a    Quest configs loaded without errors.")
        if (p != null) errors.forEach(Consumer { string: String -> p.sendMessage(string) })
        else errors.forEach { e: String -> Bukkit.getConsoleSender().sendMessage(e) }
    }

    fun getQuests(npc: Int): ArrayList<QuestModel>? = quests[npc]

    fun getQuest(id: String): QuestModel? = questsByID[id]

}