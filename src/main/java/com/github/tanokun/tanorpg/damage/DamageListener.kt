package com.github.tanokun.tanorpg.damage

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.event.tanorpg.TanoRpgEntityDamageByPlayerEvent
import com.github.tanokun.tanorpg.event.tanorpg.TanoRpgEntityKillEvent
import com.github.tanokun.tanorpg.game.entity.ActiveEntity
import com.github.tanokun.tanorpg.game.entity.EntityManager
import com.github.tanokun.tanorpg.game.entity.boss.BossActiveEntity
import com.github.tanokun.tanorpg.game.item.ItemType
import com.github.tanokun.tanorpg.player.EquipmentMap
import com.github.tanokun.tanorpg.player.status.StatusType
import com.github.tanokun.tanorpg.util.*
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.attribute.Attributable
import org.bukkit.attribute.Attribute
import org.bukkit.block.BlockFace
import org.bukkit.entity.Creature
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import xyz.xenondevs.particle.ParticleEffect
import java.util.*
import kotlin.math.ceil
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class DamageListener : Listener {
    @EventHandler
    fun onDeath(e: EntityDeathEvent) {
        e.drops.clear()
        Bukkit.getScheduler().runTaskLater(TanoRPG.plugin, Runnable {
            if (e.entity.getMetadata("isDead").size == 0) return@Runnable
            e.entity.getMetadata("isDead")[0]?.let { attacker ->
                val player = attacker.value() as Player
                val member = TanoRPG.plugin.memberManager.getMember(player.uniqueId)!!

                getActiveEntity(e.entity)?.let { activeEntity ->
                    val drop: Inventory = activeEntity.objectEntity.lootTable.generateSort(member.statusMap.getStatus(StatusType.LUCKY_ITEM))

                    if (!drop.isEmpty) drop.contents
                        .filterNotNull()
                        .filter { it.type != Material.AIR }
                        .forEach(player.inventory::addItem)


                    for (map in activeEntity.exp) {
                        TanoRPG.plugin.memberManager.getMember(map.key).addHasEXP(map.value.roundToLong())
                        TanoRPG.plugin.sidebarManager.updateSidebar(player, member)
                        if (map.key == player.uniqueId) showLog(activeEntity.entity.location, map.value.roundToLong(), drop)
                    }

                    if (activeEntity is BossActiveEntity) {
                    val bossActiveEntity: BossActiveEntity = activeEntity
                    bossActiveEntity.bossEntity.die()
                    }

                    Bukkit.getPluginManager().callEvent(TanoRpgEntityKillEvent(player, e.entity, activeEntity.objectEntity))
                    ParticleEffect.CLOUD.display(e.entity.location, 0.0f, 0.0f, 0.0f, 0.5f, 40, null, Bukkit.getOnlinePlayers())
                }
            }
        }, 1)
    }

    @EventHandler
    fun onSpawn(e: MythicMobSpawnEvent) {
        val manager = TanoRPG.plugin.entityManager
        if (manager.getEntity(e.mob.mobType) == null) return
        Bukkit.getScheduler().runTaskLater(TanoRPG.plugin, Runnable {
            val activeEntity = ActiveEntity(manager.getEntity(e.mob.mobType)!!, e.entity)
            e.entity.isCustomNameVisible = true
            e.entity.customName =
                e.mob.displayName + " §7[§dLv:§e" + activeEntity.objectEntity.level + "§7] " + "§a❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘"
            e.entity.setMetadata(EntityManager.ENTITY, FixedMetadataValue(TanoRPG.plugin, activeEntity))
        }, 10)
    }

    @EventHandler
    fun onHeld(e: PlayerItemHeldEvent) {
        val (_, _, _, equip) = TanoRPG.plugin.memberManager.getMember(e.player.uniqueId) ?: return
        val item = if (e.player.inventory.getItem(e.newSlot) == null) ItemStack(Material.AIR) else e.player.inventory.getItem(e.newSlot)!!
        equip.setEquip(EquipmentMap.EquipmentType.MAIN, item)
    }

    @EventHandler
    fun onDamageByPlayer(e: EntityDamageByEntityEvent) {
        if (e.entity.type == EntityType.GLOW_ITEM_FRAME || e.entity.type == EntityType.ITEM_FRAME && e.damager.isOp) return

        if (e.damager !is Player) return
        val member = TanoRPG.plugin.memberManager.getMember(e.damager.uniqueId)
        if (member.attack.attack) {
            member.attack.attack = false; e.isCancelled = true; return
        }
        e.damage = 0.01
        val e2 = PlayerInteractEvent((e.damager as Player).player!!, Action.LEFT_CLICK_AIR, (e.damager as Player).player!!.inventory.itemInMainHand, null, BlockFace.SELF, EquipmentSlot.HAND)
        e.isCancelled = onClick(e2)
    }

    @EventHandler
    fun onDamagePlayer(e: EntityDamageEvent) {
        val damage = e.damage
        if (e.entity.type == EntityType.GLOW_ITEM_FRAME || e.entity.type == EntityType.ITEM_FRAME) return

        if (e.cause == EntityDamageEvent.DamageCause.CUSTOM) return
        if (e.entity.hasMetadata(EntityManager.ENTITY)) return

        var activeEntity: ActiveEntity? = null
        if (TanoRPG.plugin.memberManager.getMember(e.entity.uniqueId) == null) return
        val member = TanoRPG.plugin.memberManager.getMember(e.entity.uniqueId)!!
        if (e is EntityDamageByEntityEvent) {
            if (!e.damager.hasMetadata(EntityManager.ENTITY)) return
            activeEntity = getActiveEntity(e.damager)
        }

        val entityDamageExpression = EntityDamageExpression(damage, activeEntity, member)
        when (e.cause) {
            EntityDamageEvent.DamageCause.FALL -> entityDamageExpression.multi(1.0)
            else -> entityDamageExpression.multi(1.0)
        }
        entityDamageExpression.attack(DamageType.NORMAL)
        e.isCancelled = true
    }

    @EventHandler
    fun onDamage(e: TanoRpgEntityDamageByPlayerEvent) {
        val fhp = e.activeEntity.removeHealth(e.damage, e.player)
        val hp = e.activeEntity.health

        var exp: Double = if (e.activeEntity.exp[e.player.uniqueId] == null) 0.0 else e.activeEntity.exp[e.player.uniqueId]!!
        exp += if (hp <= 0) e.activeEntity.oneExp * fhp
        else e.activeEntity.oneExp * e.damage
        e.activeEntity.exp[e.player.uniqueId] = exp
        val c = (hp / ceil((e.entity as Attributable).getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value / 20)).roundToInt() + 1
        Bukkit.getScheduler().runTaskLater(TanoRPG.plugin, Runnable {
            e.entity.customName = "${e.activeEntity.objectEntity.name.replace("&", "§")} " +
                    "§7[§dLv:§e" + e.activeEntity.objectEntity.level + "§7] ${
                when {
                    hp <= 0 -> "§c❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘"
                    else -> {
                        StringBuilder("§a❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘").insert(c, "§c")
                    }
                }
            }"
        }, 1)

        val random = Random()
        val hy: Double = random.nextDouble() + 0.5
        val taLoc: Location = e.entity.location
        if (chance(50.0)) taLoc.yaw = (-random.nextInt(120) - 50).toFloat() else taLoc.yaw =
            (random.nextInt(120) + 50).toFloat()
        val vector = e.player.location.direction.normalize().subtract(taLoc.direction.normalize()).normalize()

        bindHologramAtLocationAndEntity(
            e.damageType.P + "§l" + e.damage + e.damageType.id, e.entity.location.add(0.0, hy, 0.0), e.entity, hy, 25,
            vector.x * -1, random.nextDouble() * 2, vector.z * -1
        )
    }

    @EventHandler
    fun onClick(e: PlayerInteractEvent): Boolean {
        if (e.player.hasMetadata("wait")) return true
        if (e.action != Action.LEFT_CLICK_AIR && e.action != Action.LEFT_CLICK_BLOCK) return true
        if (e.hand != EquipmentSlot.HAND) return true
        val item = e.item?.let { getItemData(it) } ?: return true

        val member = TanoRPG.plugin.memberManager.getMember(e.player.uniqueId)

        if (item.itemType != ItemType.WEAPON && item.itemType != ItemType.MAGIC_WEAPON && item.itemType != ItemType.PROJECTILE_WEAPON) {
            e.player.setMetadata("wait", FixedMetadataValue(TanoRPG.plugin, true))
            Bukkit.getScheduler().runTaskLater(TanoRPG.plugin, Runnable { e.player.removeMetadata("wait", TanoRPG.plugin) }, 20)
            e.isCancelled = true; return true
        }
        if (!(isTrueSkillClass(item, member, e.player) && isTrueLevel(item, member, e.player))) {
            e.player.setMetadata("wait", FixedMetadataValue(TanoRPG.plugin, true))
            Bukkit.getScheduler().runTaskLater(TanoRPG.plugin, Runnable { e.player.removeMetadata("wait", TanoRPG.plugin) }, 20)
            e.isCancelled = true; return true
        }
        if (member.attack.nextAttackCombo == item.combo.size || member.attack.isAttackWait) {
            e.player.setMetadata("wait", FixedMetadataValue(TanoRPG.plugin, true))
            Bukkit.getScheduler().runTaskLater(TanoRPG.plugin, Runnable { e.player.removeMetadata("wait", TanoRPG.plugin) }, 20)
            e.isCancelled = true; return true
        }

        if (member.attack.attackCombo == 0) {
            val combo = member.attack.nextAttackCombo()
            getWaitAttack(item.coolTime, member, item).runTaskTimer(TanoRPG.plugin, 1, 1)
            if (item.itemType != ItemType.PROJECTILE_WEAPON) attack(e.player, member, item, combo)
        } else {
            member.attack.isAttackWait = true
            member.attack.nextAttackCombo = member.attack.attackCombo + 1
            Bukkit.getScheduler().runTaskLater(
                TanoRPG.plugin,
                Runnable {
                    val combo = member.attack.nextAttackCombo()
                    getWaitAttack(item.coolTime, member, item).runTaskTimer(TanoRPG.plugin, 1, 1)
                    if (item.itemType != ItemType.PROJECTILE_WEAPON) attack(e.player, member, item, combo)
                },
                ((if (item.coolTime + item.combo[member.attack.attackCombo - 1] - member.attack.lastAttackTicks <= 0) 0 else item.coolTime + item.combo.get(
                    member.attack.attackCombo - 1
                )) - member.attack.lastAttackTicks).toLong()
            )
        }

        return false
    }

    private fun showLog(location: Location, exp: Long, drop: Inventory) {
        val lines = ArrayList<String>()
        val items = HashMap<ItemStack, Int>()
        lines.add("§bEXP +$exp")
        if (!drop.isEmpty) {
            drop.contents.forEach {
                if (it != null)
                    when (items[it] == null) {
                        true -> items[it] = it.amount
                        false -> items[it] = items[it]!! + it.amount
                    }
            }
            items.keys.forEach {
                lines.add("${it.itemMeta.displayName} §7x${it.amount}")
            }
        }
        bindHologramAtLocation(lines.toTypedArray(), location, 60, 0.0, lines.size * 0.3 + 1, 0.0)
    }
}