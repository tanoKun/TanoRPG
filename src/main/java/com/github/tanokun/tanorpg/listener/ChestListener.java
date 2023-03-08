package com.github.tanokun.tanorpg.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.item.drop.ItemTable;
import com.github.tanokun.tanorpg.game.item.drop.NoneInventory;
import com.github.tanokun.tanorpg.player.ChestMap;
import com.github.tanokun.tanorpg.player.Member;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Objects;

public class ChestListener implements Listener {
    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getHand() == EquipmentSlot.HAND) {
            if (TanoRPG.getPlugin().getDropManager().getChest(e.getClickedBlock().getLocation()) == null) return;
            e.setCancelled(true);
            Member member = TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId());
            Location location = e.getClickedBlock().getLocation();
            if (!member.getChestMap().isSet(location)) {
                e.getPlayer().openInventory(TanoRPG.getPlugin().getDropManager().getChest(location).getItemTable().generate(1));
                TanoRPG.playSound(e.getPlayer(), Sound.BLOCK_CHEST_OPEN, 2, 1);
                member.getChestMap().getPlayerChests().put(location, new ChestMap.PlayerChest(TanoRPG.getPlugin().getDropManager().getChest(location), ZonedDateTime.now()));
            } else {
                if (member.getChestMap().getPlayerChests().get(location).canReopen()) {
                    e.getPlayer().openInventory(TanoRPG.getPlugin().getDropManager().getChest(location).getItemTable().generate(1));
                    TanoRPG.playSound(e.getPlayer(), Sound.BLOCK_CHEST_OPEN, 2, 1);
                    member.getChestMap().getPlayerChests().put(location, new ChestMap.PlayerChest(TanoRPG.getPlugin().getDropManager().getChest(location), ZonedDateTime.now()));
                } else {
                    new NoneInventory().getInv().open(e.getPlayer());
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (!(e.getInventory().getHolder() instanceof ItemTable.DropInventory)) return;

        TanoRPG.playSound((Player) e.getPlayer(), Sound.BLOCK_CHEST_CLOSE, 2, 1);

        Arrays.stream(e.getInventory().getContents()).filter(Objects::nonNull)
                .forEach(item -> e.getPlayer().getLocation().getWorld().dropItem(e.getPlayer().getLocation().add(0, 1, 0), item));
    }
}