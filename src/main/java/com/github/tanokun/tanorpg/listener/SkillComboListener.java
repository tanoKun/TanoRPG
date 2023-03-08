package com.github.tanokun.tanorpg.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.player.skill.SkillComboType;
import com.github.tanokun.tanorpg.player.skill.execute.Skill;
import com.github.tanokun.tanorpg.player.skill.execute.SkillCombo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class SkillComboListener implements Listener {
    private Title.Times time = Title.Times.of(Ticks.duration(0L), Ticks.duration(100000000L), Ticks.duration(0L));

    @EventHandler
    public void onChange(PlayerSwapHandItemsEvent e) {
        e.setCancelled(true);

        Member member = TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId());
        if (member == null) return;

        if (!member.getAttack().isSkillComboTrigger()) {
            member.getAttack().getSkillCombos().clear();
            member.getAttack().setSkillComboTrigger(true);
            e.getPlayer().showTitle(Title.title(Component.text("§6" + member.getAttack().getSkillCombos()), Component.text(""), time));
        } else {
            member.getAttack().setSkillComboTrigger(false);
            member.getAttack().getSkillCombos().clear();
            e.getPlayer().resetTitle();
        }

        TanoRPG.playSound(e.getPlayer(), Sound.ENTITY_BLAZE_HURT, 10, 2);

    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Member member = TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId());
        if (member == null || !member.getAttack().isSkillComboTrigger() || e.getHand() == EquipmentSlot.OFF_HAND)
            return;
        e.setCancelled(true);

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            member.getAttack().getSkillCombos().add(SkillComboType.RC);
        } else if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (e.getPlayer().hasMetadata("drop")) {
                e.getPlayer().removeMetadata("drop", TanoRPG.getPlugin());
                return;
            }
            member.getAttack().getSkillCombos().add(SkillComboType.LC);
        }

        e.getPlayer().showTitle(Title.title(Component.text("§6" + member.getAttack().getSkillCombos()), Component.text(""), time));

        if (member.getAttack().getSkillCombos().size() >= 3) {
            member.getAttack().setSkillComboTrigger(false);
            TanoRPG.playSound(e.getPlayer(), Sound.ENTITY_BLAZE_HURT, 10, 2);
            SkillCombo skillCombo = new SkillCombo(member.getAttack().getSkillCombos());
            if (member.getSkillMap().getSlotSkills().contains(skillCombo)) {
                TanoRPG.getPlugin().getSkillManager().executeSkill(TanoRPG.getPlugin().getSkillManager().getSkill(skillCombo), member);
            }
            if (member.getAttack().isSkillComboTrigger()) return;
            member.getAttack().getSkillCombos().clear();
            Bukkit.getScheduler().runTaskLater(TanoRPG.getPlugin(), () -> {
                if (member.getAttack().isSkillComboTrigger()) return;
                e.getPlayer().resetTitle();
            }, 10);
        } else {
            TanoRPG.playSound(e.getPlayer(), Sound.BLOCK_LEVER_CLICK, 10, 0.75);
        }
    }

    @EventHandler
    public void onShift(PlayerToggleSneakEvent e) {
        Member member = TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId());
        if (member == null || !member.getAttack().isSkillComboTrigger() || e.getPlayer().isSneaking()) return;

        member.getAttack().getSkillCombos().add(SkillComboType.S);

        e.getPlayer().showTitle(Title.title(Component.text("§6" + member.getAttack().getSkillCombos()), Component.text(""), time));
        TanoRPG.playSound(e.getPlayer(), Sound.BLOCK_LEVER_CLICK, 10, 0.75);

        if (member.getAttack().getSkillCombos().size() >= 3) {
            member.getAttack().setSkillComboTrigger(false);
            TanoRPG.playSound(e.getPlayer(), Sound.ENTITY_BLAZE_HURT, 10, 2);
            SkillCombo skillCombo = new SkillCombo(member.getAttack().getSkillCombos());
            if (member.getSkillMap().getSlotSkills().contains(skillCombo)) {
                TanoRPG.getPlugin().getSkillManager().executeSkill(TanoRPG.getPlugin().getSkillManager().getSkill(skillCombo), member);
            }

            if (member.getAttack().isSkillComboTrigger()) return;
            member.getAttack().getSkillCombos().clear();
            Bukkit.getScheduler().runTaskLater(TanoRPG.getPlugin(), () -> {
                if (member.getAttack().isSkillComboTrigger()) return;
                e.getPlayer().resetTitle();
            }, 10);
        } else {
            TanoRPG.playSound(e.getPlayer(), Sound.BLOCK_LEVER_CLICK, 10, 0.75);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (!e.getPlayer().isOp()) e.setCancelled(true);

        Member member = TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId());
        if (member == null || !member.getAttack().isSkillComboTrigger()) return;

        e.setCancelled(true);

        e.getPlayer().setMetadata("drop", new FixedMetadataValue(TanoRPG.getPlugin(), false));

        member.getAttack().getSkillCombos().add(SkillComboType.DR);

        e.getPlayer().showTitle(Title.title(Component.text("§6" + member.getAttack().getSkillCombos()), Component.text(""), time));
        TanoRPG.playSound(e.getPlayer(), Sound.BLOCK_LEVER_CLICK, 10, 0.75);

        if (member.getAttack().getSkillCombos().size() >= 3) {
            member.getAttack().setSkillComboTrigger(false);
            TanoRPG.playSound(e.getPlayer(), Sound.ENTITY_BLAZE_HURT, 10, 2);
            SkillCombo skillCombo = new SkillCombo(member.getAttack().getSkillCombos());
            if (member.getSkillMap().getSlotSkills().contains(skillCombo)) {
                TanoRPG.getPlugin().getSkillManager().executeSkill(TanoRPG.getPlugin().getSkillManager().getSkill(skillCombo), member);
            }
            member.getAttack().getSkillCombos().clear();
            Bukkit.getScheduler().runTaskLater(TanoRPG.getPlugin(), () -> {
                e.getPlayer().resetTitle();
            }, 10);
        } else {
            TanoRPG.playSound(e.getPlayer(), Sound.BLOCK_LEVER_CLICK, 10, 0.75);
        }
    }

    @EventHandler
    public void onClickSkill(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR) {
            Member member = TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId());
            if (member == null) return;

            if (e.getItem() == null || e.getItem().getType() == Material.AIR) return;
            if (!e.getItem().getItemMeta().getDisplayName().contains("§bスキル習得書: ")) return;
            String[] skillName = e.getItem().getItemMeta().getDisplayName().split("§6");
            Skill skill = TanoRPG.getPlugin().getSkillManager().getSkill(TanoRPG.getPlugin().getSkillManager().getSkillName(skillName[1]));
            if (!skill.getJob().contains(member.getSkillClass())) {
                e.getPlayer().getPlayer().sendMessage(TanoRPG.PX + "§c職業が違うため習得できません");
                return;
            }

            if (member.getLevel().getValue() < skill.getLvl()) {
                e.getPlayer().getPlayer().sendMessage(TanoRPG.PX + "§cレベルが足りないため習得できません");
                return;
            }

            TanoRPG.playSound(e.getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);

            member.getSkillMap().getOpenSkills().add(skill.getCombo());
            e.getPlayer().sendMessage(TanoRPG.PX + "スキル「" + skillName[1] + "」を習得しました");

            ItemStack item = e.getItem();
            item.setAmount(item.getAmount() - 1);
        }
    }
}
