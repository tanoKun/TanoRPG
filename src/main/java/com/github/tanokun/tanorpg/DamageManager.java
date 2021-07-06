package com.github.tanokun.tanorpg;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.event.tanorpg.TanoRpgEntityKillEvent;
import com.github.tanokun.tanorpg.game.entity.ActiveEntity;
import com.github.tanokun.tanorpg.game.entity.base.ObjectEntity;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.player.status.StatusMap;
import com.github.tanokun.tanorpg.player.status.StatusType;
import com.github.tanokun.tanorpg.util.EntityUtils;
import com.github.tanokun.tanorpg.util.particle.ParticleEffect;
import net.minecraft.server.v1_15_R1.DamageSource;
import net.minecraft.server.v1_15_R1.EntityHuman;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import javax.crypto.Mac;

public class DamageManager {
    public static int getDisplayDamageByPlayer(Member member, ObjectEntity entity){
        int atk = member.getStatusMap().getStatus(StatusType.ATK);
        int def = entity.getStatusMap().getStatus(StatusType.DEF);
        return atk * (5 + member.getHasLevel().getValue()) / (6 + def + entity.getHasLevel());
    }

    public static void createDamageByPlayer(int damage, Player attacker, Entity target) {
        if (target.isDead()) return;
        ActiveEntity ae = EntityUtils.getActiveEntity(target);
        Member member = TanoRPG.getPlugin().getMemberManager().getMember(attacker.getUniqueId());
        ((Mob)target).setTarget(attacker);
        int hp = ae.setHasHP(ae.getHasHP() - damage);
        ((Mob) target).damage(0);

        if (hp <= 0) {
            target.setMetadata("isDead", new FixedMetadataValue(TanoRPG.getPlugin(), true));
            ((Mob) target).damage(10000000);
            ae.getObjectEntity().getDropItems().chanceGive(attacker, member.getStatusMap().getStatus(StatusType.LUCKY_ITEM));
            member.setHasEXP(member.getHasEXP() + ae.getObjectEntity().getExp());
            Bukkit.getPluginManager().callEvent(new TanoRpgEntityKillEvent(attacker, target, ae.getObjectEntity()));
            ParticleEffect.CLOUD.display(target.getLocation(), 0, 0, 0, 0.5f, 40, null, Bukkit.getOnlinePlayers());
        }

        String display = "";
        int r = ae.getObjectEntity().getStatusMap().getStatus(StatusType.HP) / 20;
        int t = hp / r;

        for (int i = 0; i < t; i++) {
            display = display + "§a❘";
        }
        for (int i = 0; i < 20 - t; i++) {
            display = display + "§c❘";
        }

        target.setCustomName(ae.getObjectEntity().getName() + " §7[§dLv:§e" + ae.getObjectEntity().getHasLevel() + "§7] " + display);
    }

    public static int getDisplayDamageByEntity(ObjectEntity entity, Member member){
        int atk =entity.getStatusMap().getStatus(StatusType.ATK);
        int def = member.getStatusMap().getStatus(StatusType.DEF);
        return atk * (5 + entity.getHasLevel()) / (6 + def + member.getHasLevel().getValue());
    }

    public static void createDamageByEntity(int damage, Entity attacker, Player target) {
        if (target.isDead()) return;
        Member member = TanoRPG.getPlugin().getMemberManager().getMember(target.getUniqueId());
        member.setHasHP(member.getHasHP() - damage); int hp = member.getHasHP();
        target.damage(0);
        if (hp <= 0) {
            TanoRPG.playSound(target, Sound.ENTITY_WITHER_SPAWN, 1, 1);
            target.getPlayer().setGameMode(GameMode.SPECTATOR);
            target.getPlayer().sendTitle("§c死んでしまった！", "", 0, 0, 100);
            member.setHasHP(0);
            member.setHasEXP(member.getHasEXP() - (member.getHasEXP() * (5/100)));
            Bukkit.getScheduler().runTaskLater(TanoRPG.getPlugin(), () -> {
                target.teleport(TanoRPG.getPlugin().getDataManager().getStartLoc());
                member.setHasHP(member.getStatusMap().getStatus(StatusType.HP));
                target.setGameMode(GameMode.ADVENTURE);
            }, 100);
        }
    }
}
