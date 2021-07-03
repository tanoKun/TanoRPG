package com.github.tanokun.tanorpg.player.inv;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.item.type.base.ItemData;
import com.github.tanokun.tanorpg.player.EquipmentMap;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.player.status.KindOfStatusType;
import com.github.tanokun.tanorpg.player.status.StatusType;
import com.github.tanokun.tanorpg.util.ItemUtils;
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.tanorpg.util.smart_inv.inv.InventoryListener;
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class MainMenu implements InventoryProvider {
    public static SmartInventory getInv(Player player){
        return SmartInventory.builder()
                .closeable(true)
                .cancelable(false)
                .provider(new MainMenu())
                .listener(getCloseEvent())
                .size(6, 9)
                .title("§e§l" + player.getName() + "'s status")
                .id("showStatus")
                .update(false)
                .listener(new InventoryListener<>(InventoryClickEvent.class, e -> {
                    if (e.getCurrentItem() == null) return;
                    if (e.getCurrentItem().getType().equals(Material.AIR)) e.setCancelled(true);
                }))
                .build();
    }

    public void init(Player player, InventoryContents contents) {
        Member member = TanoRPG.getPlugin().getMemberManager().getMember(player.getUniqueId());
        TanoRPG.playSound(player, Sound.ENTITY_SHULKER_OPEN, 3, 1);

        contents.fill(ClickableItem.of(new ItemStack(Material.AIR), e -> e.setCancelled(true)));

        contents.fillRect(0, 0, 5, 2,
                ClickableItem.of(ItemUtils.createItem(Material.PURPLE_STAINED_GLASS_PANE, "    ", 1, false), e -> e.setCancelled(true)));
        contents.fillColumn(6,
                ClickableItem.of(ItemUtils.createItem(Material.YELLOW_STAINED_GLASS_PANE, "    ", 1, false), e -> e.setCancelled(true)));

        contents.set(0, 8, ClickableItem.of(ItemUtils.createItem(Material.CHEST_MINECART, "§6§lバックパック", 1, false), e -> {
            e.setCancelled(true);
        }));

        contents.set(3, 8, ClickableItem.of(ItemUtils.createItem(Material.WRITABLE_BOOK, "§a§lワープポイント", 1, false), e -> {
            e.setCancelled(true);
        }));

        contents.set(5, 8, ClickableItem.of(ItemUtils.createItem(Material.REDSTONE_BLOCK, "§c§l閉じる", 1, false), e -> {
            e.setCancelled(true);
            contents.inventory().close(player);
        }));

        contents.set(0, 7, ClickableItem.of(ItemUtils.createItem(Material.DIAMOND_SWORD, "§e§lスキル選択", 1, false), e -> {
            e.setCancelled(true);
        }));

        contents.set(1, 7, ClickableItem.of(ItemUtils.createItem(Material.WRITTEN_BOOK, "§d§lクエスト確認と選択", 1, false), e -> {
            e.setCancelled(true);
        }));

        contents.set(2, 7, ClickableItem.of(ItemUtils.createItem(Material.NETHER_STAR, "§b§lステータスポイント", 1, false), e -> {
            e.setCancelled(true);
        }));

        ArrayList<String> status = new ArrayList<>();
        StatusType.getBasicStatus().stream()
                .forEach(statusType -> status.add(" " + KindOfStatusType.NORMAL + "§a" + statusType.getName() + " +" +
                        member.getStatusMap().getPointAndStatus(statusType) + statusType.getEnd()));
        contents.set(1, 4, ClickableItem.of(ItemUtils.createItem(Material.IRON_SWORD, "§a§l基本ステータス", status, 1, false), e -> {
            e.setCancelled(true);
        }));

        status.clear();
        StatusType.getNotBasicStatus().stream().filter(statusType -> member.getStatusMap().getPointAndStatus(statusType) == 0)
                .forEach(statusType -> status.add(" " + KindOfStatusType.NORMAL + "§a" + statusType.getName() + " +" +
                        member.getStatusMap().getPointAndStatus(statusType) + statusType.getEnd()));
        contents.set(4, 4, ClickableItem.of(ItemUtils.createItem(Material.BEACON, "§6§l特殊ステータス", status, 1, false), e -> {
            e.setCancelled(true);
        }));
        initArmor(player, contents);
    }
    public void initArmor(Player player, InventoryContents contents) {
        Member member = TanoRPG.getPlugin().getMemberManager().getMember(player.getUniqueId());
        member.getEquipMap().getEquip().keySet().forEach(equip -> {
            if (!equip.equals(EquipmentMap.EquipmentType.MAIN) && !equip.equals(EquipmentMap.EquipmentType.SUB)){
                contents.set(equip.getRow(), equip.getColumn(), ClickableItem.of(member.getEquipMap().getEquip(equip), e -> {
                   e.setCancelled(false);
                }));
            }
        });
    }

    private static InventoryListener<InventoryCloseEvent> getCloseEvent(){
        return new InventoryListener<>(InventoryCloseEvent.class, e -> {
            final boolean[] error = {false};

            Player player = (Player) e.getPlayer();
            Member member = TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId());
            member.getStatusMap().removeAllStatus(member.getEquipMap().getStatus());

            HashMap<EquipmentMap.EquipmentType, ItemStack> equips = new HashMap<>();
            equips.put(EquipmentMap.EquipmentType.HELMET,     e.getInventory().getItem(10) == null ? new ItemStack(Material.AIR) : e.getInventory().getItem(10));
            equips.put(EquipmentMap.EquipmentType.CHESTPLATE, e.getInventory().getItem(19) == null ? new ItemStack(Material.AIR) : e.getInventory().getItem(19));
            equips.put(EquipmentMap.EquipmentType.LEGGINGS,   e.getInventory().getItem(28) == null ? new ItemStack(Material.AIR) : e.getInventory().getItem(28));
            equips.put(EquipmentMap.EquipmentType.BOOTS,      e.getInventory().getItem(37) == null ? new ItemStack(Material.AIR) : e.getInventory().getItem(37));
            equips.put(EquipmentMap.EquipmentType.ACCESSORY,  e.getInventory().getItem(18) == null ? new ItemStack(Material.AIR) : e.getInventory().getItem(18));
            equips.put(EquipmentMap.EquipmentType.ACCESSORY2, e.getInventory().getItem(20) == null ? new ItemStack(Material.AIR) : e.getInventory().getItem(20));
            equips.keySet().forEach(equip -> {
                ItemData itemData = ItemUtils.getItemData(equips.get(equip));

                if (equips.get(equip).getType().equals(Material.AIR)) {
                    member.getEquipMap().setEquip(equip, equips.get(equip));
                    return;
                }

                if (itemData == null) {
                    player.getInventory().addItem(equips.get(equip));
                    if (error[0]) return;
                    TanoRPG.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 3, 1);
                    player.sendMessage(TanoRPG.PX + "§c不正なアイテムです");
                    error[0] = true;
                    return;
                }
                if (!itemData.getItemType().equals(equip.getItemType())) {
                    player.getInventory().addItem(equips.get(equip));
                    if (error[0]) return;
                    TanoRPG.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 3, 1);
                    player.sendMessage(TanoRPG.PX + "§c装備ではありません");
                    error[0] = true;
                    return;
                }
                if (!itemData.getEquipmentType().equals(equip)) {
                    player.getInventory().addItem(equips.get(equip));
                    if (error[0]) return;
                    TanoRPG.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 3, 1);
                    player.sendMessage(TanoRPG.PX + "§cスロットが違います");
                    error[0] = true;
                    return;
                }
                if (!itemData.getProper().contains(member.getSkillClass())) {
                    player.getInventory().addItem(equips.get(equip));
                    if (error[0]) return;
                    TanoRPG.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 3, 1);
                    player.sendMessage(TanoRPG.PX + "§c職業が対応していません");
                    error[0] = true;
                    return;
                }
                if (itemData.getNecLevel() > member.getHasLevel().getValue()) {
                    player.getInventory().addItem(equips.get(equip));
                    if (error[0]) return;
                    TanoRPG.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 3, 1);
                    player.sendMessage(TanoRPG.PX + "§cレベルが足りません");
                    error[0] = true;
                    return;
                }
                member.getEquipMap().setEquip(equip, equips.get(equip));
            });

            member.getStatusMap().addAllStatus(member.getEquipMap().getStatus());
        });
    }
}
