package com.github.tanokun.tanorpg.event.worldguard;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;
import com.github.tanokun.tanorpg.event.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.util.Set;

public class Entry extends Handler implements Listener {

    public final PluginManager pm = Bukkit.getPluginManager();
    public static final Factory factory = new Factory();

    public static class Factory extends Handler.Factory<Entry> {
        @Override
        public Entry create(Session session) {
            return new Entry(session);
        }
    }

    public Entry(Session session) {
        super(session);
    }

    @Override
    public boolean onCrossBoundary(LocalPlayer player, Location from, Location to, ApplicableRegionSet toSet, Set<ProtectedRegion> entered, Set<ProtectedRegion> left, MoveType moveType)
    {
        WgRegionsChangeEvent rce = new WgRegionsChangeEvent(player.getUniqueId(), left, entered);
        pm.callEvent(rce);
        if(rce.isCancelled()) return false;

        WgRegionsEnterEvent ree = new WgRegionsEnterEvent(player.getUniqueId(), entered);
        pm.callEvent(ree);
        if(ree.isCancelled()) return false;

        WgRegionsLeftEvent rle = new WgRegionsLeftEvent(player.getUniqueId(), left);
        pm.callEvent(rle);
        if(rle.isCancelled()) return false;

        for(ProtectedRegion r : entered) {
            WgRegionEnterEvent regentered = new WgRegionEnterEvent(player.getUniqueId(), r);
            pm.callEvent(regentered);
            if(regentered.isCancelled()) return false;
        }


        for(ProtectedRegion r : left) {
            WgRegionLeftEvent regleft = new WgRegionLeftEvent(player.getUniqueId(), r);
            pm.callEvent(regleft);
            if(regleft.isCancelled()) return false;
        }
        return true;
    }




}
