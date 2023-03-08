package com.github.tanokun.tanorpg.game.item.drop

import com.github.tanokun.tanorpg.util.chance
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import java.util.*

class ItemTable(val name: String, private val min: Int, private val max: Int, items: ArrayList<DropItem>) {
    private val items: ArrayList<DropItem>

    init {
        this.items = arraySort(items)
    }

    fun generate(p2: Int): Inventory {
        var p = p2
        if (p <= 0) p = 1
        val inv = DropInventory(name).inventory
        var count = 0
        val random = Random()

        if (items.size == 0) return inv

        val min = ((this.min == 0) then 1) ?: this.min

        while (count < min) {
            for (item in items) {
                if (count >= max) break
                if (chance(item.per * p)) {
                    count++
                    var amount = item.min - 1
                    while (amount < item.min) amount = if (item.min == item.max) item.max else random.nextInt(item.max + 1)
                    var slot = random.nextInt(27)
                    while (inv.getItem(slot) != null) slot = random.nextInt(27)
                    val itemStack = item.item.init(1, 0.0, false)
                    itemStack.amount = amount
                    inv.setItem(slot, itemStack)
                }
            }

            if (this.min == 0) break

        }
        return inv

    }

    fun generateSort(p: Double): Inventory {
        var p = p
        if (p <= 0) p = 1.0
        val inv = DropInventory(name).inventory
        var count = 0
        val random = Random()
        if (items.size == 0) return inv


        val min = ((this.min == 0) then 1) ?: this.min
        while (count < min) {
            for (item in items) {
                if (count >= max) break1
                if (chance(item.per * p)) {
                    count++
                    var amount = item.min - 1
                    while (amount < item.min) amount = if (item.min == item.max) item.max else random.nextInt(item.max + 1)
                    val itemStack = item.item.init(1, 0.0, false)
                    itemStack.amount = amount
                    inv.addItem(itemStack)
                }
            }

            if (this.min == 0) break

        }
        return inv
    }

    private fun arraySort(items: ArrayList<DropItem>): ArrayList<DropItem> {
        for (i in items.indices) {
            for (j in items.indices) {
                if (items[i].per < items[j].per) {
                    changeItem(items, i, j)
                }
            }
        }
        return items
    }

    private fun changeItem(items: ArrayList<DropItem>, i: Int, j: Int) {
        val tmp = items[i]
        items[i] = items[j]
        items[j] = tmp
    }

    class DropInventory(name: String?) : InventoryHolder {
        private val inventory: Inventory

        init {
            inventory = Bukkit.createInventory(this, 27, Component.text(name!!))
        }

        override fun getInventory(): Inventory {
            return inventory
        }
    }
}

infix fun <T> Boolean.then(other: T) = if (this) other else null