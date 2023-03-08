package com.github.tanokun.tanorpg.player.skill.execute

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.TanoRPG.Companion.plugin
import com.github.tanokun.tanorpg.player.Member
import com.github.tanokun.tanorpg.player.quests.utils.getFile
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.jar.JarFile

class SkillManager {
    companion object {
        const val CT = "skill_cooltime"
    }
    val skills = HashMap<SkillCombo, Skill>()
    val skillNames = HashMap<String, SkillCombo?>()
    fun registerSkill() {
        val basePackage = "com/github/tanokun/tanorpg/player/skill/execute"
        val jar = JarFile(getFile())
        for (e in jar.entries()) {
            if (e.name.startsWith(basePackage) && e.name.endsWith(".class")) {
                val clazz = Class.forName(
                    e.name.replace("/", ".").substring(0, e.name.length - ".class".length),
                    true,
                    plugin.javaClass.classLoader
                )
                if (clazz.superclass != Skill::class.java) continue
                val skill = clazz.constructors[0].newInstance() as Skill
                skills[skill.combo] = skill
                skillNames[skill.name] = skill.combo
            }
        }
    }

    fun getSkill(skillComboTypes: SkillCombo): Skill? {
        return skills[skillComboTypes]
    }

    fun executeSkill(skill: Skill, player: Member) {
        val now = LocalDateTime.now()
        val byString = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
        if (!player.player.hasMetadata(CT + "_" + skill.name)) {
            player.player.setMetadata(CT + "_" + skill.name, FixedMetadataValue(plugin, "" + now.format(byString)))
            skill.execute(player, player.player)
            player.hasMP = player.hasMP - skill.mp
            return
        }
        val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        var date1: Date? = null
        var date2: Date? = null
        if (player.player.getMetadata(CT + "_" + skill.name).size == 0) player.player.setMetadata(
            CT + "_" + skill.name, FixedMetadataValue(
                plugin, "" + now.format(byString)
            )
        )
        try {
            date1 = format.parse(player.player.getMetadata(CT + "_" + skill.name)[0].asString())
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        try {
            date2 = format.parse(now.format(byString))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val difference = date2!!.time - date1!!.time
        val seconds = difference / 1000 % 60
        val ct = skill.ct.toLong()
        val show = ct - seconds
        if (seconds < ct) {
            player.player.sendMessage(TanoRPG.PX + "§cクールタイム中です §7(残り: " + show + "秒)")
            return
        }
        if (player.hasMP < skill.mp) {
            player.player.sendMessage(TanoRPG.PX + "§cマナが足りません")
            return
        }
        player.player.setMetadata(CT + "_" + skill.name, FixedMetadataValue(plugin, "" + now.format(byString)))
        skill.execute(player, player.player)
        player.hasMP = player.hasMP - skill.mp
    }

    fun getSkillItem(name: String): ItemStack {
        val skill = skills[skillNames[name]]!!
        val lore: MutableList<String?> = ArrayList()
        lore.add("§e〇=-=-=-=-=§b説明§e=-=-=-=-=-〇")
        for (setumei in skill.lore) {
            lore.add(setumei)
        }
        lore.add("§e〇=-=-=-=-=-=-=-=-=-=-=-=-=-〇")
        lore.add("§7対応職業: §b" + skill.job)
        lore.add("§7必要レベル: §b" + skill.lvl)
        lore.add("§7必要MP: §b" + skill.mp)
        lore.add("§7必要コンボ: §b" + skill.combo.combos)
        lore.add("§7クールタイム: §b" + skill.ct + "秒")
        val item = ItemStack(Material.PAPER)
        val meta = item.itemMeta
        meta.setDisplayName("§bスキル習得書: §6$name")
        meta.lore = lore
        meta.addEnchant(Enchantment.MENDING, 1, true)
        meta.addItemFlags(
            ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS,
            ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE
        )
        item.itemMeta = meta
        return item
    }

    fun getSkillName(name: String?): SkillCombo? {
        return skillNames[name]
    }
}