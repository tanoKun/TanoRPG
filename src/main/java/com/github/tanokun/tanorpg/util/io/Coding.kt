package com.github.tanokun.tanorpg.util.io

import java.util.*

object Coding {
    @JvmStatic
    fun encode(text: String): String {
        return Base64.getEncoder().encodeToString(text.toByteArray())
    }

    @JvmStatic
    fun decode(text: String?): String {
        return String(Base64.getDecoder().decode(text))
    }
}