package com.github.tanokun.tanorpg.player.skill.execute.soldier

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.damage.DamageType
import com.github.tanokun.tanorpg.damage.PlayerDamageExpression
import com.github.tanokun.tanorpg.player.Member
import com.github.tanokun.tanorpg.player.skill.SkillClass
import com.github.tanokun.tanorpg.player.skill.SkillComboType
import com.github.tanokun.tanorpg.player.skill.execute.Skill
import com.github.tanokun.tanorpg.player.skill.execute.SkillCombo
import com.github.tanokun.tanorpg.util.getActiveEntity
import com.github.tanokun.tanorpg.util.getNearActiveEntity
import com.github.tanokun.tanorpg.util.image.ImageParticles
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import xyz.xenondevs.particle.ParticleEffect
import xyz.xenondevs.particle.data.color.RegularColor
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import javax.imageio.ImageIO
import kotlin.math.roundToLong


class CoolTimeDownSkill : Skill(
    "鎌鼬", 0, 0, SkillCombo(SkillComboType.S, SkillComboType.LC, SkillComboType.LC), 0,
    ArrayList(listOf("§f斬撃を前方に飛ばします")),
    ArrayList(listOf(SkillClass.SOLDIER)), Material.IRON_HOE
) {
    private val imageParticles = ImageParticles(ImageIO.read(File(TanoRPG.plugin.dataFolder, "images\\skill\\circleAttack.png")), 1)

    init {
        imageParticles.ratio = 1.0
    }

    override fun execute(m: Member, p: Player) {
        val l = p.location.add(0.0, 1.0, 0.0)
        l.pitch = 0F

        val d = l.direction.normalize()

        l.yaw = l.yaw + 90
        l.add(l.direction.normalize().x * 4, 0.0, l.direction.normalize().z * 4)
        l.yaw = l.yaw - 90

        val attack = ArrayList<UUID>()
        Observable.interval((50 * 1.5).roundToLong(), TimeUnit.MILLISECONDS).take(13).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread())
            .subscribe(object : Observer<Long> {
                override fun onNext(time: Long) {
                    l.add(d)
                    for (spot in imageParticles.getParticles(l) test@ { color -> val r = RegularColor(color); r.effect = ParticleEffect.SWEEP_ATTACK; return@test r }) {
                        spot.value.effect.display(spot.key, 0.5f, 0.5f, 0.5f, 0f, 1, null, Bukkit.getOnlinePlayers())

                        Bukkit.getScheduler().scheduleSyncDelayedTask(TanoRPG.plugin) {
                            getNearActiveEntity(spot.key, 2.0).forEach { e: Entity ->
                                if (!attack.contains(e.uniqueId)) {
                                    PlayerDamageExpression(m, getActiveEntity(e)!!)
                                        .multi(1.3)
                                        .attack(DamageType.CONFUSION)
                                    e.velocity = d.add(Vector(0.0, 0.2, 0.0))
                                    attack.add(e.uniqueId)
                                }
                            }
                        }
                    }
                }

                override fun onSubscribe(p0: Disposable) {} override fun onError(p0: Throwable) {p0.printStackTrace()} override fun onComplete() {}

            })
    }
}