package com.github.tanokun.tanorpg.player.menu.main.skill;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.player.skill.execute.Skill;
import com.github.tanokun.tanorpg.player.skill.execute.SkillCombo;
import com.github.tanokun.tanorpg.util.ItemUtilsKt;
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.InventoryProvider;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.Pagination;
import com.github.tanokun.tanorpg.util.smart_inv.inv.contents.SlotIterator;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SelSkillMenu implements InventoryProvider {
    private int slot;

    public SelSkillMenu(int slot) {
        this.slot = slot;
    }

    public SmartInventory getInv() {
        return SmartInventory.builder()
                .closeable(true)
                .cancelable(true)
                .provider(this)
                .size(6, 9)
                .title("§e§lスキル選択")
                .id("selSkillMenu")
                .update(false)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();

        Member member = TanoRPG.getPlugin().getMemberManager().getMember(player.getUniqueId());
        TanoRPG.playSound(player, Sound.ENTITY_SHULKER_OPEN, 3, 1);

        contents.fillRow(4, ClickableItem.empty(ItemUtilsKt.createItem(Material.GRAY_STAINED_GLASS_PANE, "    ", 1, false)));

        ArrayList<SkillCombo> skills = new ArrayList<>();
        for (Skill skill : TanoRPG.getPlugin().getSkillManager().getSkills().values()) {
            if (skill.getJob().contains(member.getSkillClass())) skills.add(skill.getCombo());
        }

        ArrayList<ClickableItem> skills2 = new ArrayList<>();
        for (SkillCombo skillCombo : member.getSkillMap().getOpenSkills()) {
            Skill skill = TanoRPG.getPlugin().getSkillManager().getSkill(skillCombo);
            skills2.add(ClickableItem.of(getSkillItem(skill), e -> {
                TanoRPG.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 1);
                member.getSkillMap().getSlotSkills().set(slot, skillCombo);
                player.sendMessage(TanoRPG.PX + "スキルスロット" + (slot + 1) + "を「" + skill.getName() + "」に設定しました");
                new SetSkillMenu().getInv().open(player);
            }));
        }

        for (Skill skill : TanoRPG.getPlugin().getSkillManager().getSkills().values()) {
            if (!member.getSkillMap().getOpenSkills().contains(skill.getCombo()) && skill.getJob().contains(member.getSkillClass())) {
                skills2.add(ClickableItem.empty(ItemUtilsKt.createItem(Material.BARRIER, "まだ習得していません", 1, false)));
            }

            pagination.setItems(skills2.toArray(new ClickableItem[skills2.size()]));
            pagination.setItemsPerPage(36);
            pagination.addToIterator(contents.newIterator(SlotIterator.Impl.Type.HORIZONTAL, 0, 0));

            arrow(player, contents);
        }
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

    private void arrow(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();

        if (pagination.getPage() != 0)
            contents.set(5, 0, ClickableItem.of(ItemUtilsKt.createItem(Material.SPECTRAL_ARROW,
                    "§aPrevious Page §b-> " + (pagination.getPage()), 1, true), e -> {
                contents.inventory().open(player, pagination.getPage() - 1);
                TanoRPG.playSound(player, Sound.ENTITY_SHULKER_OPEN, 10, 1);
            }));

        if (!pagination.isLast())
            contents.set(5, 8, ClickableItem.of(ItemUtilsKt.createItem(Material.SPECTRAL_ARROW,
                    "§aNext Page §b-> " + (pagination.getPage() + 2), 1, true), e -> {
                contents.inventory().open(player, pagination.getPage() + 1);
                TanoRPG.playSound(player, Sound.ENTITY_SHULKER_OPEN, 10, 1);
            }));
    }
}
