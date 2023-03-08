package com.github.tanokun.tanorpg.command

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.util.bindHologramAtLocation
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.arguments.TextArgument
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderInt
import io.lumine.xikage.mythicmobs.spawning.spawners.MythicSpawner
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import xyz.xenondevs.particle.ParticleEffect

class UtilCommand {
    init {
        registerMythicMobsUtil()
        registerGatheringUtil()
    }

    private fun registerMythicMobsUtil() {
        CommandAPICommand("util").withAliases("u").withAliases("mu")
            .withPermission(CommandPermission.fromString("tanorpg.command.util"))
            .withSubcommand(CommandAPICommand("spawner")
                .executesPlayer(PlayerCommandExecutor { sender, _ ->
                    sender.sendMessage(TanoRPG.PX + "Spawnerの位置と情報をすべて表示しました")
                    TanoRPG.playSound(sender, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1.0)
                    for (mythicSpawner in TanoRPG.plugin.mythicMobs.spawnerManager.spawners) {
                        showSpawnerHolo(mythicSpawner)
                    }
                })
            )
            .withSubcommand(CommandAPICommand("spawner")
                .withArguments(TextArgument("name").replaceSuggestions {
                    TanoRPG.plugin.mythicMobs.mobManager.mobNames.toList().toTypedArray()
                })
                .executesPlayer(PlayerCommandExecutor { sender: Player, args: Array<Any> ->
                    sender.sendMessage(TanoRPG.PX + "「" + args[0] + "」のSpawnerの位置と情報をすべて表示しました")
                    TanoRPG.playSound(sender, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1.0)
                    for (mythicSpawner in TanoRPG.plugin.mythicMobs.spawnerManager.spawners) {
                        if (mythicSpawner.typeName != args[0]) continue
                        showSpawnerHolo(mythicSpawner)
                    }
                })
            )

            .withSubcommand(CommandAPICommand("egg")
                .withArguments(TextArgument("name").replaceSuggestions {
                    TanoRPG.plugin.entityManager.entities.keys.toTypedArray()
                })
                .executesPlayer(PlayerCommandExecutor { sender: Player, args: Array<Any> ->
                    Bukkit.dispatchCommand(sender, "mm egg get ${args[0]}")
                    TanoRPG.playSound(sender, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1.0)
                })
            )


            .withSubcommand(CommandAPICommand("spawner")
                .withSubcommand(CommandAPICommand("remove")
                    .withArguments(TextArgument("mobspawner").replaceSuggestions {
                        TanoRPG.plugin.mythicMobs.spawnerManager.spawners.stream().map { s -> s.name }.toList()
                            .toTypedArray()
                    })
                    .executes(CommandExecutor { sender: CommandSender, args: Array<Any> ->
                        val name: String = args[0] as String
                        val mythicSpawner = TanoRPG.plugin.mythicMobs.spawnerManager.getSpawnerByName(name)
                        if (mythicSpawner == null) {
                            sender.sendMessage(TanoRPG.PX + "§cそのスポナーは存在しません")
                            return@CommandExecutor
                        }
                        TanoRPG.plugin.mythicMobs.spawnerManager.removeSpawner(mythicSpawner)
                        sender.sendMessage(TanoRPG.PX + "Spawner「$name」を削除しました")
                    })
                )
                .withSubcommand(CommandAPICommand("create")
                    .withArguments(TextArgument("mobname").replaceSuggestions {
                        TanoRPG.plugin.mythicMobs.mobManager.mobNames.stream().toList().toTypedArray()
                    })
                    .withArguments(TextArgument("spawnername").replaceSuggestions { arrayOf("-index") })
                    .withArguments(IntegerArgument("radius"))
                    .withArguments(IntegerArgument("max"))
                    .withArguments(IntegerArgument("perSpawn"))
                    .withArguments(IntegerArgument("cooldown"))
                    .executesPlayer(PlayerCommandExecutor { sender: Player, args: Array<Any> ->
                        val o = sender.location
                        val mobName = args[0] as String
                        var spawnerName = args[1] as String
                        val radius = args[2] as Int
                        val max = args[3] as Int
                        val perSpawn = args[4] as Int
                        val cooldown = args[5] as Int
                        val map = TanoRPG.plugin.mythicMobs.spawnerManager.spawners.stream().map { s -> s.name }
                            .toList().toTypedArray()
                        if (spawnerName == "-index") for (i in 1..map.size + 1) if (!map.equals("${mobName}_$i")) {
                            spawnerName = "${mobName}_$i"; break
                        }
                        val mythicSpawner =
                            TanoRPG.plugin.mythicMobs.spawnerManager.createSpawner(spawnerName, o, mobName)
                        if (mythicSpawner == null) {
                            sender.sendMessage(TanoRPG.PX + "§cそのスポナーはすでに存在しています")
                            return@PlayerCommandExecutor
                        }
                        sender.sendMessage(TanoRPG.PX + "「$mobName」のSpawner「$spawnerName」を作成しました")
                        mythicSpawner.spawnRadius = radius
                        mythicSpawner.maxMobs = PlaceholderInt.of(max.toString() + "")
                        mythicSpawner.mobsPerSpawn = perSpawn
                        mythicSpawner.cooldownSeconds = cooldown
                        TanoRPG.plugin.mythicMobs.spawnerManager.saveSpawners()
                        TanoRPG.playSound(sender, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1.0)
                        ParticleEffect.BARRIER.display(o.add(0.0, 0.5, 0.0))
                        showSpawnerHolo(mythicSpawner)
                    })

                    .withSubcommand(CommandAPICommand("create")
                        .withArguments(TextArgument("mobname").replaceSuggestions {
                            TanoRPG.plugin.mythicMobs.mobManager.mobNames.stream().toList().toTypedArray()
                        })
                        .withArguments(TextArgument("spawnername"))
                        .withArguments(IntegerArgument("radius"))
                        .withArguments(IntegerArgument("max"))
                        .withArguments(IntegerArgument("perSpawn"))
                        .withArguments(IntegerArgument("cooldown"))
                        .withArguments(IntegerArgument("activationRange"))
                        .executesPlayer(PlayerCommandExecutor { sender: Player, args: Array<Any> ->
                            val o = sender.location
                            val mobName = args[0] as String
                            var spawnerName = args[1] as String
                            val radius = args[2] as Int
                            val max = args[3] as Int
                            val perSpawn = args[4] as Int
                            val cooldown = args[5] as Int
                            val activationRange = args[6] as Int
                            val map = TanoRPG.plugin.mythicMobs.spawnerManager.spawners.stream()
                                .map { s -> s.name }.toList().toTypedArray()
                            if (spawnerName == "-index") for (i in 1..map.size + 1) {
                                if (!map.contains("${mobName}_$i")) {
                                    spawnerName = "${mobName}_$i"; break
                                }
                            }
                            val mythicSpawner = TanoRPG.plugin.mythicMobs.spawnerManager.createSpawner(
                                spawnerName,
                                o,
                                mobName
                            )
                            if (mythicSpawner == null) {
                                sender.sendMessage(TanoRPG.PX + "§cそのスポナーはすでに存在しています")
                                return@PlayerCommandExecutor
                            }
                            sender.sendMessage(TanoRPG.PX + "「$mobName」のSpawner「$spawnerName」を作成しました")
                            mythicSpawner.spawnRadius = radius
                            mythicSpawner.maxMobs = PlaceholderInt.of(max.toString())
                            mythicSpawner.mobsPerSpawn = perSpawn
                            mythicSpawner.cooldownSeconds = cooldown
                            mythicSpawner.activationRange = activationRange
                            TanoRPG.plugin.mythicMobs.spawnerManager.saveSpawners()
                            TanoRPG.playSound(sender, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1.0)
                            ParticleEffect.BARRIER.display(o.add(0.0, 0.5, 0.0))
                            showSpawnerHolo(mythicSpawner)
                        })
                    )
                )
            ).register()
    }

