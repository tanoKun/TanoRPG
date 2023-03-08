package com.github.tanokun.tanorpg.player

import com.github.tanokun.tanorpg.player.skill.SkillComboType

class Attack {
    var attackCombo = 0
    var nextAttackCombo = 0
    var lastAttackTicks = 0
    var isAttackWait = false
    var isSkillComboTrigger = false
    val skillCombos = ArrayList<SkillComboType>()
    var attack: Boolean = false

    fun nextAttackCombo(): Int {
        attackCombo++
        return attackCombo
    }

    fun nextLastAttackTicks(): Int {
        lastAttackTicks++
        return lastAttackTicks
    }
}