package com.github.tanokun.tanorpg.player.quests.utils

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.player.quests.actions.QuestAction
import com.github.tanokun.tanorpg.player.quests.conditions.QuestCondition
import com.github.tanokun.tanorpg.player.quests.tasks.QuestTask
import com.github.tanokun.tanorpg.player.quests.utils.annotation.ActionData
import com.github.tanokun.tanorpg.player.quests.utils.annotation.ConditionData
import com.github.tanokun.tanorpg.player.quests.utils.annotation.TaskData
import com.github.tanokun.tanorpg.util.io.Config
import com.github.tanokun.tanorpg.util.regex.RegexMatcher
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import java.io.File
import java.util.jar.JarFile
fun loadActionClasses() {
    val basePackage = "com/github/tanokun/tanorpg/player/quests/actions/"
    val jar = JarFile(getFile())
    for (e in jar.entries()) {
        if (e.name.startsWith(basePackage) && e.name.endsWith(".class")) {
            val clazz = Class.forName(
                e.name.replace("/", ".").substring(0, e.name.length - ".class".length),
                true,
                TanoRPG.plugin.javaClass.classLoader
            )
            if (clazz.getAnnotation(ActionData::class.java) == null) continue
            val annotation = clazz.getAnnotation(ActionData::class.java) as ActionData
            QuestAction.actions[annotation.syntax] = clazz as Class<out QuestAction>
        }
    }
}

fun loadTaskClasses() {
    val basePackage = "com/github/tanokun/tanorpg/player/quests/tasks/models/"
    val jar = JarFile(getFile())
    for (e in jar.entries()) {
        if (e.name.startsWith(basePackage) && e.name.endsWith(".class")) {
            val clazz = Class.forName(
                e.name.replace("/", ".").substring(0, e.name.length - ".class".length),
                true,
                TanoRPG.plugin.javaClass.classLoader
            )
            if (clazz.getAnnotation(TaskData::class.java) == null) continue
            val annotation = clazz.getAnnotation(TaskData::class.java) as TaskData
            QuestTask.tasks[annotation.syntax] = clazz as Class<out QuestTask<Any>>
            Bukkit.getPluginManager().registerEvents(clazz.constructors[0].newInstance(RegexMatcher(annotation.parameters), "", Config(TanoRPG.plugin, "maker.yml")) as Listener, TanoRPG.plugin)
        }
    }
}

fun loadConditionClasses() {
    val basePackage = "com/github/tanokun/tanorpg/player/quests/conditions/"
    val jar = JarFile(getFile())
    for (e in jar.entries()) {
        if (e.name.startsWith(basePackage) && e.name.endsWith(".class")) {
            val clazz = Class.forName(
                e.name.replace("/", ".").substring(0, e.name.length - ".class".length),
                true,
                TanoRPG.plugin.javaClass.classLoader
            )
            if (clazz.getAnnotation(ConditionData::class.java) == null) continue
            val annotation = clazz.getAnnotation(ConditionData::class.java) as ConditionData
            QuestCondition.conditions[annotation.syntax] = clazz as Class<out QuestCondition>
        }
    }
}

fun getFile(): File {
    return File("plugins\\TanoRPG.jar")
}
