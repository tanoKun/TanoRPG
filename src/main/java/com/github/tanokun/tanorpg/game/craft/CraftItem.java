package com.github.tanokun.tanorpg.game.craft;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.event.tanorpg.TanoRpgCraftEvent;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.util.ItemUtils;
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;

public class CraftItem implements InventoryProvider {
    private final ArrayList<CraftRecipe> recipe;

    private final ItemStack afterItem;

    public SmartInventory getInv(){
        return SmartInventory.builder()
                .id(afterItem.getItemMeta().getDisplayName())
                .title("§9§lクラフト確認")
                .update(false)
                .provider(this)
                .size(6, 9)
                .build();
    }

    public CraftItem(ItemStack afterItem, final ArrayList<CraftRecipe> recipe) {
        this.afterItem = afterItem;
        this.recipe = recipe;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(ItemUtils.createItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, " ", 1, false)));
        contents.set(3, 7, ClickableItem.empty(afterItem));
        contents.set(3, 5, ClickableItem.empty(ItemUtils.createItem(Material.ARROW, "§b§l作成後", 1, true, 1)));
        contents.set(5, 8, ClickableItem.of(ItemUtils.createItem(Material.ANVIL, "§b§lクラフトする", 1, true, 1), e -> {
                    Member member = TanoRPG.getPlugin().getMemberManager().getMember(player.getUniqueId());
                }));
            /*
            if (player.hasMetadata("crafting")){
                player.sendMessage(TanoRPG.PX + "§cクラフト中...");
                TanoRPG.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                return;
            }
            for (ItemStack item : necItems) {
                if (ItemUtils.getAmount(player, item) < item.getAmount()) {
                    player.sendMessage(TanoRPG.PX + "§c必要素材が足りません");
                    TanoRPG.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    contents.inventory().close(player);
                    return;
                }
            }
            for (ItemStack item : necTools) {
                if (ItemUtils.getAmount(player, item) < item.getAmount()) {
                    player.sendMessage(TanoRPG.PX + "§c必要道具が足りません");
                    TanoRPG.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    contents.inventory().close(player);
                    return;
                }
            }
            if (member.getMoney() < price) {
                player.sendMessage(TanoRPG.PX + "§cお金が足りません");
                TanoRPG.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                contents.inventory().close(player);
                return;
            }

            member.removeMoney(price);
            for (ItemStack item : necItems) {
                int toDelete = item.getAmount();
                while (true) {
                    ItemStack itemStack = ItemUtils.getSameItem(player, item);
                    int amount = itemStack.getAmount();
                    itemStack.setAmount(amount - toDelete);
                    toDelete -= amount;
                    if (toDelete <= 0) break;
                }
            }

            player.setMetadata("crafting", new FixedMetadataValue(TanoRPG.getPlugin(), true));
            TanoRPG.playSound(player, Sound.BLOCK_ANVIL_DESTROY, 10, 1);
            Bukkit.getScheduler().runTaskLater(TanoRPG.getPlugin(), () -> {
                player.sendMessage(TanoRPG.PX + "クラフトが完了しました");
                ItemUtils.addItem(player, afterItem);
                player.removeMetadata("crafting", TanoRPG.getPlugin());
                TanoRPG.getPlugin().getSidebarManager().updateSidebar(player, member);
                Bukkit.getPluginManager().callEvent(new TanoRpgCraftEvent(player, member, this));
            }, 15);
        }));

        contents.fillRect(0, 1, 0, 3, ClickableItem.empty(new ItemStack(Material.AIR)));
        contents.set(3, 2, ClickableItem.empty(new ItemStack(Material.AIR)));
        for (int i = 1; i < necTools.size() + 1; i++) {
            contents.set(0, i, ClickableItem.empty(necTools.get(i - 1)));
        }

        contents.fillRect(2, 1, 4, 3, ClickableItem.empty(new ItemStack(Material.AIR)));
        int i2 = 0;
        for (int i = 1; i < 26; i++) {
            int row = (18 + i) / 9;
            int column = (18 + i) % 9;
            if (row != 2 && row != 3 && row != 4) continue;
            if (column != 1 && column != 2 && column != 3) continue;
            contents.set(row, column, ClickableItem.empty(necItems.get(i2)));
            if (necItems.size() <= i2 + 1) return;
            i2++;
        }
             */
    }

    public ItemStack getAfterItem() {
        return afterItem;
    }

    public ArrayList<CraftRecipe> getRecipe() {
        return recipe;
    }

    public boolean isAnyOpen(Member member) {
        for (CraftRecipe craftRecipe : getRecipe()) {
            if (!craftRecipe.isPermission()) return true;
            if (member.getPermissionMap().hasPermission(craftRecipe.getPermission())) return true;
        }

        return false;
    }

    public static class CraftRecipe {
        private final ArrayList<ItemStack> necItems;

        private final ArrayList<ItemStack> necTools;

        private final long price;

        private final String permission;

        private final int count;

        public CraftRecipe(ArrayList<ItemStack> necItems, ArrayList<ItemStack> necTools, long price, int count, boolean can, ItemStack afterItem, String id, String id2) {
            this.necItems = necItems;
            this.necTools = necTools;
            this.price = price;
            this.count = count;
            this.permission = can ? "craftItem." + id + "." + id2 + "." + ItemUtils.getItemData(afterItem).getId() : "";
            TanoRPG.getPlugin().getDataManager().getPermissions().add(permission);
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

        public boolean isPermission() {
            return !permission.equals("");
        }
    }
}
