package com.github.tanokun.tanorpg.game.entity.boss;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.event.WgRegionEnterEvent;
import com.github.tanokun.tanorpg.event.WgRegionLeftEvent;
import com.github.tanokun.tanorpg.game.entity.boss.inv.CheckEnterMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class BossManager implements Listener {
    HashMap<String, BossEntity> bossEntities = new HashMap<>();

    public BossManager() {
        Bukkit.getPluginManager().registerEvents(this, TanoRPG.getPlugin());
    }

    public void register(String id, BossEntity bossEntity) {
        bossEntities.put(id, bossEntity);
    }

    public BossEntity getBoss(String id) {
        return bossEntities.get(id);
    }

    @EventHandler
    public void onEnter(WgRegionEnterEvent e) {
        if (e.getPlayer() == null) return;
        if (TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId()) == null) return;

        Player p = e.getPlayer();

        if (p.hasMetadata("boss")) {
            p.removeMetadata("boss", TanoRPG.getPlugin());
            return;
        }

        if (bossEntities.keySet().contains(e.getRegionName())) {
            e.setCancelled(true);
            BossEntity bossEntity = bossEntities.get(e.getRegionName());
            if (bossEntity.getSpawnTime() == null) {
                new CheckEnterMenu(e.getRegionName()).getInv().open(p);
            } else {
                LocalDateTime now = LocalDateTime.now();
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                DateTimeFormatter byString = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

                long difference = 0;
                try {
                    difference = format.parse(now.format(byString)).getTime() - bossEntity.getSpawnTime().getTime();
                } catch (ParseException e2) {
                    e2.printStackTrace();
                }
                long seconds = difference / 1000 % 60;

                if (seconds > 60) p.sendMessage(TanoRPG.PX + "§c召喚から1分以上経ってるため参加できません");
                else new CheckEnterMenu(e.getRegionName()).getInv().open(p);
            }
        }
    }

    @EventHandler
    public void onLeave(WgRegionLeftEvent e) {
        if (e.getPlayer() == null) return;
        if (TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId()) == null) return;

        if (bossEntities.keySet().contains(e.getRegionName())) {
            BossEntity bossEntity = bossEntities.get(e.getRegionName());
            if (bossEntity.getSpawnTime() != null && !e.getPlayer().hasMetadata("die") && !e.getPlayer().hasMetadata("boss")) {
                e.setCancelled(true);
            }
        }
    }
}

