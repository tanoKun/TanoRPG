package com.github.tanokun.tanorpg.player.quest.inv;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.player.quest.Quest;
import com.github.tanokun.tanorpg.player.quest.QuestManager;
import com.github.tanokun.tanorpg.util.ItemUtils;
import com.github.tanokun.tanorpg.util.hologram.SelectableHologram;
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class QuestListMenu implements InventoryProvider {
    private final NPC npcId;
    private final Member m;
    private final SelectableHologram hologram = SelectableHologram.create();

    public QuestListMenu(NPC npcId, Player player) {
        this.npcId = npcId;
        m = TanoRPG.getPlugin().getMemberManager().getMember(player.getUniqueId());
    }

    public static SmartInventory getInv(NPC npcId, Player player){
        return SmartInventory.builder()
                .closeable(true)
                .provider(new QuestListMenu(npcId, player))
                .size(1, 9)
                .title("§dクエスト受注リスト")
                .id("questListMenu")
                .update(false)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        Member m = TanoRPG.getPlugin().getMemberManager().getMember(player.getUniqueId());

        for (Quest quest : TanoRPG.getPlugin().getQuestManager().getQuests(npcId.getId())){
            List<String> lore = new ArrayList<>();
            lore.add(" ");
            quest.getLore().forEach(lore::add);
            lore.add(" ");
            lore.add("§7難易度: " + quest.getDifficulty().getName());
            lore.add(" ");
            lore.add("§b━━報酬━━━━━━━━━");
            quest.getResult().forEach(lore::add);
            contents.add(ClickableItem.of(ItemUtils.createItem(Material.WRITTEN_BOOK, "§d" + quest.getName(), lore, 1, false), e->{
                m.getQuestMap().setAction(true);
                contents.inventory().close(player);
                Bukkit.getScheduler().runTaskAsynchronously(TanoRPG.getPlugin(), () -> {
                    quest.getShowQuestActions()
                            .forEach(a -> a.execute(m));
                    Bukkit.getScheduler().runTask(TanoRPG.getPlugin(), () -> {
                        Location location = npcId.getEntity().getLocation();
                        location.setYaw(location.getYaw() + 90);
                        Vector vector = location.getDirection();
                        double x = vector.getX() * 1;
                        double z = vector.getZ() * 1;

                        location.add(x, 1.3, z);

                        hologram.addChoice("§a受注する", getYes(player, contents, quest));
                        hologram.addChoice("§c受注しない", getNo(player, contents, quest));
                        hologram.display(player, location);
                    });
                });
            }));
        }
    }

    private Runnable getYes(Player player, InventoryContents contents, Quest quest){
        return () -> {
            m.getQuestMap().setAction(true);
            contents.inventory().close(player);
            Bukkit.getScheduler().runTaskAsynchronously(TanoRPG.getPlugin(), () -> {
                quest.getStartQuestActions()
                        .forEach(a -> a.execute(m));
                m.getQuestMap().setAction(false);
                Bukkit.getScheduler().runTask(TanoRPG.getPlugin(), () -> {
                    player.sendMessage(QuestManager.PX + "§aクエストを受注しました。");
                    TanoRPG.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 2);
                });
            });
        };
    }

    private Runnable getNo(Player player, InventoryContents contents, Quest quest){
        return () -> {
            m.getQuestMap().setAction(true);
            contents.inventory().close(player);
            Bukkit.getScheduler().runTaskAsynchronously(TanoRPG.getPlugin(), () -> {
                quest.getCancelQuestActions()
                        .forEach(a -> a.execute(m));
                player.sendMessage(QuestManager.PX + "§aクエストを取り消しました。");
                m.getQuestMap().setAction(false);
            });
        };
    }
}
