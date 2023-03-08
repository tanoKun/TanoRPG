package com.github.tanokun.tanorpg.damage

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.event.tanorpg.TanoRpgEntityDamageByPlayerEvent
import com.github.tanokun.tanorpg.game.entity.ActiveEntity
import com.github.tanokun.tanorpg.game.item.ItemType
import com.github.tanokun.tanorpg.game.item.type.base.ItemData
import com.github.tanokun.tanorpg.player.Member
import com.github.tanokun.tanorpg.player.status.StatusType
import com.github.tanokun.tanorpg.util.getActiveEntity
import com.github.tanokun.tanorpg.util.getNearActiveEntity
import com.github.tanokun.tanorpg.util.image.ImageParticles
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.objecthunter.exp4j.ExpressionBuilder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.craftbukkit.v1_17_R1.entity.*
import org.bukkit.entity.Arrow
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import xyz.xenondevs.particle.ParticleEffect
import xyz.xenondevs.particle.data.color.RegularColor
import java.awt.Color
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import javax.imageio.ImageIO
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.math.sin

class PlayerDamageExpression(private val attacker: Member, val target: ActiveEntity) {
    private var damage = 0

    private var o = 100

    private val defScaling = ExpressionBuilder(
        "(${attacker.level.value} + 2*${o} + {(${attacker.level.value}${o}) / (${target.objectEntity.level} + ${o})})"
                + "/ ((1 - 0)*(${target.objectEntity.level} + ${o}) + 2*${target.objectEntity.level} + $o + " +
                "(${target.objectEntity.statusMap.getStatus(StatusType.DEF)}))"
    )
        .build()

    private val attackDamage =
        ExpressionBuilder("(${attacker.statusMap.getStatus(StatusType.ATK)} * ${defScaling.evaluate()})")
            .build()

    init {
        val p: Double = (Random().nextInt(5).toDouble()) / 100
        damage = (attackDamage.evaluate().roundToInt() * (1 + p)).roundToInt()
    }

    fun multi(double: Double): PlayerDamageExpression {
        damage = (damage * double).roundToInt()
        return this
    }

    fun attack(damageType: DamageType) {
        if (target.entity.isDead) return
        if (attacker.attack.isSkillComboTrigger) return
        attacker.attack.attack = true
        damageByNull(target.entity, 0.00001)
        Bukkit.getPluginManager().callEvent(TanoRpgEntityDamageByPlayerEvent(attacker.player, target.entity, target, damage, damageType))
    }
}

fun getWaitAttack(ct: Int, member: Member, itemData: ItemData): BukkitRunnable {
    return object : BukkitRunnable() {
        var player = Bukkit.getPlayer(member.uuid)
        var time = 0
        var time2 = 4
        var combo = member.attack.attackCombo
        var d = (ct + itemData.combo[combo - 1]).toDouble()
        override fun run() {
            time2++
            member.attack.nextLastAttackTicks()
            val m = player!!.location
            val m2 = m.clone()
            if (itemData.itemType == ItemType.PROJECTILE_WEAPON) {
                if (time2 == 5) {
                    if (d / 2 >= time) {
                        player = Bukkit.getPlayer(member.uuid)
                        m.yaw = m.yaw + 120
                        m.add(m.direction.x * 0.8, 1.7, m.direction.z * 0.8)
                        m2.yaw = player!!.location.yaw
                        for (i in 0..10) {
                            ParticleEffect.CRIT.display(m, 0f, 0f, 0f, 0f, 1, null, Bukkit.getOnlinePlayers())
                            m.add(m2.direction.x * 0.25, 0.0, m2.direction.z * 0.25)
                        }
                        ParticleEffect.REDSTONE.display(m, 0.1f, 0.1f, 0.1f, 1f, 10, RegularColor(Color(0, 100, 255)), Bukkit.getOnlinePlayers())
                        time2 = 0
                    } else {
                        attack(player!!, member, itemData, combo)
                    }
                }
            }
            if (time.toDouble() == d + 5) {
                if (!member.attack.isAttackWait && combo == member.attack.attackCombo) {
                    member.attack.attackCombo = 0
                    member.attack.nextAttackCombo = 0
                }
                member.attack.lastAttackTicks = 0
                member.attack.isAttackWait = false
                cancel()
            }
            time++
        }
    }
}

private val random_60: Random = Random()

