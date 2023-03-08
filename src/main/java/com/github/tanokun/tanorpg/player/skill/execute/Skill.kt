package com.github.tanokun.tanorpg.player.skill.execute

import com.github.tanokun.tanorpg.player.Member
import com.github.tanokun.tanorpg.player.skill.SkillClass
import org.bukkit.Material
import org.bukkit.entity.Player

abstract class Skill(val name: String, val lvl: Int, val mp: Int, val combo: SkillCombo, val ct: Int, val lore: ArrayList<String>, val job: ArrayList<SkillClass>, val item: Material) {

    abstract fun execute(m: Member, p: Player)
}