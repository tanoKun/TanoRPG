package com.github.tanokun.tanorpg.game.item.status.generate

import com.github.tanokun.tanorpg.player.status.StatusType

enum class StatusGeneratorType(private val clazz: Class<out StatusGeneratorHandler>) {
    INT_LITERAL(IntLiteralGenerator::class.java),
    INT(IntGenerator::class.java),
    DOUBLE(DoubleGenerator::class.java),
    DOUBLE_LITERAL(DoubleLiteralGenerator::class.java),
    ;

    companion object {
        @JvmStatic
        fun get(statusGeneratorType: StatusGeneratorType, data: String, statusType: StatusType): StatusGeneratorHandler {
            val constructor = statusGeneratorType.clazz.constructors.first()
            return constructor.newInstance(data, statusType) as StatusGeneratorHandler
        }
    }

}