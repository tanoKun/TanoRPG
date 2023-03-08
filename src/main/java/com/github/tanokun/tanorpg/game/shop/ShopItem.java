package com.github.tanokun.tanorpg.game.shop;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.event.tanorpg.TanoRpgShopEvent;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.util.ItemUtilsKt;
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;

public class ShopItem implements InventoryProvider {
    private ItemStack product;

    private int price;

    private Permission permission;

    public ShopItem(ItemStack item, int price, Permission permission) {
        this.price = price;
        this.product = item;
        this.permission = permission;
        if (Bukkit.getPluginManager().getPermission(permission.getName()) == null)
            Bukkit.getPluginManager().addPermission(this.permission);
    }

    public SmartInventory getInv() {
        return SmartInventory.builder()
                .id(product.getItemMeta().getDisplayName())
                .title("§9§l購入確認")
                .update(false)
                .provider(this)
                .size(3, 9)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(ItemUtilsKt.createItem(Material.YELLOW_STAINED_GLASS_PANE, " ", 1, false)));

        Member member = TanoRPG.getPlugin().getMemberManager().getMember(player.getUniqueId());

        contents.set(1, 4, ClickableItem.of(product, e -> {
            int lastAmount = e.getCurrentItem().getAmount();
            if (lastAmount == 64) {
                e.getCurrentItem().setAmount(1);
                return;
            }

            lastAmount = lastAmount * 2;
            e.getCurrentItem().setAmount(lastAmount);

            int finalLastAmount = lastAmount;
            contents.set(2, 8, ClickableItem.of(ItemUtilsKt.createItem(Material.EMERALD
                    , "§b§l購入する (" + price * lastAmount + " " + TanoRPG.MONEY + ")", 1, true, 1), e2 -> {
                if (member.getMoney() < price * finalLastAmount) {
                    player.sendMessage(TanoRPG.PX + "§cお金が足りません");
                    TanoRPG.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    contents.inventory().close(player);
                    return;
                }
                member.removeMoney(price * finalLastAmount);
                product.setAmount(finalLastAmount);
                player.getInventory().addItem(product);
                player.sendMessage(TanoRPG.PX + "購入しました");
                TanoRPG.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                product.setAmount(1);
            }));
        }));

        contents.set(2, 8, ClickableItem.of(ItemUtilsKt.createItem(Material.EMERALD
                , "§b§l購入する (" + price + " " + TanoRPG.MONEY + ")", 1, true, 1), e -> {
            if (member.getMoney() < price) {
                player.sendMessage(TanoRPG.PX + "§cお金が足りません");
                TanoRPG.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                contents.inventory().close(player);
                return;
            }
            member.removeMoney(price);
            product.setAmount(1);
            ItemUtilsKt.addItem(player, product);
            player.sendMessage(TanoRPG.PX + "購入しました");
            Bukkit.getPluginManager().callEvent(new TanoRpgShopEvent(player, member, this));
            TanoRPG.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }));
    }

    public int getPrice() {
        return price;
    }

    public ItemStack getProduct() {
        return product;
    }

    public Permission getPermission() {
        return permission;
    }
}
