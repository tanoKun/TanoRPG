package com.github.tanokun.tanorpg.player.quest.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.player.quest.inv.QuestListMenu;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ActionQuestListener implements Listener {

    @EventHandler
    public void onSpeakToNPC(NPCRightClickEvent e){
        Member m = TanoRPG.getPlugin().getMemberManager().getMember(e.getClicker().getUniqueId());
        if (m == null) return;

        if (m.getQuestMap().isAction()) return;
        if (TanoRPG.getPlugin().getQuestManager().getQuests(e.getNPC().getId()) != null){
            QuestListMenu.getInv(e.getNPC(), e.getClicker()).open(e.getClicker());
            TanoRPG.playSound(e.getClicker(), Sound.ENTITY_SHULKER_OPEN, 3, 1);
        }
    }
}
