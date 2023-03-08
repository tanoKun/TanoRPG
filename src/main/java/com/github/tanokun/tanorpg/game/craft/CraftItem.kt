package com.github.tanokun.tanorpg.game.craft

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.TanoRPG.Companion.playSound
import com.github.tanokun.tanorpg.TanoRPG.Companion.plugin
import com.github.tanokun.tanorpg.event.tanorpg.TanoRpgCraftEvent
import com.github.tanokun.tanorpg.game.craft.special.SpecialCraft
import com.github.tanokun.tanorpg.game.item.type.base.ItemBase
import com.github.tanokun.tanorpg.util.addItem
import com.github.tanokun.tanorpg.util.getAmount
import com.github.tanokun.tanorpg.util.getSameItem
import com.github.tanokun.tanorpg.util.item.ItemBuilder
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.permissions.Permission
import java.util.*
import java.util.function.Consumer

open class CraftItem(private val craftRecipes: ArrayList<CraftRecipe>, open val item: ItemBase) : InventoryProvider {
    val inv: SmartInventory
        get() = SmartInventory.builder()
            .id(item.displayName)
            .title("§9§lレシピ選択")
            .update(false)
            .provider(this)
            .size(6, 9)
            .build()

    override fun init(player: Player, contents: InventoryContents) {
        contents.fillBorders(ClickableItem.empty(
            ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE)
                .setDisplayName("  ")
                .setAmount(1)
                .setGlowing()
                .build())
        )
        val barrier = ArrayList<CraftRecipe>()
        craftRecipes.stream().forEach { item: CraftRecipe ->
            if (player.hasPermission(item.permission)) {
                val lore = ArrayList<String>()
                item.necItems.forEach(Consumer { nec: ItemStack -> lore.add(" -" + nec.itemMeta.displayName.replace("&", "§") + " §fx" + nec.amount) })

                contents.add(ClickableItem.of(
                    ItemBuilder(this.item.material)
                        .setDisplayName(this.item.displayName)
                        .setAmount(item.count)
                        .setArrayListLore(lore)
                        .addAllItemFlags()
                        .build()
                ) {
                        playSound(player, Sound.ENTITY_SHULKER_OPEN, 3, 1.0)
                        item.inv.open(player)
                    })
            } else barrier.add(item)
        }
        barrier.stream().forEach {
            contents.add(ClickableItem.of(
                ItemBuilder(Material.BARRIER)
                    .setDisplayName(this.item.displayName)
                    .setAmount(1)
                    .addAllItemFlags()
                    .build()
            ) {
                    playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 3, 1.0)
                    player.sendMessage(TanoRPG.PX + "§cそのクラフトは開放されていません")
                })
        }
    }
}

class CraftRecipe(val item: ItemBase, val necItems: ArrayList<ItemStack>, private val necTools: ArrayList<ItemStack>, private val price: Long, val count: Int,
                       val permission: Permission) : InventoryProvider {

    var specialData: SpecialCraft.SpecialData? = null

    init {
        if (Bukkit.getPluginManager().getPermission(this.permission.name) == null) Bukkit.getPluginManager().addPermission(this.permission)
    }

    val inv: SmartInventory
        get() = SmartInventory.builder()
            .id(UUID.randomUUID().toString())
            .title("§9§lクラフト確認")
            .update(false)
            .provider(this)
            .size(6, 9)
            .build()

    override fun init(player: Player, contents: InventoryContents) {
        contents.fill(ClickableItem.empty(
            ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE)
            .setDisplayName("  ")
            .setAmount(1)
            .addAllItemFlags()
            .build())
        )

        val item = this.item.init(count, 0.0, true)

        val recipe = this

        item.amount = count
        contents[3, 7] = ClickableItem.empty(item)
        contents[3, 5] = ClickableItem.empty(
            ItemBuilder(Material.ARROW)
                .setDisplayName("§b§l作成後")
                .setAmount(1)
                .addAllItemFlags()
                .setGlowing()
                .build()
        )
        contents[5, 8] =
            ClickableItem.of(
                ItemBuilder(Material.ANVIL)
                    .setDisplayName("§b§lクラフトする")
                    .setAmount(1)
                    .addAllItemFlags()
                    .setGlowing()
                    .build()
            ) {
                val member = plugin.memberManager.getMember(player.uniqueId)
                if (player.hasMetadata("crafting")) {
                    player.sendMessage(TanoRPG.PX + "§cクラフト中...")
                    playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1.0)
                    return@of
                }
                for (item in necItems) {
                    if (getAmount(player, item) < item.amount) {
                        player.sendMessage(TanoRPG.PX + "§c必要素材が足りません")
                        playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1.0)
                        contents.inventory().close(player)
                        return@of
                    }
                }
                for (item in necTools) {
                    if (getAmount(player, item) < item.amount) {
                        player.sendMessage(TanoRPG.PX + "§c必要道具が足りません")
                        playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1.0)
                        contents.inventory().close(player)
                        return@of
                    }
                }
                if (member.getMoney() < price) {
                    player.sendMessage(TanoRPG.PX + "§cお金が足りません")
                    playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1.0)
                    contents.inventory().close(player)
                    return@of
                }
                member.removeMoney(price)
                for (item in necItems) {
                    var toDelete = item.amount
                    while (true) {
                        val itemStack = getSameItem(player, item)
                        val amount = itemStack!!.amount
                        itemStack.amount = amount - toDelete
                        toDelete -= amount
                        if (toDelete <= 0) break
                    }
                }

                if (specialData != null) {
                    SpecialCraft(player, specialData!!.amount, specialData!!.buffs, specialData!!.board, recipe).inv.open(player)
                    return@of
                }

                player.setMetadata("crafting", FixedMetadataValue(plugin, true))
                playSound(player, Sound.BLOCK_ANVIL_DESTROY, 10, 1.0)
                Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                    player.sendMessage(TanoRPG.PX + "クラフトが完了しました")
                    item.amount = count
                    addItem(player, item)
                    player.removeMetadata("crafting", plugin)
                    plugin.sidebarManager.updateSidebar(player, member)
                    Bukkit.getPluginManager().callEvent(TanoRpgCraftEvent(player, member, this))
                }, 15)
            }
        contents.fillRect(0, 1, 0, 3, ClickableItem.empty(ItemStack(Material.AIR)))
        contents[3, 2] = ClickableItem.empty(ItemStack(Material.AIR))
        for (i in 1 until necTools.size + 1) {
            contents[0, i] = ClickableItem.empty(necTools[i - 1])
        }
        contents.fillRect(2, 1, 4, 3, ClickableItem.empty(ItemStack(Material.AIR)))
        var i2 = 0
        for (i in 1..25) {
            val row = (18 + i) / 9
            val column = (18 + i) % 9
            if (row != 2 && row != 3 && row != 4) continue
            if (column != 1 && column != 2 && column != 3) continue
            contents[row, column] = ClickableItem.empty(necItems[i2])
            if (necItems.size <= i2 + 1) return
            i2++
        }
    }
}