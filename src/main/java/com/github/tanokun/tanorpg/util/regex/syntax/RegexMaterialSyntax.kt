package com.github.tanokun.tanorpg.util.regex.syntax

import org.bukkit.Material
import java.util.regex.Pattern

class RegexMaterialSyntax: RegexSyntax(Pattern.compile("([^\"]*)"), 1) {
    override fun get(string: String): Any = Material.valueOf(string)
}