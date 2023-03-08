package com.github.tanokun.tanorpg.game.entity

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.damage.damageByNull
import com.github.tanokun.tanorpg.player.status.StatusType
import org.bukkit.attribute.Attributable
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import java.util.*
import kotlin.math.roundToInt

open class ActiveEntity(val objectEntity: ObjectEntity, val entity: Entity) {
    var health: Int = (entity as Attributable).getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value.roundToInt()

    fun removeHealth(value: Int, player: Player): Int {
        val r = health

        health -= value
        if (health <= 0) {
            entity.setMetadata("isDead", FixedMetadataValue(TanoRPG.plugin, player))
            damageByNull(entity, 10000000000.0)
        }

        return r
    }

    val exp = HashMap<UUID, Double>()

    val oneExp: Double
        get() {
            val exp = objectEntity.exp.toDouble()
            val hp = (entity as Attributable).getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: return 0.0
            return exp / hp
        }
}