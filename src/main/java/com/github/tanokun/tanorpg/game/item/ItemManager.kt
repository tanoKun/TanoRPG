package com.github.tanokun.tanorpg.game.item

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.TanoRPG.Companion.plugin
import com.github.tanokun.tanorpg.game.item.drop.then
import com.github.tanokun.tanorpg.game.item.status.ItemStatus
import com.github.tanokun.tanorpg.game.item.status.generate.IntLiteralGenerator
import com.github.tanokun.tanorpg.game.item.status.generate.StatusGeneratorHandler
import com.github.tanokun.tanorpg.game.item.status.generate.StatusGeneratorType
import com.github.tanokun.tanorpg.game.item.type.*
import com.github.tanokun.tanorpg.game.item.type.base.ItemBase
import com.github.tanokun.tanorpg.player.EquipmentMap.EquipmentType
import com.github.tanokun.tanorpg.player.quests.actions.QuestGiveBuffAction
import com.github.tanokun.tanorpg.player.skill.SkillClass
import com.github.tanokun.tanorpg.player.status.KindOfStatusType
import com.github.tanokun.tanorpg.player.status.StatusMap
import com.github.tanokun.tanorpg.player.status.StatusType
import com.github.tanokun.tanorpg.util.command.CommandContext
import com.github.tanokun.tanorpg.util.getPersistent
import com.github.tanokun.tanorpg.util.io.Config
import com.github.tanokun.tanorpg.util.io.Folder
import com.github.tanokun.tanorpg.util.io.MapNode
import com.github.tanokun.tanorpg.util.regex.RegexMatcher
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.io.File
import kotlin.math.floor
import kotlin.math.roundToInt

class ItemManager(p: Player?) {
    private val items = HashMap<String, ItemBase?>()
    val itemIDs = ArrayList<String>()

    init {

        if (p != null) p.sendMessage(TanoRPG.PX + "§bLoading item configs...") else Bukkit.getConsoleSender()
            .sendMessage("[TanoRPG] §bLoading item configs...")
        loadMaterialItem(p, false, Config(plugin, "items" + File.separator + "material").file)
        loadWeaponItem(p, false, Config(plugin, "items" + File.separator + "weapon").file)
        loadMagicWeaponItem(p, false, Config(plugin, "items" + File.separator + "magicWeapon").file)
        loadProjectileWeaponItem(p, false, Config(plugin, "items" + File.separator + "projectileWeapon").file)
        loadEquipmentItem(p, false, Config(plugin, "items" + File.separator + "equip").file)
        loadRuneItem(p, false, Config(plugin, "items" + File.separator + "rune").file)
        loadAccessoryItem(p, false, Config(plugin, "items" + File.separator + "accessory").file)
        loadPotionItem(p, false, Config(plugin, "items" + File.separator + "potion").file)
        if (p != null) p.sendMessage(TanoRPG.PX + " ") else Bukkit.getConsoleSender().sendMessage(" ")
    }

