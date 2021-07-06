package com.github.tanokun.tanorpg.player.inv;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.OpenPermissionMap;
import com.github.tanokun.tanorpg.player.Attack;
import com.github.tanokun.tanorpg.player.EquipmentMap;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.player.quest.QuestMap;
import com.github.tanokun.tanorpg.player.skill.SkillClass;
import com.github.tanokun.tanorpg.player.skill.SkillMap;
import com.github.tanokun.tanorpg.player.status.PlayerStatusMap;
import com.github.tanokun.tanorpg.player.status.StatusType;
import com.github.tanokun.tanorpg.player.warppoint.WarpPointMap;
import com.github.tanokun.tanorpg.util.ItemUtils;
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SelSkillClassMenu implements InventoryProvider {
    public static SmartInventory getInv(Player player){
        return SmartInventory.builder()
                .closeable(false)
                .provider(new SelSkillClassMenu())
                .size(5, 9)
                .title("§d職業選択")
                .id("selSkillCLass")
                .update(false)
                .build();
    }

    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(ItemUtils.createItem(Material.BLUE_STAINED_GLASS_PANE, "    ", 1, false)));

        contents.set(2, 1, ClickableItem.of(ItemUtils.createItem(
                Material.BOW, SkillClass.ARCHER.NAME,Arrays.asList(SkillClass.ARCHER.desc), 1, false), no -> {
            setSkillClass(player, SkillClass.ARCHER);
            contents.inventory().close(player);
        }));

        contents.set(2, 3, ClickableItem.of(ItemUtils.createItem(
                Material.IRON_SWORD, SkillClass.SOLDIER.NAME,Arrays.asList(SkillClass.SOLDIER.desc), 1, false), no -> {
            setSkillClass(player, SkillClass.SOLDIER);
            contents.inventory().close(player);
        }));

        contents.set(2, 5, ClickableItem.of(ItemUtils.createItem(
                Material.STICK, SkillClass.MAGE.NAME,Arrays.asList(SkillClass.MAGE.desc), 1, false), no -> {
            setSkillClass(player, SkillClass.MAGE);
            contents.inventory().close(player);
        }));

        contents.set(2, 7, ClickableItem.of(ItemUtils.createItem(
                Material.ALLIUM, SkillClass.PRIEST.NAME,Arrays.asList(SkillClass.PRIEST.desc), 1, false), no -> {
            setSkillClass(player, SkillClass.PRIEST);
            contents.inventory().close(player);
        }));
    }

    private void setSkillClass(Player player, SkillClass skillClass){
        TanoRPG.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 1);
        player.sendMessage(TanoRPG.PX + "職業を「" + skillClass.NAME + "§b」に設定しました。");

        PlayerStatusMap statusMap = new PlayerStatusMap();
        statusMap.addStatus(StatusType.ATK, skillClass.ATK);
        statusMap.addStatus(StatusType.MATK, skillClass.MATK);
        statusMap.addStatus(StatusType.DEF, skillClass.DEF);
        statusMap.addStatus(StatusType.CRITICAL, skillClass.Critical);
        statusMap.addStatus(StatusType.CRITICAL_RATE, skillClass.CriticalRate);
        statusMap.addStatus(StatusType.SUS, skillClass.SUS);
        statusMap.addStatus(StatusType.HP, skillClass.MAX_HP);
        statusMap.addStatus(StatusType.MP, skillClass.MAX_MP);
        Member member = new Member(player.getUniqueId(), skillClass, statusMap, new EquipmentMap(), new SkillMap()
        , new WarpPointMap(), new QuestMap(), new OpenPermissionMap(), new Attack());
        TanoRPG.getPlugin().getMemberManager().registerMember(member);
        TanoRPG.getPlugin().getSidebarManager().setupSidebar(player, member);
    }
}