fun attack(player: Player, member: Member, itemData: ItemData, combo: Int) {
    var player = player
    val entityCounts = HashSet<Int>()
    if (player.equipment.itemInMainHand.type == Material.AIR) return
    if (itemData.itemType == ItemType.WEAPON) {
        val m = player.location
        if (combo != itemData.combo.size) {
            TanoRPG.playSound(player, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1, 1.0)
            var r: Int = random_60.nextInt(70)
            if (chance(50.0)) r = -r
            m.add(0.0, 2.5, 0.0)
            m.yaw = m.yaw + r
            m.add(m.direction.normalize().x * (itemData.reach - 1), 0.0, m.direction.normalize().z * (itemData.reach - 1))
            m.pitch = 80f
            m.yaw = m.yaw - r * 2
            for (i in 0..9) {
                m.subtract(-m.direction.normalize().x, 0.25, -m.direction.normalize().z)
                ParticleEffect.CRIT.display(m, 0f, 0f, 0f, 0f, 1, null, Bukkit.getOnlinePlayers())
                ParticleEffect.CRIT_MAGIC.display(m, 0f, 0f, 0f, 0f, 1, null, Bukkit.getOnlinePlayers())
                val es = HashSet<Entity?>()
                if (player.world.rayTraceEntities(player.location, player.location.direction, 4.0) != null) es.add(player.world.rayTraceEntities(player.location, player.location.direction, 4.0)!!.hitEntity)
                Arrays.stream(getNearActiveEntity(m, 1.5)).forEach { e: Entity? -> es.add(e) }
                for (e in es) {
                    if (entityCounts.size >= 2) break
                    if (entityCounts.contains(e!!.entityId)) continue
                    if (getActiveEntity(e) == null) continue
                    PlayerDamageExpression(member, getActiveEntity(e)!!)
                        .attack(DamageType.NORMAL)
                    entityCounts.add(e.entityId)
                }
            }
        } else {
            TanoRPG.playSound(player, Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, 1, 1.0)
            m.add(0.0, 1.0, 0.0)
            val o = Math.toRadians(m.yaw.toDouble())
            var t2: Double
            val t3 = Math.toRadians(60.0)
            var t = 0
            while (t < 120) {
                t2 = Math.toRadians(t.toDouble())
                val x = -sin(-t3 + t2 + o)
                val z = cos(-t3 + t2 + o)
                var t4 = 1.5
                while (t4 < 5) {
                    m.add(x * t4, 0.0, z * t4)
                    ParticleEffect.CRIT.display(m, 0f, 0f, 0f, 0f, 1, null, Bukkit.getOnlinePlayers())
                    ParticleEffect.CRIT_MAGIC.display(m, 0f, 0f, 0f, 0f, 1, null, Bukkit.getOnlinePlayers())
                    val es = HashSet<Entity?>()
                    if (player.world.rayTraceEntities(player.location, player.location.direction, itemData.reach.toDouble()) != null) es.add(player.world.rayTraceEntities(player.location, player.location.direction, itemData.reach.toDouble())!!.hitEntity)
                    Arrays.stream(getNearActiveEntity(m, 1.5)).forEach { e: Entity? -> es.add(e) }
                    for (e in es) {
                        if (entityCounts.size >= 3) break
                        if (entityCounts.contains(e!!.entityId)) continue
                        if (getActiveEntity(e) == null) continue
                        PlayerDamageExpression(member, getActiveEntity(e)!!)
                            .multi(0.1)
                            .attack(DamageType.NORMAL)
                        entityCounts.add(e.entityId)
                    }
                    m.subtract(x * t4, 0.0, z * t4)
                    t4 += 0.6
                }
                t += 15
            }
        }
    } else if (itemData.itemType == ItemType.PROJECTILE_WEAPON) {
        TanoRPG.playSound(player, Sound.ENTITY_ARROW_SHOOT, 1, 0.5)
        if (combo != itemData.combo.size) {
            val m = player.location
            val m2 = m.clone()
            val m3 = player.location
            player = Bukkit.getPlayer(member.uuid)!!
            m.yaw = m.yaw + 120
            m.add(m.direction.x * 0.8, 1.7, m.direction.z * 0.8)
            m2.yaw = player.player!!.location.yaw
            for (i in 0..10) {
                m.add(m2.direction.x * 0.25, 0.0, m2.direction.z * 0.25)
            }
            val arrow = Bukkit.getWorld("world")!!.spawnEntity(m, EntityType.ARROW) as Arrow
            arrow.setMetadata("arrow", FixedMetadataValue(TanoRPG.plugin, player))
            val v = m3.direction.normalize()
            v.multiply(2)
            arrow.velocity = v
            object : BukkitRunnable() {
                override fun run() {
                    if (arrow.isDead) cancel()
                    ParticleEffect.CRIT.display(arrow.location, 0f, 0f, 0f, 0f, 1, null, Bukkit.getOnlinePlayers())
                }
            }.runTaskTimer(TanoRPG.plugin, 1, 1)
        } else {
            val m = player.location
            val m2 = m.clone()
            val m3 = player.location
            player = Bukkit.getPlayer(member.uuid)!!
            m.yaw = m.yaw + 120
            m.add(m.direction.x * 0.8, 1.7, m.direction.z * 0.8)
            m2.yaw = player.player!!.location.yaw
            for (i in 0..10) {
                m.add(m2.direction.x * 0.25, 0.0, m2.direction.z * 0.25)
            }
            val arrow = Bukkit.getWorld("world")!!.spawnEntity(m, EntityType.ARROW) as Arrow
            arrow.setMetadata("max", FixedMetadataValue(TanoRPG.plugin, true))
            arrow.setMetadata("arrow", FixedMetadataValue(TanoRPG.plugin, player))
            val v = m3.direction.normalize()
            v.multiply(2)
            arrow.velocity = v
            object : BukkitRunnable() {
                override fun run() {
                    if (arrow.isDead) cancel()
                    ParticleEffect.CRIT.display(arrow.location, 0f, 0f, 0f, 0f, 1, null, Bukkit.getOnlinePlayers())
                }
            }.runTaskTimer(TanoRPG.plugin, 1, 1)
        }
    } else if (itemData.itemType == ItemType.MAGIC_WEAPON) {
        TanoRPG.playSound(player, Sound.ENTITY_GHAST_SHOOT, 2, 1.0)
        val l = player.location
        l.add(0.0, 1.5, 0.0)
        val v = l.direction
        object : BukkitRunnable() {
            var t = 0
            var regularColor = RegularColor(Color(itemData.color.red, itemData.color.green, itemData.color.blue))
            override fun run() {
                t++
                for (i in 0..4) {
                    l.add(v.x * 0.3, v.y * 0.3, v.z * 0.3)
                    ParticleEffect.REDSTONE.display(l, 0f, 0f, 0f, 0f, 1, regularColor, Bukkit.getOnlinePlayers())
                    for (e in getNearActiveEntity(l, 2.0)) {
                        PlayerDamageExpression(member, getActiveEntity(e)!!)
                            .attack(DamageType.NORMAL)
                        cancel()
                        return
                    }
                }
                if (t > 6) cancel()
            }
        }.runTaskTimer(TanoRPG.plugin, 1, 1)
    }
}

