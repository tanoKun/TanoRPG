package com.github.tanokun.tanorpg.game.entity.gathering

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.util.getItemData
import com.github.tanokun.tanorpg.util.getNearEntities
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.ArmorStand
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import kotlin.math.roundToInt

class GatheringListener: Listener {

    @EventHandler
    fun onDamage(e: EntityDamageByEntityEvent) = run { if (e.entity.hasMetadata("gathering")) e.isCancelled = true }

    @EventHandler
    fun onSpawn(e: MythicMobSpawnEvent) = TanoRPG.plugin.gatheringManager.gatherings[e.mob.mobType]?.let { gathering ->
        Bukkit.getScheduler().runTaskLater(TanoRPG.plugin, Runnable {
            var b = true
            (e.entity as ArmorStand).apply {
                isSilent = false
                isInvulnerable = true
                isInvisible = true
                equipment.helmet = gathering.item
                equipment.chestplate = ItemStack(Material.AIR)
                equipment.leggings = ItemStack(Material.AIR)
                equipment.boots = ItemStack(Material.AIR)
                equipment.setItemInMainHand(ItemStack(Material.AIR))
                setMetadata("gathering", FixedMetadataValue(TanoRPG.plugin, gathering))
            }
            val isSpawner = e.mob.spawner != null

            if (isSpawner) {
                val c: Int = getNearEntities(BukkitAdapter.adapt(e.mob.spawner.location), 1.0).filter { it.hasMetadata("gathering") }.size
                val loc = e.mob.spawner.location.clone()
                if (gathering.gatheringFlags.hasFlag("RandomRotation")) loc.yaw = (Math.random() * 360).roundToInt().toFloat()
                if (c == 0) {
                    e.entity.teleport(BukkitAdapter.adapt(loc)); b = false
                }
                if (b) Bukkit.getScheduler().scheduleSyncDelayedTask(TanoRPG.plugin) { e.entity.remove() }
            }
        }, 1)
}

    @EventHandler
    fun onClick(e: PlayerInteractAtEntityEvent) {
        if (!e.rightClicked.hasMetadata("gathering")) return
        e.isCancelled = true
        if (e.player.hasMetadata("gathering")) return

        val gathering: Gathering = e.rightClicked.getMetadata("gathering")[0].value() as Gathering
        val entity: ArmorStand = e.rightClicked as ArmorStand

        if (getItemData(e.player.equipment.itemInMainHand) == null || !gathering.tool.contains(getItemData(e.player.equipment.itemInMainHand)?.id)) {
            if (e.player.hasMetadata("wait")) return
            TanoRPG.playSound(e.player, Sound.BLOCK_NOTE_BLOCK_BASS, 3, 1.0)
            val name: List<String> = gathering.tool
                .filter { TanoRPG.plugin.itemManager.getItem(it) != null }
                .map { "§e" + TanoRPG.plugin.itemManager.getItem(it)?.displayName.toString() + "§7" }
            e.player.sendMessage("${TanoRPG.PX} §c正しいツールを使用してください §7[${name.toString().replace("[", "").replace("]", "")}§7]")
            e.player.setMetadata("wait", FixedMetadataValue(TanoRPG.plugin, true))
            Bukkit.getScheduler().runTaskLater(TanoRPG.plugin, Runnable { e.player.removeMetadata("wait", TanoRPG.plugin) }, 20)
            return
        }

        e.player.setMetadata("gathering", FixedMetadataValue(TanoRPG.plugin, true))
        Bukkit.getScheduler().runTaskAsynchronously(TanoRPG.plugin, Runnable {
            gathering.runGatherings.forEach {
                Bukkit.getScheduler().runTask(TanoRPG.plugin, Runnable {
                    entity.equipment.helmet = it.item
                    TanoRPG.playSound(e.player, it.sound, it.volume, it.pitch)
                })
                Thread.sleep((it.nextTime * 50).toLong())
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(TanoRPG.plugin) {
                TanoRPG.playSound(e.player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 1.0)
                entity.remove()
                gathering.itemTable.generate(0).contents.forEach {
                    it?.let { it1 -> e.player.inventory.addItem(it1) }
                }
                e.player.removeMetadata("gathering", TanoRPG.plugin)
            }
        })
    }
}
