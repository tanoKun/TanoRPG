package com.github.tanokun.tanorpg.player.skill.execute.mage

import com.github.tanokun.tanorpg.TanoRPG.Companion.playSound
import com.github.tanokun.tanorpg.TanoRPG.Companion.plugin
import com.github.tanokun.tanorpg.damage.DamageType
import com.github.tanokun.tanorpg.damage.PlayerDamageExpression
import com.github.tanokun.tanorpg.player.Member
import com.github.tanokun.tanorpg.player.skill.SkillClass
import com.github.tanokun.tanorpg.player.skill.SkillComboType
import com.github.tanokun.tanorpg.player.skill.execute.Skill
import com.github.tanokun.tanorpg.player.skill.execute.SkillCombo
import com.github.tanokun.tanorpg.util.getActiveEntity
import com.github.tanokun.tanorpg.util.getNearActiveEntity
import com.github.tanokun.tanorpg.util.getNearPlayers
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity
import net.minecraft.world.entity.EntityLightning
import net.minecraft.world.entity.EntityTypes
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.craftbukkit.v1_17_R1.CraftServer
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftLightningStrike
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import xyz.xenondevs.particle.ParticleEffect
import xyz.xenondevs.particle.data.color.ParticleColor
import xyz.xenondevs.particle.data.color.RegularColor
import java.awt.Color
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class LightningSKill : Skill(
    "ライトニング", 5, 30, SkillCombo(SkillComboType.LC, SkillComboType.S, SkillComboType.LC), 0,
    ArrayList(listOf("§f前方の敵に雷を当てます")),
    ArrayList(listOf(SkillClass.MAGE)), Material.AMETHYST_CLUSTER
) {
    override fun execute(m: Member, p: Player) {
        val location = p.location
        location.add(location.direction.normalize().x * 6, 0.0, location.direction.normalize().z * 6)
        object : BukkitRunnable() {
            var t = 1
            var color: ParticleColor = RegularColor(Color.red)
            override fun run() {
                for (i in 0..36) {
                    val x = 2 * sin(i * 0.5)
                    val z = 2 * cos(i * 0.5)
                    location.add(x, 0.0, z)
                    ParticleEffect.REDSTONE.display(location, Vector(0, 1, 0), 0f, 10, color, Bukkit.getOnlinePlayers())
                    location.subtract(x, 0.0, z)
                }
                if (t == 3) {
                    cancel()
                    val entityLightning = EntityLightning(EntityTypes.U, (location.world as CraftWorld).handle)
                    val lightning = CraftLightningStrike(Bukkit.getServer() as CraftServer, entityLightning)
                    lightning.handle.setLocation(location.x, location.y, location.z, 0f, 0f)
                    Arrays.stream(getNearPlayers(location, 20.0)).forEach { player: Player ->
                        (player as CraftPlayer).handle.b.sendPacket(PacketPlayOutSpawnEntity(lightning.handle, 0))
                        playSound(player, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 10, 1.0)
                    }
                    Arrays.stream(getNearActiveEntity(location, 2.0)).forEach { e: Entity ->
                        PlayerDamageExpression(m, getActiveEntity(e)!!)
                            .multi(1.3)
                            .attack(DamageType.NORMAL)
                    }
                }
                t++
            }
        }.runTaskTimer(plugin, 0, 10)
    }
}