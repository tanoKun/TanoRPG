package com.github.tanokun.tanorpg.util.regex.syntax

import java.util.regex.Pattern

abstract class RegexSyntax(val pattern: Pattern, val next: Int) {
    abstract fun get(string: String): Any
}