package com.github.tanokun.tanorpg.player.quests.menus

import com.github.tanokun.tanorpg.TanoRPG.Companion.plugin
import com.github.tanokun.tanorpg.player.Member
import com.github.tanokun.tanorpg.util.createItem
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player


class QuestListMenu(val member: Member, private val npc: Int): InventoryProvider {
    fun getInv(): SmartInventory {
        return SmartInventory.builder()
            .provider(this)
            .size(1, 9)
            .title("§8§l》§dクエスト")
            .id("QuestListMenu")
            .update(false)
            .build()
    }


    override fun init(player: Player, contents: InventoryContents){
        plugin.questManager.getQuests(npc)?.let { quests ->
            quest@ for (quest in quests) {
                if (member.questMap.clearQuests.containsKey(quest.id)) continue
                for (condition in quest.conditions) if (!condition.apply(player)) continue@quest

                val lore: MutableList<String> = ArrayList()
                lore.add(" ")
                quest.lore.forEach(lore::add)
                lore.add(" ")
                lore.add("§7難易度: " + quest.difficulty.getName())
                lore.add(" ")
                lore.add("§b━━報酬━━━━━━━━━")
                quest.result.forEach(lore::add)

                contents.add(ClickableItem.of(createItem(Material.WRITTEN_BOOK, "§d" + quest.name, lore, 1, false)) {
                    member.questMap.isAction = true
                    contents.inventory().close(player)
                    Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
                        quest.showQuestActions.forEach { a ->
                            if (!player.isOnline) {
                                member.questMap.isAction = false
                                return@forEach
                            }
                            a.run(member)
                        }
                        Bukkit.getScheduler().runTask(plugin, Runnable { QuestCheckMenu(quest, member).getInv().open(player) })
                    })
                })
            }

            if (contents.all().isEmpty()) contents.inventory().close(player)
        }
    }
}