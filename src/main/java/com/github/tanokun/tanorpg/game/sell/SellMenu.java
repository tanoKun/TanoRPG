package com.github.tanokun.tanorpg.game.sell;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.item.type.base.ItemData;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.util.ItemUtils;
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.tanorpg.util.smart_inv.inv.InventoryListener;
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;

public class SellMenu implements InventoryProvider {
    private Member member;

    public SmartInventory getInv(){
        return SmartInventory.builder()
                .id("SellMenu")
                .title("§d§l売却")
                .update(true)
                .updatePeriod(10)
                .provider(this)
                .cancelable(false)
                .size(6, 9)
                .listener(new InventoryListener<>(InventoryCloseEvent.class, e -> {
            if (e.getPlayer().hasMetadata("selling")){
                e.getPlayer().removeMetadata("selling", TanoRPG.getPlugin());
                return;
            }
            for (int i = 0; i < 53; i++) {
                if (e.getView().getItem(i) == null) continue;
                if (e.getView().getItem(i).getType().equals(Material.AIR)) continue;
                if (ItemUtils.getItemData(e.getView().getItem(i)) == null) continue;
                e.getPlayer().getInventory().addItem(e.getView().getItem(i));
            }
        }))
                .build();
    }

    public SellMenu(Member m) {
        member = m;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        ClickableItem BSG = ClickableItem.of(ItemUtils.createItem(Material.PURPLE_STAINED_GLASS_PANE, "    ", 1, false), e -> {
            e.setCancelled(true);
        });
        ClickableItem side = ClickableItem.of(ItemUtils.createItem(Material.PURPLE_GLAZED_TERRACOTTA, "  ", 1, false), e -> {
            e.setCancelled(true);
        });

        contents.fillBorders(BSG);
        contents.set(0, 0, side);
        contents.set(0, 8, side);
        contents.set(5, 0, side);
        contents.set(5, 8, side);

        contents.set(5, 4, ClickableItem.of(ItemUtils.createItem(Material.EMERALD, "§d§l合計値段: 0" + " " + TanoRPG.MONEY,
                Arrays.asList("§bクリックで売却する"), 1, true), e -> {
            e.setCancelled(true);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    "execute at " + player.getName() + " run playsound minecraft:shop.buy master @p ~ ~ ~ 3 1");
            long price = check(e.getClickedInventory().getContents());
            player.sendMessage(TanoRPG.PX + "§d売却しました！ (合計価格: " + price + ")");
            member.addMoney(price);
            player.setMetadata("selling", new FixedMetadataValue(TanoRPG.getPlugin(), true));
            contents.inventory().close(player);
            TanoRPG.getPlugin().getSidebarManager().updateSidebar(player, member);
        }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        long money = check(player.getOpenInventory().getTopInventory().getContents());
        ItemStack item = player.getOpenInventory().getItem(49);
        ItemMeta itemM = item.getItemMeta();
        itemM.setDisplayName("§d§l合計値段: " + money + " " + TanoRPG.MONEY);
        item.setItemMeta(itemM);
        player.getOpenInventory().setItem(49, item);
    }

    private static long check(ItemStack[] items){
        long price = 0;
        for (ItemStack item : items){
            if (item == null) continue;
            if (ItemUtils.getItemData(item) == null) continue;
            ItemData sell_item = ItemUtils.getItemData(item);
            price += sell_item.getPrice() * item.getAmount();
        }
        return price;
    }
}
