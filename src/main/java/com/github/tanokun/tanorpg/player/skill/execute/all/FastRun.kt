package com.github.tanokun.tanorpg.player.skill.execute.all

import com.github.tanokun.tanorpg.TanoRPG.Companion.playSound
import com.github.tanokun.tanorpg.TanoRPG.Companion.plugin
import com.github.tanokun.tanorpg.player.Member
import com.github.tanokun.tanorpg.player.skill.SkillClass
import com.github.tanokun.tanorpg.player.skill.SkillComboType
import com.github.tanokun.tanorpg.player.skill.execute.Skill
import com.github.tanokun.tanorpg.player.skill.execute.SkillCombo
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import xyz.xenondevs.particle.ParticleEffect

class FastRun : Skill("ダッシュ", 1, 12, SkillCombo(SkillComboType.S, SkillComboType.LC, SkillComboType.RC), 3, ArrayList(listOf("§f前方に素早くダッシュする")),
    ArrayList(listOf(SkillClass.ARCHER, SkillClass.SOLDIER, SkillClass.MAGE, SkillClass.PRIEST)), Material.FEATHER) {

    override fun execute(m: Member, p: Player) {
        playSound(p, Sound.ENTITY_GHAST_SHOOT, 10, 1.0)
        val vector = p.location.direction.normalize()
        object : BukkitRunnable() {
            var t = 0
            override fun run() {
                if (t > 10) cancel()
                t++
                ParticleEffect.SWEEP_ATTACK.display(p.location.add(0.0, 1.0, 0.0), 0.7f, 0.7f, 0.7f, 0.3f, 2, null, Bukkit.getOnlinePlayers())
            }
        }.runTaskTimer(plugin, 1, 1)
        vector.multiply(3)
        vector.y = 0.1
        p.velocity = vector
    }
}