package com.github.tanokun.tanorpg.event.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

public class WgEvents {
    static RegionContainer container;

    public static void setup() {
        container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        WorldGuard.getInstance().getPlatform().getSessionManager().registerHandler(Entry.factory, null);
    }

    @Nonnull
    public static Set<ProtectedRegion> getRegions(UUID playerUUID) {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null || !player.isOnline())
            return Collections.emptySet();

        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));
        return set.getRegions();
    }

    @Nonnull
    public static Set<String> getRegionsNames(UUID playerUUID) {
        return getRegions(playerUUID).stream().map(ProtectedRegion::getId).collect(Collectors.toSet());
    }

    public static boolean isPlayerInAllRegions(UUID playerUUID, Set<String> regionNames) {
        Set<String> regions = getRegionsNames(playerUUID);
        if (regions.isEmpty()) throw new IllegalArgumentException("You need to check for at least one region !");

        return regions.containsAll(regionNames.stream().map(String::toLowerCase).collect(Collectors.toSet()));
    }

    public static boolean isPlayerInAnyRegion(UUID playerUUID, Set<String> regionNames) {
        Set<String> regions = getRegionsNames(playerUUID);
        if (regions.isEmpty()) throw new IllegalArgumentException("You need to check for at least one region !");
        for (String region : regionNames) {
            if (regions.contains(region.toLowerCase()))
                return true;
        }
        return false;
    }

    public static boolean isPlayerInAnyRegion(UUID playerUUID, String... regionName) {
        return isPlayerInAnyRegion(playerUUID, new HashSet<>(Arrays.asList(regionName)));
    }

    public static boolean isPlayerInAllRegions(UUID playerUUID, String... regionName) {
        return isPlayerInAllRegions(playerUUID, new HashSet<>(Arrays.asList(regionName)));
    }

}
