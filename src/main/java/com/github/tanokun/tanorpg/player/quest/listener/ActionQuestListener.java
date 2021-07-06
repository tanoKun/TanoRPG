package com.github.tanokun.tanorpg.player.quest.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.player.quest.Quest;
import com.github.tanokun.tanorpg.player.quest.inv.QuestListMenu;
import com.github.tanokun.tanorpg.util.ItemUtils;
import com.github.tanokun.tanorpg.util.io.MapNode;
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public class ActionQuestListener implements Listener {

    @EventHandler
    public void onSpeakToNPC(NPCRightClickEvent e){
        Member m = TanoRPG.getPlugin().getMemberManager().getMember(e.getClicker().getUniqueId());
        if (m == null) return;

        if (m.getQuestMap().isAction()) return;

        if (TanoRPG.getPlugin().getQuestManager().getQuests(e.getNPC().getId()) != null){
            if (m.getQuestMap().getActiveQuest() != null && m.getQuestMap().getActiveQuest().getNpcId() == e.getNPC().getId()) {
                if (!m.getQuestMap().getActiveQuest().isClear()) return;
                Quest quest = m.getQuestMap().getActiveQuest().getQuest();
                m.getQuestMap().setAction(true);
                Bukkit.getScheduler().runTaskAsynchronously(TanoRPG.getPlugin(), () -> {
                    quest.getShowQuestActions()
                            .forEach(a -> a.execute(m));
                    Bukkit.getScheduler().runTask(TanoRPG.getPlugin(), () -> {
                        m.getQuestMap().setAction(false);
                        m.getQuestMap().addClearQuest(m.getQuestMap().getActiveQuest());
                        m.getQuestMap().setActiveQuest(new MapNode<>(null, 0));
                        m.getQuestMap().removeQuest(quest.getName());
                        e.getClicker().sendMessage(TanoRPG.PX + "クエスト「" + quest.getName() + "」をクリアしました！");
                        e.getClicker().setCompassTarget(TanoRPG.getPlugin().getDataManager().getStartLoc());
                    });
                });
                return;
            }
            QuestListMenu.getInv(e.getNPC(), e.getClicker()).open(e.getClicker());
            TanoRPG.playSound(e.getClicker(), Sound.ENTITY_SHULKER_OPEN, 3, 1);
        }
    }
}
