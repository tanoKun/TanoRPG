package com.github.tanokun.tanorpg.util.regex.syntax

import org.bukkit.Bukkit
import java.util.regex.Pattern

class RegexWorldSyntax: RegexSyntax(Pattern.compile("([^\"]*)"), 1) {
    override fun get(string: String): Any = Bukkit.getWorld(string)!!
}