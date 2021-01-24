package com.github.tanokun.tanorpg.game.player.skill.execute.warrior;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayerJobType;
import com.github.tanokun.tanorpg.game.player.skill.Skill;
import com.github.tanokun.tanorpg.game.player.status.buff.Buff;
import com.github.tanokun.tanorpg.game.player.status.buff.BuffSelf;
import com.github.tanokun.tanorpg.game.player.status.buff.BuffType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;

import static com.github.tanokun.tanorpg.game.player.GamePlayerJobType.WARRIOR;

public class PlayerSkAtkUp extends Skill {
    public PlayerSkAtkUp() {
        super("奮起", 4, 35, 35,
                new ArrayList<String>(Arrays.asList("DR", "DR", "SRC")),
                new ArrayList<String>(Arrays.asList("§f自分に 攻撃力上昇「小」を付与します")),
                new ArrayList<GamePlayerJobType>(Arrays.asList(WARRIOR)), Material.LEATHER);
    }

    public void execute(Entity entity) {
        Location player_loc = entity.getLocation();
        player_loc.setPitch(-45);
        TanoRPG.playSound((Player) entity, Sound.ENTITY_POLAR_BEAR_WARNING, 10, 1);
        final int[] t = {1};
        new BukkitRunnable(){
            @Override
            public void run() {
                if (t[0] == 30){this.cancel();}
                entity.teleport(player_loc);
                t[0]++;
            }
        }.runTaskTimer(TanoRPG.getPlugin(), 0, 1);
        Buff.addBuff(new BuffSelf(BuffType.ATK_UP_S, entity, 15));
    }
}