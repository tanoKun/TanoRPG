package com.github.tanokun.tanorpg.damage

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.TanoRPG.Companion.plugin
import com.github.tanokun.tanorpg.game.entity.ActiveEntity
import com.github.tanokun.tanorpg.player.Member
import com.github.tanokun.tanorpg.player.status.StatusType
import com.github.tanokun.tanorpg.util.variable.Variables
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import net.objecthunter.exp4j.ExpressionBuilder
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer
import org.bukkit.scheduler.BukkitRunnable
import java.time.Duration
import java.util.*
import kotlin.math.roundToInt

class EntityDamageExpression(rawDamage: Double, attacker: ActiveEntity?, val target: Member) {
    private var damage: Int = 0

    private var o = 100

    private val defScaling = ExpressionBuilder("(${attacker?.objectEntity?.level ?: 0} + 2*${o} + {(${attacker?.objectEntity?.level ?: 0}${o}) / (${target.level.value} + ${o})})"
            + "/ ((1 - 0)*(${target.level.value} + ${o}) + 2*${target.level.value} + $o + " +
            "(${target.statusMap.getStatus(StatusType.DEF)}))")
        .build()

    private val attackDamage = ExpressionBuilder("(${rawDamage} * ${defScaling.evaluate()})").build()

    init{
        damage =
            if (attacker == null) {
                rawDamage.roundToInt()
            } else {
                val p: Double = (Random().nextInt(5).toDouble()) / 100
                (attackDamage.evaluate().roundToInt() * (1 + p)).roundToInt()
        }

    }

    fun multi(double: Double): EntityDamageExpression {
        damage = (damage * (1 + double)).roundToInt()
        return this
    }

    fun attack(damageType: DamageType) {
        if (target.hasHP <= 0) return

        target.setHP(target.hasHP - damage)
        val hp: Int = target.hasHP
        target.player.damage(0.00000001)

        if (hp <= 0) {
            TanoRPG.playSound(target.player, Sound.ENTITY_WITHER_SPAWN, 1, 1.0)
            target.player.gameMode = GameMode.SPECTATOR
            object : BukkitRunnable() {
                var i = 5
                override fun run() {
                    target.player.showTitle(
                        Title.title(Component.text("§c死んでしまった！"),
                            Component.text("§7${i}秒後にテレポートします"),
                            Title.Times.of(Duration.ofMillis(0), Duration.ofMillis(0), Duration.ofSeconds(10)))
                    )

                    i--
                    if (i < 0) {
                        target.player.clearTitle()
                        cancel()
                    }
                }
            }.runTaskTimer(plugin, 0, 20)

            val o = target.player.location.clone()
            object : BukkitRunnable() {
                var i = 98
                override fun run() {
                    target.player.teleport(o)
                    i--
                    if (i < 0) cancel()
                }
            }.runTaskTimer(plugin, 0, 1)

            
            target.setHP(0)
            target.player.health = (target.player as CraftPlayer).handle.maxHealth.toDouble()
            if (target.bossEntity != null) {
                target.bossEntity!!.bossActiveEntity.join.remove(target.player)
                if (target.bossEntity!!.bossActiveEntity.join.size <= 0) target.bossEntity!!.stop()
            }
            Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                Bukkit.dispatchCommand(
                    Bukkit.getConsoleSender(),
                    "effect give " + target.player.name + " minecraft:blindness 5 1"
                )
                target.player.gameMode = GameMode.ADVENTURE
                if (target.player.isOp) target.setHP(target.statusMap.getStatus(StatusType.HP).roundToInt()) else target.setHP(1)
                target.player.teleport(plugin.variables.getVariable(Variables.LOCATION_RESPAWN, Bukkit.getWorld("world")!!.spawnLocation))
            }, 100)
        } else {
            var r: Int = target.statusMap.getStatus(StatusType.HP).roundToInt() / 20
            if (r <= 0) r = 1
            if (target.hasHP / r <= 20) {
                val finalR: Int = r
                Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                    if (((target.hasHP / finalR).toDouble()) <= 0.5) { target.player.health = 2.0; return@Runnable }
                    target.player.health = ((target.hasHP / finalR).toDouble())
                }, 1)

            }
        }
    }
}