package com.github.tanokun.tanorpg.event.tanorpg

import com.github.tanokun.tanorpg.game.entity.ObjectEntity
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

data class TanoRpgEntityKillEvent(val who: Player, val entity: Entity, val objectEntity: ObjectEntity) : PlayerEvent(who) {

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }
}