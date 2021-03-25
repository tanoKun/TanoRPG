package com.github.tanokun.tanorpg.game.player.status;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayer;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.mission.Mission;
import com.github.tanokun.tanorpg.game.player.mission.MissionManager;
import com.github.tanokun.tanorpg.game.player.mission.task.MissionTask;
import com.github.tanokun.tanorpg.game.player.skill.combo.ComboManager;
import com.github.tanokun.tanorpg.util.scoreboard.FastBoard;
import org.bukkit.entity.Player;

import java.util.*;

public class Sidebar {
    public final static HashMap<UUID, FastBoard> boards = new HashMap<>();
    public static void setupSidebar(Player p){
        if (GamePlayerManager.getPlayer(p.getUniqueId()) == null) return;
        if (boards.containsKey(p.getUniqueId())) return;
        GamePlayer gamePlayer = GamePlayerManager.getPlayer(p.getUniqueId());

        Mission mission = MissionManager.getMission(gamePlayer.getActive_mission_NPC_ID(),
                gamePlayer.getActive_mission_NPC_Name());

        if (mission == null){
            gamePlayer.setActive_mission_NPC_ID(-1);
        }else {
            if (!MissionManager.isActive(p.getUniqueId(), mission)) {
                gamePlayer.setActive_mission_NPC_ID(-1);
            }
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
                    "§e§l・Mission: §d「" + MissionManager.getMission(gamePlayer.getActive_mission_NPC_ID(), gamePlayer.getActive_mission_NPC_Name()).getMissionName() + "」"
            ));
            mission = MissionManager.getMission(gamePlayer.getActive_mission_NPC_ID(), gamePlayer.getActive_mission_NPC_Name());
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
        if (GamePlayerManager.getPlayer(p.getUniqueId()) == null) return;
        GamePlayer gamePlayer = GamePlayerManager.getPlayer(p.getUniqueId());

        Mission mission = MissionManager.getMission(gamePlayer.getActive_mission_NPC_ID(),
                gamePlayer.getActive_mission_NPC_Name());

        if (mission == null){
            gamePlayer.setActive_mission_NPC_ID(-1);
        }else {
            if (MissionManager.isClear(p.getUniqueId(), mission)) {
                gamePlayer.setActive_mission_NPC_ID(-1);
            }
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
                    "§e§l・Mission: §d「" + MissionManager.getMission(gamePlayer.getActive_mission_NPC_ID(),
                            gamePlayer.getActive_mission_NPC_Name()).getMissionName() + "」"
            ));
            mission = MissionManager.getMission(gamePlayer.getActive_mission_NPC_ID(),
                    gamePlayer.getActive_mission_NPC_Name());
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
