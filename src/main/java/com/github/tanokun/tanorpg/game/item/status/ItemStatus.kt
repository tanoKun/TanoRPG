package com.github.tanokun.tanorpg.game.item.status

import com.github.tanokun.tanorpg.game.item.status.generate.StatusGeneratorHandler
import com.github.tanokun.tanorpg.player.status.StatusMap

class ItemStatus(private val generators: ArrayList<StatusGeneratorHandler>) {
    fun generate(p: Double): StatusMap {
        val statusMap = StatusMap()
        generators.forEach {val i = it.generate(p); if (i != 0.0) statusMap.addStatus(it.statusType, i)}
        return statusMap
    }

    fun generateMax(): StatusMap {
        val statusMap = StatusMap()
        generators.forEach {val i = it.generateMax(); if (i != 0.0) statusMap.addStatus(it.statusType, i)}
        return statusMap
    }
}