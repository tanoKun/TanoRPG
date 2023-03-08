package com.github.tanokun.tanorpg.player.quests.utils.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ActionData(val syntax: String, val parameters: String, val show: String, val dec: String)
