package com.github.tanokun.tanorpg.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.event.WgRegionEnterEvent;
import com.github.tanokun.tanorpg.event.WgRegionLeftEvent;
import com.github.tanokun.tanorpg.util.Meta;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RegionListener implements Listener {

    @EventHandler
    public void onEnter(WgRegionEnterEvent e) {
        if (e.getPlayer() == null) return;
        if (TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId()) == null) return;
        if (e.getRegionName().contains("bp"))
            TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId()).getBackpackMenu().setEnterRegion(true);
        else if (e.getRegionName().contains("tp")) Meta.setMetadata(e.getPlayer(), "tp", true);
    }

    @EventHandler
    public void onLeft(WgRegionLeftEvent e) {
        if (e.getPlayer() == null) return;
        if (TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId()) == null) return;
        if (e.getRegionName().contains("bp"))
            TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId()).getBackpackMenu().setEnterRegion(false);
        else if (e.getRegionName().contains("tp")) e.getPlayer().removeMetadata("tp", TanoRPG.getPlugin());
    }
}
