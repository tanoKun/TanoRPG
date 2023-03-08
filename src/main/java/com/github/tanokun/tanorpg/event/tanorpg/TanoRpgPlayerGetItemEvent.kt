package com.github.tanokun.tanorpg.event.tanorpg

import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent
import org.bukkit.inventory.ItemStack

class TanoRpgPlayerGetItemEvent(who: Player?, val itemStack: ItemStack) : PlayerEvent(who!!), Cancellable {
    private var isCancelled = false
    override fun getHandlers(): HandlerList {
        return handlerList
    }

    override fun isCancelled(): Boolean {
        return isCancelled
    }

    override fun setCancelled(bl: Boolean) {
        isCancelled = bl
    }

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }
}