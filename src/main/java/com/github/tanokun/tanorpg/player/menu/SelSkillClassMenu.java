package com.github.tanokun.tanorpg.player.menu;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.player.*;
import com.github.tanokun.tanorpg.player.menu.main.backpack.BackpackMenu;
import com.github.tanokun.tanorpg.player.quests.QuestMap;
import com.github.tanokun.tanorpg.player.skill.SkillClass;
import com.github.tanokun.tanorpg.player.skill.SkillMap;
import com.github.tanokun.tanorpg.player.status.PlayerStatusMap;
import com.github.tanokun.tanorpg.player.status.StatusType;
import com.github.tanokun.tanorpg.player.status.buff.BuffMap;
import com.github.tanokun.tanorpg.player.warppoint.WarpPointMap;
import com.github.tanokun.tanorpg.util.ItemUtilsKt;
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;

public class SelSkillClassMenu implements InventoryProvider {
    public SmartInventory getInv() {
        return SmartInventory.builder()
                .closeable(false)
                .provider(this)
                .size(5, 9)
                .title("§d職業選択")
                .id("SelSkillClassMenu")
                .update(false)
                .build();
    }

    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(ItemUtilsKt.createItem(Material.BLUE_STAINED_GLASS_PANE, "    ", 1, false)));

        contents.set(2, 1, ClickableItem.of(ItemUtilsKt.createItem(
                Material.BOW, SkillClass.ARCHER.getClassName(), Arrays.asList(SkillClass.ARCHER.getDesc()), 1, false), no -> {
            setSkillClass(player, SkillClass.ARCHER);
            contents.inventory().close(player);
        }));

        contents.set(2, 3, ClickableItem.of(ItemUtilsKt.createItem(
                Material.IRON_SWORD, SkillClass.SOLDIER.getClassName(), Arrays.asList(SkillClass.SOLDIER.getDesc()), 1, false), no -> {
            setSkillClass(player, SkillClass.SOLDIER);
            contents.inventory().close(player);
        }));

        contents.set(2, 5, ClickableItem.of(ItemUtilsKt.createItem(
                Material.STICK, SkillClass.MAGE.getClassName(), Arrays.asList(SkillClass.MAGE.getDesc()), 1, false), no -> {
            setSkillClass(player, SkillClass.MAGE);
            contents.inventory().close(player);
        }));

        contents.set(2, 7, ClickableItem.of(ItemUtilsKt.createItem(
                Material.ALLIUM, SkillClass.PRIEST.getClassName(), Arrays.asList(SkillClass.PRIEST.getDesc()), 1, false), no -> {
            setSkillClass(player, SkillClass.PRIEST);
            contents.inventory().close(player);
        }));
    }

    private void setSkillClass(Player player, SkillClass skillClass) {
        TanoRPG.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 1);
        player.sendMessage(TanoRPG.PX + "職業を「" + skillClass.getClassName() + "§b」に設定しました。");

        PlayerStatusMap statusMap = new PlayerStatusMap(null);
        statusMap.addStatus(StatusType.ATK, skillClass.getATK());
        statusMap.addStatus(StatusType.INT, skillClass.getINT());
        statusMap.addStatus(StatusType.STR, skillClass.getSTR());
        statusMap.addStatus(StatusType.DEF, skillClass.getDEF());
        statusMap.addStatus(StatusType.CRITICAL, skillClass.getCritical());
        statusMap.addStatus(StatusType.CRITICAL_RATE, skillClass.getCriticalRate());
        statusMap.addStatus(StatusType.SUS, skillClass.getSUS());
        statusMap.addStatus(StatusType.HP, skillClass.getMaxHP());
        statusMap.addStatus(StatusType.MP, skillClass.getMaxHP());
        statusMap.addStatus(StatusType.HEAL_MP, skillClass.getHeal_mp());
        Member member = new Member(player.getUniqueId(), skillClass, statusMap, new EquipmentMap(), new SkillMap()
                , new WarpPointMap(), new QuestMap(), new BuffMap(), new Attack(), new BackpackMenu(player.getUniqueId()), new ChestMap(new HashMap<>()),
                skillClass.getMaxHP(), skillClass.getMaxMP(), 0, MemberLevelType.Lv_1, new MemberRunnable(), null);
        statusMap.setMember(member);
        TanoRPG.getPlugin().getMemberManager().registerMember(member);
        TanoRPG.getPlugin().getSidebarManager().setupSidebar(player, member);
        member.online();
    }
}
