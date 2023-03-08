package com.github.tanokun.tanorpg.player

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.event.tanorpg.TanoRpgLevelUpEvent
import com.github.tanokun.tanorpg.event.tanorpg.TanoRpgPlayerGetItemEvent
import com.github.tanokun.tanorpg.game.entity.boss.BossEntity
import com.github.tanokun.tanorpg.player.menu.main.backpack.BackpackMenu
import com.github.tanokun.tanorpg.player.quests.QuestMap
import com.github.tanokun.tanorpg.player.skill.SkillClass
import com.github.tanokun.tanorpg.player.skill.SkillMap
import com.github.tanokun.tanorpg.player.status.PlayerStatusMap
import com.github.tanokun.tanorpg.player.status.StatusType
import com.github.tanokun.tanorpg.player.status.buff.BuffMap
import com.github.tanokun.tanorpg.player.warppoint.WarpPointMap
import com.github.tanokun.tanorpg.util.io.Config
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import java.io.File
import java.util.*
import kotlin.math.roundToInt
import kotlin.math.roundToLong

data class Member(val uuid: UUID, val skillClass: SkillClass, val statusMap: PlayerStatusMap, val equip: EquipmentMap, val skillMap: SkillMap,
                  val warpPointMap: WarpPointMap, val questMap: QuestMap, val buffMap: BuffMap, val attack: Attack,
                  val backpackMenu: BackpackMenu, val chestMap: ChestMap, var hasHP: Int = statusMap.getPointAndStatus(StatusType.HP).roundToInt(),
                  var hasMP: Int = statusMap.getPointAndStatus(StatusType.MP).roundToInt(), var hasEXP: Long = 0,
                  var level: MemberLevelType = MemberLevelType.Lv_1, val runnable: MemberRunnable, var bossEntity: BossEntity? = null){

    val player = Bukkit.getPlayer(uuid)!!

    fun setMoney(money: Long) {
        TanoRPG.plugin.econ.depositPlayer(Bukkit.getPlayer(uuid), (getMoney() - money).toDouble())
        TanoRPG.plugin.sidebarManager.updateSidebar(Bukkit.getPlayer(uuid), this)
    }

    fun removeMoney(money: Long) {
        TanoRPG.plugin.econ.withdrawPlayer(Bukkit.getPlayer(uuid), money.toDouble())
        TanoRPG.plugin.sidebarManager.updateSidebar(Bukkit.getPlayer(uuid), this)
    }

    fun addMoney(money: Long) {
        TanoRPG.plugin.econ.depositPlayer(Bukkit.getPlayer(uuid), money.toDouble())
        TanoRPG.plugin.sidebarManager.updateSidebar(Bukkit.getPlayer(uuid), this)
    }

    fun getMoney(): Long {
        return TanoRPG.plugin.econ.getBalance(Bukkit.getOfflinePlayer(uuid)).roundToLong()
    }

    fun online() {
        runnable.setMember(this)
        runnable.runTaskTimer(TanoRPG.plugin, 0, 20)
        player.setPlayerListName("[無所属] " + "§b§l" + player.name + "<Lv. " + level.value + ">")
    }

    fun offline() {
        runnable.cancel()
    }

    fun addItem(item: ItemStack) {
        player.inventory.addItem(item)
        Bukkit.getPluginManager().callEvent(TanoRpgPlayerGetItemEvent(player, item))
    }

    fun setHP(hasHP: Int) {
        this.hasHP = hasHP
        if (this.hasHP >= statusMap.getStatus(StatusType.HP)) this.hasHP = statusMap.getStatus(StatusType.HP).roundToInt()
        TanoRPG.plugin.sidebarManager.updateSidebar(Bukkit.getPlayer(uuid), this)
    }

    fun setMP(hasMP: Int) {
        this.hasMP = hasMP
        if (this.hasMP >= statusMap.getStatus(StatusType.MP)) this.hasMP = statusMap.getStatus(StatusType.MP).roundToInt()
        TanoRPG.plugin.sidebarManager.updateSidebar(Bukkit.getPlayer(uuid), this)
    }

    fun addHasEXP(hasEXP: Long) {
        this.hasEXP += hasEXP
        if (!level.hasNext()) {
            this.hasEXP = 0
            return
        }
        var i = 0
        while (this.hasEXP >= level.maxEXP) {
            if (!level.hasNext()) {
                this.hasEXP = 0
                TanoRPG.plugin.sidebarManager.updateSidebar(Bukkit.getPlayer(uuid), this)
                return
            }
            this.hasEXP = this.hasEXP - level.maxEXP
            level = level.next
            player.sendMessage(TanoRPG.PX + "§aレベルが §b" + level + "Lv §aになりました！")
            statusMap.statusPoint = statusMap.statusPoint + 2
            Bukkit.getPluginManager().callEvent(TanoRpgLevelUpEvent(player, this))
            i++
        }
        TanoRPG.plugin.sidebarManager.updateSidebar(Bukkit.getPlayer(uuid), this)
        player.setPlayerListName("[無所属] " + "§b§l" + player.name + "<Lv. " + level.value + ">")
    }

    fun saveData() {
        val data = Config(TanoRPG.plugin, "player_database" + File.separator + uuid.toString() + ".yml")
        val c = data.config
        data.createConfig()
        c["skillClass"] = skillClass.name
        statusMap.save(data, "")
        equip.save(data, "")
        questMap.save(data, "")
        skillMap.save(data, "")
        buffMap.save(data, "")
        backpackMenu.save(data, "")
        chestMap.save(data, "")
        c["hasHP"] = hasHP
        c["hasMP"] = hasMP
        c["hasEXP"] = hasEXP
        c["hasLevel"] = level.name
        data.saveConfig()
    }
}
