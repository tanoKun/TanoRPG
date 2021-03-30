package com.github.tanokun.tanorpg.util.task;

import com.github.tanokun.tanorpg.game.player.GamePlayer;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;

public class PlayerRegenerationTask extends BukkitRunnable {
    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            boolean bool = false;
            GamePlayer game = GamePlayerManager.getPlayer(p.getUniqueId());
            if (game == null) continue;
            int status_vit = new BigDecimal("" + game.getMAX_HP() / 20).setScale(2,BigDecimal.ROUND_DOWN).intValue();
            int status_int = new BigDecimal("" + game.getMAX_MP() / 20).setScale(2,BigDecimal.ROUND_DOWN).intValue();

            if (!(game.getHAS_HP() >= game.getMAX_HP())){
                game.setHAS_HP(game.getHAS_HP() + status_vit);
            }

            if (!(game.getHAS_MP() >= game.getMAX_MP())){
                game.setHAS_MP(game.getHAS_MP() + status_int);
            }
            if (game.getHAS_MP() > game.getMAX_MP()) game.setHAS_MP(game.getMAX_MP());
            if (game.getHAS_HP() > game.getMAX_HP()) game.setHAS_HP(game.getMAX_HP());
        }
    }
}
