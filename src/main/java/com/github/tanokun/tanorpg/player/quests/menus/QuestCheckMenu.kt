package com.github.tanokun.tanorpg.player.quests.menus

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.TanoRPG.Companion.plugin
import com.github.tanokun.tanorpg.player.Member
import com.github.tanokun.tanorpg.player.quests.QuestManager
import com.github.tanokun.tanorpg.player.quests.QuestModel
import com.github.tanokun.tanorpg.util.createItem
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player


class QuestCheckMenu(val quest: QuestModel, val member: Member) : InventoryProvider {
    fun getInv(): SmartInventory {
        return SmartInventory.builder()
            .closeable(false)
            .cancelable(true)
            .provider(this)
            .size(1, 9)
            .title("§8§l》§dクエスト確認")
            .id("questCheckMenu")
            .update(false)
            .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        contents[0, 2] = ClickableItem.of(createItem(Material.GREEN_WOOL, "§a§l受注する", 1, false)) { e ->
            member.questMap.isAction = true
            contents.inventory().close(player)
            Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
                quest.questIndex[0].actions.forEach { a ->
                    if (!player.isOnline) { member.questMap.isAction = false; return@forEach }
                    a.run(member)
                }

                member.questMap.isAction = false
                Bukkit.getScheduler().runTask(plugin, Runnable {
                    player.sendMessage(QuestManager.PX + "§aクエストを受注しました。")
                    TanoRPG.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 2.0)
                    val questData = quest.getNewData()
                    member.questMap.quests[quest.id] = questData
                    member.questMap.activeQuest = quest.id
                    plugin.sidebarManager.updateSidebar(player, member)
                })
            })
        }

        contents[0, 6] = ClickableItem.of(createItem(Material.RED_WOOL, "§a§l受注しない", 1, false)) {
            member.questMap.isAction = true
            contents.inventory().close(player)
            Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
                quest.cancelQuestActions.forEach { a ->
                    if (!player.isOnline) {
                        member.questMap.isAction = false
                        return@forEach
                    }
                    a.run(member)
                }
                player.sendMessage(QuestManager.PX + "§aクエストを取り消しました。")
                member.questMap.isAction = false
            })
        }
    }
}
