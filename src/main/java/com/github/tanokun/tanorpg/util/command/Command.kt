package com.github.tanokun.tanorpg.util.command

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Command(val parentName: String, val name: String, val desc: String, val flags: String = "")