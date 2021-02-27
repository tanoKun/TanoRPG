package com.github.tanokun.tanorpg.listener;

import com.github.tanokun.tanorpg.game.player.GamePlayer;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.skill.SkillManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.List;

public class SkillShortCutListener implements Listener {
    @EventHandler
    public void onDeath(PlayerSwapHandItemsEvent e){
        GamePlayer player = GamePlayerManager.getPlayer(e.getPlayer().getUniqueId());
        if (player.getSkill_F() != null || player.getSkill_Shift_F() != null){
            e.setCancelled(true);
            if (!e.getPlayer().isSneaking()){
                List<String> combo = SkillManager.getSkillName(player.getSkill_F()).getCombo();
                SkillManager.runPlayerSkill(player, combo);
            } else {
                if (player.getSkill_Shift_F() != null);
                List<String> combo = SkillManager.getSkillName(player.getSkill_Shift_F()).getCombo();
                SkillManager.runPlayerSkill(player, combo);
            }
        }
    }
}
