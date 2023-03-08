package com.github.tanokun.tanorpg.event.tanorpg

import com.github.tanokun.tanorpg.damage.DamageType
import com.github.tanokun.tanorpg.game.entity.ActiveEntity
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class TanoRpgEntityDamageByPlayerEvent(who: Player, val entity: Entity, val activeEntity: ActiveEntity, var damage: Int, val damageType: DamageType) : PlayerEvent(who) {

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