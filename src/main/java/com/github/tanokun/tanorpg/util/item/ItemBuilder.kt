package com.github.tanokun.tanorpg.util.item

import com.github.tanokun.tanorpg.TanoRPG
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import java.lang.IllegalArgumentException

open class ItemBuilder(private val item: ItemStack) {
    private val meta: ItemMeta = item.itemMeta


    /**
     * @throws IllegalArgumentException AIRでの作成
     */
    constructor(material: Material): this(ItemStack(material)) {
        if (item.type == Material.AIR) throw IllegalArgumentException("AIRで設定できません")
    }

    fun setDisplayName(name: String): ItemBuilder {
        meta.displayName(Component.text(name))
        return this
    }


    fun setArrayListLore(lore: ArrayList<String>): ItemBuilder {
        val components = ArrayList<Component>(); lore.forEach { components.add(Component.text(it)) }
        meta.lore(components)
        return this
    }

    fun setArrayLore(lore: Array<String>): ItemBuilder {
        val components = ArrayList<Component>(); lore.forEach { components.add(Component.text(it)) }
        meta.lore(components)
        return this
    }

    fun setLore(vararg lore: String): ItemBuilder {
        val components = ArrayList<Component>(); lore.forEach { components.add(Component.text(it)) }
        meta.lore(components)
        return this
    }

    fun setAmount(amount: Int): ItemBuilder {
        item.amount = amount
        return this
    }

    fun setGlowing(): ItemBuilder {
        meta.addEnchant(Enchantment.MENDING, 1, true)
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        return this
    }

    fun addItemFlags(vararg flags: ItemFlag): ItemBuilder {
        flags.forEach { item.itemFlags.add(it) }
        return this
    }


    fun addAllItemFlags(): ItemBuilder {
        ItemFlag.values().forEach { item.itemFlags.add(it) }
        return this
    }

    fun setCustomModelData(cmd: Int): ItemBuilder {
        meta.setCustomModelData(cmd)
        return this
    }

    fun <T, Z> setPersistent(key: String, persistentDataType: PersistentDataType<T, Z>, value: Z): ItemBuilder {
        meta.persistentDataContainer.set(NamespacedKey(TanoRPG.plugin, key), persistentDataType, value)
        return this
    }

    fun build(): ItemStack {
        item.itemMeta = meta
        return item
    }
}