package com.github.tanokun.tanorpg.util.task;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.mission.MissionManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoSaveTask extends BukkitRunnable {
    @Override
    public void run() {
        Bukkit.broadcastMessage(TanoRPG.PX + "オートセーブ中...");
        GamePlayerManager.saveDataAll();
        MissionManager.saveDataAll();
        Bukkit.broadcastMessage(TanoRPG.PX + "オートセーブ完了");
        System.gc();
    }
}
