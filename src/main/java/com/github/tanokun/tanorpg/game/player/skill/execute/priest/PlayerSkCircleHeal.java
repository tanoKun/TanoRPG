package com.github.tanokun.tanorpg.game.player.skill.execute.priest;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayer;
import com.github.tanokun.tanorpg.game.player.GamePlayerJobType;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.skill.Skill;
import com.github.tanokun.tanorpg.util.particle.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;

import static com.github.tanokun.tanorpg.game.player.GamePlayerJobType.PRIEST;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class PlayerSkCircleHeal extends Skill {
    public PlayerSkCircleHeal() {
        super("天の回復", 6, 60, 50,
                new ArrayList<String>(Arrays.asList("DR", "SRC", "RC")),
                new ArrayList<String>(Arrays.asList("§f範囲5m以内にいる見方を50%回復します")),
                new ArrayList<GamePlayerJobType>(Arrays.asList(PRIEST)), Material.EMERALD_BLOCK);
    }

    @Override
    public void execute(Entity entity) {
        Location location = entity.getLocation();
        Location teleport = entity.getLocation();
        new BukkitRunnable(){
            int t;
            double y = 0;
            double r = 5;
            @Override
            public void run() {
                if (t == 6) {
                    this.cancel();
                    for (Entity player : TanoRPG.getNearbyEntities(location, 5)){
                        if (GamePlayerManager.getPlayer(player.getUniqueId()) == null) continue;
                        GamePlayer gamePlayer = GamePlayerManager.getPlayer(player.getUniqueId());
                        gamePlayer.setHAS_HP(gamePlayer.getHAS_HP() + gamePlayer.getMAX_HP() * 0.25);
                        if (gamePlayer.getHAS_HP() > gamePlayer.getMAX_HP()) gamePlayer.setHAS_HP(gamePlayer.getMAX_HP());
                        gamePlayer.getPlayer().sendMessage(TanoRPG.PX + "回復しました！");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                                "execute " + player.getName() + " ~ ~ ~ playsound entity.experience_orb.pickup player @s ~ ~ ~ 10 1");
                        for (int i = 0; i < 60; i++) {
                            double x = 1 * cos(2 * Math.PI * i * 0.02);
                            double z = 1 * sin(2 * Math.PI * i * 0.02);
                            location.add(x, y, z);
                            ParticleEffect.VILLAGER_HAPPY.display(location, new Vector(0, 2, 0), 0, 10, null, Bukkit.getOnlinePlayers());
                            location.subtract(x, y, z);
                        }
                    }
                    return;
                }
                for (int i = 0; i < 100; i++) {
                    double x = r * cos(2 * Math.PI * i * 0.02);
                    double z = r * sin(2 * Math.PI * i * 0.02);
                    location.add(x, y, z);
                    ParticleEffect.REDSTONE.display(location, new Vector(0, 1, 0), 0, 10, null, Bukkit.getOnlinePlayers());
                    location.subtract(x, y, z);
                }
                for (Entity player : TanoRPG.getNearbyEntities(location, 5)) {
                    if (GamePlayerManager.getPlayer(player.getUniqueId()) == null) continue;
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                            "execute " + player.getName() + " ~ ~ ~ playsound block.lever.click player @s ~ ~ ~ 10 1");
                }
                t++;
            }
        }.runTaskTimer(TanoRPG.getPlugin(), 0, 10);
        new BukkitRunnable(){
            int t = 0;
            @Override
            public void run() {
                if (t == 60) this.cancel();
                entity.teleport(teleport);
                t++;
            }
        }.runTaskTimer(TanoRPG.getPlugin(), 0, 1);
    }
}
