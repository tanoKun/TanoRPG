package com.github.tanokun.tanorpg.player.quest.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.event.tanorpg.TanoRpgCraftEvent;
import com.github.tanokun.tanorpg.event.tanorpg.TanoRpgEntityKillEvent;
import com.github.tanokun.tanorpg.event.tanorpg.TanoRpgLevelUpEvent;
import com.github.tanokun.tanorpg.event.tanorpg.TanoRpgShopEvent;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.player.quest.QuestData;
import com.github.tanokun.tanorpg.player.quest.task.QuestTalkToNpcTask;
import com.github.tanokun.tanorpg.player.quest.task.TaskData;
import com.github.tanokun.tanorpg.player.quest.task.TaskType;
import com.github.tanokun.tanorpg.util.io.MapNode;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Bukkit;
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

        TanoRPG.getPlugin().getSidebarManager().updateSidebar(e.getPlayer(), member);

        if (questData.isClearTasks()) return;
        if (questData.isClear()) {
            questData.setClearTasks(true);
            TanoRPG.playSound(e.getPlayer(), Sound.ENTITY_PLAYER_LEVELUP, 3, 0.6);
            e.getPlayer().sendMessage(TanoRPG.PX + "クエスト「" + questData.getName() + "」の達成条件を達しました！ 話しかけに行きましょう！");
            e.getPlayer().setCompassTarget(CitizensAPI.getNPCRegistry().getById(questData.getNpcId()).getEntity().getLocation());
        }
    }

    @EventHandler
    public void onLevelUp(TanoRpgLevelUpEvent e) {
        Member member = TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId());
        QuestData questData = member.getQuestMap().getActiveQuest();
        if (questData == null) return;
        questData.getTasks().stream().filter(taskData -> taskData.getTask().getTaskType().equals(TaskType.LEVEL_UP)).forEach(taskData -> {
            taskData.setValue((Integer.valueOf(String.valueOf(member.getHasLevel().getValue()))));
        });

        TanoRPG.getPlugin().getSidebarManager().updateSidebar(e.getPlayer(), member);

        if (questData.isClearTasks()) return;
        if (questData.isClear()) {
            questData.setClearTasks(true);
            TanoRPG.playSound(e.getPlayer(), Sound.ENTITY_PLAYER_LEVELUP, 3, 0.6);
            e.getPlayer().sendMessage(TanoRPG.PX + "クエスト「" + questData.getName() + "」の達成条件を達しました！ 話しかけに行きましょう！");
            e.getPlayer().setCompassTarget(CitizensAPI.getNPCRegistry().getById(questData.getNpcId()).getEntity().getLocation());
        }
    }

    @EventHandler
    public void onBuyShop(TanoRpgShopEvent e) {
        Member member = TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId());
        QuestData questData = member.getQuestMap().getActiveQuest();
        if (questData == null) return;
        questData.getTasks().stream().filter(taskData -> taskData.getTask().getTaskType().equals(TaskType.SHOP)).forEach(taskData -> {
            taskData.setValue((Integer.valueOf(String.valueOf(taskData.getValue())) + 1));
        });

        TanoRPG.getPlugin().getSidebarManager().updateSidebar(e.getPlayer(), member);


        if (questData.isClearTasks()) return;
        if (questData.isClear()) {
            questData.setClearTasks(true);
            TanoRPG.playSound(e.getPlayer(), Sound.ENTITY_PLAYER_LEVELUP, 3, 0.6);
            e.getPlayer().sendMessage(TanoRPG.PX + "クエスト「" + questData.getName() + "」の達成条件を達しました！ 話しかけに行きましょう！");
            e.getPlayer().setCompassTarget(CitizensAPI.getNPCRegistry().getById(questData.getNpcId()).getEntity().getLocation());
        }
    }

    @EventHandler
    public void onCraft(TanoRpgCraftEvent e) {
        Member member = TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId());
        QuestData questData = member.getQuestMap().getActiveQuest();
        if (questData == null) return;
        questData.getTasks().stream().filter(taskData -> taskData.getTask().getTaskType().equals(TaskType.CRAFT)).forEach(taskData -> {
            taskData.setValue((Integer.valueOf(String.valueOf(taskData.getValue())) + 1));
        });

        TanoRPG.getPlugin().getSidebarManager().updateSidebar(e.getPlayer(), member);

        if (questData.isClearTasks()) return;
        if (questData.isClear()) {
            questData.setClearTasks(true);
            TanoRPG.playSound(e.getPlayer(), Sound.ENTITY_PLAYER_LEVELUP, 3, 0.6);
            e.getPlayer().sendMessage(TanoRPG.PX + "クエスト「" + questData.getName() + "」の達成条件を達しました！ 話しかけに行きましょう！");
            e.getPlayer().setCompassTarget(CitizensAPI.getNPCRegistry().getById(questData.getNpcId()).getEntity().getLocation());
        }
    }

    @EventHandler
    public void onTalkToNpc(NPCRightClickEvent e) {
        Member member = TanoRPG.getPlugin().getMemberManager().getMember(e.getClicker().getUniqueId());
        QuestData questData = member.getQuestMap().getActiveQuest();
        if (questData == null) return;
        questData.getTasks().stream().filter(taskData -> taskData.getTask().getTaskType().equals(TaskType.TALK_TO_NPC)).forEach(taskData -> {
            QuestTalkToNpcTask data = (QuestTalkToNpcTask) taskData.getTask();

            if (questData.isClearTasks() || data.getNpcId() != e.getNPC().getId()) return;

            Bukkit.getScheduler().runTaskAsynchronously(TanoRPG.getPlugin(), () -> {
                data.getActions()
                        .forEach(a -> a.execute(member));
                Bukkit.getScheduler().runTask(TanoRPG.getPlugin(), () -> {
                    taskData.setValue(true);

                    TanoRPG.getPlugin().getSidebarManager().updateSidebar(e.getClicker(), member);

                    if (questData.isClearTasks()) return;
                    if (questData.isClear()) {
                        questData.setClearTasks(true);
                        TanoRPG.playSound(e.getClicker(), Sound.ENTITY_PLAYER_LEVELUP, 3, 0.6);
                        e.getClicker().sendMessage(TanoRPG.PX + "クエスト「" + questData.getName() + "」の達成条件を達しました！ 話しかけに行きましょう！");
                        e.getClicker().setCompassTarget(CitizensAPI.getNPCRegistry().getById(questData.getNpcId()).getEntity().getLocation());
                    }
                });
            });
        });
    }
}
