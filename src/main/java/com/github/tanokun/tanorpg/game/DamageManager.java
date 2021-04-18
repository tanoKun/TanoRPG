package com.github.tanokun.tanorpg.game;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.event.tanorpg.CustomEntityKillEvent;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.game.entity.base.ObjectEntity;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.game.item.itemtype.base.Item;
import com.github.tanokun.tanorpg.game.item.itemtype.base.ItemJob;
import com.github.tanokun.tanorpg.game.player.GamePlayer;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.status.buff.BuffType;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static com.github.tanokun.tanorpg.game.player.status.buff.Buff.getBuffPercent;

public class DamageManager {
    public static long getCompDamage(double atk, double def, int attackerLv, int victimLv, Entity entity){
        long damage = Math.round(atk * (5 + attackerLv) / (5.1 + def + victimLv));
        double percent = getBuffPercent(entity, BuffType.ATK_UP_S) +
                getBuffPercent(entity, BuffType.ATK_UP_M) +
                getBuffPercent(entity, BuffType.ATK_UP_L);
        percent = percent / 100;
        damage = damage + Math.round(percent * damage);
        return damage;
    }
    public static double getDamage(double atk, double ing, double agi){
        return atk + ing/2 + agi/3;
    }

    public static void createDamage(long damage, Entity attacker, Entity target){
        final String[] hp = {null};
        if (target.isDead()) return;
        new BukkitRunnable(){
            @Override
            public void run() {
                if (!target.hasMetadata("TanoRPG_entity")) return;
                Mob mob = (Mob) target;
                mob.setTarget((LivingEntity) attacker);
                String[] name = mob.getName().split(" ");
                ObjectEntity customEntity;
                try {
                    customEntity = EntityManager.getBaseEntity(target);
                }catch (NullPointerException e){return;}
                GamePlayer gamePlayer = GamePlayerManager.getPlayer(attacker.getUniqueId());
                if (mob.getHealth() - damage <= 0) {
                    Bukkit.getServer().getPluginManager().callEvent(new CustomEntityKillEvent(customEntity, (Player) attacker, target));
                    mob.damage(10000000);
                    customEntity.getDropItems().giveDropItems((Player) attacker);
                    gamePlayer.setHAS_EXP(gamePlayer.getHAS_EXP() + EntityManager.getBaseEntity(name[0]).getEXP());
                }

                ItemStack item = gamePlayer.getPlayer().getEquipment().getItemInMainHand();
                ItemJob item2 = (ItemJob) ItemManager.getItem(item);
                item = ItemManager.setDurabilityValue(item, ItemManager.getDurabilityValue(item) - 1);
                List<String> lore = item.getItemMeta().getLore();
                lore.set(lore.size() - 3, "§7DurabilityValue: (" + ItemManager.getDurabilityValue(item) + "/" + item2.getMaxDurabilityValue() + ")");
                ItemMeta itemMeta = item.getItemMeta(); itemMeta.setLore(lore);
                item.setItemMeta(itemMeta);
                gamePlayer.getPlayer().getEquipment().setItemInMainHand(item);

                mob.damage(damage);
                gamePlayer.getPlayer().sendMessage(TanoRPG.PX + damage + "ダメージ！");

                int hasHP = (int) Math.floor((mob.getHealth() / customEntity.getHP()) * 20);
                for (int i = 0; i < hasHP; i++) {
                    hp[0] = (hp[0] == null) ? "§a❘" :  "§a❘" + hp[0];
                }
                for (int i = 0; i < 20 - hasHP; i++) {
                    hp[0] = hp[0] + "§c❘";
                }
                hp[0] =  hp[0].replace("null", "");
                mob.setCustomName(customEntity.getName() + " §7[§dLv:§e" + customEntity.getLEVEL() + "§7] " + hp[0]);
            }
        }.runTask(TanoRPG.getPlugin());
    }

}
