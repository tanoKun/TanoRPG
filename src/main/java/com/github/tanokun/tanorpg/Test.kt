package com.github.tanokun.tanorpg

import org.bukkit.Material
import org.bukkit.block.ShulkerBox
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BlockStateMeta
import java.util.*


fun main() {
    val shulker = ItemStack(Material.SHULKER_BOX)
    val shulkerBoxMeta = (shulker.itemMeta as BlockStateMeta).blockState as ShulkerBox
    shulkerBoxMeta.inventory.contents
}