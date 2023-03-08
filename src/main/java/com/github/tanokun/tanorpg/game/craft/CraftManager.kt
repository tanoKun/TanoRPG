package com.github.tanokun.tanorpg.game.craft

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.TanoRPG.Companion.plugin
import com.github.tanokun.tanorpg.game.craft.special.SpecialCraft
import com.github.tanokun.tanorpg.game.item.status.generate.StatusGeneratorHandler
import com.github.tanokun.tanorpg.game.item.status.generate.StatusGeneratorType
import com.github.tanokun.tanorpg.player.status.StatusType
import com.github.tanokun.tanorpg.util.io.Config
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault
import java.io.File

class CraftManager(p: Player?) {
    private val crafts = HashMap<String, Craft>()
    private val npcIds = HashMap<Int, String>()

    init {
        loadCraft(p, false, Config(plugin, "crafts").file)
    }

    private fun loadCraft(p: Player?, b: Boolean, tempFile: File) {
        var path = ""
        var filePath = ""
        val errors = HashSet<String>()
        if (!b) errors.add("§a    Craft configs loaded without errors.")
        val files = tempFile.listFiles()
        for (tmpFile in files) {
            if (tmpFile.isDirectory) {
                loadCraft(p, true, tmpFile)
            } else try {
                val config = Config(plugin, tmpFile, tmpFile.name)
                filePath = "..." + File.separator + tempFile.name + File.separator + config.fileName + File.separator
                for (id in config.config.getKeys(false)) {
                    path = "$id.name"
                    val name = config.config.getString(path, "unknown")!!

                    path = "$id.permission"
                    val mainPermName = config.config.getString(path, "unknown@TRUE")!!.split("@").toTypedArray()[0]
                    val mainPermDefault = PermissionDefault.valueOf(
                        config.config.getString(path, "unknown@TRUE")!!.split("@").toTypedArray()[1]
                    )
                    val mainPerm = Permission(mainPermName, mainPermDefault)

                    path = "$id.npcId"
                    val npcId = config.config.getInt(path, 0)

                    path = "$id.items"
                    val craftItems = ArrayList<CraftItem>()
                    for (item in config.config.getConfigurationSection(path)!!.getKeys(false)) {
                        val craftRecipes = ArrayList<CraftRecipe>()
                        val itemStack = plugin.itemManager.getItem(item)!!
                        path = "$id.items.$item.recipes"
                        for (data in config.config.getConfigurationSection(path)!!.getKeys(false)) {
                            path = "$id.items.$item.recipes.$data.count"
                            val count = config.config.getInt(path)
                            path = "$id.items.$item.recipes.$data.necI"
                            val neciData = config.config.getString(path)!!
                            val necI = ArrayList<ItemStack>()
                            for (temp in neciData.split(",").toTypedArray()) {
                                val itemId = temp.split("@").toTypedArray()[0]
                                val itemCount = if (temp.split("@").toTypedArray().size == 1) 1 else temp.split("@").toTypedArray()[1].toInt()
                                necI.add(plugin.itemManager.getItem(itemId)!!.init(itemCount, 0.0, true))
                            }

                            path = "$id.items.$item.recipes.$data.necT"
                            val nectData = config.config.getString(path)!!
                            val necT = ArrayList<ItemStack>()
                            if (nectData != "") {
                                for (temp in nectData.split(",").toTypedArray()) {
                                    val itemId = temp.split("@").toTypedArray()[0]
                                    val itemCount =
                                        if (temp.split("@").toTypedArray().size == 1) 1 else temp.split("@")
                                            .toTypedArray()[1].toInt()
                                    necT.add(plugin.itemManager.getItem(itemId)!!.init(itemCount, 0.0, true))
                                }
                            }

                            path = "$id.items.$item.recipes.$data.price"
                            val price = config.config.getInt(path)

                            path = "$id.items.$item.recipes.$data.perm"
                            val itemPermName = config.config.getString(path, "unknown@TRUE")!!.split("@").toTypedArray()[0]
                            val itemPermDefault = PermissionDefault.valueOf(config.config.getString(path, "unknown@TRUE")!!.split("@").toTypedArray()[1])
                            val itemPerm = Permission(itemPermName, itemPermDefault)
                            val recipe = CraftRecipe(itemStack, necI, necT, price.toLong(), count, itemPerm)

                            path = "$id.items.$item.recipes.$data.special.amount"
                            if (config.config.isSet(path)) {
                                val specialAmount = config.config.getInt(path, 1)

                                path = "$id.items.$item.recipes.$data.special.boards"
                                val specialBoard = config.config.getStringList(path).toTypedArray()

                                val specialBuffs = ArrayList<StatusGeneratorHandler>()
                                path = "$id.items.$item.recipes.$data.special.statuses"
                                config.config.getStringList(path).forEach { string ->
                                    val type = StatusType.valueOf(string.split(" ")[0])
                                    specialBuffs.add(StatusGeneratorType.get(StatusGeneratorType.valueOf(string.split(" ")[1]), string.split(" ")[2], type))
                                }
                                recipe.specialData = SpecialCraft.SpecialData(specialAmount, specialBuffs, specialBoard)
                            }

                            craftRecipes.add(recipe)
                        }
                        craftItems.add(CraftItem(craftRecipes, itemStack))
                    }
                    crafts[id] = Craft(id, name, craftItems, npcId, mainPerm)
                    npcIds[npcId] = id
                }
            } catch (e: Exception) {
                errors.remove("§a    Craft configs loaded without errors.")
                errors.add("§c    " + e.message + "§7" + "(Path: " + filePath + path + ")")
            }
        }
        showErrors(errors, p)
    }

    fun getCraft(id: String): Craft? = crafts[id]

    fun getCraftId(npc: Int): String? = npcIds[npc]

    fun getCraftIds(): Set<String> = crafts.keys

    private fun showErrors(errors: HashSet<String>, p: Player?) {
        if (p != null) {
            p.sendMessage(TanoRPG.PX + "§bLoading craft configs...")
            errors.stream().forEach { e: String? -> p.sendMessage(e!!) }
            p.sendMessage("  ")
        } else {
            Bukkit.getConsoleSender().sendMessage("[TanoRPG] §bLoading craft configs...")
            errors.stream().forEach { e: String? ->
                Bukkit.getConsoleSender().sendMessage(
                    e!!
                )
            }
            Bukkit.getConsoleSender().sendMessage("  ")
        }
    }
}