package com.github.tanokun.tanorpg.game;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.game.entity.base.ObjectEntity;
import com.github.tanokun.tanorpg.game.player.GamePlayer;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.status.buff.BuffType;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
        new BukkitRunnable(){
            @Override
            public void run() {
                if (!(target instanceof Creature)) return;
                Creature creature = (Creature) target;
                ((Creature)target).setTarget((LivingEntity) attacker);
                String[] name = target.getName().split(" ");
                if (!target.hasMetadata("TanoRPG_entity")) return;
                ObjectEntity customEntity;
                try {
                    customEntity = EntityManager.getBaseEntity(target);
                }catch (NullPointerException e){return;}
                GamePlayer gamePlayer = GamePlayerManager.getPlayer(attacker.getUniqueId());
                if (((Creature) target).getHealth() - damage <= 0) {
                    customEntity.getDropItems().giveDropItems((Player) attacker);
                    gamePlayer.setHAS_EXP(gamePlayer.getHAS_EXP() + EntityManager.getBaseEntity(name[0]).getEXP());
                }

                ((Creature) target).damage(damage);
                gamePlayer.getPlayer().sendMessage(TanoRPG.PX + damage + "ダメージ！");

                int hasHP = (int) Math.floor((creature.getHealth() / customEntity.getHP()) * 20);
                for (int i = 0; i < hasHP; i++) {
                    hp[0] = (hp[0] == null) ? "§a❘" :  "§a❘" + hp[0];
                }
                for (int i = 0; i < 20 - hasHP; i++) {
                    hp[0] = hp[0] + "§c❘";
                }
                hp[0] = hp[0].replace("null", "");
                creature.setCustomName(customEntity.getName() + " §7[§dLv:§e" + customEntity.getLEVEL() + "§7] " + hp[0]);
            }
        }.runTask(TanoRPG.getPlugin());
    }
}
