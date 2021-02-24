package com.github.tanokun.tanorpg.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.skill.SkillManager;
import com.github.tanokun.tanorpg.game.player.status.Sidebar;
import com.google.common.base.Equivalence;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedMainHandEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EditComboEventListener implements Listener {
    private final static String COMBO = "COMBO";
    public static HashMap<UUID, List<String>> combos = new HashMap<>();
    @EventHandler
    public void onClick(PlayerInteractEvent e){
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK){
            if (e.getPlayer().hasMetadata("drop")) return;
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
        e.getPlayer().setMetadata("drop", new FixedMetadataValue(TanoRPG.getPlugin(), true));
        if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) e.setCancelled(true);
        if (e.getPlayer().isSneaking()){
            addCombo(e.getPlayer(), "SDR");
            new BukkitRunnable(){
                @Override
                public void run() {
                    try {Thread.sleep(1);} catch (InterruptedException interruptedException) {interruptedException.printStackTrace();}
                    e.getPlayer().removeMetadata("drop", TanoRPG.getPlugin());
                }
            }.runTaskAsynchronously(TanoRPG.getPlugin());
            return;
        }
        addCombo(e.getPlayer(), "DR");
        new BukkitRunnable(){
            @Override
            public void run() {
                try {Thread.sleep(1);} catch (InterruptedException interruptedException) {interruptedException.printStackTrace();}
                e.getPlayer().removeMetadata("drop", TanoRPG.getPlugin());
            }
        }.runTaskAsynchronously(TanoRPG.getPlugin());
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
        try{
            return (List<String>) m.value();
        }catch (Exception e){return new ArrayList<>();}
    }
    private void addCombo(Player player, String combo){
        UUID uuid = player.getUniqueId();
        List<String> playerCombo = combos.get(uuid);
        if (playerCombo.size() >= 3) {return;}
        new BukkitRunnable(){
            boolean bool = false;
            boolean skill;
            @Override
            public void run() {
                if (!bool){
                    playerCombo.add(combo);
                    if (playerCombo.size() >= 3) {
                        if (SkillManager.runPlayerSkill(GamePlayerManager.getPlayer(uuid), playerCombo))
                            skill = true;
                    }
                    Sidebar.updateSidebar(player);
                } else {
                    if (skill) {cancel();return;}
                    if (!(playerCombo.size() == 0)) {
                        playerCombo.remove(0);
                    }
                    Sidebar.updateSidebar(player);
                    cancel();

                }
                bool = true;
            }
        }.runTaskTimer(TanoRPG.getPlugin(), 1, 40);
    }
}