    private fun loadMaterialItem(p: Player?, b: Boolean, tempFile: File) {
        var path = ""
        var filePath = ""
        val errors = HashSet<String>()
        if (!b) errors.add("§a    Material item configs loaded without errors.")
        val files = tempFile.listFiles()
        for (tmpFile in files) {
            if (tmpFile.isDirectory) {
                loadMaterialItem(p, true, tmpFile)
            } else try {
                val config = Config(plugin, tmpFile, tmpFile.name)
                filePath = "..." + File.separator + tempFile.name + File.separator + config.fileName + File.separator
                for (id in config.config.getKeys(false)) {
                    path = "$id.all"
                    val base = Base.create(config, id)

                    path = "$id.special"
                    val array = ArrayList<MapNode<Int, Int>>()
                    val regexMatcher = RegexMatcher("<Int:r> <Int:c>")
                    config.config.getStringList(path).forEach {
                        regexMatcher.matchResult(it) { data ->
                            println("${data.get("r", 0)}, ${data.get("c", 0)}")
                            array.add(MapNode(data.get("r", 0), data.get("c", 0)))
                        }
                    }

                    val item = ItemMaterial(id, base.material, base.name, base.lore, base.statusMap, base.glowing, base.rarity, array)
                    item.price = base.price
                    item.customModelData = base.customModelData
                    items[id] = item
                    itemIDs.add(id)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                errors.remove("§a    Material item configs loaded without errors.")
                errors.add("§c    " + e.message + "§7" + "(Path: " + filePath + path + ")")
            }
        }
        showErrors(errors, p)
    }

    private fun loadWeaponItem(p: Player?, b: Boolean, tempFile: File) {
        var path = ""
        var filePath = ""
        val errors = HashSet<String>()
        if (!b) errors.add("§a    Weapon item configs loaded without errors.")
        val files = tempFile.listFiles()
        for (tmpFile in files) {
            if (tmpFile.isDirectory) {
                loadWeaponItem(p, true, tmpFile)
            } else try {
                val config = Config(plugin, tmpFile, tmpFile.name)
                filePath = "..." + File.separator + tempFile.name + File.separator + config.fileName + File.separator
                for (id in config.config.getKeys(false)) {
                    path = "$id.all"
                    val base = Base.create(config, id)

                    path = "$id.lvl"
                    val lvl = config.config.getInt(path, 0)
                    path = "$id.ct"
                    val ct = config.config.getInt(path, 0)
                    path = "$id.maxDurabilityValue"
                    val maxDurabilityValue = config.config.getInt(path, 4500)
                    path = "$id.reach"
                    val reach = config.config.getInt(path, 2)
                    path = "$id.proper"
                    val proper: MutableList<SkillClass> = ArrayList()
                    config.config.getList(path)!!
                        .stream().forEach { job: Any? -> proper.add(SkillClass.valueOf((job as String?)!!)) }
                    path = "$id.combo"
                    val combos: MutableList<Int> = ArrayList()
                    config.config.getList(path)!!.stream()
                        .forEach { i: Any? -> combos.add(Integer.valueOf(i.toString())) }

                    val item = ItemWeapon(id, base.material, base.name, base.lore, base.statusMap, base.glowing, base.rarity)
                    item.price = base.price
                    item.customModelData = base.customModelData
                    item.coolTime = ct
                    item.necLevel = lvl
                    item.proper = proper
                    item.combo = combos
                    item.reach = reach
                    item.maxDurabilityValue = maxDurabilityValue
                    items[id] = item
                    itemIDs.add(id)
                }
            } catch (e: Exception) {
                errors.remove("§a    Weapon item configs loaded without errors.")
                errors.add("§c    " + e.message + "§7" + "(Path: " + filePath + path + ")")
            }
        }
        showErrors(errors, p)
    }

    private fun loadMagicWeaponItem(p: Player?, b: Boolean, tempFile: File) {
        var path = ""
        var filePath = ""
        val errors = HashSet<String>()
        if (!b) errors.add("§a    MagicWeapon item configs loaded without errors.")
        val files = tempFile.listFiles()
        for (tmpFile in files) {
            if (tmpFile.isDirectory) {
                loadMagicWeaponItem(p, true, tmpFile)
            } else try {
                val config = Config(plugin, tmpFile, tmpFile.name)
                filePath = "..." + File.separator + tempFile.name + File.separator + config.fileName + File.separator
                for (id in config.config.getKeys(false)) {
                    path = "$id.all"
                    val base = Base.create(config, id)

                    path = "$id.lvl"
                    val lvl = config.config.getInt(path, 0)
                    path = "$id.ct"
                    val ct = config.config.getInt(path, 0)
                    path = "$id.maxDurabilityValue"
                    val maxDurabilityValue = config.config.getInt(path, 4500)
                    path = "$id.proper"
                    val proper: MutableList<SkillClass> = ArrayList()
                    config.config.getList(path)!!
                        .stream().forEach { job: Any? -> proper.add(SkillClass.valueOf((job as String?)!!)) }
                    path = "$id.combo"
                    val combos: MutableList<Int> = ArrayList()
                    config.config.getList(path)!!.stream()
                        .forEach { i: Any? -> combos.add(Integer.valueOf(i.toString())) }
                    path = "$id.reach"
                    val reach = config.config.getInt(path, 4)
                    var color = Color.fromRGB(255, 255, 255)
                    path = "$id.color.*"
                    if (config.config["$id.color.RED"] != null && config.config["$id.color.GREEN"] != null && config.config["$id.color.BLUE"] != null) {
                        color = Color.fromRGB(
                            config.config.getInt("$id.color.RED"),
                            config.config.getInt("$id.color.GREEN"),
                            config.config.getInt("$id.color.BLUE")
                        )
                    }
                    val item = ItemMagicWeapon(id, base.material, base.name, base.lore, base.statusMap, base.glowing, base.rarity)
                    item.price = base.price
                    item.customModelData = base.customModelData
                    item.coolTime = ct
                    item.necLevel = lvl
                    item.proper = proper
                    item.combo = combos
                    item.color = color
                    item.reach = reach
                    item.maxDurabilityValue = maxDurabilityValue
                    items[id] = item
                    itemIDs.add(id)
                }
            } catch (e: Exception) {
                errors.remove("§a    MagicWeapon item configs loaded without errors.")
                errors.add("§c    " + e.message + "§7" + "(Path: " + filePath + path + ")")
            }
        }
        showErrors(errors, p)
    }

    private fun loadProjectileWeaponItem(p: Player?, b: Boolean, tempFile: File) {
        var path = ""
        var filePath = ""
        val errors = HashSet<String>()
        if (!b) errors.add("§a    ProjectileWeapon item configs loaded without errors.")
        val files = tempFile.listFiles()
        for (tmpFile in files) {
            if (tmpFile.isDirectory) {
                loadProjectileWeaponItem(p, true, tmpFile)
            } else try {
                val config = Config(plugin, tmpFile, tmpFile.name)
                filePath = "..." + File.separator + tempFile.name + File.separator + config.fileName + File.separator
                for (id in config.config.getKeys(false)) {
                    path = "$id.all"
                    val base = Base.create(config, id)

                    path = "$id.lvl"
                    val lvl = config.config.getInt(path, 0)
                    path = "$id.ct"
                    val ct = config.config.getInt(path, 0)
                    path = "$id.proper"
                    val proper: MutableList<SkillClass> = ArrayList()
                    config.config.getList(path)!!
                        .stream().forEach { job: Any? -> proper.add(SkillClass.valueOf((job as String?)!!)) }
                    path = "$id.maxDurabilityValue"
                    val maxDurabilityValue = config.config.getInt(path, 4500)
                    path = "$id.combo"
                    val combos: MutableList<Int> = ArrayList()
                    config.config.getList(path)!!.stream()
                        .forEach { i: Any? -> combos.add(Integer.valueOf(i.toString())) }
                    val item = ItemProjectile(id, base.material, base.name, base.lore, base.statusMap, base.glowing, base.rarity)
                    item.price = base.price
                    item.customModelData = base.customModelData
                    item.coolTime = ct
                    item.necLevel = lvl
                    item.proper = proper
                    item.combo = combos
                    item.maxDurabilityValue = maxDurabilityValue
                    items[id] = item
                    itemIDs.add(id)
                }
            } catch (e: Exception) {
                errors.remove("§a    ProjectileWeapon item configs loaded without errors.")
                errors.add("§c    " + e.message + "§7" + "(Path: " + filePath + path + ")")
            }
        }
        showErrors(errors, p)
    }

    private fun loadEquipmentItem(p: Player?, b: Boolean, tempFile: File) {
        var path = ""
        var filePath = ""
        val errors = HashSet<String>()
        if (!b) errors.add("§a    Equipment item configs loaded without errors.")
        val files = tempFile.listFiles()
        for (tmpFile in files) {
            if (tmpFile.isDirectory) {
                loadEquipmentItem(p, true, tmpFile)
            } else try {
                val config = Config(plugin, tmpFile, tmpFile.name)
                filePath = "..." + File.separator + tempFile.name + File.separator + config.fileName + File.separator
                for (id in config.config.getKeys(false)) {
                    path = "$id.all"
                    val base = Base.create(config, id)

                    val lvl = config.config.getInt(path, 0)
                    path = "$id.ct"
                    val ct = config.config.getInt(path, 0)
                    path = "$id.proper"
                    val proper: MutableList<SkillClass> = ArrayList()
                    config.config.getList(path)!!
                        .stream().forEach { job: Any? -> proper.add(SkillClass.valueOf((job as String?)!!)) }
                    var equipmentType: EquipmentType? = null
                    path = "$id.equipType"
                    if (config.config.isSet("$id.equipType")) {
                        equipmentType = EquipmentType.valueOf(config.config.getString(path)!!)
                    } else {
                        EquipmentType.valueOf(config.config.getString("$id.material")!!.split("_").toTypedArray()[1])
                    }
                    var color: Color? = null
                    path = "$id.color.*"
                    if (base.material.toString()
                            .contains("LEATHER") && config.config["$id.color.RED"] != null && config.config["$id.color.GREEN"] != null && config.config["$id.color.BLUE"] != null
                    ) {
                        color = Color.fromRGB(
                            config.config.getInt("$id.color.RED"),
                            config.config.getInt("$id.color.GREEN"),
                            config.config.getInt("$id.color.BLUE")
                        )
                    }
                    val item = ItemEquipment(id, base.material, base.name, base.lore, base.statusMap, base.glowing, base.rarity)
                    item.price = base.price
                    item.customModelData = base.customModelData
                    item.color = color
                    item.necLevel = lvl
                    item.proper = proper
                    item.equipmentType = equipmentType
                    items[id] = item
                    itemIDs.add(id)
                }
            } catch (e: Exception) {
                errors.remove("§a    Equipment item configs loaded without errors.")
                errors.add("§c    " + e.message + "§7" + "(Path: " + filePath + path + ")")
            }
        }
        showErrors(errors, p)
    }

    private fun loadRuneItem(p: Player?, b: Boolean, tempFile: File) {
        var path = ""
        var filePath = ""
        val errors = HashSet<String>()
        if (!b) errors.add("§a    Rune item configs loaded without errors.")
        val files = tempFile.listFiles()!!
        for (tmpFile in files) {
            if (tmpFile.isDirectory) {
                loadMaterialItem(p, true, tmpFile)
            } else try {
                val config = Config(plugin, tmpFile, tmpFile.name)
                filePath = "..." + File.separator + tempFile.name + File.separator + config.fileName + File.separator
                for (id in config.config.getKeys(false)) {
                    path = "$id.all"
                    val base = Base.create(config, id)

                    val item = ItemRune(id, base.material, base.name, base.lore, base.statusMap, base.glowing, base.rarity)
                    item.price = base.price
                    item.customModelData = base.customModelData
                    items[id] = item
                    itemIDs.add(id)
                }
            } catch (e: Exception) {
                errors.remove("§a    Rune item configs loaded without errors.")
                errors.add("§c    " + e.message + "§7" + "(Path: " + filePath + path + ")")
            }
        }
        showErrors(errors, p)
    }

    private fun loadAccessoryItem(p: Player?, b: Boolean, tempFile: File) {
        var path = ""
        var filePath = ""
        val errors = HashSet<String>()
        if (!b) errors.add("§a    Accessory item configs loaded without errors.")
        val files = tempFile.listFiles()
        for (tmpFile in files) {
            if (tmpFile.isDirectory) {
                loadMaterialItem(p, true, tmpFile)
            } else try {
                val config = Config(plugin, tmpFile, tmpFile.name)
                filePath = "..." + File.separator + tempFile.name + File.separator + config.fileName + File.separator
                for (id in config.config.getKeys(false)) {
                    path = "$id.all"
                    val base = Base.create(config, id)

                    path = "$id.lvl"
                    val lvl = config.config.getInt(path, 0)

                    path = "$id.proper"
                    val proper: MutableList<SkillClass> = ArrayList()
                    config.config.getList(path)!!
                        .stream().forEach { job: Any? -> proper.add(SkillClass.valueOf((job as String?)!!)) }

                    val item = ItemAccessory(id, base.material, base.name, base.lore, base.statusMap, base.glowing, base.rarity)
                    item.price = base.price
                    item.necLevel = lvl
                    item.proper = proper
                    item.customModelData = base.customModelData
                    items[id] = item
                    itemIDs.add(id)
                }
            } catch (e: Exception) {
                errors.remove("§a    Accessory item configs loaded without errors.")
                errors.add("§c    " + e.message + "§7" + "(Path: " + filePath + path + ")")
            }
        }
        showErrors(errors, p)
    }

    private fun loadPotionItem(p: Player?, b: Boolean, tempFile: File?) {
        var path = ""
        var filePath = ""
        val errors = HashSet<String>()
        errors.add("§a    Potion item configs loaded without errors.")
        try {
            path = "items" + File.separator + "potion"
            for (config in Folder(plugin, path).getFiles()) {
                filePath = path + File.separator + config.fileName + File.separator
                for (id in config.config.getKeys(false)) {
                    path = "$id.all"
                    val base = Base.create(config, id)

                    path = "$id.ct"
                    val ct = config.config.getInt(path, 0)
                    path = "$id.buff"
                    val cc = CommandContext(null, arrayOf(""))
                    val usingActions: MutableList<QuestGiveBuffAction> = ArrayList()
                    for (buff in config.config.getStringList(path)) {
                        cc.init(null, buff.split(" ").toTypedArray())
                        //usingActions.add(QuestGiveBuffAction(cc))
                    }

                    val item = ItemPotion(id, base.material, base.name, base.lore, base.statusMap, base.glowing, base.rarity, usingActions)
                    item.price = base.price
                    item.customModelData = base.customModelData
                    item.coolTime = ct
                    items[id] = item
                    itemIDs.add(id)
                }
            }
        } catch (e: Exception) {
            errors.remove("§a    Potion item configs loaded without errors.")
            errors.add("§c    " + e.message + "§7" + "(Path: " + filePath + path + ")")
        }
        showErrors(errors, p)
    }

    fun getItem(id: String): ItemBase? {
        return items[id]
    }

    fun isExists(id: String): Boolean {
        return items[id] != null
    }

    fun getID(item: ItemStack?): String {
        return if (getPersistent(item!!, "id", PersistentDataType.STRING) == null) "" else getPersistent(
            item, "id", PersistentDataType.STRING
        )!!
    }

    fun getItem(item: ItemStack?): ItemBase? {
        if (item!!.type != Material.AIR && item.itemMeta.lore != null) {
            val id = item.itemMeta.lore!![item.itemMeta.lore!!.size - 1].split(" ").toTypedArray()
            return getItem(id[1])
        }
        return null
    }

    fun fromNormalStatus(statusMap: StatusMap): List<String> {
        val statuses: MutableList<String> = ArrayList()
        for (status in statusMap.hasStatuses.keys) {
            if (status == StatusType.NONE) continue
            val value = ((status.end == "%") then floor(statusMap.hasStatuses[status]!! * 100) / 100) ?: statusMap.hasStatuses[status]!!.roundToInt()
            if (statusMap.hasStatuses[status]!! > 0) {
                statuses.add(KindOfStatusType.NORMAL.toString() + "§a" + status.getName() + " +" + value + status.end)
            } else {
                statuses.add(KindOfStatusType.NORMAL.toString() + "§c" + status.getName() + " " + value + status.end)
            }
        }
        return statuses
    }

    fun fromEvolutionStatus(statusMap: StatusMap): List<String> {
        val statuses: MutableList<String> = ArrayList()
        for (status in statusMap.hasStatuses.keys) {
            if (status == StatusType.NONE) continue
            val value = ((status.end == "%") then floor(statusMap.hasStatuses[status]!! * 100) / 100) ?: statusMap.hasStatuses[status]!!.roundToInt()
            if (statusMap.hasStatuses[status]!! > 0) {
                statuses.add(KindOfStatusType.EVOLUTION.toString() + "§a" + status.getName() + " +" + value + status.end)
            } else {
                statuses.add(KindOfStatusType.EVOLUTION.toString() + "§c" + status.getName() + " " + value + status.end)
            }
        }
        return statuses
    }

    fun fromRuneStatus(statusMap: StatusMap): List<String> {
        val statuses: MutableList<String> = ArrayList()
        for (status in statusMap.hasStatuses.keys) {
            if (status == StatusType.NONE) continue
            val value = ((status.end == "%") then floor(statusMap.hasStatuses[status]!! * 100) / 100) ?: statusMap.hasStatuses[status]!!.roundToInt()
            if (statusMap.hasStatuses[status]!! > 0) {
                statuses.add(KindOfStatusType.RUNE.toString() + "§a" + status.getName() + " +" + value + status.end)
            } else {
                statuses.add(KindOfStatusType.RUNE.toString() + "§c" + status.getName() + " " + value + status.end)
            }
        }
        return statuses
    }

    private fun showErrors(errors: HashSet<String>, p: Player?) {
        if (p != null) errors.stream().forEach { e: String -> p.sendMessage(e)
        } else errors.stream().forEach { e: String -> Bukkit.getConsoleSender().sendMessage(e) }
    }

    companion object {
        const val LORE = "§e〇=-=-=-=-=§b説明§e=-=-=-=-=-〇"
        const val FIRST_STATUS = "§e〇=-=-=-§bステータス§e-=-=-=-〇"
        const val FINAL_STATUS = "§e〇=-=-=-=-=-=-=-=-=-=-=-〇"
    }
}

private data class Base(
    val name: String,
    val material: Material,
    val lore: List<String>,
    val statusMap: ItemStatus,
    val glowing: Boolean,
    val price: Long,
    val rarity: ItemRarityType,
    val customModelData: Int){

   companion object {
       @JvmStatic
       fun create(config: Config, id: String): Base {
           var path

           = "$id.name"
           val name = config.config.getString(path, "unknown")!!

           path = "$id.material"
           var material = Material.valueOf(config.config.getString(path, "BARRIER")!!)

           path = "$id.lore"
           val lore = config.config.getStringList(path)

           path = "$id.status"
           val data = ArrayList<StatusGeneratorHandler>()
           config.config.getConfigurationSection(path)!!.getKeys(false).forEach { text: String ->
               if (config.config.getInt("$id.status.$text", Int.MAX_VALUE) != Int.MAX_VALUE) {
                   data.add(IntLiteralGenerator("${config.config.getInt("$id.status.$text", Int.MAX_VALUE)}", StatusType.valueOf(text)))
               } else {
                   val commandContext = CommandContext(null, config.config.getString("$id.status.$text", "")!!.split(" ").toTypedArray())
                   commandContext.valueFlags.keys.forEach { data.add(StatusGeneratorType.get(StatusGeneratorType.valueOf(it), commandContext.getFlag(it), StatusType.valueOf(text))) }
               }
           }
           val itemStatus = ItemStatus(data)

           path = "$id.glowing"
           val glowing = config.config.getBoolean(path, false)

           path = "$id.price"
           val price = config.config.getLong(path, 0)

           path = "$id.rarity"
           val rarity = ItemRarityType.valueOf(config.config.getString(path, "COMMON")!!)

           path = "$id.customModelData"
           val customModelData = config.config.getInt(path, 0)

           return Base(name, material, lore, itemStatus, glowing, price, rarity, customModelData)
       }
   }
}