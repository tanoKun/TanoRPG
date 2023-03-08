package com.github.tanokun.tanorpg.player.quests.actions

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.player.Member
import com.github.tanokun.tanorpg.player.quests.utils.annotation.ActionData
import com.github.tanokun.tanorpg.util.addItem
import com.github.tanokun.tanorpg.util.regex.RegexMatcher
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

@ActionData("giveItem", "<String:id> <Int:amount>", "giveItem [id] [amount]", "playerにアイテムを与えます")
class QuestGiveItemAction(regexMatcher: RegexMatcher, parameters: String): QuestAction {
    var itemStack = ItemStack(Material.AIR)

    init {
        regexMatcher.matchResult(parameters) {
            TanoRPG.plugin.itemManager.getItem(it.get("id", ""))?.let { item ->
                itemStack = item.init(it.get("amount", 0), 0.0, false)
            }
        }
    }

    override fun run(member: Member) {
        Bukkit.getScheduler().runTask(TanoRPG.plugin, Runnable { addItem(member.player, itemStack) })
    }
}