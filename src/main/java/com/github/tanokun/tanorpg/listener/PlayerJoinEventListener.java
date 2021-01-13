package com.github.tanokun.tanorpg.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.menu.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinEventListener implements Listener {
    private String join = "§a[§bJoin§a] §f";
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        new BukkitRunnable(){
            @Override
            public void run() {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {Thread.sleep(100);} catch (InterruptedException interruptedException) {interruptedException.printStackTrace();}
                        Player player = e.getPlayer();
                        player.setGameMode(GameMode.ADVENTURE);
                        if (GamePlayerManager.loadData(player.getUniqueId()) == null){
                            player.teleport(new Location(Bukkit.getWorld("world"), 729, 25, -73, 90 ,0));
                            player.getInventory().clear();
                            MenuManager.getMenu("§c§l職業選択 §7説明をよく読んで選択しよう！").openInv(player);
                            e.setJoinMessage(join + "§d" + player.getName() + "§dが初Joinしました！");
                        } else {
                            e.setJoinMessage(join + "§a" + player.getName() + "§aがJoinしました！");
                            GamePlayerManager.loadData(player.getUniqueId());
                        }
                    }
                }.runTask(TanoRPG.getPlugin());
            }
        }.runTaskAsynchronously(TanoRPG.getPlugin());
    }
}
