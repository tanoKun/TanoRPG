package com.github.tanokun.tanorpg.game.item.status.generate

import com.github.tanokun.tanorpg.player.status.StatusType
import com.github.tanokun.tanorpg.util.chance
import com.github.tanokun.tanorpg.util.regex.RegexMatcher
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import java.util.*
import java.util.concurrent.ThreadLocalRandom

@StatusGenerator("--DOUBLE <percent>%{<min>~<max>}...")
class DoubleGenerator(data: String, statusType: StatusType) : StatusGeneratorHandler(statusType) {

    private val data = ArrayList<MultiDouble>()

    init {
        val regexMatcher = RegexMatcher("<Double:percent>%{<Double:min>~<Double:max>}")
        regexMatcher.matchResult(data) {
            val percent: Double = it.get("percent", 10.0)
            val min: Double = it.get("min", 10.0)
            val max: Double = it.get("max", 10.0)
            require(percent >= 0) { "確率を0%以下にすることはできません" }
            require(max >= min) { "maxをmin未満にすることはできません" }
            this.data.add(MultiDouble(percent, min, max))
        }
    }

    override fun generate(p: Double): Double {
        var multiDouble: MultiDouble? = null
        var i = 0
        while (multiDouble == null) {
            if (i >= 10) multiDouble = data[data.size - 1]
            for (temp in this.data) {
                val percent = temp.percent * (1 + (p / 100))
                if (chance(percent)) {multiDouble = temp; break}
            }
            i++
        }

        return multiDouble.random()
    }

    override fun generateMax(): Double {
        return data[0].max
    }

    data class MultiDouble(val percent: Double, val min: Double, val max: Double) {
        fun random(): Double = ThreadLocalRandom.current().doubles(1, min, max).toArray().sum() //Random().doubles(1, min, max + 1).sum()
    }
}
