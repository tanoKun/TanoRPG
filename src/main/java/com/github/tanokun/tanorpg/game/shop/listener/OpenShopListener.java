package com.github.tanokun.tanorpg.game.shop.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.shop.Shop;
import com.github.tanokun.tanorpg.player.Member;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OpenShopListener implements Listener {

    @EventHandler
    public void onSpeakToNPC(NPCRightClickEvent e){
        Shop shop = TanoRPG.getPlugin().getShopManager().getShop(TanoRPG.getPlugin().getShopManager().getShopId(e.getNPC().getId()));

        Member member = TanoRPG.getPlugin().getMemberManager().getMember(e.getClicker().getUniqueId());

        if (shop == null || member == null) return;

        if (!shop.isPermission() || e.getClicker().isOp()) shop.getInv().open(e.getClicker());

        if (member.getPermissionMap().hasPermission(shop.getPermission())) shop.getInv().open(e.getClicker());

        TanoRPG.getPlugin().getSidebarManager().updateSidebar(e.getClicker(), member);
    }
}
