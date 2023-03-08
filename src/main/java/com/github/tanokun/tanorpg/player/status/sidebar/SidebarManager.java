package com.github.tanokun.tanorpg.player.status.sidebar;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.player.quests.data.task.TaskPlayerData;
import com.github.tanokun.tanorpg.player.status.StatusType;
import com.github.tanokun.tanorpg.util.scoreboard.FastBoard;
import org.bukkit.entity.Player;

import java.util.*;

public class SidebarManager {
    private HashMap<UUID, FastBoard> fastBoards = new HashMap<>();

    public void setupSidebar(Player p, Member m) {
        if (fastBoards.containsKey(p.getUniqueId())) return;
        FastBoard board = new FastBoard(p);

        ArrayList<String> quest = new ArrayList<>();
        if (m.getQuestMap().getActiveQuest().equals("")) {
            quest.add("  ");
            quest.add("§e§l・Quest: §c受注していません");
            quest.add("§b-----==-----------==-----");
        } else {
            quest.add("  ");
            quest.add("§e§l・Quest: §d「" + m.getQuestMap().getQuests().get(m.getQuestMap().getActiveQuest()).getQuestModel().getName() + "」");
            for (TaskPlayerData<?> task : m.getQuestMap().getQuests().get(m.getQuestMap().getActiveQuest()).getIndexTaskData()) {
                quest.add("    " + task.getTask().getMessage(task));
            }
            quest.add("§b-----==-----------==-----");
        }

        board.updateTitle("§b---==[-| §a§lTanoRPG §b|-]==---");
        List<String> lines = new ArrayList<>(Arrays.asList(
                "§e§l・" + p.getName() + "'s status",
                "    §bName§7>> §b" + p.getName(),
                "    §bClass§7>> §b" + m.getSkillClass().getClassName(),
                "    §6Money§7>> §b" + m.getMoney() + " " + TanoRPG.MONEY,
                "",
                "    §dHP§7>> §b" + m.getHasHP() + "§d§l/§b" + m.getStatusMap().getPointAndStatus(StatusType.HP),
                "    §3MP§7>> §b" + m.getHasMP() + "§d§l/§b" + m.getStatusMap().getPointAndStatus(StatusType.MP),
                "    §eLv§7>> §b" + m.getLevel().getValue() + "§7 (" + m.getHasEXP() + "§e§l/§7" + m.getLevel().getMaxEXP() + "§7)"
        ));
        lines.addAll(quest);
        board.updateLines(lines);

        fastBoards.put(p.getUniqueId(), board);
    }

    public void updateSidebar(Player p, Member m) {
        if (!p.isOnline()) return;
        FastBoard board = fastBoards.get(p.getUniqueId());


        ArrayList<String> quest = new ArrayList<>();
        if (m.getQuestMap().getActiveQuest().equals("")) {
            quest.add("  ");
            quest.add("§e§l・Quest: §c受注していません");
            quest.add("§b-----==-----------==-----");
        } else {
            quest.add("  ");
            quest.add("§e§l・Quest: §d「" + m.getQuestMap().getQuests().get(m.getQuestMap().getActiveQuest()).getQuestModel().getName() + "」");
            for (TaskPlayerData<?> task : m.getQuestMap().getQuests().get(m.getQuestMap().getActiveQuest()).getIndexTaskData()) {
                quest.add("    " + task.getTask().getMessage(task));
            }
            quest.add("§b-----==-----------==-----");
        }

        List<String> lines = new ArrayList<>(Arrays.asList(
                "§e§l・" + p.getName() + "'s status",
                "    §bName§7>> §b" + p.getName(),
                "    §bClass§7>> §b" + m.getSkillClass().getClassName(),
                "    §6Money§7>> §b" + m.getMoney() + " " + TanoRPG.MONEY,
                "",
                "    §dHP§7>> §b" + m.getHasHP() + "§d§l/§b" + m.getStatusMap().getPointAndStatus(StatusType.HP),
                "    §3MP§7>> §b" + m.getHasMP() + "§d§l/§b" + m.getStatusMap().getPointAndStatus(StatusType.MP),
                "    §eLv§7>> §b" + m.getLevel().getValue() + "§7 (" + m.getHasEXP() + "§e§l/§7" + m.getLevel().getMaxEXP() + "§7)"
        ));
        lines.addAll(quest);
        board.updateLines(lines);
    }

    public void removeSidebar(Player p) {
        FastBoard fastBoard = fastBoards.remove(p.getUniqueId());
        fastBoard.delete();
    }
}
