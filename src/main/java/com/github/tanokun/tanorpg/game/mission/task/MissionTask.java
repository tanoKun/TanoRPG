package com.github.tanokun.tanorpg.game.mission.task;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public abstract class MissionTask<V> {
    protected final HashMap<UUID, V> playerData = new HashMap<>();
    private String message;

    public String getMessage() {return message;}
    public void setMessage(String message) {this.message = message;}

    abstract public String getMessage(Player player);

    abstract public void setValue(UUID uuid, Object value);
    abstract public void removeValue(UUID uuid);
    abstract public V getValue(UUID uuid);

    abstract public boolean isClearTask(UUID uuid);

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MissionTask<?> that = (MissionTask<?>) o;
        return Objects.equals(playerData, that.playerData);
    }
    public int hashCode() {
        return Objects.hash(playerData);
    }
}
