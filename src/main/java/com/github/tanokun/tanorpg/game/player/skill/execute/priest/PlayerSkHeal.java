package com.github.tanokun.tanorpg.game.player.skill.execute.priest;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayer;
import com.github.tanokun.tanorpg.game.player.GamePlayerJobType;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.skill.Skill;
import com.github.tanokun.api.particle.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;

import static com.github.tanokun.tanorpg.game.player.GamePlayerJobType.PRIEST;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class PlayerSkHeal extends Skill {
    public PlayerSkHeal() {
        super("回復", 3, 20, 20,
                new ArrayList<String>(Arrays.asList("DR", "RC", "RC")),
                new ArrayList<String>(Arrays.asList("§f自分の体力を25%回復します")),
                new ArrayList<GamePlayerJobType>(Arrays.asList(PRIEST)), Material.EMERALD);
    }

    @Override
    public void execute(Entity entity) {
        TanoRPG.playSound((Player) entity, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
        Location location = entity.getLocation();
        for (int i = 0; i < 60; i++) {
            double x = 1 * cos(2 * Math.PI * i * 0.02);
            double z = 1 * sin(2 * Math.PI * i * 0.02);
            location.add(x, 0, z);
            ParticleEffect.VILLAGER_HAPPY.display(location, new Vector(0, 2, 0), 0, 10, null, Bukkit.getOnlinePlayers());
            location.subtract(x, 0, z);
        }
        GamePlayer gamePlayer = GamePlayerManager.getPlayer(entity.getUniqueId());
        gamePlayer.setHAS_HP((int) Math.floor(gamePlayer.getHAS_HP() + gamePlayer.getMAX_HP() * 0.25));
        if (gamePlayer.getHAS_HP() > gamePlayer.getMAX_HP()) gamePlayer.setHAS_HP(gamePlayer.getMAX_HP());
        gamePlayer.getPlayer().sendMessage(TanoRPG.PX + "回復しました！");
    }
}
