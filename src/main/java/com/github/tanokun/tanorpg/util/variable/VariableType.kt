package com.github.tanokun.tanorpg.util.variable

import com.github.tanokun.tanorpg.util.regex.syntax.*

enum class VariableType(val syntax: RegexSyntax, val type: String) {
    INTEGER(RegexIntSyntax(), "Int"),
    DOUBLE(RegexDoubleSyntax(), "Double"),
    STRING(RegexStringSyntax(), "String"),
    LOCATION(RegexLocationSyntax(), "Location"),
}