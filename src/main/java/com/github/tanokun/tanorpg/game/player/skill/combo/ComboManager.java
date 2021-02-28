package com.github.tanokun.tanorpg.game.player.skill.combo;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.skill.combo.ComboRunnable;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ComboManager implements Listener {
    private final static String COMBO = "COMBO";
    public static HashMap<UUID, List<ComboRunnable>> comboRunnable = new HashMap<>();

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

    private void addCombo(Player player, String combo){
        UUID playerUuid = player.getUniqueId();
        if (getCombos(playerUuid).size() >= 3) {return;}

        List<ComboRunnable> playerComboRunnable = comboRunnable.get(playerUuid);
        playerComboRunnable.add(new ComboRunnable(combo, player));
    }
    public static ArrayList<String> getCombos(UUID uuid){
        ArrayList<String> combos = new ArrayList<>();
        for(ComboRunnable runnable : comboRunnable.get(uuid)){
            combos.add(runnable.getCombo());
        }
        return combos;
    }
}