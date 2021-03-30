package com.github.tanokun.tanorpg.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.DamageManager;
import com.github.tanokun.tanorpg.game.entity.ActiveEntity;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.game.entity.base.ObjectEntity;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.game.item.itemtype.ItemMagicWeapon;
import com.github.tanokun.tanorpg.game.item.itemtype.base.ItemJob;
import com.github.tanokun.tanorpg.game.player.GamePlayer;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.status.Sidebar;
import com.github.tanokun.tanorpg.game.player.status.StatusType;
import com.github.tanokun.tanorpg.util.task.MagicTask;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftItemFrame;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;

public class DamageEventListener implements Listener {


    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL || event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION){
            event.setCancelled(true);
            return;
        }
        if (!(event instanceof EntityDamageByEntityEvent)) return;
        EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
        if (e.getEntity() instanceof CraftItemFrame) {
            if (e.getDamager() instanceof Player){
                Player player = (Player) e.getDamager();
                if (player.getGameMode().equals(GameMode.CREATIVE)) {
                    return;
                }
                else {
                    e.setCancelled(true);
                }
                return;
            }
            return;
        }
        ((LivingEntity)e.getEntity()).setMaximumNoDamageTicks(0);
        if (e.getDamager() instanceof Player) player(e);
        else entity(e);
    }

    private void player(EntityDamageByEntityEvent e) {
        try {
            if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
                e.setCancelled(true);
                return;
            }
            if (!e.getEntity().hasMetadata("TanoRPG_entity")) return;
            e.setCancelled(true);
            GamePlayer attacker = GamePlayerManager.getPlayer(e.getDamager().getUniqueId());
            ActiveEntity victim = EntityManager.getActiveEntity(e.getEntity());
            ObjectEntity customEntity = victim.getObjectEntity();
            if (attacker.getPlayer().hasMetadata("cooltime")) {
                e.setCancelled(true);
                return;
            }
            attacker.getPlayer().setNoDamageTicks(0);
            if (!attacker.isProper(attacker.getPlayer().getEquipment().getItemInMainHand())) {
                attacker.getPlayer().sendMessage(TanoRPG.PX + "§c対応していない武器です");
                e.setCancelled(true);
                return;
            }
            if (!attacker.isLv(attacker.getPlayer().getEquipment().getItemInMainHand())) {
                attacker.getPlayer().sendMessage(TanoRPG.PX + "§c必要レベルが足りません");
                e.setCancelled(true);
                return;
            }
            if (ItemManager.getItem(attacker.getPlayer().getEquipment().getItemInMainHand()) instanceof ItemMagicWeapon){
                if (!attacker.isProper(attacker.getPlayer().getEquipment().getItemInMainHand())) {
                    attacker.getPlayer().sendMessage(TanoRPG.PX + "§c対応していない武器です");
                    e.setCancelled(true);
                    return;
                }
                e.setCancelled(true);
                GamePlayer gamePlayer = attacker;
                if (gamePlayer.getPlayer().hasMetadata("cooltime_magic")) {
                    e.setCancelled(true);
                    return;
                }
                gamePlayer.getPlayer().setMetadata("cooltime_magic", new FixedMetadataValue(TanoRPG.getPlugin(), true));
                String id = ItemManager.getID(gamePlayer.getPlayer().getEquipment().getItemInMainHand());
                final int[] cool = {Math.round(((ItemJob)ItemManager.getItem(id)).getCoolTime())};
                gamePlayer.getPlayer().setLevel(0);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        cool[0] -= 1;
                        if (cool[0] <= 0) {
                            gamePlayer.getPlayer().removeMetadata("cooltime_magic", TanoRPG.getPlugin());
                            this.cancel();
                        }
                    }
                }.runTaskTimerAsynchronously(TanoRPG.getPlugin(), 0, 1L);
                new MagicTask(attacker).runTaskTimerAsynchronously(TanoRPG.getPlugin(), 2, 1);
                return;
            }
            int at_lvl = attacker.getLEVEL();
            int vi_lvl = customEntity.getLEVEL();
            double atk = DamageManager.getDamage(attacker.getStatus(StatusType.ATK).getLevel(),
                    attacker.getStatus(StatusType.ING).getLevel(),
                    attacker.getStatus(StatusType.AGI).getLevel());
            long damage = DamageManager.getCompDamage(atk, customEntity.getDEF(), at_lvl, vi_lvl, attacker.getPlayer());
            DamageManager.createDamage(damage, attacker.getPlayer(), victim.getActiveEntity());
            attacker.getPlayer().setMetadata("cooltime", new FixedMetadataValue(TanoRPG.getPlugin(), true));
            String id = ItemManager.getID(attacker.getPlayer().getEquipment().getItemInMainHand());
            final int[] cool = {Math.round(((ItemJob)ItemManager.getItem(id)).getCoolTime())};
            attacker.getPlayer().setLevel(0);
            new BukkitRunnable() {
                @Override
                public void run() {
                    cool[0] -= 1;
                    if (cool[0] <= 0) {
                        attacker.getPlayer().removeMetadata("cooltime", TanoRPG.getPlugin());
                        this.cancel();
                    }
                }
            }.runTaskTimer(TanoRPG.getPlugin(), 0, 1L);
        }catch (Exception ex){
            return;
        }
    }
    private void entity(EntityDamageByEntityEvent e) {
        try{
            try{
                if (!e.getDamager().hasMetadata("TanoRPG_entity")) return;
            }catch (Exception ex) {
                return;
            }
            e.setDamage(0);
            ActiveEntity attacker = EntityManager.getActiveEntity(e.getDamager());
            ObjectEntity customEntity = attacker.getObjectEntity();
            GamePlayer victim = GamePlayerManager.getPlayer(e.getEntity().getUniqueId());
            ((LivingEntity) victim.getPlayer()).setNoDamageTicks(0);
            int at_lvl = customEntity.getLEVEL();
            int vi_lvl = victim.getLEVEL();
            double atk = DamageManager.getDamage(customEntity.getATK(), customEntity.getINT(), customEntity.getAGI());
            long damage = DamageManager.getCompDamage(atk, victim.getStatus(StatusType.DEF).getLevel(), at_lvl, vi_lvl, e.getDamager());
            victim.setHAS_HP(victim.getHAS_HP() - damage);
            victim.setHAS_HP(new BigDecimal("" + victim.getHAS_HP()).setScale(2,BigDecimal.ROUND_DOWN).doubleValue());
            if (victim.getHAS_HP() <= 0){
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                victim.getPlayer().setGameMode(GameMode.SPECTATOR);
                                victim.getPlayer().sendTitle("§c死んでしまった！", "", 0, 0, 100);
                                victim.setHAS_HP(0);
                            }
                        }.runTask(TanoRPG.getPlugin());
                        try {Thread.sleep(5000);} catch (InterruptedException interruptedException) {interruptedException.printStackTrace();}
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                victim.getPlayer().teleport(new Location(Bukkit.getWorld("world"), -814, 47, 97, 0, 0));
                                victim.setHAS_HP(victim.getMAX_HP());
                                victim.getPlayer().setGameMode(GameMode.ADVENTURE);
                                Sidebar.updateSidebar((Player) e.getEntity());
                            }
                        }.runTask(TanoRPG.getPlugin());
                    }
                }.runTaskAsynchronously(TanoRPG.getPlugin());
            }
            e.getEntity().sendMessage(TanoRPG.PX + "§c" + damage + "ダメージ！");
            Sidebar.updateSidebar((Player) e.getEntity());
        }catch (Exception ex) {
            return;
        }
    }
}