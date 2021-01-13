package com.github.tanokun.tanorpg.listener;

import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitEventListener implements Listener {
    private String quit = "§a[§cQuit§a] §a";
    @EventHandler
    public void onJoin(PlayerQuitEvent e){
        Player player = e.getPlayer();
        e.setQuitMessage(quit + player.getName() + "がQuitしました！");
        GamePlayerManager.saveData(player.getUniqueId());
        GamePlayerManager.removeData(player.getUniqueId());
    }
}