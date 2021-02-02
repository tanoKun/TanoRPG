package com.github.tanokun.tanorpg.game.player.status;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayer;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.listener.EditComboEventListener;
import com.github.tanokun.tanorpg.util.scoreboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Sidebar {
    public final static HashMap<UUID, FastBoard> boards = new HashMap<>();
    public static void setupSidebar(Player p){
        if (boards.containsKey(p.getUniqueId())) return;
        GamePlayer gamePlayer = GamePlayerManager.getPlayer(p.getUniqueId());
        FastBoard board = new FastBoard(p);
        board.updateTitle("§b---==[-| §a§lTanoRPG §b|-]==---");
        board.updateLines(
                "§e§l・" + p.getName() + "'s status",
                "    §b§lName§7>> §b" + p.getName(),
                "    §b§lClass§7>> §b" + gamePlayer.getJob().getName(),
                "    §6§lMoney§7>> §b" + gamePlayer.getMoney(),
                "",
                "    §d§lHP§7>> §b" + gamePlayer.getHAS_HP() + "§d§l/§b" + gamePlayer.getMAX_HP(),
                "    §3§lMP§7>> §b" + gamePlayer.getHAS_MP() + "§d§l/§b" + gamePlayer.getMAX_MP(),
                "    §e§lLv§7>> §b" + gamePlayer.getLEVEL() + "§7 (" + gamePlayer.getHAS_EXP() + "§e§l/§7" + gamePlayer.getMAX_EXP() + "§7)",
                "    §a§lCombos§7>> §b",
                "  ",
                "§e§l・Server info",
                "    §bPlayers§7>> §b" + Bukkit.getOnlinePlayers().size() + "§d§l/§b" + Bukkit.getMaxPlayers(),
                "    §aIP§7>> §b" + TanoRPG.IP,
                "§b-----==-----------==-----"
        );
        if (EditComboEventListener.getCombos(p).size() > 0) {
            board.updateLine(8, "    §a§lCombos§7>> §b" + EditComboEventListener.getCombos(p));
        }
        boards.put(p.getUniqueId(), board);
    }
    public static void updateSidebar(Player p){
        FastBoard board = boards.get(p.getUniqueId());
        GamePlayer gamePlayer = GamePlayerManager.getPlayer(p.getUniqueId());

        board.updateLine(1,"    §b§lName§7>> §b" + p.getName());
        board.updateLine(2,"    §b§lClass§7>> §b" + gamePlayer.getJob().getName());
        board.updateLine(3,"    §6§lMoney§7>> §b" + gamePlayer.getMoney());
        board.updateLine(5,"    §d§lHP§7>> §b" + gamePlayer.getHAS_HP() + "§d§l/§b" + gamePlayer.getMAX_HP());
        board.updateLine(6,"    §3§lMP§7>> §b" + gamePlayer.getHAS_MP() + "§d§l/§b" + gamePlayer.getMAX_MP());
        board.updateLine(7,"    §e§lLv: §b" + gamePlayer.getLEVEL() + "§7 (" + gamePlayer.getHAS_EXP() + "§e§l/§7" + gamePlayer.getMAX_EXP() + "§7)");
        board.updateLine(11, "    §bPlayers§7>> §b" + Bukkit.getOnlinePlayers().size() + "§d§l/§b" + Bukkit.getMaxPlayers());
        if (EditComboEventListener.getCombos(p).size() > 0) {
            board.updateLine(8, "    §a§lCombos§7>> §b" + EditComboEventListener.getCombos(p));
        } else {
            board.updateLine(8, "    §a§lCombos§7>> §b");
        }
    }
}
