@file:Suppress("UNCHECKED_CAST")

package com.github.tanokun.tanorpg.util.regex

open class RegexMatchResults(private val results: HashMap<String, Any>) {
    fun<T> get(index: String, d: T): T {
        val any = results[index]
        return if (any != null) any as T else d
    }

    fun get(index: String): Any {
        return results[index]!!
    }

    fun getResults(): HashMap<String, Any> = results
}