fun chance(chance: Double): Boolean {
    var chance = chance
    chance /= 100
    return Math.random() <= chance
}

fun damageByNull(entity: Entity, damage: Double) {
    when (entity) {
        is CraftElderGuardian -> entity.damage(damage)
        is CraftWitherSkeleton -> entity.damage(damage)
        is CraftStray -> entity.damage(damage)
        is CraftHusk -> entity.damage(damage)
        is CraftVillagerZombie -> entity.damage(damage)
        is CraftSkeletonHorse -> entity.damage(damage)
        is CraftZombieHorse -> entity.damage(damage)
        is CraftArmorStand -> entity.damage(damage)
        is CraftDonkey -> entity.damage(damage)
        is CraftMule -> entity.damage(damage)
        is CraftEvoker -> entity.damage(damage)
        is CraftVex -> entity.damage(damage)
        is CraftVindicator -> entity.damage(damage)
        is CraftIllusioner -> entity.damage(damage)
        is CraftCreeper -> entity.damage(damage)
        is CraftSkeleton -> entity.damage(damage)
        is CraftSpider -> entity.damage(damage)
        is CraftGiant -> entity.damage(damage)
        is CraftZombie -> entity.damage(damage)
        is CraftSlime -> entity.damage(damage)
        is CraftGhast -> entity.damage(damage)
        is CraftEnderman -> entity.damage(damage)
        is CraftSilverfish -> entity.damage(damage)
        is CraftBlaze -> entity.damage(damage)
        is CraftEnderDragon -> entity.damage(damage)
        is CraftWither -> entity.damage(damage)
        is CraftBat -> entity.damage(damage)
        is CraftWitch -> entity.damage(damage)
        is CraftEndermite -> entity.damage(damage)
        is CraftGuardian -> entity.damage(damage)
        is CraftShulker -> entity.damage(damage)
        is CraftPig -> entity.damage(damage)
        is CraftSheep -> entity.damage(damage)
        is CraftCow -> entity.damage(damage)
        is CraftChicken -> entity.damage(damage)
        is CraftSquid -> entity.damage(damage)
        is CraftWolf -> entity.damage(damage)
        is CraftSnowman -> entity.damage(damage)
        is CraftOcelot -> entity.damage(damage)
        is CraftIronGolem -> entity.damage(damage)
        is CraftHorse -> entity.damage(damage)
        is CraftRabbit -> entity.damage(damage)
        is CraftPolarBear -> entity.damage(damage)
        is CraftLlama -> entity.damage(damage)
        is CraftParrot -> entity.damage(damage)
        is CraftVillager -> entity.damage(damage)
        is CraftTurtle -> entity.damage(damage)
        is CraftPhantom -> entity.damage(damage)
        is CraftCod -> entity.damage(damage)
        is CraftSalmon -> entity.damage(damage)
        is CraftPufferFish -> entity.damage(damage)
        is CraftTropicalFish -> entity.damage(damage)
        is CraftDolphin -> entity.damage(damage)
        is CraftCat -> entity.damage(damage)
        is CraftPanda -> entity.damage(damage)
        is CraftPillager -> entity.damage(damage)
        is CraftRavager -> entity.damage(damage)
        is CraftWanderingTrader -> entity.damage(damage)
        is CraftFox -> entity.damage(damage)
        is CraftBee -> entity.damage(damage)
        is CraftHoglin -> entity.damage(damage)
        is CraftPiglin -> entity.damage(damage)
        is CraftStrider -> entity.damage(damage)
        is CraftZoglin -> entity.damage(damage)
        is CraftPiglinBrute -> entity.damage(damage)
        is CraftAxolotl -> entity.damage(damage)
        is CraftGoat -> entity.damage(damage)
    }
}