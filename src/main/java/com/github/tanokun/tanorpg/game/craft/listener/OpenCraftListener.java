package com.github.tanokun.tanorpg.game.craft.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.craft.Craft;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OpenCraftListener implements Listener {

    @EventHandler
    public void openListener(NPCRightClickEvent e) {
        String id = TanoRPG.getPlugin().getCraftManager().getCraftId(e.getNPC().getId());
        if (id == null) return;
        Craft craft = TanoRPG.getPlugin().getCraftManager().getCraft(id);
        if (craft == null || TanoRPG.getPlugin().getMemberManager().getMember(e.getClicker().getUniqueId()) == null)
            return;

        if (e.getClicker().hasPermission(craft.getPermission())) craft.getInv().open(e.getClicker());

        TanoRPG.getPlugin().getSidebarManager().updateSidebar(e.getClicker(), TanoRPG.getPlugin().getMemberManager().getMember(e.getClicker().getUniqueId()));
    }
}
