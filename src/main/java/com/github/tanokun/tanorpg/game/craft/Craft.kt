package com.github.tanokun.tanorpg.game.craft

import com.github.tanokun.tanorpg.TanoRPG.Companion.playSound
import com.github.tanokun.tanorpg.util.item.ItemBuilder
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.permissions.Permission

data class Craft(val id: String, val name: String, val items: ArrayList<CraftItem>, val npcId: Int, val permission: Permission) : InventoryProvider {

    init { if (Bukkit.getPluginManager().getPermission(permission.name) == null) Bukkit.getPluginManager().addPermission(permission) }

    val inv: SmartInventory
        get() = SmartInventory.builder()
            .id(id)
            .title("§9§lクラフト「$name§9§l」")
            .update(false)
            .provider(this)
            .size(5, 9)
            .build()

    override fun init(player: Player, contents: InventoryContents) {
        contents.fillBorders(ClickableItem.empty(
            ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE)
                .setDisplayName("  ")
                .setAmount(1)
                .addAllItemFlags()
                .build())
        )
        items.stream().forEach { item: CraftItem ->
            contents.add(ClickableItem.of(item.item.init(1, 0.0, true)) {
                playSound(player, Sound.ENTITY_SHULKER_OPEN, 3, 1.0)
                item.inv.open(player)
            })
        }
    }
}