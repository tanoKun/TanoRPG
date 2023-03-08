package com.github.tanokun.tanorpg.util

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.game.entity.ActiveEntity
import com.github.tanokun.tanorpg.game.entity.EntityManager
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.util.Vector
import kotlin.math.atan

fun getNearEntities(l: Location, radius: Double): Array<Entity> {
    val chunkRadius: Double = if (radius < 16) 1.0 else (radius - radius % 16) / 16
    val radiusEntities = HashSet<Entity>()
    try {
        var chX = 0 - chunkRadius
        while (chX <= chunkRadius) {
            var chZ = 0 - chunkRadius
            while (chZ <= chunkRadius) {
                val x = l.x.toInt()
                val y = l.y.toInt()
                val z = l.z.toInt()
                for (e in Location(l.world, x + chX * 16, y.toDouble(), z + chZ * 16).chunk.entities) {
                    if (e.location.distance(l) <= radius && e.location.block !== l.block) radiusEntities.add(e)
                }
                chZ++
            }
            chX++
        }
    } catch (e: NoSuchElementException) {
        return radiusEntities.toTypedArray()
    } catch (e: NullPointerException) {
        return radiusEntities.toTypedArray()
    }
    return radiusEntities.toTypedArray()
}

fun getNearPlayers(l: Location, radius: Double): Array<Player> {
    val chunkRadius: Double = if (radius < 16) 1.0 else (radius - radius % 16) / 16
    val radiusEntities = HashSet<Player>()
    try {
        var chX = 0 - chunkRadius
        while (chX <= chunkRadius) {
            var chZ = 0 - chunkRadius
            while (chZ <= chunkRadius) {
                val x = l.x.toInt()
                val y = l.y.toInt()
                val z = l.z.toInt()
                for (e in Location(l.world, x + chX * 16, y.toDouble(), z + chZ * 16).chunk.entities) {
                    if (e.location.distance(l) <= radius && e.location.block !== l.block && e is Player) radiusEntities.add(e)
                }
                chZ++
            }
            chX++
        }
    } catch (e: NoSuchElementException) {
        return radiusEntities.toTypedArray()
    } catch (e: NullPointerException) {
        return radiusEntities.toTypedArray()
    }
    return radiusEntities.toTypedArray()
}

fun getNearActiveEntity(l: Location, radius: Double): Array<Entity> {
    val chunkRadius: Double = if (radius < 16) 1.0 else (radius - radius % 16) / 16
    val radiusEntities = HashSet<Entity>()
    try {
        var chX = 0 - chunkRadius
        while (chX <= chunkRadius) {
            var chZ = 0 - chunkRadius
            while (chZ <= chunkRadius) {
                val x = l.x.toInt()
                val y = l.y.toInt()
                val z = l.z.toInt()
                for (e in Location(l.world, x + chX * 16, y.toDouble(), z + chZ * 16).chunk.entities) {
                    if (e.location.distance(l) <= radius && e.location.block !== l.block && e.hasMetadata("TanoRPG_entity")) radiusEntities.add(e)
                }
                chZ++
            }
            chX++
        }
    } catch (e: NoSuchElementException) {
        return radiusEntities.toTypedArray()
    } catch (e: NullPointerException) {
        return radiusEntities.toTypedArray()
    }
    return radiusEntities.toTypedArray()
}

fun getActiveEntity(entity: Entity): ActiveEntity? {
    return if (!entity.hasMetadata("TanoRPG_entity")) null else entity.getMetadata("TanoRPG_entity")[0].value() as ActiveEntity?
}

fun getAI(entity: Entity): ActiveEntity? {
    return if (!entity.hasMetadata(EntityManager.AI)) null else entity.getMetadata(EntityManager.AI)[0].value() as ActiveEntity?
}

fun getDisplayName(entity: Entity): ActiveEntity? {
    return if (!entity.hasMetadata(EntityManager.DISPLAY_NAME)) null else entity.getMetadata(EntityManager.DISPLAY_NAME)[0].value() as ActiveEntity?
}

fun isActiveEntity(entity: Entity): Boolean {
    return entity.hasMetadata("TanoRPG_entity")
}

fun setActiveEntity(entity: Entity, activeEntity: ActiveEntity?) {
    entity.setMetadata("TanoRPG_entity", FixedMetadataValue(TanoRPG.plugin, activeEntity))
}

fun chance(chance: Double): Boolean {
    var chance = chance
    chance /= 100
    return Math.random() <= chance
}


fun getLookAtYaw(motion: Vector): Float {
    val dx = motion.x
    val dz = motion.z
    var yaw = 0.0
    if (dx != 0.0) {
        yaw = if (dx < 0) {
            1.5 * Math.PI
        } else {
            0.5 * Math.PI
        }
        yaw -= atan(dz / dx)
    } else if (dz < 0) {
        yaw = Math.PI
    }
    return (-yaw * 180 / Math.PI - 90).toFloat()
}