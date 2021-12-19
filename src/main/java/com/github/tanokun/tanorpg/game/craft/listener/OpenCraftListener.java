package com.github.tanokun.tanorpg.game.craft.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.craft.Craft;
import com.github.tanokun.tanorpg.player.Member;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OpenCraftListener implements Listener {

    @EventHandler
    public void onSpeakToNPC(NPCRightClickEvent e){
        Craft craft = TanoRPG.getPlugin().getCraftManager().getCraft(TanoRPG.getPlugin().getCraftManager().getCraftId(e.getNPC().getId()));

        Member member = TanoRPG.getPlugin().getMemberManager().getMember(e.getClicker().getUniqueId());

        if (craft == null || member == null) return;

        if (!craft.isPermission() || e.getClicker().isOp()) craft.getInv().open(e.getClicker());
        if (member.getPermissionMap().hasPermission(craft.getPermission())) craft.getInv().open(e.getClicker());
    }
}
