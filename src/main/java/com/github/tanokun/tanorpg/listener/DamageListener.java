package com.github.tanokun.tanorpg.listener;

import com.github.tanokun.tanorpg.DamageManager;
import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.item.ItemType;
import com.github.tanokun.tanorpg.game.item.type.base.ItemData;
import com.github.tanokun.tanorpg.player.EquipmentMap;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.util.EntityUtils;
import com.github.tanokun.tanorpg.util.ItemUtils;
import com.github.tanokun.tanorpg.util.particle.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import static java.lang.Math.*;

public class DamageListener implements Listener {

    private final Random random_60 = new Random();

    @EventHandler
    public void onHeld(PlayerItemHeldEvent e) {
        Member member = TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId());

        if (member == null) return;

        Bukkit.getScheduler().runTaskLater(TanoRPG.getPlugin(), () -> {
            if (ItemUtils.getItemData(e.getPlayer().getEquipment().getItemInMainHand()) == null) {
                member.getStatusMap().removeAllStatus(member.getEquipMap().getStatus());
                member.getEquipMap().setEquip(EquipmentMap.EquipmentType.MAIN, null);
                member.getStatusMap().addAllStatus(member.getEquipMap().getStatus());
            } else {
                member.getStatusMap().removeAllStatus(member.getEquipMap().getStatus());
                member.getEquipMap().setEquip(EquipmentMap.EquipmentType.MAIN, e.getPlayer().getEquipment().getItemInMainHand());
                member.getStatusMap().addAllStatus(member.getEquipMap().getStatus());
            }
        }, 2);

    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)
                || e.getCause().equals(EntityDamageEvent.DamageCause.FIRE)
                || e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)
                || e.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION)
                || e.getEntity() instanceof Player)
            e.setDamage(0);

        if (e.getEntity() instanceof Player && e.getEntity().getHeight() - e.getDamage() <= 0) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) e.setCancelled(true);
        else e.setDamage(0);

        if (TanoRPG.getPlugin().getMemberManager().getMember(e.getEntity().getUniqueId()) != null && e.getDamager().hasMetadata("TanoRPG_entity")) {
            Member member = TanoRPG.getPlugin().getMemberManager().getMember(e.getEntity().getUniqueId());
            if (member == null) return;
            int damage = DamageManager.getDisplayDamageByEntity(EntityUtils.getActiveEntity(e.getDamager()).getObjectEntity(), member);
            DamageManager.createDamageByEntity(damage, (Player) e.getEntity());
            return;
        }

        if (e.getEntity().hasMetadata("TanoRPG_entity") && e.getDamager() instanceof Player) {
            Member member = TanoRPG.getPlugin().getMemberManager().getMember(e.getDamager().getUniqueId());
            if (member == null) return;

            onClick(
                    new PlayerInteractEvent(((Player) e.getDamager()).getPlayer(), Action.LEFT_CLICK_AIR,
                            ((Player) e.getDamager()).getEquipment().getItemInMainHand() == null
                                    ? null : ((Player) e.getDamager()).getEquipment().getItemInMainHand(),
                            null, null));
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (e.getHand() != EquipmentSlot.HAND) return;
            Member member = TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId());

            if (member == null) return;

            if (e.getItem() == null) return;

            ItemData itemData = ItemUtils.getItemData(e.getItem());


            if (!(itemData != null && ItemUtils.isTrueSkillClass(itemData, member, e.getPlayer()) && ItemUtils.isTrueLevel(itemData, member, e.getPlayer())))
                return;

            if (member.getAttack().getNextAttackCombo() == itemData.getCombo().size()) return;

            if (member.getAttack().getAttackCombo() == 0) {
                int combo = member.getAttack().nextAttackCombo();
                getWaitAttack(itemData.getCoolTime(), member, itemData).runTaskTimer(TanoRPG.getPlugin(), 1, 1);
                attack(e.getPlayer(), member, itemData, combo);
            } else {
                if (member.getAttack().isAttackWait()) return;
                member.getAttack().setAttackWait(true);
                member.getAttack().setNextAttackCombo(member.getAttack().getAttackCombo() + 1);

                Bukkit.getScheduler().runTaskLater(TanoRPG.getPlugin(), () -> {
                    int combo = member.getAttack().nextAttackCombo();
                    getWaitAttack(itemData.getCoolTime(), member, itemData).runTaskTimer(TanoRPG.getPlugin(), 1, 1);
                    attack(e.getPlayer(), member, itemData, combo); }, (itemData.getCoolTime() + itemData.getCombo().get(member.getAttack().getAttackCombo() - 1) - member.getAttack().getLastAttackTicks() <= 0 ?
                        0 : itemData.getCoolTime() + itemData.getCombo().get(member.getAttack().getAttackCombo() - 1)) - member.getAttack().getLastAttackTicks());
            }
        }
    }

    private BukkitRunnable getWaitAttack(int ct, Member member, ItemData itemData) {
        return new BukkitRunnable() {

            Player player = Bukkit.getPlayer(member.getUuid());

            int time = 0;

            int time2 = 4;

            int combo = member.getAttack().getAttackCombo();

            int d = ct + itemData.getCombo().get(combo - 1);

            int da = (ct + itemData.getCombo().get(combo - 1)) / 3;

            int da2 = 0;

            boolean isAttack = false;

            public void run() {
                time2++;
                if (time == 0 && itemData.getItemType().equals(ItemType.PROJECTILE_WEAPON)) d = (ct + itemData.getCombo().get(combo - 1)) * 2;

                member.getAttack().nextLastAttackTicks();

                if (itemData.getItemType().equals(ItemType.PROJECTILE_WEAPON)) {
                    if (time2 == 5) {
                        if (d / 2 >= time){
                            player = Bukkit.getPlayer(member.getUuid());
                            Location m = player.getLocation();
                            m.setYaw(m.getYaw() + 120);
                            m.add(m.getDirection().getX() * 0.8, 1.7, m.getDirection().getZ() * 0.8);

                            Location m2 = m.clone();
                            m2.setYaw(player.getPlayer().getLocation().getYaw());

                            for (int i = 0; i < 11; i++) {
                                ParticleEffect.CRIT.display(m, 0, 0, 0, 0f, 1, null, Bukkit.getOnlinePlayers());
                                m.add(m2.getDirection().getX() * 0.25, 0, m2.getDirection().getZ() * 0.25);
                            }
                            time2 = 0;
                        } else if (!isAttack) {
                            isAttack = true;
                            System.out.println("aaa");
                        }
                    }
                }

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
        HashSet<Integer> entityCounts = new HashSet<>();
        if (itemData.getItemType().equals(ItemType.WEAPON)) {
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

                    HashSet<Entity> es = new HashSet<>();
                    if (player.getWorld().rayTraceEntities(player.getLocation(), player.getLocation().getDirection(), 3) != null)
                        es.add(player.getWorld().rayTraceEntities(player.getLocation(), player.getLocation().getDirection(), 3).getHitEntity());
                    Arrays.stream(EntityUtils.getNearActiveEntity(m, 1.5)).forEach(es::add);
                    for (Entity e : es) {
                        if (entityCounts.size() >= 3) break;
                        if (entityCounts.contains(e.getEntityId())) continue;
                        if (EntityUtils.getActiveEntity(e) == null) continue;
                        int atk = toIntExact(round(DamageManager.getDisplayDamageByPlayer(member, EntityUtils.getActiveEntity(e).getObjectEntity()) * 1.2));
                        DamageManager.createDamageByPlayer(atk, player, e);
                        entityCounts.add(e.getEntityId());
                    }
                }

            } else {
                TanoRPG.playSound(player, Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, 1, 1);
                m.add(0, 1, 0);

                double o = toRadians(m.getYaw());
                double t2;
                double t3 = toRadians(60);

                for (int t = 0; t < 120; t+=15) {
                    t2 = toRadians(t);

                    double x = -sin(-t3 + t2 + o);
                    double z = cos(-t3 + t2 + o);

                    for (double t4 = 1.5; t4 < 5; t4 += 0.6) {
                        m.add(x * t4, 0, z * t4);
                        ParticleEffect.CRIT.display(m, 0, 0, 0, 0f, 1, null, Bukkit.getOnlinePlayers());
                        ParticleEffect.ENCHANTED_HIT.display(m, 0, 0, 0, 0f, 1, null, Bukkit.getOnlinePlayers());

                        HashSet<Entity> es = new HashSet<>();
                        if (player.getWorld().rayTraceEntities(player.getLocation(), player.getLocation().getDirection(), 3) != null)
                            es.add(player.getWorld().rayTraceEntities(player.getLocation(), player.getLocation().getDirection(), 3).getHitEntity());
                        Arrays.stream(EntityUtils.getNearActiveEntity(m, 1.5)).forEach(es::add);
                        for (Entity e : es) {
                            if (entityCounts.size() >= 3) break;
                            if (entityCounts.contains(e.getEntityId())) continue;
                            if (EntityUtils.getActiveEntity(e) == null) continue;
                            int atk = toIntExact(round(DamageManager.getDisplayDamageByPlayer(member, EntityUtils.getActiveEntity(e).getObjectEntity()) * 1.2));
                            DamageManager.createDamageByPlayer(atk, player, e);
                            entityCounts.add(e.getEntityId());

                        }

                        m.subtract(x * t4, 0, z * t4);
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