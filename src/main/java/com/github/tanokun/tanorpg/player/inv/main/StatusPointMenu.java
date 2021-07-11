package com.github.tanokun.tanorpg.player.inv.main;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.player.status.StatusType;
import com.github.tanokun.tanorpg.util.ItemUtils;
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class StatusPointMenu implements InventoryProvider {
    private Member member;

    public SmartInventory getInv(){
        return SmartInventory.builder()
                .closeable(true)
                .provider(this)
                .size(3, 9)
                .title("§b§lステータスポイント §7(point: " + member.getStatusMap().getStatusPoint() + ")")
                .id("StatusPointMenu")
                .update(false)
                .build();
    }

    public StatusPointMenu(Member member) {
        this.member = member;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        int r = 1;
        int c = 1;
        for (StatusType statusType : StatusType.getBasicStatus()) {
            if (statusType.equals(StatusType.HP)) continue;
            contents.set(r, c, ClickableItem.of(ItemUtils.createItem(statusType.getMaterial()
                    , statusType.getFirst() + statusType.getName() + " §7>>> " + member.getStatusMap().getPointStatus(statusType) + statusType.getEnd(),
                    Arrays.asList("§fクリックで割り振り"), 1, false), e -> {
                if (member.getStatusMap().getStatusPoint() <= 0) {
                    TanoRPG.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    player.sendMessage(TanoRPG.PX + "§cステータスポイントが足りません");
                    return;
                }
                TanoRPG.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1.5);
                member.getStatusMap().addPointStatus(statusType, 1);
                member.getStatusMap().setStatusPoint(member.getStatusMap().getStatusPoint() - 1);
                contents.inventory().setTitle("§b§lステータスポイント §7(point: " + member.getStatusMap().getStatusPoint() + ")");
                contents.inventory().open(player);
                TanoRPG.getPlugin().getSidebarManager().updateSidebar(player, member);
            }));
            c++;
        }

        contents.set(2, 8, ClickableItem.of(ItemUtils.createItem(Material.ARROW, "§c§l戻る", 1, false), e -> {
            new MainMenu().getInv(player).open(player);
        }));
    }
}
