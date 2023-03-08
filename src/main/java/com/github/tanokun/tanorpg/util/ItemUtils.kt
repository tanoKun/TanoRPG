package com.github.tanokun.tanorpg.util

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.event.tanorpg.TanoRpgPlayerGetItemEvent
import com.github.tanokun.tanorpg.game.item.type.base.ItemData
import com.github.tanokun.tanorpg.player.Member
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

private val gson = Gson()

fun createItem(material: Material?, name: String?, count: Int, glowing: Boolean): ItemStack {
    val i = ItemStack(material!!)
    i.amount = count
    val im = i.itemMeta
    im.setDisplayName(name)
    if (glowing) {
        im.addEnchant(Enchantment.MENDING, 1, true)
    }
    im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS,
            ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE)
    i.itemMeta = im
    return i
}

fun createItem(material: Material, name: String, lore: List<String>, count: Int, glowing: Boolean): ItemStack {
    val i = ItemStack(material)
    i.amount = count
    val im = i.itemMeta
    im.setDisplayName(name)
    im.lore = lore
    if (glowing) {
        im.addEnchant(Enchantment.MENDING, 1, true)
    }
    im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS,
            ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE)
    i.itemMeta = im
    return i
}

fun createItem(material: Material?, name: String?, count: Int, glowing: Boolean, c: Int): ItemStack {
    val i = ItemStack(material!!)
    i.amount = count
    val im = i.itemMeta
    im.setDisplayName(name)
    if (glowing) {
        im.addEnchant(Enchantment.MENDING, 1, true)
    }
    im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS,
            ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE)
    im.setCustomModelData(c)
    i.itemMeta = im
    return i
}

fun createItem(material: Material, name: String, lore: List<String>, count: Int, glowing: Boolean, c: Int): ItemStack {
    val i = ItemStack(material!!)
    i.amount = count
    val im = i.itemMeta
    im.setDisplayName(name)
    im.lore = lore
    if (glowing) {
        im.addEnchant(Enchantment.MENDING, 1, true)
    }
    im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS,
            ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE)
    im.setCustomModelData(c)
    i.itemMeta = im
    return i
}

fun createItem(material: Material, name: String, lore: Array<String>, count: Int, glowing: Boolean, c: Int): ItemStack {
    val i = ItemStack(material)
    i.amount = count
    val im = i.itemMeta
    im.setDisplayName(name)
    im.lore = lore.asList()
    if (glowing) {
        im.addEnchant(Enchantment.MENDING, 1, true)
    }
    im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS,
        ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE)
    im.setCustomModelData(c)
    i.itemMeta = im
    return i
}

fun <T, Z> setPersistent(itemStack: ItemStack, key: String, persistentDataType: PersistentDataType<T, Z>, value: Z) {
    val meta = itemStack.itemMeta
    val pdc = meta.persistentDataContainer
    pdc.set(NamespacedKey(TanoRPG.plugin, key), persistentDataType, value)
    itemStack.itemMeta = meta
}

fun <T, Z> getPersistent(itemStack: ItemStack, key: String, persistentDataType: PersistentDataType<T, Z>): Z? {
    if (itemStack.type == Material.AIR) return null
    return if (itemStack.itemMeta.persistentDataContainer.isEmpty) null else itemStack.itemMeta.persistentDataContainer.get(NamespacedKey(TanoRPG.plugin, key), persistentDataType)
}

fun getItemData(itemStack: ItemStack): ItemData? {
    if (itemStack.type == Material.AIR) return null
    return if (getPersistent(itemStack, "data", PersistentDataType.STRING) == null) null else gson.fromJson(getPersistent(itemStack, "data", PersistentDataType.STRING), object : TypeToken<ItemData?>() {}.type)
}

fun setItemData(itemStack: ItemStack, itemData: ItemData?) {
    setPersistent(itemStack, "data", PersistentDataType.STRING, gson.toJson(itemData, object : TypeToken<ItemData?>() {}.type))
}

fun isTrueSkillClass(itemData: ItemData, member: Member, player: Player): Boolean {
    if (!itemData.proper.contains(member.skillClass)) {
        TanoRPG.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 3, 1.0)
        player.sendMessage(TanoRPG.PX + "§c職業が対応していません")
        return false
    }
    return true
}

fun isTrueLevel(itemData: ItemData, member: Member, player: Player): Boolean {
    if (itemData.necLevel > member.level.value) {
        TanoRPG.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 3, 1.0)
        player.sendMessage(TanoRPG.PX + "§cレベルが足りません")
        return false
    }
    return true
}

fun getAmount(player: Player, item: ItemStack): Int {
    var au = 0
    for (i in player.inventory.contents) {
        if (i == null || i.type == Material.AIR) continue
        val isName = if (i.itemMeta.displayName == null) "" else i.itemMeta.displayName
        val itemName = if (item.itemMeta.displayName == null) "" else item.itemMeta.displayName
        if (isName == itemName && i.type == item.type) {
            au += i.amount
        }
    }
    return au
}

fun getSameItem(player: Player, item: ItemStack): ItemStack? {
    for (i in player.inventory.contents) {
        if (i == null || i.type == Material.AIR) continue
        val isName = if (i.itemMeta.displayName == null) "" else i.itemMeta.displayName
        val itemName = if (item.itemMeta.displayName == null) "" else item.itemMeta.displayName
        if (isName == itemName && i.type == item.type) return i
    }
    return null
}

fun addItem(player: Player, item: ItemStack?) {
    Bukkit.getScheduler().runTask(TanoRPG.plugin, Runnable {
        player.inventory.addItem(item!!)
        Bukkit.getPluginManager().callEvent(TanoRpgPlayerGetItemEvent(player, item))
    })
}