package com.github.tanokun.tanorpg.util.regex

import com.github.tanokun.tanorpg.util.regex.syntax.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegexMatcher(var original: String) {
    private val pattern: Pattern
    private val syntax = HashMap<String, RegexSyntax>()
    private val names = ArrayList<String>()

    init {
        val p: Pattern = Pattern.compile("<([a-zA-Z_0-9]+):([a-zA-Z_0-9]+)>")
        val m: Matcher = p.matcher(original)
        m.results().forEach {
            val type = it.group(1)
            val name = it.group(2)
            require(patternSyntax[type] != null) {"Type:${type} は存在しません"}

            names.add(name)
            syntax[name] = patternSyntax[type]!!
            original = original.replace(it.group(0), "pattern_$name")
        }

        original = original.replace(".", "\\.")
        original = original.replace("^", "\\^")
        original = original.replace("$", "\\$")
        original = original.replace("[", "\\[")
        original = original.replace("]", "\\]")
        original = original.replace("*", "\\*")
        original = original.replace("+", "\\+")
        original = original.replace("?", "\\?")
        original = original.replace("|", "\\|")
        original = original.replace("(", "\\(")
        original = original.replace(")", "\\)")
        original = original.replace("{", "\\{")

        for (name in syntax.keys) {
            original = original.replace("pattern_$name", syntax[name]!!.pattern.pattern())
        }

        pattern = Pattern.compile("(${original})")
    }

    fun matchResult(s: String, function: (RegexMatchResults) -> Unit) {
        val m: Matcher = pattern.matcher(s)
        m.results().forEach {
            val regexMatchResults = RegexMatchResults(HashMap())

            var o = 1
            names.forEach { name ->
                regexMatchResults.getResults()[name] = syntax[name]!!.get(it.group(o + 1))
                o += syntax[name]!!.next
            }
            function(regexMatchResults)
        }
    }

    companion object {
        private val patternSyntax = HashMap<String, RegexSyntax>()
        init {
            patternSyntax["Int"] = RegexIntSyntax()
            patternSyntax["Double"] = RegexDoubleSyntax()
            patternSyntax["String"] = RegexStringSyntax()
            patternSyntax["Sound"] = RegexSoundSyntax()
            patternSyntax["Material"] = RegexMaterialSyntax()
            patternSyntax["World"] = RegexWorldSyntax()
            patternSyntax["Location"] = RegexLocationSyntax()
            patternSyntax["BlockLocation"] = RegexBlockLocationSyntax()
        }
    }
}
