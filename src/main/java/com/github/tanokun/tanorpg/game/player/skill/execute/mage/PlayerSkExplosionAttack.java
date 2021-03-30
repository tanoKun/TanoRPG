package com.github.tanokun.tanorpg.game.player.skill.execute.mage;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.DamageManager;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.game.entity.base.ObjectEntity;
import com.github.tanokun.tanorpg.game.player.GamePlayer;
import com.github.tanokun.tanorpg.game.player.GamePlayerJobType;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.skill.AttackSkill;
import com.github.tanokun.tanorpg.game.player.skill.Skill;
import com.github.tanokun.tanorpg.game.player.status.StatusType;
import com.github.tanokun.tanorpg.util.particle.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;

import static com.github.tanokun.tanorpg.game.player.GamePlayerJobType.MAGE;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class PlayerSkExplosionAttack extends Skill implements AttackSkill {
    public PlayerSkExplosionAttack() {
        super("爆散", 8, 40, 40,
                new ArrayList<String>(Arrays.asList("LC", "DR", "SLC")),
                new ArrayList<String>(Arrays.asList("§f視点の先にいる敵を爆破します")),
                new ArrayList<GamePlayerJobType>(Arrays.asList(MAGE)), Material.TNT);
    }

    @Override
    public void execute(Entity entity) {
        Entity target = null;
        Location location = entity.getLocation(); location.setPitch(0);
        Vector vector = location.getDirection();
        for (int i = 0; i < 10; i++) {
            location.add(vector.getX(), vector.getY(), vector.getZ());
            for (Entity temp_target : TanoRPG.getNearbyEntities(location, 6)){
                if (temp_target instanceof Player) continue;
                if (!temp_target.hasMetadata("TanoRPG_entity")) continue;
                target = temp_target;
            }
        }
        if (target == null) {entity.sendMessage(TanoRPG.PX + "§cターゲットがいません"); return;}
        final int[] t = {0, 1};
        Entity finalTarget = target;
        ItemStack helmet = ((Creature)target).getEquipment().getHelmet();
        ((Creature)target).getEquipment().setHelmet(new ItemStack(Material.TNT));
        Location teleport = target.getLocation();
        Location targetLoc =  target.getLocation();
        GamePlayer gamePlayer = GamePlayerManager.getPlayer(entity.getUniqueId());
        ((Creature)target).setTarget((LivingEntity) entity);
        new BukkitRunnable(){
            @Override
            public void run() {
                Location loc = finalTarget.getLocation();
                loc.setY(loc.getY() + 2);
                if (t[0] == 3){
                    this.cancel();
                    ((Creature) finalTarget).getEquipment().setHelmet(helmet);
                    ParticleEffect.EXPLOSION_HUGE.display(finalTarget.getLocation(), 0, 0, 0, 0, 4, null, Bukkit.getOnlinePlayers());
                    ObjectEntity custom = EntityManager.getBaseEntity(finalTarget);
                    double atk = DamageManager.getDamage(gamePlayer.getStatus(StatusType.MATK).getLevel(),
                            gamePlayer.getStatus(StatusType.INT).getLevel(),
                            gamePlayer.getStatus(StatusType.AGI).getLevel());
                    int at_lvl = gamePlayer.getLEVEL();
                    int vi_lvl = custom.getLEVEL();
                    long damage = DamageManager.getCompDamage(atk, custom.getMDEF(), at_lvl, vi_lvl, entity) * 2;
                    DamageManager.createDamage(damage, entity, finalTarget);
                    for (Entity damager : TanoRPG.getNearbyEntities(finalTarget.getLocation(), 3)){
                         custom = EntityManager.getBaseEntity(damager);
                        atk = DamageManager.getDamage(gamePlayer.getStatus(StatusType.MATK).getLevel(),
                                gamePlayer.getStatus(StatusType.INT).getLevel(),
                                gamePlayer.getStatus(StatusType.AGI).getLevel());
                        at_lvl = gamePlayer.getLEVEL();
                        vi_lvl = custom.getLEVEL();
                        damage = Math.round(DamageManager.getCompDamage(atk, custom.getMDEF(), at_lvl, vi_lvl, entity) * 1.2);
                        if (damager.equals(finalTarget)) continue;
                        DamageManager.createDamage(damage, entity, damager);
                    }
                }
                for (int i = 0; i < 80; i++) {
                    double x = 0.5 * cos(2 * Math.PI * i * 0.04);
                    double z = 0.5 * sin(2 * Math.PI * i * 0.04);
                    targetLoc.add(x, 0.025, z);
                    ParticleEffect.FLAME.display(targetLoc, 0f, 0f, 0f, 0, 2, null, Bukkit.getOnlinePlayers());
                    targetLoc.subtract(x, 0, z);
                }
                targetLoc.subtract(0, 2, 0);
                TanoRPG.playSound((Player) entity, Sound.BLOCK_LEVER_CLICK, 10, 1);
                t[0]++;
            }
        }.runTaskTimer(TanoRPG.getPlugin(), 0, 10);
        new BukkitRunnable(){
            @Override
            public void run() {
                if (t[1] == 39) {
                    this.cancel();
                }
                finalTarget.teleport(teleport);
                t[1]++;
            }
        }.runTaskTimer(TanoRPG.getPlugin(), 0, 1);
    }
}