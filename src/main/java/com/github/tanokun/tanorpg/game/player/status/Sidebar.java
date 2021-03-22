package com.github.tanokun.tanorpg.game.player.status;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayer;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.mission.Mission;
import com.github.tanokun.tanorpg.game.player.mission.MissionManager;
import com.github.tanokun.tanorpg.game.player.mission.task.MissionTask;
import com.github.tanokun.tanorpg.game.player.skill.combo.ComboManager;
import com.github.tanokun.tanorpg.util.scoreboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class Sidebar {
    public final static HashMap<UUID, FastBoard> boards = new HashMap<>();
    public static void setupSidebar(Player p){
        if (GamePlayerManager.getPlayer(p.getUniqueId()) == null) return;
        if (boards.containsKey(p.getUniqueId())) return;
        GamePlayer gamePlayer = GamePlayerManager.getPlayer(p.getUniqueId());

        if (!MissionManager.isActive(p.getUniqueId(), MissionManager.getMission(gamePlayer.getActive_mission_NPC_ID()))){
            gamePlayer.setActive_mission_NPC_ID(-1);
        }

        FastBoard board = new FastBoard(p);
        board.updateTitle("§b---==[-| §a§lTanoRPG §b|-]==---");
        if (gamePlayer.getActive_mission_NPC_ID() == -1) {
            board.updateLines(
                    "§e§l・" + p.getName() + "'s status",
                    "    §bName§7>> §b" + p.getName(),
                    "    §bClass§7>> §b" + gamePlayer.getJob().getName(),
                    "    §6Money§7>> §b" + gamePlayer.getMoney() + " " + TanoRPG.MONEY,
                    "",
                    "    §dHP§7>> §b" + gamePlayer.getHAS_HP() + "§d§l/§b" + gamePlayer.getMAX_HP(),
                    "    §3MP§7>> §b" + gamePlayer.getHAS_MP() + "§d§l/§b" + gamePlayer.getMAX_MP(),
                    "    §eLv§7>> §b" + gamePlayer.getLEVEL() + "§7 (" + gamePlayer.getHAS_EXP() + "§e§l/§7" + gamePlayer.getMAX_EXP() + "§7)",
                    "    §aCombos§7>> §b",
                    "  ",
                    "§e§l・Mission: §c受けていません",
                    "§b-----==-----------==-----"
            );
        } else {
            List<String> lines = new ArrayList<>(Arrays.asList(
                    "§e§l・" + p.getName() + "'s status",
                    "    §bName§7>> §b" + p.getName(),
                    "    §bClass§7>> §b" + gamePlayer.getJob().getName(),
                    "    §6Money§7>> §b" + gamePlayer.getMoney() + " " + TanoRPG.MONEY,
                    "",
                    "    §dHP§7>> §b" + gamePlayer.getHAS_HP() + "§d§l/§b" + gamePlayer.getMAX_HP(),
                    "    §3MP§7>> §b" + gamePlayer.getHAS_MP() + "§d§l/§b" + gamePlayer.getMAX_MP(),
                    "    §eLv§7>> §b" + gamePlayer.getLEVEL() + "§7 (" + gamePlayer.getHAS_EXP() + "§e§l/§7" + gamePlayer.getMAX_EXP() + "§7)",
                    "    §aCombos§7>> §b",
                    "  ",
                    "§e§l・Mission: §d「" + MissionManager.getMission(gamePlayer.getActive_mission_NPC_ID()).getMissionName() + "」"
            ));
            Mission mission = MissionManager.getMission(gamePlayer.getActive_mission_NPC_ID());
            for (MissionTask missionTask : mission.getMissionTasks()){
                lines.add("    " + missionTask.getMessage(p));
            }
            lines.add("§b-----==-----------==-----");
            board.updateLines(lines);
        }
        boards.put(p.getUniqueId(), board);
    }
    public static void updateSidebar(Player p){
        FastBoard board = boards.get(p.getUniqueId());
        GamePlayer gamePlayer = GamePlayerManager.getPlayer(p.getUniqueId());

        if (!MissionManager.isActive(p.getUniqueId(), MissionManager.getMission(gamePlayer.getActive_mission_NPC_ID()))){
            gamePlayer.setActive_mission_NPC_ID(-1);
        }

        if (gamePlayer.getActive_mission_NPC_ID() == -1) {
            board.updateLines(
                    "§e§l・" + p.getName() + "'s status",
                    "    §bName§7>> §b" + p.getName(),
                    "    §bClass§7>> §b" + gamePlayer.getJob().getName(),
                    "    §6Money§7>> §b" + gamePlayer.getMoney() + " " + TanoRPG.MONEY,
                    "",
                    "    §dHP§7>> §b" + gamePlayer.getHAS_HP() + "§d§l/§b" + gamePlayer.getMAX_HP(),
                    "    §3MP§7>> §b" + gamePlayer.getHAS_MP() + "§d§l/§b" + gamePlayer.getMAX_MP(),
                    "    §eLv§7>> §b" + gamePlayer.getLEVEL() + "§7 (" + gamePlayer.getHAS_EXP() + "§e§l/§7" + gamePlayer.getMAX_EXP() + "§7)",
                    "    §aCombos§7>> §b",
                    "  ",
                    "§e§l・Mission: §c受けていません",
                    "§b-----==-----------==-----"
            );
        } else {
            List<String> lines = new ArrayList<>(Arrays.asList(
                    "§e§l・" + p.getName() + "'s status",
                    "    §bName§7>> §b" + p.getName(),
                    "    §bClass§7>> §b" + gamePlayer.getJob().getName(),
                    "    §6Money§7>> §b" + gamePlayer.getMoney() + " " + TanoRPG.MONEY,
                    "",
                    "    §dHP§7>> §b" + gamePlayer.getHAS_HP() + "§d§l/§b" + gamePlayer.getMAX_HP(),
                    "    §3MP§7>> §b" + gamePlayer.getHAS_MP() + "§d§l/§b" + gamePlayer.getMAX_MP(),
                    "    §eLv§7>> §b" + gamePlayer.getLEVEL() + "§7 (" + gamePlayer.getHAS_EXP() + "§e§l/§7" + gamePlayer.getMAX_EXP() + "§7)",
                    "    §aCombos§7>> §b",
                    "  ",
                    "§e§l・Mission: §d「" + MissionManager.getMission(gamePlayer.getActive_mission_NPC_ID()).getMissionName() + "」"
            ));
            Mission mission = MissionManager.getMission(gamePlayer.getActive_mission_NPC_ID());
            for (MissionTask missionTask : mission.getMissionTasks()){
                lines.add("    " + missionTask.getMessage(p));
            }
            lines.add("§b-----==-----------==-----");
            board.updateLines(lines);
        }
        if (!(ComboManager.getCombos(p.getUniqueId()).size() == 0)) {
            board.updateLine(8, "    §a§lCombos§7>> §b" + ComboManager.getCombos(p.getUniqueId()));
        } else {
            board.updateLine(8, "    §a§lCombos§7>> §b");
        }
    }
}
