package com.github.tanokun.tanorpg.game.item.drop;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.util.smart_inv.inv.InventoryListener;
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class NoneInventory implements InventoryProvider {
    public SmartInventory getInv() {
        return SmartInventory.builder()
                .id("NoneInventory")
                .title("§d§l既に漁られている...")
                .update(false)
                .provider(this)
                .cancelable(true)
                .size(6, 9)
                .listener(new InventoryListener<>(InventoryCloseEvent.class,
                        e -> TanoRPG.playSound((Player) e.getPlayer(), Sound.BLOCK_CHEST_CLOSE, 2, 1)))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        TanoRPG.playSound(player, Sound.BLOCK_CHEST_OPEN, 2, 1);
    }
}
