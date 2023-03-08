package com.github.tanokun.tanorpg.listener

import com.github.tanokun.tanorpg.TanoRPG.Companion.plugin
import com.github.tanokun.tanorpg.util.getItemData
import com.github.tanokun.tanorpg.TanoRPG.Companion.playSound
import org.bukkit.event.entity.EntityDropItemEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import com.github.tanokun.tanorpg.event.PlayerArmorEquipEvent
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityChangeBlockEvent
import org.bukkit.event.entity.SlimeSplitEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.EventPriority
import org.bukkit.event.inventory.InventoryOpenEvent
import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.game.item.ItemType
import org.bukkit.event.player.PlayerInteractEvent
import java.lang.Runnable
import java.lang.StringBuilder
import com.github.tanokun.tanorpg.game.item.type.base.ItemData
import com.github.tanokun.tanorpg.player.quests.actions.QuestGiveBuffAction
import java.util.concurrent.atomic.AtomicBoolean
import org.bukkit.scheduler.BukkitRunnable
import net.minecraft.network.chat.ChatMessageType
import net.minecraft.network.chat.IChatBaseComponent
import net.minecraft.network.protocol.game.PacketPlayOutChat
import org.bukkit.*
import org.bukkit.block.data.type.Door
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.Action.*
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitTask
import java.util.*
import java.util.function.Consumer
import kotlin.collections.HashMap
import kotlin.math.ceil

class StopEventListener : Listener {
    private val door = HashMap<UUID, BukkitRunnable>()
    @EventHandler
    fun onDeath(e: EntityDropItemEvent) = when (e.entity) {
        !is Player -> e.isCancelled = true
        else -> {}
    }

    @EventHandler
    fun itemFrame(e: PlayerInteractEntityEvent) {
        if (e.player.isOp) return

        when (e.rightClicked.type) {
            EntityType.ITEM_FRAME -> e.isCancelled = true
            EntityType.GLOW_ITEM_FRAME -> e.isCancelled = true
            else -> {}
        }
    }

    @EventHandler
    fun onDamage(e: EntityDamageByEntityEvent) = when (e.entity) {
        is EnderCrystal -> e.isCancelled = true
        is ItemFrame -> e.isCancelled = !e.damager.isOp
        else -> {}
    }

    @EventHandler
    fun onEquip(e: PlayerArmorEquipEvent) { e.isCancelled = true }

    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        if (e.player.gameMode == GameMode.CREATIVE) return
        e.isCancelled = true
    }

    @EventHandler
    fun onBlockPlace(e: BlockPlaceEvent) {
        if (e.player.gameMode == GameMode.CREATIVE) return
        e.isCancelled = true
    }

    @EventHandler
    fun onBlock(e: EntityChangeBlockEvent) {
        if (e.entity.isOp) return
        e.isCancelled = true
    }

    @EventHandler
    fun onSplit(event: SlimeSplitEvent) { event.count = 0 }

    @EventHandler
    fun onRegainHealth(event: EntityRegainHealthEvent) { event.isCancelled = true }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerOpenInv(e: InventoryOpenEvent) {
        if (e.player.isOp) return
        if (plugin.inventoryManager.getInventory(e.player as Player).isEmpty) e.isCancelled = true
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerDoorOpen(e: PlayerInteractEvent) {
        if (e.action == RIGHT_CLICK_BLOCK && e.clickedBlock != null) {
            val block = e.clickedBlock!!
            if (block.blockData is Door) {
                val blockData = block.blockData as Door

                if (blockData.isOpen) return
                if (door[e.player.uniqueId] != null) door[e.player.uniqueId]?.cancel()

                val runnable = object : BukkitRunnable() {
                    override fun run() {
                        blockData.isOpen = false
                        block.blockData = blockData
                        door.remove(e.player.uniqueId)
                    }
                }

                runnable.runTaskLater(plugin, 60)
                door[e.player.uniqueId] = runnable
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerTrapdoorOpen(e: PlayerInteractEvent) {
        if (e.action == RIGHT_CLICK_BLOCK) {
            if (("" + e.clickedBlock!!.type).contains("TRAP") && !e.player.isOp) e.isCancelled = true
            if (("" + e.clickedBlock!!.type).contains("GATE") && !e.player.isOp) e.isCancelled = true
        }
    }

    @EventHandler
    fun onGather(e: PlayerInteractEvent) {
        if (e.player.gameMode == GameMode.CREATIVE || e.action != RIGHT_CLICK_BLOCK) return
        if (e.clickedBlock!!.type == Material.GLOW_BERRIES || e.clickedBlock!!.type == Material.CAVE_VINES_PLANT || e.clickedBlock!!.type == Material.SWEET_BERRY_BUSH) e.isCancelled = true
    }

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val bar = "§a❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘"
        val stringBuilder = StringBuilder(bar)
        if (e.action != RIGHT_CLICK_AIR && e.action != RIGHT_CLICK_BLOCK) return
        val member = plugin.memberManager.getMember(e.player.uniqueId) ?: return
        if (e.item == null) return
        if (getItemData(e.item!!) == null) return
        if (member.questMap.isAction) return
        val itemData = getItemData(e.item!!)
        if (itemData!!.itemType != ItemType.POTION) return
        val stop = AtomicBoolean(false)
        val o = ceil((itemData.coolTime / 30).toDouble()).toInt()
        e.player.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 10000, 3, false))
        playSound(e.player, Sound.ITEM_BOTTLE_FILL, 3, 1.0)
        member.questMap.isAction = true
        object : BukkitRunnable() {
            var time = 0
            override fun run() {
                if (e.player.inventory.itemInMainHand != e.item) {
                    cancel()
                    e.player.removePotionEffect(PotionEffectType.SLOW)
                    sendActionbar(e.player, "§c§lキャンセルしました")
                    stop.set(true)
                    member.questMap.isAction = false
                    return
                }
                if (time > 30) {
                    cancel()
                    e.player.removePotionEffect(PotionEffectType.SLOW)
                    stop.set(true)
                    playSound(e.player, Sound.ENTITY_PLAYER_BURP, 3, 1.0)
                    e.item!!.amount = e.item!!.amount - 1
                    member.questMap.isAction = false
                    itemData.buffs.forEach(Consumer { action: QuestGiveBuffAction -> action.run(member) })
                    return
                }
                stringBuilder.setLength(0)
                stringBuilder.append(bar)
                stringBuilder.insert(time + 2, "§f")
                sendActionbar(e.player, stringBuilder.toString())
                time++
            }
        }.runTaskTimer(plugin, 1, o.toLong())
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            object : BukkitRunnable() {
                override fun run() {
                    if (stop.get()) { cancel(); return }
                    playSound(e.player, Sound.ENTITY_GENERIC_DRINK, 3, 1.0)
                }
            }.runTaskTimer(plugin, 1, 5)
        }, 20)
    }

    companion object {
        fun sendActionbar(p: Player, message: String?) {
            val icbc: IChatBaseComponent? = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', message!!) + "\"}")
            val bar = PacketPlayOutChat(icbc, ChatMessageType.c, p.uniqueId)
            (p as CraftPlayer).handle.b.sendPacket(bar)
        }
    }
}