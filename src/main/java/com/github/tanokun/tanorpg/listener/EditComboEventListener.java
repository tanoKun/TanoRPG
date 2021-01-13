package com.github.tanokun.tanorpg.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.skill.SkillManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.ArrayList;
import java.util.List;

public class EditComboEventListener implements Listener {
    private final static String COMBO = "COMBO";
    @EventHandler
    public void onClick(PlayerInteractEvent e){
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK){
            if (e.getPlayer().isSneaking()){
                addCombo(e.getPlayer(), "SLC");
                return;
            }
            addCombo(e.getPlayer(), "LC");
            return;
        }
        if (e.getAction() == Action.RIGHT_CLICK_AIR){
            if (e.getPlayer().isSneaking()){
                addCombo(e.getPlayer(), "SRC");
                return;
            }
            addCombo(e.getPlayer(), "RC");
            return;
        }
    }
    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) e.setCancelled(true);
        if (e.getPlayer().isSneaking()){
            addCombo(e.getPlayer(), "SDR");
            return;
        }
        addCombo(e.getPlayer(), "DR");
    }
    public static List<String> getCombos(Player p){
        MetadataValue m = null;
        List<String> combos = new ArrayList<>();
        if (p.getPlayer().hasMetadata(COMBO)) {
            for (MetadataValue v : p.getPlayer().getMetadata(COMBO)) {
                if (v.getOwningPlugin().getName().equals(TanoRPG.getPlugin().getName())) {
                    m = v;
                    break;
                }
            }
        } else {
            return new ArrayList<>();
        }
        return (List<String>) m.value();
    }
    private void addCombo(Player player, String combo){
        Bukkit.getScheduler().runTaskAsynchronously(TanoRPG.getPlugin(), () -> {
            List<String> combos = getCombos(player);
            if (combos.size() >= 3) return;
            combos.add(combo);
            player.setMetadata(COMBO, new FixedMetadataValue(TanoRPG.getPlugin(), combos));
            boolean skill = false;
            if (combos.size() >= 3) {
                if (SkillManager.runPlayerSkill(GamePlayerManager.getPlayer(player.getUniqueId()), new ArrayList<>(combos))) skill = true;
            }
            for (int i = 0; i < 40; i++) {
                if (skill == true) {
                    try {Thread.sleep(150);} catch (InterruptedException interruptedException) {interruptedException.printStackTrace();}
                    return;
                }
                try {Thread.sleep(50);} catch (InterruptedException interruptedException) {interruptedException.printStackTrace();}
            }
            combos = getCombos(player);
            if (combos.size() > 0) {
                combos.remove(0);
            }
            if (combos.size() <= 0){player.removeMetadata(COMBO, TanoRPG.getPlugin()); return;}
            player.setMetadata(COMBO, new FixedMetadataValue(TanoRPG.getPlugin(), combos));
        });
    }
}
