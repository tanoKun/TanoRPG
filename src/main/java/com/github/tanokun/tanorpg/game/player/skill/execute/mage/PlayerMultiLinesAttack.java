package com.github.tanokun.tanorpg.game.player.skill.execute.mage;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayerJobType;
import com.github.tanokun.tanorpg.game.player.skill.AttackSkill;
import com.github.tanokun.tanorpg.game.player.skill.Skill;
import com.github.tanokun.api.particle.ParticleEffect;
import com.github.tanokun.api.particle.data.color.RegularColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static com.github.tanokun.tanorpg.game.player.GamePlayerJobType.MAGE;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class PlayerMultiLinesAttack extends Skill implements AttackSkill {
    public PlayerMultiLinesAttack() {
        super("凍雨", 0, 0, 0,
                new ArrayList<String>(Arrays.asList("DR", "DR", "LC")),
                new ArrayList<String>(Arrays.asList("§f氷の雨を召喚します")),
                new ArrayList<GamePlayerJobType>(Arrays.asList(MAGE)), Material.BONE);
    }

    @Override
    public void execute(Entity entity) {
        RegularColor rainParticleColor = new RegularColor(new Color(70, 40, 255));
        Location particle1 = entity.getLocation();
        Location particle2 = entity.getLocation();
        particle1.add(0, 13, 0);
        particle2.add(0, 13, 0);
        new BukkitRunnable() {
            int time;

            public void run() {
                if (time == 1) this.cancel();

                for (int i2 = 0; i2 < 1; i2++) {
                    int i = 0;
                    double y = 0;
                    double r = 1;
                    for (int t = 0; t < 600; t++) {
                        if (i == 60) {
                            i = 0;
                            r++;
                        }
                        double x = r * cos(1 * Math.PI * t * 0.1);
                        double z = r * sin(1 * Math.PI * t * 0.1);
                        particle1.add(x, y, z);
                        ParticleEffect.CAMPFIRE_COSY_SMOKE.display(particle1, new Vector(0,0,0), (float) 0.01, 1, null, Bukkit.getOnlinePlayers());
                        particle1.subtract(x, y, z);
                        i++;
                    }
                }
                time++;
            }
        }.runTaskTimerAsynchronously(TanoRPG.getPlugin(), 1, 80);

        new BukkitRunnable(){
            int time;
            public void run() {
                if (time == 30) this.cancel();
                Location loc = randomizeSpawnLocation(particle2, 7);

                new BukkitRunnable() {
                    int time2;
                    public void run() {
                        time2++;
                        for (int i = 0; i < 5; i++) {
                            loc.subtract(0, 0.3, 0);
                            ParticleEffect.REDSTONE.display(loc, 0, 0, 0, 0f, 5, rainParticleColor, Bukkit.getOnlinePlayers());
                        }
                        if (time2 > 20) {this.cancel();}
                    }
                }.runTaskTimerAsynchronously(TanoRPG.getPlugin(), 1, 1);
                time++;
            }
        }.runTaskTimerAsynchronously(TanoRPG.getPlugin(), 1, 4);
    }

    public Location randomizeSpawnLocation(Location location, int r) {
        double x, z, sX = location.getX();
        double sY = location.getY();
        double sZ = location.getZ();
        do {
            x = randomRange(sX - r, sX + r);
            z = randomRange(sZ - r, sZ + r);
        } while (location.distance(new Location(location.getWorld(), x, sY, z)) > r);
        return new Location(location.getWorld(), x, sY, z);
    }

    public double randomRange(double arg0, double arg1) {
        double range = (arg0 < arg1) ? (arg1 - arg0) : (arg0 - arg1);
        if (range < 1.0D)
            return Math.floor(arg0) + 0.5D;
        double min = (arg0 < arg1) ? arg0 : arg1;
        return Math.floor(min + Math.random() * range) + 0.5D;
    }

}
