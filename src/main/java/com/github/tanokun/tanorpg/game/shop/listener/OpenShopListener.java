package com.github.tanokun.tanorpg.game.shop.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.shop.Shop;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OpenShopListener implements Listener {

    @EventHandler
    public void openListener(NPCRightClickEvent e) {
        String id = TanoRPG.getPlugin().getShopManager().getShopId(e.getNPC().getId());
        if (id == null) return;
        Shop shop = TanoRPG.getPlugin().getShopManager().getShop(id);
        if (shop == null || TanoRPG.getPlugin().getMemberManager().getMember(e.getClicker().getUniqueId()) == null)
            return;

        if (e.getClicker().hasPermission(shop.getPermission())) shop.getInv().open(e.getClicker());

        TanoRPG.getPlugin().getSidebarManager().updateSidebar(e.getClicker(), TanoRPG.getPlugin().getMemberManager().getMember(e.getClicker().getUniqueId()));
    }
}
