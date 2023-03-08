package com.github.tanokun.tanorpg.game.shop

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.TanoRPG.Companion.plugin
import com.github.tanokun.tanorpg.util.command.CommandContext
import com.github.tanokun.tanorpg.util.io.Config
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault
import java.io.File

class ShopManager(p: Player?) {
    private val shops = HashMap<String, Shop>()
    private val npcIds = HashMap<Int, String>()

    init {
        loadShop(p, false, Config(plugin, "shops").file)
    }

    private fun loadShop(p: Player?, b: Boolean, tempFile: File) {
        var path = ""
        var filePath = ""
        val errors = HashSet<String>()
        if (!b) errors.add("§a    Shop configs loaded without errors.")
        val files = tempFile.listFiles()
        for (tmpFile in files) {
            if (tmpFile.isDirectory) {
                loadShop(p, true, tmpFile)
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
                    val shopItems = ArrayList<ShopItem>()
                    val cc = CommandContext(arrayOf(""))
                    for (data in config.config.getStringList(path)) {
                        cc.init(null, data.split(" ").toTypedArray())
                        val product = plugin.itemManager.getItem(cc.getArg(0, "unknown"))!!
                            .init(1, 0.0, true)
                        val price = cc.getInteger(1, 0)
                        val itemPermName = cc.getString(2, "unknown@TRUE").split("@").toTypedArray()[0]
                        val itemPermDefault =
                            PermissionDefault.valueOf(cc.getArg(2, "unknown@TRUE").split("@").toTypedArray()[1])
                        val itemPerm = Permission(itemPermName, itemPermDefault)
                        shopItems.add(ShopItem(product, price, itemPerm))
                    }
                    shops[id] = Shop(id, name, shopItems, npcId, mainPerm)
                    npcIds[npcId] = id
                }
            } catch (e: Exception) {
                errors.remove("§a    Shop configs loaded without errors.")
                errors.add("§c    " + e.message + "§7" + "(Path: " + filePath + path + ")")
            }
        }
        showErrors(errors, p)
    }

    fun getShop(id: String): Shop? = shops[id]

    fun getShopId(npc: Int): String? = npcIds[npc]

    fun getShopIds(): Set<String> = shops.keys

    private fun showErrors(errors: HashSet<String>, p: Player?) {
        if (p != null) {
            p.sendMessage(TanoRPG.PX + "§bLoading shop configs...")
            errors.stream().forEach { e: String -> p.sendMessage(e) }
            p.sendMessage("  ")
        } else {
            Bukkit.getConsoleSender().sendMessage("[TanoRPG] §bLoading shop configs...")
            errors.stream().forEach { e: String ->
                Bukkit.getConsoleSender().sendMessage(e)
            }
            Bukkit.getConsoleSender().sendMessage("  ")
        }
    }
}