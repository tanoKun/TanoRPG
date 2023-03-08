package com.github.tanokun.tanorpg.game.entity

import com.github.tanokun.tanorpg.game.item.drop.ItemTable
import com.github.tanokun.tanorpg.player.status.StatusMap

data class ObjectEntity(val id: String, val name: String, val exp: Int, val level: Int, val statusMap: StatusMap, val lootTable: ItemTable)