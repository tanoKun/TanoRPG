package com.github.tanokun.tanorpg.game.player.status;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayer;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.listener.EditComboEventListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;

public class Sidebar {
    public static void setupSidebar(Player p){
        new BukkitRunnable(){
            @Override
            public void run() {
                GamePlayer player = GamePlayerManager.getPlayer(p.getUniqueId());
                Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                Objective o = scoreboard.registerNewObjective(p.getName(), "dummy");
                o.setDisplaySlot(DisplaySlot.SIDEBAR);
                o.setDisplayName("§a-----==[ TanoRPG ]==-----");
                o.getScore("§b§lName: §b" + p.getName()).setScore(0);
                o.getScore("§b§lClass: §b" + player.getJob().getName()).setScore(-1);
                o.getScore( " ").setScore(-2);
                o.getScore("§e§lLv: §b" + player.getLEVEL() + "§7 (" + player.getHAS_EXP() + "§e§l/§7" + player.getMAX_EXP() + "§7)").setScore(-3);
                o.getScore("§d§lHP: §b" + player.getHAS_HP() + "§d§l/§b" + player.getMAX_HP()).setScore(-4);
                o.getScore("§3§lMP: §b" + player.getHAS_MP() + "§d§l/§b" + player.getMAX_MP()).setScore(-5);
                o.getScore("  ").setScore(-6);
                o.getScore("§6§lMoney: §b" + player.getMoney()).setScore(-7);
                p.setScoreboard(scoreboard);
            }
        }.runTaskLater(TanoRPG.getPlugin(), 1);
    }
    public static void updateSidebar(Player p){
        new BukkitRunnable() {
            @Override
            public void run() {
                GamePlayer player = GamePlayerManager.getPlayer(p.getUniqueId());
                Scoreboard scoreboard = p.getScoreboard();
                Objective o = scoreboard.getObjective(p.getName());
                o.unregister();
                o = scoreboard.registerNewObjective(p.getName(), "dummy");
                o.setDisplaySlot(DisplaySlot.SIDEBAR);
                o.setDisplayName("§a-----==[ TanoRPG ]==-----");
                o.getScore("§b§lName: §b" + p.getName()).setScore(0);
                o.getScore("§b§lClass: §b" + player.getJob().getName()).setScore(-1);
                o.getScore(" ").setScore(-2);
                o.getScore("§e§lLv: §b" + player.getLEVEL() + "§7 (" + player.getHAS_EXP() + "§e§l/§7" + player.getMAX_EXP() + "§7)").setScore(-3);
                o.getScore("§d§lHP: §b" + player.getHAS_HP() + "§d§l/§b" + player.getMAX_HP()).setScore(-4);
                o.getScore("§3§lMP: §b" + player.getHAS_MP() + "§d§l/§b" + player.getMAX_MP()).setScore(-5);
                o.getScore("  ").setScore(-6);
                o.getScore("§6§lMoney: §b" + player.getMoney()).setScore(-7);
                if (EditComboEventListener.getCombos(p).size() > 0) {
                    o.getScore("   ").setScore(-8);
                    o.getScore("§a§lCB: §b" + EditComboEventListener.getCombos(p)).setScore(-9);
                }
                p.setScoreboard(scoreboard);
            }
        }.runTaskLater(TanoRPG.getPlugin(), 1);
    }
}
