package com.github.tanokun.tanorpg.event;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * @author Weby &amp; Anrza (info@raidstone.net)
 * @version 1.0.0
 * @since 2/24/19
 */
public class WgRegionEnterEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled = false;

    private final UUID uuid;
    private final ProtectedRegion region;
    private final String regionName;

    public WgRegionEnterEvent(UUID playerUUID, @NotNull ProtectedRegion region) {
        this.uuid = playerUUID;
        this.region = region;
        this.regionName = region.getId();
    }

    @Contract(pure = true)
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public UUID getUUID() {
        return uuid;
    }

    @Nullable
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    @NotNull
    public String getRegionName() {
        return regionName;
    }

    @NotNull
    public ProtectedRegion getRegion() {
        return region;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}