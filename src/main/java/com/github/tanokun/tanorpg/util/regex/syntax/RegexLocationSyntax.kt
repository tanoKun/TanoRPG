package com.github.tanokun.tanorpg.util.regex.syntax

import org.bukkit.Bukkit
import org.bukkit.Location
import java.util.regex.Pattern
//                                                            world:           x             :         y            :           z          :         yaw          :        pitch
class RegexLocationSyntax: RegexSyntax(Pattern.compile("(([^\"]*):([+-]?[0-9]+\\.[0-9]*):([+-]?[0-9]+\\.[0-9]*):([+-]?[0-9]+\\.[0-9]*):([+-]?[0-9]+\\.[0-9]*):([+-]?[0-9]+\\.[0-9]*))"), 7) {

    override fun get(string: String): Any {
        val list = string.split(":")
        return Location(Bukkit.getWorld(list[0]), list[1].toDouble(), list[2].toDouble(), list[3].toDouble(), list[4].toFloat(), list[5].toFloat())
    }
}