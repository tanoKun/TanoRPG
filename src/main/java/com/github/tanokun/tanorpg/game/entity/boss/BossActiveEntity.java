package com.github.tanokun.tanorpg.game.entity.boss;

import com.github.tanokun.tanorpg.game.entity.ActiveEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class BossActiveEntity extends ActiveEntity {
    private BossEntity bossEntity;

    private HashSet<Player> join = new HashSet<>();

    public BossActiveEntity(BossEntity bossEntity, Entity entity) {
        super(bossEntity.getObjectEntity(), entity);
        this.bossEntity = bossEntity;
    }

    public BossEntity getBossEntity() {
        return bossEntity;
    }

    public HashSet<Player> getJoin() {
        return join;
    }
}
