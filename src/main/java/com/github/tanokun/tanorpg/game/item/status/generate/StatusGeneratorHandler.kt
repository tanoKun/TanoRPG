package com.github.tanokun.tanorpg.game.item.status.generate

import com.github.tanokun.tanorpg.player.status.StatusType

abstract class StatusGeneratorHandler(val statusType: StatusType) {

    abstract fun generate(p: Double): Double

    abstract fun generateMax(): Double
}