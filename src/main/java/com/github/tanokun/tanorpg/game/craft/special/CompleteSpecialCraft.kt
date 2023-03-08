package com.github.tanokun.tanorpg.game.craft.special

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.event.tanorpg.TanoRpgCraftEvent
import com.github.tanokun.tanorpg.game.item.drop.then
import com.github.tanokun.tanorpg.player.status.StatusType
import com.github.tanokun.tanorpg.util.addItem
import com.github.tanokun.tanorpg.util.createItem
import com.github.tanokun.tanorpg.util.io.MapNode
import com.github.tanokun.tanorpg.util.item.ItemBuilder
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem
import com.github.tanokun.tanorpg.util.smart_inv.inv.InventoryListener
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitRunnable

class CompleteSpecialCraft(private val specialCraft: SpecialCraft): InventoryProvider {

    val inv: SmartInventory = SmartInventory.builder()
        .id("create")
        .title(((specialCraft.amount == 0) then "§9§lアイテム作成") ?: "§c§lアイテム作成 (まだ行動回数が残っています)")
        .update(false)
        .provider(this)
        .closeable(false)
        .size(3, 9)
        .build()

    override fun init(player: Player, contents: InventoryContents) {
        val member = TanoRPG.plugin.memberManager.getMember(player.uniqueId)

        contents[1, 2] = ClickableItem.of(
            ItemBuilder(Material.GREEN_WOOL)
                .addAllItemFlags()
                .setDisplayName(((specialCraft.amount == 0) then "§a§lアイテム作成") ?: "§a§lアイテム作成 (まだ行動回数が残っています)")
                .build()) { _ ->
            contents.inventory().close(player)
            player.setMetadata("crafting", FixedMetadataValue(TanoRPG.plugin, true))
            TanoRPG.playSound(player, Sound.BLOCK_ANVIL_DESTROY, 10, 1.0)
            Bukkit.getScheduler().runTaskLater(TanoRPG.plugin, Runnable {
                player.sendMessage(TanoRPG.PX + "クラフトが完了しました")
                val item = specialCraft.recipe.item.init(specialCraft.recipe.count, specialCraft.gotBuff.getStatus(StatusType.DEVELOP_PERCENT).toDouble(), false, specialCraft.gotBuff)
                specialCraft.gotBuff.removeStatus(StatusType.DEVELOP_PERCENT, specialCraft.gotBuff.getStatus(StatusType.DEVELOP_PERCENT))
                addItem(player, item)
                player.removeMetadata("crafting", TanoRPG.plugin)
                TanoRPG.plugin.sidebarManager.updateSidebar(player, member)
                Bukkit.getPluginManager().callEvent(TanoRpgCraftEvent(player, member, specialCraft.recipe))
            }, 15)
        }

        contents[1, 6] = ClickableItem.of(
            ItemBuilder(Material.RED_WOOL)
                .addAllItemFlags()
                .setDisplayName("§c§l編集画面に戻る")
                .build()) { _ ->
            specialCraft.inv.open(player)
        }
    }
}