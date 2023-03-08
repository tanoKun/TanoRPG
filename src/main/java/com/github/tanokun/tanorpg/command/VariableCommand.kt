package com.github.tanokun.tanorpg.command

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.util.variable.VariableType
import com.github.tanokun.tanorpg.util.variable.Variables
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.arguments.*
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.wrappers.Location2D
import dev.jorel.commandapi.wrappers.Rotation
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import org.bukkit.Location
import org.bukkit.craftbukkit.libs.org.apache.commons.io.input.Tailer
import org.w3c.dom.Text

class VariableCommand {
    init {
        registerSetVariable()
    }

    private fun registerSetVariable() {
        CommandAPICommand("variable").withAliases("var")
            .withPermission(CommandPermission.fromString("tanorpg.command.variable"))
            .withSubcommand(CommandAPICommand("set")
                .withSubcommand(CommandAPICommand("Integer")
                    .withArguments(StringArgument("int.name"))
                    .withArguments(IntegerArgument("value"))
                    .executes(CommandExecutor { _, args ->
                        val name = args[0] as String
                        val value = args[1] as Int
                        TanoRPG.plugin.variables.setVariable(name, value, VariableType.INTEGER)
                        TanoRPG.plugin.variables.save()
                    })
                )
                .withSubcommand(CommandAPICommand("Double")
                    .withArguments(StringArgument("name"))
                    .withArguments(DoubleArgument("value"))
                    .executes(CommandExecutor { _, args ->
                        val name = args[0] as String
                        val value = args[1] as Double
                        TanoRPG.plugin.variables.setVariable(name, value, VariableType.DOUBLE)
                        TanoRPG.plugin.variables.save()
                    })
                )
                .withSubcommand(CommandAPICommand("String")
                    .withArguments(StringArgument("str.name").replaceSuggestions { arrayOf("region.guild", "region.home") })
                    .withArguments(TextArgument("value"))
                    .executes(CommandExecutor { _, args ->
                        val name = args[0] as String
                        val value = args[1] as String
                        TanoRPG.plugin.variables.setVariable(name, value, VariableType.STRING)
                        TanoRPG.plugin.variables.save()
                    })
                )
                .withSubcommand(CommandAPICommand("Location")
                    .withArguments(StringArgument("loc.name").replaceSuggestions { arrayOf("location.guild", "location.home", "location.respawn") })
                    .withArguments(LocationArgument("value"))
                    .executes(CommandExecutor { _, args ->
                        val name = args[0] as String
                        val value = args[1] as Location
                        TanoRPG.plugin.variables.setVariable(name, value, VariableType.LOCATION)
                        TanoRPG.plugin.variables.save()
                    })
                )
                .withSubcommand(CommandAPICommand("Location")
                    .withArguments(StringArgument("loc.name").replaceSuggestions { arrayOf("location.guild", "location.home", "location.respawn") })
                    .withArguments(LocationArgument("value"))
                    .withArguments(RotationArgument("r"))
                    .executes(CommandExecutor { _, args ->
                        val name = args[0] as String
                        val value = args[1] as Location
                        val r = args[2] as Rotation
                        value.yaw = r.yaw
                        value.pitch = r.pitch
                        TanoRPG.plugin.variables.setVariable(name, value, VariableType.LOCATION)
                        TanoRPG.plugin.variables.save()
                    })
                )
            )

            .withSubcommand(CommandAPICommand("remove")
                .withArguments(StringArgument("name").replaceSuggestions { TanoRPG.plugin.variables.variables.keys.toTypedArray() })
                .executes(CommandExecutor { _, args ->
                    TanoRPG.plugin.variables.variables.remove(args[0])
                    TanoRPG.plugin.variables.file.saveConfig()
                }))

            .withSubcommand(CommandAPICommand("list")
                .executes(CommandExecutor { sender, _ ->
                    TanoRPG.plugin.variables.variables.forEach { (name, value) ->
                        sender.sendMessage("${name}, ${value.key}, ${value.value}")
                    }
                })
            ).register()

    }
}