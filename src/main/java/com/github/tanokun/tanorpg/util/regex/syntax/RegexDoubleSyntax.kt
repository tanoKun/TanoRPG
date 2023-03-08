package com.github.tanokun.tanorpg.util.regex.syntax

import java.util.regex.Pattern

class RegexDoubleSyntax: RegexSyntax(Pattern.compile("([+-]?[0-9]+\\.[0-9]*)"), 1) {
    override fun get(string: String): Any = string.toDouble()
}