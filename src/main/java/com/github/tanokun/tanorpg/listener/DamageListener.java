package com.github.tanokun.tanorpg.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.item.type.base.ItemData;
import com.github.tanokun.tanorpg.player.EquipmentMap;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.player.skill.SkillClass;
import com.github.tanokun.tanorpg.util.ItemUtils;
import com.github.tanokun.tanorpg.util.particle.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

import static java.lang.Math.*;

public class DamageListener implements Listener {

    private final Random random_60 = new Random();

    @EventHandler
    public void onHeld(PlayerItemHeldEvent e) {
        Member member = TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId());

        if (ItemUtils.getItemData(e.getPlayer().getEquipment().getItemInMainHand()) == null) {
            member.getStatusMap().removeAllStatus(member.getEquipMap().getStatus());
            member.getEquipMap().setEquip(EquipmentMap.EquipmentType.MAIN, null);
            member.getStatusMap().addAllStatus(member.getEquipMap().getStatus());
        } else {
            member.getStatusMap().removeAllStatus(member.getEquipMap().getStatus());
            member.getEquipMap().setEquip(EquipmentMap.EquipmentType.MAIN, e.getPlayer().getEquipment().getItemInMainHand());
            member.getStatusMap().addAllStatus(member.getEquipMap().getStatus());
        }
    }

    @EventHandler
    public void onClick(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        Bukkit.getPluginManager().callEvent(new PlayerInteractEvent
                (((Player) e.getDamager()).getPlayer(), Action.LEFT_CLICK_AIR,
                        ((Player) e.getDamager()).getEquipment().getItemInMainHand() == null
                                ? new ItemStack(Material.AIR) : ((Player) e.getDamager()).getEquipment().getItemInMainHand(),
                        null, null));
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            Member member = TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId());

            if (e.getItem() == null) return;

            ItemData itemData = ItemUtils.getItemData(e.getItem());


            if (!(itemData != null && ItemUtils.isTrueSkillClass(itemData, member, e.getPlayer()) && ItemUtils.isTrueLevel(itemData, member, e.getPlayer())))
                return;

            if (member.getAttack().getNextAttackCombo() == itemData.getCombo().size()) return;

            if (member.getAttack().getAttackCombo() == 0) {
                int combo = member.getAttack().nextAttackCombo();
                e.getPlayer().sendMessage("§a" + combo);
                getWaitAttack(itemData.getCoolTime(), member, itemData).runTaskTimer(TanoRPG.getPlugin(), 1, 1);
                attack(e.getPlayer(), member, itemData, combo);
            } else {
                if (member.getAttack().isAttackWait()) return;
                member.getAttack().setAttackWait(true);
                member.getAttack().setNextAttackCombo(member.getAttack().getAttackCombo() + 1);
                Bukkit.getScheduler().runTaskLater(TanoRPG.getPlugin(), () -> {
                    int combo = member.getAttack().nextAttackCombo();
                    e.getPlayer().sendMessage("§a" + combo);
                    getWaitAttack(itemData.getCoolTime(), member, itemData).runTaskTimer(TanoRPG.getPlugin(), 1, 1);
                    attack(e.getPlayer(), member, itemData, combo);
                }, (itemData.getCoolTime() + itemData.getCombo().get(member.getAttack().getAttackCombo() - 1) - member.getAttack().getLastAttackTicks() <= 0 ?
                        0 : itemData.getCoolTime() + itemData.getCombo().get(member.getAttack().getAttackCombo() - 1)) - member.getAttack().getLastAttackTicks());
            }
        }
    }

    private BukkitRunnable getWaitAttack(int ct, Member member, ItemData itemData) {
        return new BukkitRunnable() {

            int time = 0;
            int combo = member.getAttack().getAttackCombo();

            int d = ct + itemData.getCombo().get(combo - 1);

            public void run() {
                member.getAttack().nextLastAttackTicks();

                if (time == d + 5) {
                    if (!member.getAttack().isAttackWait() && combo == member.getAttack().getAttackCombo()) {
                        member.getAttack().setAttackCombo(0);
                        member.getAttack().setNextAttackCombo(0);
                    }
                    member.getAttack().setLastAttackTicks(0);
                    member.getAttack().setAttackWait(false);
                    cancel();
                }
                time++;

            }
        };
    }

    private void attack(Player player, Member member, ItemData itemData, int combo) {
        if (member.getSkillClass().equals(SkillClass.SOLDIER) || member.getSkillClass().equals(SkillClass.GLADIATOR) || member.getSkillClass().equals(SkillClass.KNIGHT)) {
            Location m = player.getLocation();
            if (combo != itemData.getCombo().size()) {
                TanoRPG.playSound(player, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1, 1);
                int r = random_60.nextInt(70);
                if (chance(50)) r = -r;

                m.add(0, 2.5, 0);

                m.setYaw(m.getYaw() + r);

                m.add(m.getDirection().normalize().getX() * 2, 0, m.getDirection().normalize().getZ() * 2);

                m.setPitch(80);
                m.setYaw(m.getYaw() - r*2);

                for (int i = 0; i < 10; i++) {
                    m.subtract(-m.getDirection().normalize().getX(), 0.25, -m.getDirection().normalize().getZ());
                    ParticleEffect.CRIT.display(m, 0, 0, 0, 0f, 1, null, Bukkit.getOnlinePlayers());
                    ParticleEffect.ENCHANTED_HIT.display(m, 0, 0, 0, 0f, 1, null, Bukkit.getOnlinePlayers());
                }

            } else {
                TanoRPG.playSound(player, Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, 1, 1);
                m.add(0, 1, 0);
                for (double i = 0; i < 270; i+=15) {
                    double t = toRadians(10) + toRadians(m.getYaw()) + toRadians(i);
                    double x = cos(t);
                    double z = sin(t);
                    for (double t2 = 1.5; t2 < 3; t2+=0.6) {
                        m.add(x * t2, 0, z * t2);
                        ParticleEffect.CRIT.display(m, 0, 0, 0, 0f, 1, null, Bukkit.getOnlinePlayers());
                        ParticleEffect.ENCHANTED_HIT.display(m, 0, 0, 0, 0f, 1, null, Bukkit.getOnlinePlayers());
                        m.subtract(x * t2, 0, z * t2);
                    }
                }
            }
        }
    }

    private boolean chance(double chance) {
        chance = chance / 100;
        if (Math.random() <= chance) {
            return true;
        }
        return false;
    }
}