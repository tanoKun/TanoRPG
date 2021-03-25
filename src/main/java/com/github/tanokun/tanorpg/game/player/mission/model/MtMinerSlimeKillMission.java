package com.github.tanokun.tanorpg.game.player.mission.model;

import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.game.player.mission.Mission;
import com.github.tanokun.tanorpg.game.player.mission.task.EntityKillTask;
import org.bukkit.entity.Player;

public class MtMinerSlimeKillMission extends Mission {
    private final String PX = "§a§l鉱夫>> §f";

    public MtMinerSlimeKillMission() {
        super("鉱石が欲しくばお願いを聞け", 210);
    }

    public void showNPCMessages(Player player) throws Exception {

    }

    public void startMission(Player player) throws Exception {

    }

    public void finishMission(Player player) throws Exception {

    }

    public void cancelMission(Player player) throws Exception {

    }
}
