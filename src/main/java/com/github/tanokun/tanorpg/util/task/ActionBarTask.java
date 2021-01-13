package com.github.tanokun.tanorpg.util.task;

import com.github.tanokun.tanorpg.game.player.GameActionbar;
import com.github.tanokun.tanorpg.game.player.GamePlayer;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.listener.EditComboEventListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ConcurrentModificationException;

public class ActionBarTask extends BukkitRunnable {
    private GameActionbar actionbar;

    @Override
    public void run() {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (GamePlayerManager.getPlayer(player.getUniqueId()) != null) {
                    GamePlayer p = GamePlayerManager.getPlayer(player.getUniqueId());
                    if (EditComboEventListener.getCombos(player).size() > 0) {
                        actionbar = new GameActionbar(
                                "§6§l<<Money: §b" + p.getMoney() + "§6§l>> " +
                                        "§d§l<<HP: §b" + p.getHAS_HP() + "§d§l/§b" + p.getMAX_HP() + "§d§l>> " +
                                        "§e§l<<Lv: §b" + p.getLEVEL() + "§7 (" + p.getHAS_EXP() + "§e§l/§7" + p.getMAX_EXP() + "§e§l>> " +
                                        "§3§l<<MP: §b" + p.getHAS_MP() + "§d§l/§b" + p.getMAX_MP() + "§3§l>> " +
                                        "§a§l<<CB: §b" + EditComboEventListener.getCombos(player) + "§a§l>>"
                        );
                    } else {
                        actionbar = new GameActionbar(
                                "§6§l<<Money: §b" + p.getMoney() + "§6§l>> " +
                                        "§d§l<<HP: §b" + p.getHAS_HP() + "§d§l/§b" + p.getMAX_HP() + "§d§l>> " +
                                        "§e§l<<Lv: §b" + p.getLEVEL() + "§7 (" + p.getHAS_EXP() + "§e§l/§7" + p.getMAX_EXP() + "§e§l>> " +
                                        "§3§l<<MP: §b" + p.getHAS_MP() + "§d§l/§b" + p.getMAX_MP() + "§3§l>> "
                        );
                    }
                    actionbar.showActionBar(player);
                }
            }
        }catch (ConcurrentModificationException e){return;}
    }
}