package com.github.tanokun.tanorpg.player.skill.execute.priest

import com.github.tanokun.tanorpg.TanoRPG.Companion.playSound
import com.github.tanokun.tanorpg.TanoRPG.Companion.plugin
import com.github.tanokun.tanorpg.player.Member
import com.github.tanokun.tanorpg.player.skill.SkillClass
import com.github.tanokun.tanorpg.player.skill.SkillComboType
import com.github.tanokun.tanorpg.player.skill.execute.Skill
import com.github.tanokun.tanorpg.player.skill.execute.SkillCombo
import com.github.tanokun.tanorpg.player.status.buff.BuffType
import com.github.tanokun.tanorpg.util.getNearPlayers
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import xyz.xenondevs.particle.ParticleEffect
import xyz.xenondevs.particle.data.color.ParticleColor
import xyz.xenondevs.particle.data.color.RegularColor
import java.awt.Color
import kotlin.math.cos
import kotlin.math.sin

class CoolTimeDownSkill : Skill(
    "心身強化", 15, 60, SkillCombo(SkillComboType.DR, SkillComboType.DR, SkillComboType.S), 120,
    ArrayList(listOf("§f半径5mの味方に「スキルCT減少15%」を与えます")),
    ArrayList(listOf(SkillClass.PRIEST)), Material.OXEYE_DAISY
) {
    override fun execute(m: Member, p: Player) {
        val location = p.location
        val teleport = p.location
        object : BukkitRunnable() {
            var t = 1
            var color: ParticleColor = RegularColor(Color.red)
            override fun run() {
                if (t == 4) {
                    cancel()
                    location.add(0.0, 0.5, 0.0)
                    var i = 0
                    var r = 0
                    for (t in 0..374) {
                        if (i == 75) {
                            i = 0
                            r++
                        }
                        val x = r * cos(i * 0.2)
                        val z = r * sin(i * 0.2)
                        location.add(x, 0.0, z)
                        ParticleEffect.VILLAGER_HAPPY.display(location, 0.5f, 0f, 0.5f, 1f, 4, null, Bukkit.getOnlinePlayers())
                        location.subtract(x, 0.0, z)
                        i++
                    }
                    for (player in getNearPlayers(teleport, 5.0)) {
                        val (_, _, _, _, _, _, _, buffMap) = plugin.memberManager.getMember(player.uniqueId) ?: continue
                        buffMap.addBuff(BuffType.SKILL_COOL_TIME_15, 60, player)
                        ParticleEffect.LIGHT.display(
                            player.location.add(0.0, 1.0, 0.0),
                            Vector(0, 1, 0),
                            0f,
                            5,
                            null,
                            Bukkit.getOnlinePlayers()
                        )
                        playSound(player, Sound.ENTITY_EVOKER_CAST_SPELL, 10, 1.0)
                    }
                    i = 0
                    while (i < 63) {
                        val x = 5 * sin(i * 0.5)
                        val z = 5 * cos(i * 0.5)
                        location.add(x, 0.0, z)
                        ParticleEffect.REDSTONE.display(location, Vector(0, 1, 0), 0f, 10, color, Bukkit.getOnlinePlayers())
                        location.subtract(x, 0.0, z)
                        i++
                    }
                    return
                }
                for (i in 0..62) {
                    val x = 5 * sin(i * 0.5)
                    val z = 5 * cos(i * 0.5)
                    location.add(x, 0.0, z)
                    ParticleEffect.REDSTONE.display(location, Vector(0, 1, 0), 0f, 10, color, Bukkit.getOnlinePlayers())
                    location.subtract(x, 0.0, z)
                }
                playSound(p, Sound.BLOCK_LEVER_CLICK, 10, 1.0)
                t++
            }
        }.runTaskTimer(plugin, 0, 20)
        object : BukkitRunnable() {
            var t = 0
            override fun run() {
                if (t == 60) cancel()
                p.teleport(teleport)
                t++
            }
        }.runTaskTimer(plugin, 0, 1)
    }
}