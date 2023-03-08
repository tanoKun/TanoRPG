package com.github.tanokun.tanorpg.player.warppoint.inv;

import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider;
import org.bukkit.entity.Player;

public class WarpPointMenu implements InventoryProvider {
    private final Member m;

    public WarpPointMenu(Member member) {
        m = member;
    }

    public static SmartInventory getInv(Member m) {
        return SmartInventory.builder()
                .closeable(true)
                .provider(new WarpPointMenu(m))
                .size(1, 9)
                .title("§dクエスト受注リスト")
                .id("questListMenu")
                .update(false)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {

    }
}
