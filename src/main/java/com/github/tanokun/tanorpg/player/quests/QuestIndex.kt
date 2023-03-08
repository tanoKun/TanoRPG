package com.github.tanokun.tanorpg.player.quests

import com.github.tanokun.tanorpg.player.quests.actions.QuestAction
import com.github.tanokun.tanorpg.player.quests.tasks.QuestTask

data class QuestIndex(val tasks: List<QuestTask<Any>>, val actions: List<QuestAction>)