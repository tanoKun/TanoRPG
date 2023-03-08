package com.github.tanokun.tanorpg.util.regex.syntax

import java.util.regex.Pattern

class RegexStringSyntax: RegexSyntax(Pattern.compile("([^\"]*)"), 1)  {
    override fun get(string: String): Any = string
}