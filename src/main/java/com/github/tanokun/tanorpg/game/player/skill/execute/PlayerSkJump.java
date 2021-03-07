package com.github.tanokun.tanorpg.game.player.skill.execute;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayerJobType;
import com.github.tanokun.tanorpg.game.player.skill.Skill;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;

import static com.github.tanokun.tanorpg.game.player.GamePlayerJobType.*;

public class PlayerSkJump extends Skill {
    public PlayerSkJump() {
        super("ジャンプ", 5, 30, 10, new ArrayList<String>(Arrays.asList("DR", "LC", "RC")),
                new ArrayList<String>(Arrays.asList("§f斜め上にジャンプすることができ、", "§f移動に最適です")),
                new ArrayList<GamePlayerJobType>(Arrays.asList(WARRIOR, MAGE, PRIEST)), Material.FEATHER);
    }

    @Override
    public void execute(Entity entity) {
        TanoRPG.playSound((Player) entity, Sound.ENTITY_GHAST_SHOOT, 10, 1);
        Vector vector = entity.getLocation().getDirection().normalize();
        vector.setY(1.5);
        entity.setVelocity(vector);
    }
}