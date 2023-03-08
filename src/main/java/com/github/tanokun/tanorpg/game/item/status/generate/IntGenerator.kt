package com.github.tanokun.tanorpg.game.item.status.generate

import com.github.tanokun.tanorpg.player.status.StatusType
import com.github.tanokun.tanorpg.util.chance
import com.github.tanokun.tanorpg.util.regex.RegexMatcher
import java.util.*

@StatusGenerator("--INT <percent>%{<min>~<max>}...")
class IntGenerator(data: String, statusType: StatusType) : StatusGeneratorHandler(statusType) {

    private val data = ArrayList<MultiInt>()

    init {
        val regexMatcher = RegexMatcher("<Double:percent>%{<Int:min>~<Int:max>}")
        regexMatcher.matchResult(data) {
            val percent: Double = it.get("percent", 10.0)
            val min: Int = it.get("min", 10)
            val max: Int = it.get("max", 10)
            require(percent >= 0) { "確率を0%以下にすることはできません" }
            require(max >= min) { "maxをmin未満にすることはできません" }
            this.data.add(MultiInt(percent, min, max))
        }
    }

    override fun generate(p: Double): Double {
        var multiInt: MultiInt? = null
        var i = 0
        while (multiInt == null) {
            if (i >= 10) multiInt = data[data.size - 1]
            for (temp in this.data) {
                val percent = temp.percent * (1 + (p / 100))
                if (chance(percent)) {multiInt = temp; break}
            }
            i++
        }

        return multiInt.random().toDouble()
    }

    override fun generateMax(): Double {
        return data[0].max.toDouble()
    }

    data class MultiInt(val percent: Double, val min: Int, val max: Int) {
        fun random(): Int = Random().ints(1, min, max + 1).sum()
    }
}
