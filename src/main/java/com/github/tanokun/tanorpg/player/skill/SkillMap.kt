package com.github.tanokun.tanorpg.player.skill

import com.github.tanokun.tanorpg.player.Member
import com.github.tanokun.tanorpg.player.skill.execute.SkillCombo
import com.github.tanokun.tanorpg.util.SaveMarker
import com.github.tanokun.tanorpg.util.io.Config
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class SkillMap : SaveMarker<SkillMap?> {
    var member: Member? = null
    var slotSkills: ArrayList<SkillCombo?>? = ArrayList()
        private set
    var openSkills: ArrayList<SkillCombo>? = ArrayList()
        private set

    init {
        for (i in 0..7) {
            slotSkills!!.add(null)
        }
    }

    override fun save(config: Config, key: String) {
        val gson = Gson()
        config.config[key + "slotSkills"] = gson.toJson(slotSkills)
        config.config[key + "openSkills"] = gson.toJson(openSkills)
    }

    override fun load(config: Config, key: String): SkillMap {
        val type = object : TypeToken<ArrayList<SkillCombo>>() {}.type
        slotSkills = Gson().fromJson(config.config.getString(key + "slotSkills"), type)
        openSkills = Gson().fromJson(config.config.getString(key + "openSkills"), type)
        if (slotSkills == null) slotSkills = ArrayList()
        if (openSkills == null) openSkills = ArrayList()
        return this
    }
}