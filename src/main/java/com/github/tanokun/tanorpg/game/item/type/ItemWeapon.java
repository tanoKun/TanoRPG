package com.github.tanokun.tanorpg.game.item.type;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.game.item.ItemRarityType;
import com.github.tanokun.tanorpg.game.item.ItemType;
import com.github.tanokun.tanorpg.game.item.status.ItemStatus;
import com.github.tanokun.tanorpg.game.item.type.base.ItemBase;
import com.github.tanokun.tanorpg.game.item.type.base.ItemData;
import com.github.tanokun.tanorpg.player.status.KindOfStatusType;
import com.github.tanokun.tanorpg.player.status.StatusMap;
import com.github.tanokun.tanorpg.util.ItemUtilsKt;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemWeapon extends ItemBase {

    public ItemWeapon(String id, Material material, String name, List<String> lore, ItemStatus statuses, boolean glowing, ItemRarityType itemRarityType) {
        super(id, material, name, lore, statuses, glowing, itemRarityType);
    }

    public ItemType getItemType() {
        return ItemType.WEAPON;
    }

    public ItemStack init(int amount, double p, boolean max) {
        ItemStack result = new ItemStack(getMaterial());
        ItemMeta im = result.getItemMeta();

        StatusMap statusMap = max ? getBasicStatuses().generateMax() : getBasicStatuses().generate(p);

        List<String> statuses = TanoRPG.getPlugin().getItemManager().fromNormalStatus(statusMap);

        im.setDisplayName(getRarity().getColor() + getDisplayName());
        List<String> lore = new ArrayList<>();
        lore.add(ItemManager.LORE);
        getLore().stream().forEach(lore::add);
        lore.add(ItemManager.FIRST_STATUS);
        statuses.stream().forEach(lore::add);
        lore.add(" ");
        lore.add("§7レアリティ: " + getRarity().getName());
        lore.add("§7対応職業: " + getProper());
        lore.add("§7レベル制限: " + getNecLevel());
        lore.add("§7CT: " + getCoolTime());
        lore.add("§7タイプ: " + getItemType().getName());
        lore.add(" ");
        lore.add("§7耐久値: (" + getMaxDurabilityValue() + "/" + getMaxDurabilityValue() + ")");
        lore.add(ItemManager.FINAL_STATUS);
        lore.add(KindOfStatusType.EVOLUTION + "§d§l進化回数: 0");
        lore.add(KindOfStatusType.RUNE + "§b§lルーン");
        lore.add("§f - なし");
        lore.add("§f - 未開放");
        lore.add("§f - 未開放");
        lore.add("§f - 未開放");
        im.setLore(lore);

        if (isGlowing()) {
            im.addEnchant(Enchantment.MENDING, 1, true);
        }
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);

        if (getCustomModelData() != 0) {
            im.setCustomModelData(getCustomModelData());
        }

        im.setUnbreakable(true);
        result.setItemMeta(im);
        result.setAmount(amount);

        return initData(result, statusMap);
    }

    @Override
    public ItemStack init(int amount, double p, boolean max, StatusMap temp) {
        ItemStack result = new ItemStack(getMaterial());
        ItemMeta im = result.getItemMeta();

        StatusMap statusMap = max ? getBasicStatuses().generateMax() : getBasicStatuses().generate(p);
        statusMap.addAllStatuses(temp.getHasStatuses());
        List<String> statuses = TanoRPG.getPlugin().getItemManager().fromNormalStatus(statusMap);

        im.setDisplayName(getRarity().getColor() + getDisplayName());
        List<String> lore = new ArrayList<>();
        lore.add(ItemManager.LORE);
        getLore().stream().forEach(lore::add);
        lore.add(ItemManager.FIRST_STATUS);
        statuses.stream().forEach(lore::add);
        lore.add(" ");
        lore.add("§7レアリティ: " + getRarity().getName());
        lore.add("§7対応職業: " + getProper());
        lore.add("§7レベル制限: " + getNecLevel());
        lore.add("§7CT: " + getCoolTime());
        lore.add("§7タイプ: " + getItemType().getName());
        lore.add(" ");
        lore.add("§7耐久値: (" + getMaxDurabilityValue() + "/" + getMaxDurabilityValue() + ")");
        lore.add(ItemManager.FINAL_STATUS);
        lore.add(KindOfStatusType.EVOLUTION + "§d§l進化回数: 0");
        lore.add(KindOfStatusType.RUNE + "§b§lルーン");
        lore.add("§f - なし");
        lore.add("§f - 未開放");
        lore.add("§f - 未開放");
        lore.add("§f - 未開放");
        im.setLore(lore);

        if (isGlowing()) {
            im.addEnchant(Enchantment.MENDING, 1, true);
        }
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);

        if (getCustomModelData() != 0) {
            im.setCustomModelData(getCustomModelData());
        }

        im.setUnbreakable(true);
        result.setItemMeta(im);
        result.setAmount(amount);

        return initData(result, statusMap);
    }

    public ItemStack initData(ItemStack itemStack, StatusMap statusMap) {
        ItemUtilsKt.setItemData(itemStack, new ItemData(this, statusMap));
        return itemStack;
    }
}
