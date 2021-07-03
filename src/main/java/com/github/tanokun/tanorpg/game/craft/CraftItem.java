package com.github.tanokun.tanorpg.game.craft;

import com.github.tanokun.tanorpg.util.ItemUtils;
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class CraftItem implements InventoryProvider {
    private final ItemStack afterItem;

    private final ArrayList<ItemStack> necItems;

    private final ArrayList<ItemStack> necTools;

    private final long price;

    private final String permission;

    public SmartInventory getInv(){
        return SmartInventory.builder()
                .id(afterItem.getItemMeta().getDisplayName())
                .title("§d§lクラフト確認")
                .update(false)
                .provider(this)
                .size(6, 9)
                .build();
    }

    public CraftItem(String id, String name, ItemStack afterItem, ArrayList<ItemStack> beforeItems, ArrayList<ItemStack> necTools, long price, boolean can) {
        this.afterItem = afterItem;
        this.necItems = beforeItems;
        this.necTools = necTools;
        this.price = price;
        this.permission = can ? "craft." + id + "." + name + "." + afterItem.getItemMeta().getDisplayName() : "";
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(ItemUtils.createItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, " ", 1, false)));
        contents.set(3, 7, ClickableItem.empty(afterItem));
        contents.set(3, 5, ClickableItem.empty(ItemUtils.createItem(Material.ARROW, "§b§l作成後", 1, true, 1)));
        contents.set(5, 8, ClickableItem.of(ItemUtils.createItem(Material.ANVIL, "§b§lクラフトする", 1, true, 1), e -> {

        }));

        contents.fillRect(2, 1, 4, 3, ClickableItem.empty(new ItemStack(Material.AIR)));
        for (int i = 0; i < necItems.size(); i++) {
            contents.add(ClickableItem.empty(necItems.get(i - 1)));
        }

        contents.fillRect(0, 1, 0, 3, ClickableItem.empty(new ItemStack(Material.AIR)));
        for (int i = 0; i < necTools.size(); i++) {
            contents.add(ClickableItem.empty(necTools.get(i - 1)));
        }
    }

    public ItemStack getAfterItem() {
        return afterItem;
    }

    public ArrayList<ItemStack> getNecItems() {
        return necItems;
    }

    public ArrayList<ItemStack> getNecTools() {
        return necTools;
    }

    public long getPrice() {
        return price;
    }

    public String getPermission() {
        return permission;
    }

    public boolean isPermission(){
        return permission.equals("");
    }
}
