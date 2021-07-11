package com.github.tanokun.tanorpg.player.quest.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.player.quest.Quest;
import com.github.tanokun.tanorpg.player.quest.inv.QuestListMenu;
import com.github.tanokun.tanorpg.util.io.MapNode;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

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
                    quest.getFinishQuestActions()
                            .forEach(a -> a.execute(m));
                    Bukkit.getScheduler().runTask(TanoRPG.getPlugin(), () -> {
                        m.getQuestMap().setAction(false);
                        m.getQuestMap().addClearQuest(m.getQuestMap().getActiveQuest());
                        m.getQuestMap().setActiveQuest(new MapNode<>(null, 0));
                        m.getQuestMap().removeQuest(quest.getName());
                        e.getClicker().sendMessage(TanoRPG.PX + "クエスト「" + quest.getName() + "」をクリアしました！");
                        e.getClicker().setCompassTarget(TanoRPG.getPlugin().getDataManager().getGuildLoc());
                        TanoRPG.getPlugin().getSidebarManager().updateSidebar(e.getClicker(), m);
                    });
                });
                return;
            }
            new QuestListMenu(e.getNPC(), e.getClicker()).getInv().open(e.getClicker());
            TanoRPG.playSound(e.getClicker(), Sound.ENTITY_SHULKER_OPEN, 3, 1);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Member member = TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId());
        if (member == null) return;

        if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) return;

        Location from = e.getFrom().clone();
        from.setYaw(0);
        from.setPitch(0);

        Location to = e.getTo().clone();
        to.setYaw(0);
        to.setPitch(0);

        if (!from.equals(to) && member.getQuestMap().isAction())
            e.setCancelled(true);
    }
}
