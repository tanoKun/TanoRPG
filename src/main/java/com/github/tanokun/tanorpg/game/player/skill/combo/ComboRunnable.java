package com.github.tanokun.tanorpg.game.player.skill.combo;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.skill.SkillManager;
import com.github.tanokun.tanorpg.game.player.status.Sidebar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class ComboRunnable extends BukkitRunnable {
    public final String combo;
    private Player player;

    private boolean stop = true;
    private boolean skill = false;
    private boolean bool = false;

    public ComboRunnable(String combo, Player player){
        this.combo = combo;
        this.player = player;
        this.runTaskTimer(TanoRPG.getPlugin(), 1, 40);
    }
    @Override
    public void run() {
        if (!bool) {
            if (ComboManager.getCombos(player.getUniqueId()).size() >= 3) {
                if (SkillManager.runPlayerSkill(GamePlayerManager.getPlayer(player.getUniqueId()),
                        new ArrayList<>(ComboManager.getCombos(player.getUniqueId())))){
                    skill = true;
                    for(ComboRunnable runnable : ComboManager.comboRunnable.get(player.getUniqueId())){
                        runnable.setStop();
                    }
                    ComboManager.comboRunnable.get(player.getUniqueId()).clear();
                    ComboManager.comboRunnable.put(player.getUniqueId(), ComboManager.comboRunnable.get(player.getUniqueId()));
                    Sidebar.updateSidebar(player);
                    cancel();
                    return;
                }
            }
            Sidebar.updateSidebar(player);
        } else {
            if (stop && ComboManager.comboRunnable.get(player.getUniqueId()).size() != 0) {
                ComboManager.comboRunnable.get(player.getUniqueId()).remove(0);
            }
            Sidebar.updateSidebar(player);
            if (!skill) {
                cancel();
            }
            return;
        }

        bool = true;
    }
    public void setStop(){
        stop = false;
        this.cancel();
    }

    public String getCombo() {
        return combo;
    }
}

