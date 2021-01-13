package com.github.tanokun.tanorpg.game.player.skill.execute.warrior;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.DamageManager;
import com.github.tanokun.tanorpg.game.mob.CustomEntity;
import com.github.tanokun.tanorpg.game.mob.CustomEntityManager;
import com.github.tanokun.tanorpg.game.player.GamePlayer;
import com.github.tanokun.tanorpg.game.player.GamePlayerJobType;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.skill.Skill;
import com.github.tanokun.tanorpg.game.player.status.StatusType;
import com.github.tanokun.tanorpg.util.particle.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;

import static com.github.tanokun.tanorpg.game.player.GamePlayerJobType.WARRIOR;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class PlayerSkJumpAttack extends Skill {
    public PlayerSkJumpAttack() {
        super("薙ぎ払い", 7, 40, 45,
                new ArrayList<String>(Arrays.asList("LC", "RC", "LC")),
                new ArrayList<String>(Arrays.asList("§f剣を振って敵を払います (半径10m)")),
                new ArrayList<GamePlayerJobType>(Arrays.asList(WARRIOR)), Material.WOOD_SWORD);
    }

    @Override
    public void execute(Entity entity) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute " + entity.getName() + " ~ ~ ~ /playsound entity.wither.shoot player @s ~ ~ ~ 10 1");
        Location location = entity.getLocation();
        location.add(0, 0.5, 0);
        int i = 0;
        double y = 0;
        double r = 1;
        for (int t = 0; t < 600; t++) {
            if (i == 60) {
                i = 0;
                r++;
            }
            double x = r * cos(2 * Math.PI * t * 0.05);
            double z = r * sin(2 * Math.PI * t * 0.05);
            location.add(x, y, z);
            ParticleEffect.SWEEP_ATTACK.display(location, Bukkit.getOnlinePlayers());
            location.subtract(x, y, z);
            i++;
        }
        GamePlayer gamePlayer = GamePlayerManager.getPlayer(entity.getUniqueId());
        for (Entity entity2 : TanoRPG.getNearbyEntities(entity.getLocation(), 10)){
            {
                if (entity2 instanceof Player || !CustomEntityManager.isExists(entity2)) continue;
                ((Creature) entity2).setTarget((LivingEntity) entity);
                CustomEntity custom = CustomEntityManager.getEntity(entity2);
                int at_lvl = gamePlayer.getLEVEL();
                int vi_lvl = custom.getLEVEL();
                double atk = DamageManager.getDamage(gamePlayer.getStatus(StatusType.ATK).getLevel(),
                        gamePlayer.getStatus(StatusType.ING).getLevel(),
                        gamePlayer.getStatus(StatusType.AGI).getLevel());
                long damage = Math.round(DamageManager.getCompDamage(atk, custom.getDEF(), at_lvl, vi_lvl, gamePlayer.getPlayer()) * 1.5);
                DamageManager.createMake(damage, entity, entity2);
                entity2.setVelocity(new Vector(0, 1, 0));
            }
        }
    }
}
