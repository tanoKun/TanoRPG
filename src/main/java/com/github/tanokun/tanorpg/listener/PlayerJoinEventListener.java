package com.github.tanokun.tanorpg.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.mission.MissionManager;
import com.github.tanokun.tanorpg.game.player.skill.combo.ComboManager;
import com.github.tanokun.tanorpg.game.player.status.Sidebar;
import com.github.tanokun.tanorpg.menu.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class PlayerJoinEventListener implements Listener {
    private String join = "§a[§bJoin§a] §f";
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        final Player player = e.getPlayer();
        e.setJoinMessage("");
        ComboManager.comboRunnable.put(e.getPlayer().getUniqueId(), new ArrayList<>());
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.getGameMode().equals(GameMode.CREATIVE)) player.setGameMode(GameMode.SURVIVAL);
                if (GamePlayerManager.loadData(player.getUniqueId()) == null){
                    player.teleport(new Location(Bukkit.getWorld("world"), -814, 49, 97, 0, 0));
                    Sidebar.setupSidebar(e.getPlayer());
                    player.getInventory().clear();
                    MenuManager.getMenu("§c§l職業選択 §7説明をよく読んで選択しよう！").openInv(player);
                    for (Player all : Bukkit.getOnlinePlayers()){
                        all.sendMessage(join + "§d" + player.getName() + "§dが初Joinしました！");
                    }
                    player.sendMessage(TanoRPG.PX + "左にいるロバートに話しかけてみよう。");
                    player.sendMessage(TanoRPG.PX + "必要な情報が得られるぞ！");
                } else {
                    GamePlayerManager.loadData(player.getUniqueId());
                    MissionManager.loadData(player.getUniqueId());
                    Sidebar.setupSidebar(e.getPlayer());
                    for (Player all : Bukkit.getOnlinePlayers()){
                        all.sendMessage(join + "§a" + player.getName() + "§aがJoinしました！");
                    }
                }
            }
        }.runTask(TanoRPG.getPlugin());
    }
}
