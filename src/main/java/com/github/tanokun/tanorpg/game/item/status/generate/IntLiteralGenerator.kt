package com.github.tanokun.tanorpg.game.item.status.generate

import com.github.tanokun.tanorpg.player.status.StatusType

@StatusGenerator("--INT_LITERAL <literal>")
class IntLiteralGenerator(data: String, statusType: StatusType) : StatusGeneratorHandler(statusType) {
    private val number: Int

    init {
        number = data.toInt()
    }

    override fun generate(p: Double): Double = number.toDouble()
    override fun generateMax(): Double = number.toDouble()
}
