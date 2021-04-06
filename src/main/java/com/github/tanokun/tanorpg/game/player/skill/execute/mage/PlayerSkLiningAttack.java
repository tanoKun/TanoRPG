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
import net.minecraft.server.v1_15_R1.EntityLightning;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_15_R1.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;

import static com.github.tanokun.tanorpg.game.player.GamePlayerJobType.MAGE;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class PlayerSkLiningAttack extends Skill implements AttackSkill {
    public PlayerSkLiningAttack() {
        super("落雷", 5, 30, 15,
                new ArrayList<String>(Arrays.asList("DR", "SRC", "SRC")),
                new ArrayList<String>(Arrays.asList("§f前方にいる敵に雷を落とします")),
                new ArrayList<GamePlayerJobType>(Arrays.asList(MAGE)), Material.BLAZE_ROD);
    }

    @Override
    public void execute(Entity entity) {
        TanoRPG.playSound((Player) entity, Sound.ENTITY_ILLUSIONER_CAST_SPELL, 10, 1);
        Location location = entity.getLocation(); location.setPitch(0);
        location.setPitch(0);
        Vector vector = location.getDirection();
        double x = vector.getX() * 7;
        double z = vector.getZ() * 7;
        location.add(x, 1, z);
        new BukkitRunnable(){
            int t;
            double y = 0;
            double r = 2;
            @Override
            public void run() {
                if (t == 3) {
                    this.cancel();
                    lining(location);
                    TanoRPG.playSound((Player) entity, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 10, 1);
                    TanoRPG.playSound((Player) entity, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 10, 1);
                    ParticleEffect.EXPLOSION_HUGE.display(location, 0, 0, 0, 0, 1, null, Bukkit.getOnlinePlayers());
                    GamePlayer gamePlayer = GamePlayerManager.getPlayer(entity.getUniqueId());
                    for (Entity entity2 : TanoRPG.getNearbyEntities(location, 2.5)) {
                        {
                            if (entity2 instanceof Player || !entity2.hasMetadata("TanoRPG_entity")) continue;
                            ((Creature) entity2).setTarget((LivingEntity) entity);
                            ObjectEntity custom = EntityManager.getBaseEntity(entity2);
                            int at_lvl = gamePlayer.getLEVEL().getLEVEL();
                            int vi_lvl = custom.getLEVEL();
                            double atk = DamageManager.getDamage(gamePlayer.getStatus(StatusType.MATK).getLevel(),
                                    gamePlayer.getStatus(StatusType.INT).getLevel(),
                                    gamePlayer.getStatus(StatusType.AGI).getLevel());
                            long damage = Math.round(DamageManager.getCompDamage(atk, custom.getMDEF(), at_lvl, vi_lvl, entity) * 2);
                            ((LivingEntity) entity2).damage(damage);
                            DamageManager.createDamage(damage, entity, entity2);
                        }
                    }
                }
                for (int i = 0; i < 60; i++) {
                    double x = r * cos(2 * Math.PI * i * 0.02);
                    double z = r * sin(2 * Math.PI * i * 0.02);
                    location.add(x, y, z);
                    ParticleEffect.FLAME.display(location, new Vector(0, 1, 0), 0, 10, null, Bukkit.getOnlinePlayers());
                    location.subtract(x, y, z);
                }
                t++;
            }
        }.runTaskTimer(TanoRPG.getPlugin(), 0, 7);
    }
    private void lining(Location loc){
        EntityLightning lightning = new EntityLightning(((CraftWorld)loc.getWorld()).getHandle(), loc.getX(), loc.getY(), loc.getZ(), true);
        Vec3D vec = new Vec3D(0, 0, 0);
        PacketPlayOutSpawnEntity lightningPacket = new PacketPlayOutSpawnEntity(lightning.getId(), lightning.getUniqueID(), loc.getX(), loc.getY(), loc.getZ(), 0f, 0f, EntityTypes.LIGHTNING_BOLT, 0, vec);
        for (Player player : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(lightningPacket);
        }
    }
}