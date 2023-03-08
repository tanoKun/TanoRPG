package com.github.tanokun.tanorpg.listener

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.TanoRPG.Companion.playSound
import com.github.tanokun.tanorpg.TanoRPG.Companion.plugin
import com.github.tanokun.tanorpg.event.WgRegionLeftEvent
import com.github.tanokun.tanorpg.player.menu.SelSkillClassMenu
import com.github.tanokun.tanorpg.util.item.ItemBuilder
import com.github.tanokun.tanorpg.util.variable.Variables
import net.citizensnpcs.api.event.NPCRightClickEvent
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class PlayerInitListener : Listener {
    private val isActive = ArrayList<UUID>()
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onJoin(e: PlayerJoinEvent) {
        e.player.setResourcePack("file:c:/Users/owner/Desktop/assets.zip", "null")
        //Objects.requireNonNull(plugin.config.getString("resourcePackURL"))?.let { e.player.setResourcePack(it, "null", false, null) }
        val member = plugin.memberManager.loadData(e.player.uniqueId)
        val p = e.player
        e.player.inventory.setItem(8,
            ItemBuilder(Material.COMPASS)
                .setDisplayName("§bMenu")
                .setLore("§f初期アイテム",
                    "§f自分の情報を見ることができる",
                    "§fコンパスはクエストの場所を指し示してくれるぞ")
                .setAmount(1)
                .setGlowing()
                .build()
        )

        e.joinMessage(Component.text( TanoRPG.PX + e.player.name + "がログインしました。"))
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            if (member == null) {
                //if (e.player.isOp) {
                //    Bukkit.dispatchCommand(e.player, "open init")
                //    return@Runnable
                //}
                playSound(Bukkit.getOnlinePlayers().toTypedArray(), Sound.ENTITY_PLAYER_LEVELUP, 3.0, 1)
                p.inventory.clear()
                p.teleport(plugin.variables.getVariable(Variables.LOCATION_HOME, Bukkit.getWorld("world")!!.spawnLocation))
                p.inventory.setItem(4,             ItemBuilder(Material.COMPASS)
                    .setDisplayName("クリックで夢から覚める")
                    .setAmount(1)
                    .build()
                )
            } else {
                plugin.memberManager.registerMember(member)
                plugin.sidebarManager.setupSidebar(e.player, member)
                member.online()
            }
            if (!p.isOp) p.gameMode = GameMode.ADVENTURE
        }, 1)
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        e.quitMessage = TanoRPG.PX + e.player.name + "がログアウトしました。"
        val member = plugin.memberManager.getMember(e.player.uniqueId) ?: return
        plugin.sidebarManager.removeSidebar(e.player)
        plugin.memberManager.unregisterMember(member.uuid)
        if (member.bossEntity != null) {
            member.bossEntity!!.bossActiveEntity.join.remove(e.player)
            if (member.bossEntity!!.bossActiveEntity.join.size <= 0) {
                member.bossEntity!!.stop()
            }
            e.player.teleport(plugin.variables.getVariable(Variables.LOCATION_RESPAWN, Bukkit.getWorld("world")!!.spawnLocation))
            member.hasHP = 1
        }
        member.saveData()
    }

    @EventHandler
    fun onStartRegionLeave(e: WgRegionLeftEvent) {
        if (plugin.memberManager.getMember(e.player!!.uniqueId) != null) return
        val p = e.player
        if (e.regionName == plugin.variables.getVariable(Variables.REGION_HOME, "")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "effect give " + p!!.name + " minecraft:blindness 90 1")
            p.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 100, 0))
            Bukkit.getScheduler().runTask(plugin, Runnable { p.teleport(Location(Bukkit.getWorld("world"), 0.0, 1.0, 0.0)) })
            Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
                Thread.sleep(2000)

                p.sendMessage("§c" + p.name + "§7「あ！そこの人！」")
                playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5)
                Thread.sleep(3000)
                p.sendMessage("§a" + "???" + "§7「ん？どうかしたかな」")
                playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5)
                Thread.sleep(2000)
                p.sendMessage("§c" + p.name + "§7「この前はありがとうございました！」")
                playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5)
                Thread.sleep(3000)
                p.sendMessage("§a" + "???" + "§7「ああ。この前の冒険者か。危なかったね」")
                playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5)
                Thread.sleep(2000)
                p.sendMessage("§c" + p.name + "§7「はい！あなたのおかげで助かりました」")
                playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5)
                Thread.sleep(3000)
                p.sendMessage("§a" + "???" + "§7「この町はあそこから大分遠いんだけどゆっくりしていってよ」")
                playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5)
                Thread.sleep(4000)
                p.sendMessage("§a" + "???" + "§7「あ。この町の中心に§aギルド§7があるから、そこに行くといいかも」")
                playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5)
                Thread.sleep(4000)

                Bukkit.getScheduler().runTask(plugin, Runnable { p.teleport(plugin.variables.getVariable(Variables.LOCATION_GUILD, Bukkit.getWorld("world")!!.spawnLocation)); p.removePotionEffect(PotionEffectType.BLINDNESS) })
                p.sendMessage(TanoRPG.PX + "NPCに話しかけてみましょう。ストーリーの発展に重要です")
                playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 1.0)
            })
            return
        }
        if (e.regionName == plugin.variables.getVariable(Variables.REGION_GUILD, "")) e.isCancelled = true
    }

    @EventHandler
    fun onSpeakToNPC(e: NPCRightClickEvent) {
        val p = e.clicker
        if (e.npc.name != "§8《§c§l ギルド総長「アバエフ」 §8》" || plugin.memberManager.getMember(p.uniqueId) != null || isActive.contains(p.uniqueId)) return
        isActive.add(p.uniqueId)
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            Thread.sleep(1000)

            p.sendMessage("§aアバエフ §7「何だ？今は忙しいんだ。用ならすぐ言ってくれ。」")
            playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5)
            Thread.sleep(2700)
            p.sendMessage("§c" + p.name + "§7「この前、この町の騎士(?)の方に助けられまして」")
            playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5)
            Thread.sleep(2400)
            p.sendMessage("§c" + p.name + "§7「その方から町の中心に行くと良い。と言われたんですが、何をしたら良いのか分からなくて」")
            playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5)
            Thread.sleep(4000)
            p.sendMessage("§aアバエフ §7「ほう。要するに旅に出ている最中、死にかけになっているところをある騎士に助けてもらったと」")
            playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5)
            Thread.sleep(1000)
            p.sendMessage("§7" + p.name + "§7(旅中とも何も言ってないけど...あの騎士が伝えてくれたのかな？)")
            playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5)
            Thread.sleep(1000)
            p.sendMessage("§c" + p.name + "§7「そうです。前住んでいた町では§a「冒険者」§7という町公認の組織があったのですが...")
            Thread.sleep(4000)
            p.sendMessage("§aアバエフ §7「ふむふむ...確かにこの町でもその§a「冒険者」に近い組織はある")
            playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5)
            Thread.sleep(2000)
            p.sendMessage("§aアバエフ §7「しかしそれに入るにはこの町の上層部と会議をし、決定しなければならない」")
            playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5)
            Thread.sleep(300)
            p.sendMessage("§aアバエフ §7「その為に、まず君の職業を教えてくれないか？」")
            playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5)
            Thread.sleep(2500)

            Bukkit.getScheduler().runTask(plugin, Runnable { SelSkillClassMenu().inv.open(p) })
            isActive.remove(p.uniqueId)
        })
    }

    @EventHandler
    fun onPlayerDoorOpen(e: PlayerInteractEvent) {
        val member = plugin.memberManager.getMember(e.player.uniqueId)
        if (member == null) {
            if (e.action == Action.RIGHT_CLICK_BLOCK && !e.player.hasMetadata("start")) if (("" + e.clickedBlock!!.type).contains("DOOR")
            ) e.isCancelled = true
            if (e.item == null) return
            if (e.item!!.type == Material.AIR) return
            if (e.item!!.itemMeta.displayName == "クリックで夢から覚める") {
                start(e.player)
                e.player.inventory.remove(e.item!!)
            }
        }
    }

    private fun start(p: Player) {
        p.teleport(plugin.variables.getVariable(Variables.LOCATION_HOME, Bukkit.getWorld("world")!!.spawnLocation))
        Bukkit.getScheduler().runTask(plugin, Runnable {
            p.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 100, 0))
        })
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            Thread.sleep(2000)

            p.sendMessage("§c" + p.name + "§7「うーん...ここはどこだっけ」")
            playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5)
            Thread.sleep(4000)
            p.sendMessage("§c" + p.name + "§7「....」")
            playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5)
            Thread.sleep(3000)
            p.sendMessage("§c" + p.name + "§7「確か...昨日モンスターに襲われてるところを助けてもらったんだっけ...」")
            playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5)
            Thread.sleep(4000)
            p.sendMessage("§c" + p.name + "§7「とりあえず外に出てみるか...」")
            playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5)
            Thread.sleep(3000)
            playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 1.0)
            Bukkit.getScheduler().runTask(plugin, Runnable { p.setMetadata("start", FixedMetadataValue(plugin, true)); p.removePotionEffect(PotionEffectType.BLINDNESS) })
        })
    }
}