package com.github.tanokun.tanorpg.player.quests.data.task

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.player.quests.QuestManager
import com.github.tanokun.tanorpg.player.quests.data.QuestPlayerData
import com.github.tanokun.tanorpg.player.quests.tasks.QuestTask
import com.github.tanokun.tanorpg.util.SaveMarker
import org.bukkit.Sound
import org.bukkit.entity.Player

abstract class TaskPlayerData<T>(open val task: QuestTask<T>, private var value: T): SaveMarker<TaskPlayerData<T>> {
    var flag = false

    fun setValue(value: T, player: Player, questPlayerData: QuestPlayerData) {
        this.value = value

        task.clear(this, player)
        if (!questPlayerData.indexTaskData.none { !it.flag } || questPlayerData.flag) return
        questPlayerData.flag = true
        player.sendMessage("${QuestManager.PX}§bクエスト「${questPlayerData.questModel.name}§b」の報告条件を達成しました！")

        TanoRPG.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 3, 1.0)
    }

    fun setValue(value: T) {
        this.value = value
    }


    fun getValue(): T = value
}