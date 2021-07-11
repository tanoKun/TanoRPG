package com.github.tanokun.tanorpg.player.status.sidebar;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.player.quest.task.TaskData;
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
        if (m.getQuestMap().getActiveQuest() == null) {
            quest.add("  ");
            quest.add("§e§l・Quest: §c受注していません");
            quest.add("§b-----==-----------==-----");
        } else {
            quest.add("  ");
            quest.add("§e§l・Quest: §d「" + m.getQuestMap().getActiveQuest().getName() + "」");
            for (TaskData task : m.getQuestMap().getActiveQuest().getTasks()) {
                quest.add("    " + task.getTask().getMessage(task.getTask().isClearTask(task.getValue()), task));
            }
            quest.add("§b-----==-----------==-----");
        }

        board.updateTitle("§b---==[-| §a§lTanoRPG §b|-]==---");
        List<String> lines = new ArrayList<>(Arrays.asList(
                "§e§l・" + p.getName() + "'s status",
                "    §bName§7>> §b" + p.getName(),
                "    §bClass§7>> §b" + m.getSkillClass().NAME,
                "    §6Money§7>> §b" + m.getMoney() + " " + TanoRPG.MONEY,
                "",
                "    §dHP§7>> §b" + m.getHasHP() + "§d§l/§b" + m.getStatusMap().getPointAndStatus(StatusType.HP),
                "    §3MP§7>> §b" + m.getHasMP() + "§d§l/§b" + m.getStatusMap().getPointAndStatus(StatusType.MP),
                "    §eLv§7>> §b" + m.getHasLevel().getValue() + "§7 (" + m.getHasEXP() + "§e§l/§7" + m.getHasLevel().getMaxEXP() + "§7)"
        ));
        lines.addAll(quest);
        board.updateLines(lines);

        fastBoards.put(p.getUniqueId(), board);
    }

    public void updateSidebar(Player p, Member m) {
        FastBoard board = fastBoards.get(p.getUniqueId());

        ArrayList<String> quest = new ArrayList<>();
        if (m.getQuestMap().getActiveQuest() == null) {
            quest.add("  ");
            quest.add("§e§l・Quest: §c受注していません");
            quest.add("§b-----==-----------==-----");
        } else {
            quest.add("  ");
            quest.add("§e§l・Quest: §d「" + m.getQuestMap().getActiveQuest().getName() + "」");
            for (TaskData task : m.getQuestMap().getActiveQuest().getTasks()) {
                quest.add("    " + task.getTask().getMessage(task.getTask().isClearTask(task.getValue()), task));
            }
            quest.add("§b-----==-----------==-----");
        }

        List<String> lines = new ArrayList<>(Arrays.asList(
                "§e§l・" + p.getName() + "'s status",
                "    §bName§7>> §b" + p.getName(),
                "    §bClass§7>> §b" + m.getSkillClass().NAME,
                "    §6Money§7>> §b" + m.getMoney() + " " + TanoRPG.MONEY,
                "",
                "    §dHP§7>> §b" + m.getHasHP() + "§d§l/§b" + m.getStatusMap().getPointAndStatus(StatusType.HP),
                "    §3MP§7>> §b" + m.getHasMP() + "§d§l/§b" + m.getStatusMap().getPointAndStatus(StatusType.MP),
                "    §eLv§7>> §b" + m.getHasLevel().getValue() + "§7 (" + m.getHasEXP() + "§e§l/§7" + m.getHasLevel().getMaxEXP() + "§7)"
        ));
        lines.addAll(quest);
        board.updateLines(lines);
    }

    public void removeSidebar(Player p) {
        FastBoard fastBoard = fastBoards.remove(p.getUniqueId());
        if (fastBoard != null){
            fastBoard.delete();
        }
    }
}
