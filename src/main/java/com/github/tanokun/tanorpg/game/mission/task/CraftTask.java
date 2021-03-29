package com.github.tanokun.tanorpg.game.mission.task;

import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public class CraftTask extends MissionTask<Integer>{
    private final int clearCount;
    private final String itemId;

    public CraftTask(int clearCount, String itemId, String message){this.clearCount = clearCount;
        this.itemId = itemId;
        setMessage(message);}

    public String getMessage(Player player) {
        if (isClearTask(player.getUniqueId()))
            return "§a" + getMessage() + " §a[§a" + getValue(player.getUniqueId()) +"§a/§a" + clearCount + "§a]";
        else
            return "§f" + getMessage() + " §7[§b" + getValue(player.getUniqueId()) +"§7/§b" + clearCount + "§7]";
    }

    public void setValue(UUID uuid, Object value) {
        Integer count = Integer.valueOf("" + value);
        playerData.put(uuid, count); if (playerData.get(uuid) >= clearCount) playerData.put(uuid, clearCount);
    }

    public String getItemId() {return itemId;}

    public void removeValue(UUID uuid) {
        playerData.remove(uuid);
    }

    public Integer getValue(UUID uuid) {return (playerData.get(uuid) == null) ? 0 : playerData.get(uuid);}

    public boolean isClearTask(UUID uuid) {
        if (getValue(uuid) >= clearCount) return true;
        else return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CraftTask craftTask = (CraftTask) o;
        return clearCount == craftTask.clearCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), clearCount);
    }
}
