package com.github.tanokun.tanorpg.game.player.skill.execute.priest;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayerJobType;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.skill.Skill;
import com.github.tanokun.tanorpg.game.player.status.buff.Buff;
import com.github.tanokun.tanorpg.game.player.status.buff.BuffSelf;
import com.github.tanokun.tanorpg.game.player.status.buff.BuffType;
import com.github.tanokun.api.particle.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;

import static com.github.tanokun.tanorpg.game.player.GamePlayerJobType.PRIEST;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class PlayerSkCoolTimeUpBuff extends Skill {
    public PlayerSkCoolTimeUpBuff() {
        super("心身強化", 15, 70, 50,
                new ArrayList<String>(Arrays.asList("DR", "DR", "SRC")),
                new ArrayList<String>(Arrays.asList("§f半径4mの味方に「スキルCT減少15%」を与えます")),
                new ArrayList<GamePlayerJobType>(Arrays.asList(PRIEST)), Material.LAPIS_BLOCK);
    }

    @Override
    public void execute(Entity entity) {
        final Location[] location = {entity.getLocation(), entity.getLocation()};
        Location teleport = entity.getLocation();
        new BukkitRunnable(){
            int t = 1;
            double y = 0;
            double r = 5;
            @Override
            public void run() {
                if (t == 5) {
                    this.cancel();
                    location[1].add(0, 0.5, 0);
                    int i = 0;
                    double y = 0;
                    double r = 1;
                    for (int t = 0; t < 300; t++) {
                        if (i == 75) {
                            i = 0;
                            r++;
                        }
                        double x = r * cos(2 * Math.PI * t * 0.05);
                        double z = r * sin(2 * Math.PI * t * 0.05);
                        location[1].add(x, y, z);
                        ParticleEffect.VILLAGER_HAPPY.display(location[1], 0.5f, 0f, 0.5f, 0, 4, null, Bukkit.getOnlinePlayers());
                        location[1].subtract(x, y, z);
                        i++;
                    }
                    for (Entity player : TanoRPG.getNearbyEntities(teleport, 4)) {
                        if (GamePlayerManager.getPlayer(player.getUniqueId()) == null) continue;
                        Buff.addBuff(new BuffSelf(BuffType.SKILL_COOL_TIME_15, player, 30));
                        TanoRPG.playSound((Player) entity, Sound.ENTITY_EVOKER_CAST_SPELL, 10, 1);
                    }

                }
                for (int i = 0; i < 100; i++) {
                    double x = r * cos(i * 0.5);
                    double z = r * sin(i * 0.5);
                    location[0].add(x, y, z);
                    ParticleEffect.FLAME.display(location[0], new Vector(0, 1, 0), 0, 10, null, Bukkit.getOnlinePlayers());
                    location[0].subtract(x, y, z);
                }
                for (Entity player : TanoRPG.getNearbyEntities(location[0], 5)) {
                    if (GamePlayerManager.getPlayer(player.getUniqueId()) == null) continue;
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                            "execute " + player.getName() + " ~ ~ ~ playsound block.lever.click player @s ~ ~ ~ 10 1");
                    TanoRPG.playSound((Player) entity, Sound.BLOCK_LEVER_CLICK, 10, 1);
                }
                t++;
            }
        }.runTaskTimer(TanoRPG.getPlugin(), 0, 20);
        new BukkitRunnable(){
            int t = 0;
            @Override
            public void run() {
                if (t == 80) this.cancel();
                entity.teleport(teleport);
                t++;
            }
        }.runTaskTimer(TanoRPG.getPlugin(), 0, 1);
    }
}
