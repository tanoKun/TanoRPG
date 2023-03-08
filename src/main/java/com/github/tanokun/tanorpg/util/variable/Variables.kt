package com.github.tanokun.tanorpg.util.variable

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.util.io.Config
import com.github.tanokun.tanorpg.util.io.MapNode
import com.github.tanokun.tanorpg.util.regex.RegexMatcher
import org.bukkit.Location


class Variables {
    companion object {
        const val LOCATION_GUILD = "location.guild"
        const val LOCATION_HOME = "location.home"
        const val LOCATION_RESPAWN = "location.respawn"
        const val REGION_GUILD = "region.guild"
        const val REGION_HOME = "region.home"
    }
    val file: Config = Config(TanoRPG.plugin, "variables.yml")

    val variables = HashMap<String, MapNode<VariableType, Any>>()

    init {
        file.saveConfig()
        load()
    }

    fun load() {
        VariableType.values().forEach { variableType ->
            val matcher = RegexMatcher("<String:name>, <${variableType.type}:value>")
            file.config.getStringList(variableType.name).forEach { data ->
                matcher.matchResult(data) {
                    variables[it.get("name", "unknown")] = MapNode(variableType, it.get("value"))
                }
            }
        }
    }

    @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
    fun save() {
        val integers = ArrayList<String>()
        val doubles = ArrayList<String>()
        val strings = ArrayList<String>()
        val locations = ArrayList<String>()
        variables.forEach { map ->
            when (map.value.key) {
                VariableType.INTEGER -> integers.add("${map.key}, ${map.value.value}")
                VariableType.DOUBLE -> doubles.add("${map.key}, ${map.value.value}")
                VariableType.STRING -> strings.add("${map.key}, ${map.value.value}")
                VariableType.LOCATION -> {
                    val loc = map.value.value as Location
                    locations.add("${map.key}, ${loc.world.name}:${loc.x}:${loc.y}:${loc.z}:${loc.yaw}:${loc.pitch}")
                }
            }

            file.config.set("INTEGER", integers)
            file.config.set("DOUBLE", doubles)
            file.config.set("STRING", strings)
            file.config.set("LOCATION", locations)

            file.saveConfig()
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getVariable(name: String, value: T): T {
        return try {
            if (variables[name] == null) value
            else variables[name]?.value as T
        }catch (e: ClassCastException) {
            value
        }
    }

    fun setVariable(name: String, value: Any, variableType: VariableType) = run { variables[name] = MapNode(variableType, value) }
}