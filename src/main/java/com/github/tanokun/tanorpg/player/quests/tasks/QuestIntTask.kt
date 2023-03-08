package com.github.tanokun.tanorpg.player.quests.tasks

import com.github.tanokun.tanorpg.util.io.Config

abstract class QuestIntTask(config: Config, val excess: Boolean): QuestTask<Int>(config)