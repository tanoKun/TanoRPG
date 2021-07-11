package com.github.tanokun.tanorpg.game.item.inv;

import com.github.tanokun.tanorpg.game.item.type.base.ItemData;
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SelRuneItem implements InventoryProvider {
    private ItemStack item;
    private ItemData itemData;

    public SmartInventory getInv() {
        return SmartInventory.builder()
                .id("SelRuneItem")
                .title("§b§lルーン設定")
                .update(false)
                .provider(this)
                .size(3, 9)
                .build();
    }



    @Override
    public void init(Player player, InventoryContents contents) {

    }
}
