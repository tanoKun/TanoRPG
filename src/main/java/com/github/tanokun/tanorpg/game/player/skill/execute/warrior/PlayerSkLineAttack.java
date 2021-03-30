package com.github.tanokun.tanorpg.game.player.skill.execute.warrior;

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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;

import static com.github.tanokun.tanorpg.game.player.GamePlayerJobType.WARRIOR;

public class PlayerSkLineAttack extends Skill implements AttackSkill {
    public PlayerSkLineAttack() {
        super("猪突猛進", 12, 40, 14,
                new ArrayList<String>(Arrays.asList("DR", "LC", "LC")),
                new ArrayList<String>(Arrays.asList("§f前方に突進してダメージを与えます")),
                new ArrayList<GamePlayerJobType>(Arrays.asList(WARRIOR)), Material.BLAZE_ROD);
    }

    @Override
    public void execute(Entity entity) {
        Location loc = entity.getLocation();
        Vector dire = loc.getDirection().normalize();
        final double[] time = {0, 0};
        final GamePlayer gamePlayer = GamePlayerManager.getPlayer(entity.getUniqueId());
        new BukkitRunnable(){
            @Override
            public void run() {
                TanoRPG.playSound((Player) entity, Sound.ENTITY_WITHER_SHOOT, 3, 1.5);
                for (int i = 0; i < 4; i++) {
                    time[0] += 0.8;
                    double x = dire.getX() * time[0];
                    double z = dire.getZ() * time[0];
                    loc.add(x, 0, z);
                    if (!(loc.getBlock().getType() == Material.AIR)) {cancel(); return;}
                    entity.teleport(loc);
                    entity.setVelocity(loc.getDirection());
                    for (Entity target : TanoRPG.getNearbyEntities(loc, 2)){
                        if (target instanceof Player || !target.hasMetadata("TanoRPG_entity")) continue;
                        ((Creature) target).setTarget((LivingEntity) entity);
                        ObjectEntity custom = EntityManager.getBaseEntity(target);
                        int at_lvl = gamePlayer.getLEVEL();
                        int vi_lvl = custom.getLEVEL();
                        double atk = DamageManager.getDamage(gamePlayer.getStatus(StatusType.ATK).getLevel(),
                                gamePlayer.getStatus(StatusType.ING).getLevel(),
                                gamePlayer.getStatus(StatusType.AGI).getLevel());
                        long damage = Math.round(DamageManager.getCompDamage(atk, custom.getDEF(), at_lvl, vi_lvl, gamePlayer.getPlayer()) * 1.6);
                        DamageManager.createDamage(damage, entity, target);
                    }
                    ParticleEffect.CLOUD.display(loc, 1, 1, 1, 0f, 10, null, Bukkit.getOnlinePlayers());
                    loc.subtract(x, 0, z);
                    time[1]++;
                    if (time[1] == 10){
                        cancel();
                        return;
                    }
                }
            }
        }.runTaskTimer(TanoRPG.getPlugin(), 0, 1);
    }
}
