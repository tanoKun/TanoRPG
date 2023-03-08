package com.github.tanokun.tanorpg.event.tanorpg

import com.github.tanokun.tanorpg.player.Member
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class TanoRpgSellEvent(who: Player?, val member: Member) : PlayerEvent(who!!) {

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