package com.github.tanokun.tanorpg.player.status.sidebar;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.player.status.StatusType;
import com.github.tanokun.tanorpg.util.scoreboard.FastBoard;
import org.bukkit.entity.Player;

import java.util.*;

public class SidebarManager {
    private HashMap<UUID, FastBoard> fastBoards = new HashMap<>();

    public void setupSidebar(Player p, Member m) {
        if (fastBoards.containsKey(p.getUniqueId())) return;

        FastBoard board = new FastBoard(p);

        board.updateTitle("§b---==[-| §a§lTanoRPG §b|-]==---");
        List<String> lines = new ArrayList<>(Arrays.asList(
                "§e§l・" + p.getName() + "'s status",
                "    §bName§7>> §b" + p.getName(),
                "    §bClass§7>> §b" + m.getSkillClass().NAME,
                "    §6Money§7>> §b" + m.getMoney() + " " + TanoRPG.MONEY,
                "",
                "    §dHP§7>> §b" + m.getHasHP() + "§d§l/§b" + m.getStatusMap().getPointAndStatus(StatusType.HP),
                "    §3MP§7>> §b" + m.getHasMP() + "§d§l/§b" + m.getStatusMap().getPointAndStatus(StatusType.MP),
                "    §eLv§7>> §b" + m.getHasLevel().getValue() + "§7 (" + m.getHasEXP() + "§e§l/§7" + m.getHasLevel().getMaxEXP() + "§7)",
                "    §aCombos§7>> §b"
        ));
        lines.add("§b-----==-----------==-----");
        board.updateLines(lines);

        fastBoards.put(p.getUniqueId(), board);
    }

    public void updateSidebar(Player p, Member m) {
        FastBoard board = fastBoards.get(p.getUniqueId());
        List<String> lines = new ArrayList<>(Arrays.asList(
                "§e§l・" + p.getName() + "'s status",
                "    §bName§7>> §b" + p.getName(),
                "    §bClass§7>> §b" + m.getSkillClass().NAME,
                "    §6Money§7>> §b" + m.getMoney() + " " + TanoRPG.MONEY,
                "",
                "    §dHP§7>> §b" + m.getHasHP() + "§d§l/§b" + m.getStatusMap().getPointAndStatus(StatusType.HP),
                "    §3MP§7>> §b" + m.getHasMP() + "§d§l/§b" + m.getStatusMap().getPointAndStatus(StatusType.MP),
                "    §eLv§7>> §b" + m.getHasLevel().getValue() + "§7 (" + m.getHasEXP() + "§e§l/§7" + m.getHasLevel().getMaxEXP() + "§7)",
                "    §aCombos§7>> §b"
        ));
        lines.add("§b-----==-----------==-----");
        board.updateLines(lines);
    }

    public void removeSidebar(Player p) {
        FastBoard fastBoard = fastBoards.remove(p.getUniqueId());
        if (fastBoard != null){
            fastBoard.delete();
        }
    }
}
