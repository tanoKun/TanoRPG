package com.github.tanokun.tanorpg.util.regex.syntax

import org.bukkit.Sound
import java.util.regex.Pattern

class RegexSoundSyntax: RegexSyntax(Pattern.compile("([^\"]*)"), 1) {
    override fun get(string: String): Any = Sound.valueOf(string)
}