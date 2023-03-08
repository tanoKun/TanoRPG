package com.github.tanokun.tanorpg.player.skill.execute

import com.github.tanokun.tanorpg.player.skill.SkillComboType
import java.util.*

class SkillCombo {
    val combos = ArrayList<SkillComboType>()

    constructor(no1: SkillComboType?, no2: SkillComboType?, no3: SkillComboType?) {
        require(!(no1 == null || no2 == null || no3 == null)) { "SKillComboType can't be null." }
        combos.add(no1)
        combos.add(no2)
        combos.add(no3)
    }

    constructor(combos: ArrayList<SkillComboType>?) {
        this.combos.addAll(combos!!)
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as SkillCombo
        return combos == that.combos
    }

    override fun hashCode(): Int {
        return Objects.hash(combos)
    }
}