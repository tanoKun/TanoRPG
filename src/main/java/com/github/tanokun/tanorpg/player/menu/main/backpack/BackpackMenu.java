package com.github.tanokun.tanorpg.player.menu.main.backpack;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.util.ItemUtilsKt;
import com.github.tanokun.tanorpg.util.SaveMarker;
import com.github.tanokun.tanorpg.util.io.Config;
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.tanorpg.util.smart_inv.inv.InventoryListener;
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.Pagination;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.SlotIterator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class BackpackMenu implements InventoryProvider, SaveMarker<BackpackMenu> {
    private UUID uuid;

    private ArrayList<ArrayList<ItemStack>> backpackList = new ArrayList<>();

    private InventoryContents contents;

    private boolean isEnterRegion = false;

    public BackpackMenu(UUID uuid) {
        this.uuid = uuid;
        ArrayList<ItemStack> ec = new ArrayList<>();
        for (int i = 0; i < 45; i++) ec.add(new ItemStack(Material.AIR));
        backpackList.add(ec);
    }

    public SmartInventory getInv() {
        return SmartInventory.builder()
                .closeable(true)
                .cancelable(false)
                .provider(this)
                .size(6, 9)
                .title("§7§l" + Bukkit.getOfflinePlayer(uuid).getName() + "'s Backpack")
                .id("Backpack_" + Bukkit.getOfflinePlayer(uuid).getName())
                .update(false)
                .listener(getCloseListener())
                .build();
    }

    public void init(Player player, InventoryContents contents) {
        this.contents = contents;
        contents.fillRow(5, ClickableItem.of(ItemUtilsKt.createItem(Material.BLACK_STAINED_GLASS_PANE, "", 1, false), e -> e.setCancelled(true)));

        Pagination pagination = contents.pagination();
        ClickableItem[] items = new ClickableItem[this.backpackList.size() * 45];
        for (int n = 0; n < items.length; n++)
            items[n] = ClickableItem.empty(backpackList.get(n / 45).get(n % 45));

        pagination.setItems(items);
        pagination.setItemsPerPage(45);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

        arrow(player, contents);
    }

    private void arrow(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();

        if (pagination.getPage() == 0)
            contents.set(5, 0, ClickableItem.of(ItemUtilsKt.createItem(Material.BLACK_STAINED_GLASS_PANE, "", 1, false), e -> e.setCancelled(true)));
        else
            contents.set(5, 0, ClickableItem.of(ItemUtilsKt.createItem(Material.SPECTRAL_ARROW,
                    pagination.getPage() + "ページに戻る", 1, true), e -> {
                contents.inventory().open(player, pagination.getPage() - 1);
                TanoRPG.playSound(player, Sound.ENTITY_SHULKER_OPEN, 10, 1);
            }));

        if (pagination.isLast())
            contents.set(5, 8, ClickableItem.of(ItemUtilsKt.createItem(Material.BLACK_STAINED_GLASS_PANE, "", 1, false), e -> e.setCancelled(true)));
        else
            contents.set(5, 8, ClickableItem.of(ItemUtilsKt.createItem(Material.SPECTRAL_ARROW,
                    (pagination.getPage() + 2) + "ページに進む", 1, true), e -> {
                contents.inventory().open(player, pagination.getPage() + 1);
                TanoRPG.playSound(player, Sound.ENTITY_SHULKER_OPEN, 10, 1);
            }));
    }

    private InventoryListener<InventoryCloseEvent> getCloseListener() {
        return new InventoryListener<>(InventoryCloseEvent.class, e -> {
            int p = contents.pagination().getPage();
            for (int i = 0; i < 45; i++) backpackList.get(p).set(i, e.getInventory().getContents()[i]);
            save(new Config(TanoRPG.getPlugin(), "player_database" + File.separator + uuid.toString() + ".yml"), "");
        });
    }

    public void addPage() {
        ArrayList<ItemStack> page = new ArrayList<>();
        for (int i = 0; i < 45; i++) page.add(new ItemStack(Material.AIR));
        backpackList.add(page);
        save(new Config(TanoRPG.getPlugin(), "player_database" + File.separator + uuid.toString() + ".yml"), "");
    }

    public void removePage() {
        backpackList.remove(backpackList.size() - 1);
        save(new Config(TanoRPG.getPlugin(), "player_database" + File.separator + uuid.toString() + ".yml"), "");
    }

    public ArrayList<ArrayList<ItemStack>> getBackpackList() {
        return backpackList;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setEnterRegion(boolean enterRegion) {
        isEnterRegion = enterRegion;
    }

    public boolean isEnterRegion() {
        return isEnterRegion;
    }

    @Override
    public void save(Config config, String key) {
        config.createConfig();
        int i = 0;
        for (ArrayList<ItemStack> backpack : this.getBackpackList()) {
            config.getConfig().set("backpack." + i, backpack);
            i++;
        }
        config.saveConfig();
    }

    @Override
    public BackpackMenu load(Config config, String key) {
        if (!config.getConfig().isSet("backpack.0")) return this;
        this.backpackList = new ArrayList<>();
        config.getConfig().getConfigurationSection("backpack").getKeys(false).forEach(text -> {
            this.backpackList.add((ArrayList<ItemStack>) config.getConfig().getList("backpack." + text, new ArrayList<ItemStack>()));
        });
        return this;
    }
}