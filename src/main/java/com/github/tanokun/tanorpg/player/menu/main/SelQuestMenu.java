package com.github.tanokun.tanorpg.player.menu.main;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.player.quests.QuestManager;
import com.github.tanokun.tanorpg.player.quests.QuestModel;
import com.github.tanokun.tanorpg.player.quests.data.QuestPlayerData;
import com.github.tanokun.tanorpg.util.ItemUtilsKt;
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SelQuestMenu implements InventoryProvider {
    private Member member;

    public SmartInventory getInv() {
        return SmartInventory.builder()
                .closeable(true)
                .provider(this)
                .size(3, 9)
                .title("§d§lクエスト確認と選択")
                .id("SelQuestMenu")
                .update(false)
                .build();
    }

    public SelQuestMenu(Member member) {
        this.member = member;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(ItemUtilsKt.createItem(Material.PURPLE_STAINED_GLASS_PANE, "  ", 1, false)));
        contents.set(1, 1, ClickableItem.empty(ItemUtilsKt.createItem(Material.PURPLE_STAINED_GLASS_PANE, "  ", 1, false)));
        contents.set(1, 7, ClickableItem.empty(ItemUtilsKt.createItem(Material.PURPLE_STAINED_GLASS_PANE, "  ", 1, false)));

        int r = 1;
        int c = 2;
        for (QuestPlayerData questData : member.getQuestMap().getQuests().values()) {
            QuestModel quest = questData.getQuestModel();

            List<String> lore = new ArrayList<>();
            lore.add(" ");
            quest.getLore().forEach(lore::add);
            lore.add(" ");
            lore.add("§7難易度: " + quest.getDifficulty().getName());
            lore.add(" ");
            lore.add("§b━━報酬━━━━━━━━━");
            quest.getResult().forEach(lore::add);

            if (member.getQuestMap().getActiveQuest().equals(quest.getId()))
                contents.add(ClickableItem.of(ItemUtilsKt.createItem(Material.WRITTEN_BOOK, "§d" + quest.getName() + "§7(アクティブ状態)", lore, 1, false), e -> {
                    TanoRPG.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 3, 1);
                    player.sendMessage(QuestManager.PX + "既にアクティブです。");
                }));
            else
                contents.add(ClickableItem.of(ItemUtilsKt.createItem(Material.BOOK, "§d" + quest.getName() + "§7(クリックで選択)", lore, 1, false), e -> {
                    TanoRPG.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 1);
                    player.sendMessage(QuestManager.PX + "アクティブミッションを§d「" + quest.getName() + "」§aに設定しました。");
                    member.getQuestMap().setActiveQuest(quest.getId());
                    contents.inventory().open(player);
                    TanoRPG.getPlugin().getSidebarManager().updateSidebar(player, member);
                }));
        }

        contents.set(2, 8, ClickableItem.of(ItemUtilsKt.createItem(Material.ARROW, "§c§l戻る", 1, false), e -> {
            new MainMenu().getInv(player).open(player);
        }));
    }
}