    private fun registerGatheringUtil() {
        CommandAPICommand("util").withAliases("u").withAliases("mu")
            .withPermission(CommandPermission.fromString("tanorpg.command.util"))
            .withSubcommand(CommandAPICommand("gathering")
                .withSubcommand(CommandAPICommand("addloc")
                    .withSubcommand(CommandAPICommand("angle")
                        .withArguments(StringArgument("gatheringName").replaceSuggestions {
                            TanoRPG.plugin.gatheringManager.gatherings.keys.toList().toTypedArray()
                        })
                        .executesPlayer(PlayerCommandExecutor { sender: Player, args: Array<Any> ->
                            TanoRPG.plugin.gatheringManager.gatherings[args[0] as String]?.let { gathering ->
                                val loc: Location = sender.getTargetBlock(5)?.location!!
                                loc.add(0.5, 0.0, 0.5)
                                sender.sendMessage(TanoRPG.PX + "現在のアングルのロケーションを追加しました")
                                gathering.getSpawnLocations().add(loc)
                                val formatLoc = gathering.getSpawnLocations().stream()
                                    .map { "${it.world.name} ${it.x} ${it.y} ${it.z}" }.toList()
                                gathering.config.config.set(
                                    "${gathering.id}.gathering.locations",
                                    formatLoc
                                )
                                gathering.config.saveConfig()
                                gathering.init()
                                TanoRPG.plugin.mythicMobs.spawnerManager.saveSpawners()
                            }
                        })
                    )
                )
            ).register()
    }

    private fun showSpawnerHolo(mythicSpawner: MythicSpawner) {
        val o = Location(
            Bukkit.getWorld(mythicSpawner.location.world.uniqueId),
            mythicSpawner.location.x,
            mythicSpawner.location.y + 0.5,
            mythicSpawner.location.z
        )
        val lore = arrayOfNulls<String>(6)
        lore[0] = "§6SpawnerName§7: " + mythicSpawner.name
        lore[1] = "§6MobName§7: " + mythicSpawner.typeName
        lore[2] = "§6Radius§7: " + mythicSpawner.spawnRadius
        lore[3] = "§6MaxMobs§7: " + mythicSpawner.maxMobs
        lore[4] = "§6MobsPerSpawn§7: " + mythicSpawner.mobsPerSpawn
        lore[5] = "§6Cooldown§7: " + mythicSpawner.cooldownSeconds
        ParticleEffect.BARRIER.display(o)
        bindHologramAtLocation(lore, o, 160, 0.0, 2.3, 0.0)
        Bukkit.getScheduler().runTaskLater(TanoRPG.plugin, Runnable { ParticleEffect.BARRIER.display(o) }, 80)
    }
}