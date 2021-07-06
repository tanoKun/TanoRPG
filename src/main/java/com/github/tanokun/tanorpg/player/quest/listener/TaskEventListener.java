package com.github.tanokun.tanorpg.player.quest.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.event.tanorpg.TanoRpgEntityKillEvent;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.player.quest.Quest;
import com.github.tanokun.tanorpg.player.quest.QuestData;
import com.github.tanokun.tanorpg.player.quest.inv.QuestListMenu;
import com.github.tanokun.tanorpg.player.quest.task.TaskType;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TaskEventListener implements Listener {

    @EventHandler
    public void onEntityKill(TanoRpgEntityKillEvent e) {
        Member member = TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId());
        QuestData questData = member.getQuestMap().getActiveQuest();
        if (questData == null) return;
        questData.getTasks().stream().filter(taskData -> taskData.getTask().getTaskType().equals(TaskType.ENTITY_KILL)).forEach(taskData -> {
            taskData.setValue((Integer.valueOf(String.valueOf(taskData.getValue())) + 1));
        });


        if (questData.isClearTasks()) return;
        if (questData.isClear()) {
            questData.setClearTasks(true);
            TanoRPG.playSound(e.getPlayer(), Sound.ENTITY_PLAYER_LEVELUP, 3, 0.6);
            e.getPlayer().sendMessage(TanoRPG.PX + "クエスト「" + questData.getName() + "」の達成条件を達しました！ 話しかけに行きましょう！");
            e.getPlayer().setCompassTarget(CitizensAPI.getNPCRegistry().getById(questData.getNpcId()).getEntity().getLocation());
        }

    }
}
