package com.github.tanokun.tanorpg.game.item.status.generate

import com.github.tanokun.tanorpg.player.status.StatusType

@StatusGenerator("--DOUBLE_LITERAL <literal>")
class DoubleLiteralGenerator(data: String, statusType: StatusType) : StatusGeneratorHandler(statusType) {
    private val number: Double

    init {
        number = data.toDouble()
    }

    override fun generate(p: Double): Double = number
    override fun generateMax(): Double = number
}
