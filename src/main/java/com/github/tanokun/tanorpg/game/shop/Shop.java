package com.github.tanokun.tanorpg.game.shop;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.util.ItemUtils;
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Shop implements InventoryProvider {
    private final String id;

    private final String name;

    private final ArrayList<ShopItem> items;

    private final int npcId;

    private final String permission;

    public Shop(String id, String name, ArrayList<ShopItem> items, boolean can, int npcId) {
        this.id = id;
        this.name = name;
        this.items = items;
        this.npcId = npcId;
        this.permission = can ? "shop." + id : "";
        TanoRPG.getPlugin().getDataManager().getPermissions().add(permission);
    }

    public SmartInventory getInv(){
        return SmartInventory.builder()
                .id(id)
                .title("§6§lショップ「" + name + "§6§l」")
                .update(false)
                .provider(this)
                .size(5, 9)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(ItemUtils.createItem(Material.YELLOW_STAINED_GLASS_PANE, " ", 1, false)));
        Member member = TanoRPG.getPlugin().getMemberManager().getMember(player.getUniqueId());

        ArrayList<ShopItem> barrier = new ArrayList<>();
        items.stream().forEach(item -> {
            if (!item.isPermission() || player.isOp()) {

                contents.add(ClickableItem.of(item.getItemStack(), e -> {
                    TanoRPG.playSound(player, Sound.ENTITY_SHULKER_OPEN, 3, 1);
                    item.getInv().open(player);
                }));

            } else {
                if (!member.getOpenPermissionMap().hasPermission(item.getPermission())) barrier.add(item);

                else
                    contents.add(ClickableItem.of(item.getItemStack(), e -> {
                        TanoRPG.playSound(player, Sound.ENTITY_SHULKER_OPEN, 3, 1);
                        item.getInv().open(player);
                    }));
            }
        });

        barrier.stream().forEach(item -> {
            contents.add(ClickableItem.of(ItemUtils.createItem(Material.BARRIER, item.getItemStack().getItemMeta().getDisplayName(), 1, false), e -> {
                TanoRPG.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 3, 1);
                player.sendMessage(TanoRPG.PX + "§cそのショップは開放されていません");
            }));
        });
    }

    public ArrayList<ShopItem> getItems() {
        return items;
    }

    public String getPermission() {
        return permission;
    }

    public boolean isPermission(){
        return !permission.equals("");
    }

    public int getNpcId() {
        return npcId;
    }

    public String getName() {
        return name;
    }
}
