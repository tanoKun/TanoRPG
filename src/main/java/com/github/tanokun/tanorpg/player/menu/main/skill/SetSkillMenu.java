package com.github.tanokun.tanorpg.player.menu.main.skill;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.player.menu.main.MainMenu;
import com.github.tanokun.tanorpg.player.skill.execute.Skill;
import com.github.tanokun.tanorpg.util.ItemUtilsKt;
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SetSkillMenu implements InventoryProvider {

    public SmartInventory getInv() {
        return SmartInventory.builder()
                .closeable(true)
                .cancelable(true)
                .provider(this)
                .size(5, 9)
                .title("§e§lスキル設定")
                .id("setKillMenu")
                .update(false)
                .build();
    }

    public void init(Player player, InventoryContents contents) {
        Member member = TanoRPG.getPlugin().getMemberManager().getMember(player.getUniqueId());
        TanoRPG.playSound(player, Sound.ENTITY_SHULKER_OPEN, 3, 1);

        ClickableItem side = ClickableItem.empty(ItemUtilsKt.createItem(Material.YELLOW_GLAZED_TERRACOTTA, "  ", 1, false));

        contents.fillBorders(ClickableItem.empty(ItemUtilsKt.createItem(Material.ORANGE_STAINED_GLASS_PANE, "  ", 1, false)));

        contents.set(0, 0, side);
        contents.set(0, 8, side);
        contents.set(4, 0, side);
        contents.set(4, 8, ClickableItem.of(ItemUtilsKt.createItem(Material.ARROW, "§c§l戻る", 1, false), e -> {
            new MainMenu().getInv(player).open(player);
        }));

        contents.set(0, 4, get1(member));
        contents.set(1, 1, get2(member));
        contents.set(1, 7, get3(member));
        contents.set(2, 3, get4(member));
        contents.set(2, 5, get5(member));
        contents.set(3, 1, get6(member));
        contents.set(3, 7, get7(member));
        contents.set(4, 4, get8(member));

    }

    private ClickableItem get1(Member member) {
        if (member.getLevel().getValue() < 1)
            return ClickableItem.empty(ItemUtilsKt.createItem(Material.BARRIER, "§cレベル1で開放されます", 1, false));
        Material material = Material.FLOWER_BANNER_PATTERN;
        int slot = 0;
        if (member.getSkillMap().getSlotSkills().get(slot) != null)
            material = TanoRPG.getPlugin().getSkillManager().getSkill(member.getSkillMap().getSlotSkills().get(0)).getItem();

        if (material == Material.FLOWER_BANNER_PATTERN) {
            return ClickableItem.of(ItemUtilsKt.createItem(material, "§bクリックで選択", 1, false), getConsumer(slot));
        }

        return ClickableItem.of(getSkillItem(TanoRPG.getPlugin().getSkillManager().getSkill(member.getSkillMap().getSlotSkills().get(slot))), getConsumer(slot));

    }

    private ClickableItem get2(Member member) {
        if (member.getLevel().getValue() < 5)
            return ClickableItem.empty(ItemUtilsKt.createItem(Material.BARRIER, "§cレベル5で開放されます", 1, false));
        Material material = Material.FLOWER_BANNER_PATTERN;
        int slot = 1;
        if (member.getSkillMap().getSlotSkills().get(slot) != null)
            material = TanoRPG.getPlugin().getSkillManager().getSkill(member.getSkillMap().getSlotSkills().get(0)).getItem();

        if (material == Material.FLOWER_BANNER_PATTERN) {
            return ClickableItem.of(ItemUtilsKt.createItem(material, "§bクリックで選択", 1, false), getConsumer(slot));
        }

        return ClickableItem.of(getSkillItem(TanoRPG.getPlugin().getSkillManager().getSkill(member.getSkillMap().getSlotSkills().get(slot))), getConsumer(slot));

    }

    private ClickableItem get3(Member member) {
        if (member.getLevel().getValue() < 10)
            return ClickableItem.empty(ItemUtilsKt.createItem(Material.BARRIER, "§cレベル10で開放されます", 1, false));
        Material material = Material.FLOWER_BANNER_PATTERN;
        int slot = 2;
        if (member.getSkillMap().getSlotSkills().get(slot) != null)
            material = TanoRPG.getPlugin().getSkillManager().getSkill(member.getSkillMap().getSlotSkills().get(0)).getItem();

        if (material == Material.FLOWER_BANNER_PATTERN) {
            return ClickableItem.of(ItemUtilsKt.createItem(material, "§bクリックで選択", 1, false), getConsumer(slot));
        }

        return ClickableItem.of(getSkillItem(TanoRPG.getPlugin().getSkillManager().getSkill(member.getSkillMap().getSlotSkills().get(slot))), getConsumer(slot));

    }

    private ClickableItem get4(Member member) {
        if (member.getLevel().getValue() < 15)
            return ClickableItem.empty(ItemUtilsKt.createItem(Material.BARRIER, "§cレベル15で開放されます", 1, false));
        Material material = Material.FLOWER_BANNER_PATTERN;
        int slot = 3;
        if (member.getSkillMap().getSlotSkills().get(slot) != null)
            material = TanoRPG.getPlugin().getSkillManager().getSkill(member.getSkillMap().getSlotSkills().get(0)).getItem();

        if (material == Material.FLOWER_BANNER_PATTERN) {
            return ClickableItem.of(ItemUtilsKt.createItem(material, "§bクリックで選択", 1, false), getConsumer(slot));
        }

        return ClickableItem.of(getSkillItem(TanoRPG.getPlugin().getSkillManager().getSkill(member.getSkillMap().getSlotSkills().get(slot))), getConsumer(slot));

    }

    private ClickableItem get5(Member member) {
        if (member.getLevel().getValue() < 20)
            return ClickableItem.empty(ItemUtilsKt.createItem(Material.BARRIER, "§cレベル20で開放されます", 1, false));
        Material material = Material.FLOWER_BANNER_PATTERN;
        int slot = 4;
        if (member.getSkillMap().getSlotSkills().get(slot) != null)
            material = TanoRPG.getPlugin().getSkillManager().getSkill(member.getSkillMap().getSlotSkills().get(0)).getItem();

        if (material == Material.FLOWER_BANNER_PATTERN) {
            return ClickableItem.of(ItemUtilsKt.createItem(material, "§bクリックで選択", 1, false), getConsumer(slot));
        }

        return ClickableItem.of(getSkillItem(TanoRPG.getPlugin().getSkillManager().getSkill(member.getSkillMap().getSlotSkills().get(slot))), getConsumer(slot));

    }

    private ClickableItem get6(Member member) {
        if (member.getLevel().getValue() < 30)
            return ClickableItem.empty(ItemUtilsKt.createItem(Material.BARRIER, "§cレベル30で開放されます", 1, false));
        Material material = Material.FLOWER_BANNER_PATTERN;
        int slot = 5;
        if (member.getSkillMap().getSlotSkills().get(slot) != null)
            material = TanoRPG.getPlugin().getSkillManager().getSkill(member.getSkillMap().getSlotSkills().get(0)).getItem();

        if (material == Material.FLOWER_BANNER_PATTERN) {
            return ClickableItem.of(ItemUtilsKt.createItem(material, "§bクリックで選択", 1, false), getConsumer(slot));
        }

        return ClickableItem.of(getSkillItem(TanoRPG.getPlugin().getSkillManager().getSkill(member.getSkillMap().getSlotSkills().get(slot))), getConsumer(slot));

    }

    private ClickableItem get7(Member member) {
        if (member.getLevel().getValue() < 40)
            return ClickableItem.empty(ItemUtilsKt.createItem(Material.BARRIER, "§cレベル40で開放されます", 1, false));
        Material material = Material.FLOWER_BANNER_PATTERN;
        int slot = 6;
        if (member.getSkillMap().getSlotSkills().get(slot) != null)
            material = TanoRPG.getPlugin().getSkillManager().getSkill(member.getSkillMap().getSlotSkills().get(0)).getItem();

        if (material == Material.FLOWER_BANNER_PATTERN) {
            return ClickableItem.of(ItemUtilsKt.createItem(material, "§bクリックで選択", 1, false), getConsumer(slot));
        }

        return ClickableItem.of(getSkillItem(TanoRPG.getPlugin().getSkillManager().getSkill(member.getSkillMap().getSlotSkills().get(slot))), getConsumer(slot));

    }

    private ClickableItem get8(Member member) {
        if (member.getLevel().getValue() < 50)
            return ClickableItem.empty(ItemUtilsKt.createItem(Material.BARRIER, "§cレベル50で開放されます", 1, false));
        Material material = Material.FLOWER_BANNER_PATTERN;
        int slot = 7;
        if (member.getSkillMap().getSlotSkills().get(slot) != null)
            material = TanoRPG.getPlugin().getSkillManager().getSkill(member.getSkillMap().getSlotSkills().get(0)).getItem();

        if (material == Material.FLOWER_BANNER_PATTERN) {
            return ClickableItem.of(ItemUtilsKt.createItem(material, "§bクリックで選択", 1, false), getConsumer(slot));
        }

        return ClickableItem.of(getSkillItem(TanoRPG.getPlugin().getSkillManager().getSkill(member.getSkillMap().getSlotSkills().get(slot))), getConsumer(slot));

    }

    private Consumer<InventoryClickEvent> getConsumer(int slot) {
        return e -> new SelSkillMenu(slot).getInv().open((Player) e.getWhoClicked());
    }

    private ItemStack getSkillItem(Skill skill) {
        List<String> lore = new ArrayList<>();
        lore.add("§e〇=-=-=-=-=§b説明§e=-=-=-=-=-〇");
        lore.addAll(skill.getLore());
        lore.add("§e〇=-=-=-=-=-=-=-=-=-=-=-=-=-〇");
        lore.add("§7必要コンボ: §b" + skill.getCombo().getCombos());
        lore.add("§7クールタイム: §b" + skill.getCt() + "秒");
        ItemStack item = new ItemStack(skill.getItem());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6§l「" + skill.getName() + "」");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.MENDING, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);
        return item;
    }
}
