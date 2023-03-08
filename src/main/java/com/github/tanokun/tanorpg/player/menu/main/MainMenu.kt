package com.github.tanokun.tanorpg.player.menu.main

import com.github.tanokun.tanorpg.TanoRPG
import com.github.tanokun.tanorpg.TanoRPG.Companion.playSound
import com.github.tanokun.tanorpg.TanoRPG.Companion.plugin
import com.github.tanokun.tanorpg.game.item.ItemType
import com.github.tanokun.tanorpg.game.item.type.base.ItemData
import com.github.tanokun.tanorpg.player.EquipmentMap.EquipmentType
import com.github.tanokun.tanorpg.player.EquipmentMap.EquipmentType.*
import com.github.tanokun.tanorpg.player.menu.main.skill.SetSkillMenu
import com.github.tanokun.tanorpg.player.menu.util.AdminUtilMenu
import com.github.tanokun.tanorpg.player.status.KindOfStatusType
import com.github.tanokun.tanorpg.player.status.StatusType
import com.github.tanokun.tanorpg.util.createItem
import com.github.tanokun.tanorpg.util.getItemData
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem
import com.github.tanokun.tanorpg.util.smart_inv.inv.InventoryListener
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class MainMenu : InventoryProvider {
    fun getInv(player: Player): SmartInventory {
        val member = plugin.memberManager.getMember(player.uniqueId)

        val function = { equipmentType: EquipmentType, itemStack: ItemStack, itemData: ItemData ->
            run {
                if (!itemData.proper.contains(member.skillClass)) {
                    playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 3, 1.0)
                    player.sendMessage(TanoRPG.PX + "§c職業が対応していません")
                    return@run
                } else if (itemData.necLevel > member.level.value) {
                    playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 3, 1.0)
                    player.sendMessage(TanoRPG.PX + "§cレベルが足りません")
                    return@run
                }

                player.inventory.removeItem(itemStack)
                member.equip.equip[equipmentType]?.let { if (it.type != Material.AIR) player.inventory.addItem(it) }
                member.equip.equip[equipmentType] = itemStack
                MainMenu().getInv(player).open(player)
                player.stopSound(Sound.ENTITY_SHULKER_OPEN)
                playSound(player, Sound.ITEM_ARMOR_EQUIP_LEATHER, 10, 1.0)
                if (equipmentType.raw != -1) player.inventory.setItem(equipmentType.raw, itemStack)
            }
        }

        return SmartInventory.builder()
            .closeable(true)
            .provider(this)
            .size(6, 9)
            .title("§e§l" + player.name + "'s status")
            .id("MainMenu")
            .update(false)
            .listener(InventoryListener(InventoryClickEvent::class.java) {
                if (it.clickedInventory === player.openInventory.topInventory) return@InventoryListener
                it.currentItem?.let { item -> getItemData(item)?.let { itemData ->
                    val clone = item.clone()
                    clone.amount = 1
                    if (itemData.itemType == ItemType.EQUIPMENT) function(itemData.equipmentType, clone, itemData)
                    else if (itemData.itemType == ItemType.ACCESSORY){
                        if (member.equip.equip[ACCESSORY]!!.type == Material.AIR) function(ACCESSORY, clone, itemData)
                        else if (member.equip.equip[ACCESSORY2]!!.type == Material.AIR) function(ACCESSORY2, clone, itemData)
                        else function(ACCESSORY, clone, itemData)
                    }
                }}
            })
            .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        val member = plugin.memberManager.getMember(player.uniqueId)
        playSound(player, Sound.ENTITY_SHULKER_OPEN, 3, 1.0)
        contents.fill(ClickableItem.empty(ItemStack(Material.AIR)))
        contents.fillRect(0, 0, 5, 2, ClickableItem.empty(createItem(Material.PURPLE_STAINED_GLASS_PANE, "    ", 1, false)))
        contents.fillColumn(6, ClickableItem.empty(createItem(Material.YELLOW_STAINED_GLASS_PANE, "    ", 1, false)))

        contents[5, 8] =
            ClickableItem.of(createItem(Material.REDSTONE_BLOCK, "§c§l閉じる", 1, false)) { e: InventoryClickEvent ->
                contents.inventory().close(player)
            }

        contents[0, 8] =
            ClickableItem.of(createItem(Material.CHEST_MINECART, "§6§lバックパック", 1, false)) { e: InventoryClickEvent ->
                if (player.gameMode != GameMode.CREATIVE) {
                    if (!member.backpackMenu.isEnterRegion) {
                        player.sendMessage(TanoRPG.PX + "§cそこではチェストを開くことはできません")
                        playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 10, 0.0)
                        contents.inventory().close(player)
                        return@of
                    }
                }
                playSound(player, Sound.ENTITY_SHULKER_OPEN, 3, 1.0)
                member.backpackMenu.inv.open(player)
            }

        contents[0, 7] =
            ClickableItem.of(createItem(Material.DIAMOND_SWORD, "§e§lスキル選択", 1, false)) { e: InventoryClickEvent ->
                SetSkillMenu().inv.open(player)
            }

        contents[1, 7] =
            ClickableItem.of(createItem(Material.WRITTEN_BOOK, "§d§lクエスト確認と選択", 1, false)) { e: InventoryClickEvent ->
                playSound(player, Sound.ENTITY_SHULKER_OPEN, 3, 1.0)
                SelQuestMenu(member).inv.open(player)
            }

        contents[2, 7] =
            ClickableItem.of(createItem(Material.NETHER_STAR, "§b§lステータスポイント", 1, false)) { e: InventoryClickEvent ->
                playSound(player, Sound.ENTITY_SHULKER_OPEN, 3, 1.0)
                contents.inventory().open(player)
                StatusPointMenu(member).inv.open(player)
            }

        val status = ArrayList<String>()
        StatusType.getBasicStatus().stream().forEach { statusType: StatusType -> status.add(" " + KindOfStatusType.NORMAL + "§a" + statusType.name + " +" + member.statusMap.getPointAndStatus(statusType) + statusType.end) }
        contents[1, 4] = ClickableItem.empty(createItem(Material.IRON_SWORD, "§a§l基本ステータス", status, 1, false))
        status.clear()
        StatusType.getNotBasicStatus().stream()
            .filter { statusType: StatusType? -> member.statusMap.getPointAndStatus(statusType) != 0.0 }
            .forEach { statusType: StatusType -> status.add(" " + KindOfStatusType.NORMAL + "§a" + statusType.name + " +" + member.statusMap.getPointAndStatus(statusType) + statusType.end) }

        contents[4, 4] = ClickableItem.empty(createItem(Material.BEACON, "§6§l特殊ステータス", status, 1, false))

        initArmor(player, contents)

        if (player.isOp) contents[2, 8] =
            ClickableItem.of(createItem(Material.COMMAND_BLOCK_MINECART, "§c§lAdminUtil", 1, false)) {
                playSound(player, Sound.ENTITY_SHULKER_OPEN, 3, 1.0)
                AdminUtilMenu().inv.open(player)
        }
    }

    private fun initArmor(player: Player, contents: InventoryContents) {
        val member = plugin.memberManager.getMember(player.uniqueId)
        member.equip.equip.keys
            .filter { !(it == MAIN || it == SUB) }
            .forEach { equip: EquipmentType ->
            member.equip.equip[equip]?.let { item ->
                if (item.type == Material.AIR) contents[equip.row, equip.column] = ClickableItem.empty(createItem(Material.BARRIER, "§7§l未設定装備 (${equip.getName()})", 1, false))
                else contents[equip.row, equip.column] =
                    ClickableItem.of(item) {
                        player.inventory.addItem(item)
                        member.equip.equip[equip] = ItemStack(Material.AIR)
                        MainMenu().getInv(player).open(player)
                        player.stopSound(Sound.ENTITY_SHULKER_OPEN)
                        playSound(player, Sound.ITEM_ARMOR_EQUIP_LEATHER, 10, 1.0)
                        if (equip.raw != -1) player.inventory.setItem(equip.raw, ItemStack(Material.AIR))
                    }
            }
        }
    }
}