package com.github.tanokun.tanorpg.game.item.drop

import com.github.tanokun.tanorpg.game.item.type.base.ItemBase
import com.github.tanokun.tanorpg.util.io.MapNode
import java.util.*

data class DropItem(val min: Int, val max: Int, val item: ItemBase, val per: Double) {
    private val random: Random = Random()

    fun random(): MapNode<ItemBase, Int> {
        return MapNode(item, random.nextInt(max) + min)
    }
}