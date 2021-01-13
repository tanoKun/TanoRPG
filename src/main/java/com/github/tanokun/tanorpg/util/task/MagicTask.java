package com.github.tanokun.tanorpg.util.task;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.DamageManager;
import com.github.tanokun.tanorpg.game.item.CustomItem;
import com.github.tanokun.tanorpg.game.item.CustomItemManager;
import com.github.tanokun.tanorpg.game.mob.CustomEntity;
import com.github.tanokun.tanorpg.game.mob.CustomEntityManager;
import com.github.tanokun.tanorpg.game.player.GamePlayer;
import com.github.tanokun.tanorpg.game.player.status.StatusType;
import com.github.tanokun.tanorpg.util.particle.ParticleEffect;
import com.github.tanokun.tanorpg.util.particle.data.color.RegularColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.HashMap;

public class MagicTask extends BukkitRunnable {
    private double time = 0;
    private GamePlayer gamePlayer;
    private Location loc;
    private Vector dire;
    private World world;
    private RegularColor regularColor;
    private HashMap<Entity, Boolean> entities = new HashMap<>();

    @Override
    public void run() {
        time += 0.4;
        double x = dire.getX() * time;
        double y = dire.getY() * time + 1.7;
        double z = dire.getZ() * time;
        loc.add(x, y, z);
        if (time > 4) {this.cancel();}
        ParticleEffect.REDSTONE.display(loc, 0, 0, 0, 0f, 1, regularColor, Bukkit.getOnlinePlayers());
        for (Entity entity : TanoRPG.getNearbyEntities(loc, 2)){
            if (CustomEntityManager.isExists(entity)){
                if (entities.get(entity) == null) {
                    entities.put(entity, true);
                    CustomEntity entity2 = CustomEntityManager.getEntity(entity);
                    int at_lvl = gamePlayer.getLEVEL();
                    int vi_lvl = entity2.getLEVEL();
                    double atk = DamageManager.getDamage(gamePlayer.getStatus(StatusType.MATK).getLevel(),
                            gamePlayer.getStatus(StatusType.INT).getLevel(),
                            gamePlayer.getStatus(StatusType.AGI).getLevel());
                    long damage = DamageManager.getCompDamage(atk, entity2.getMDEF(), at_lvl, vi_lvl, gamePlayer.getPlayer());
                    DamageManager.createMake(damage, gamePlayer.getPlayer(), entity);
                    this.cancel();
                }
            }
        }
        loc.subtract(x, y, z);
    }
    public MagicTask(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
        loc = gamePlayer.getPlayer().getLocation();
        dire = loc.getDirection().normalize();
        world = loc.getWorld();
        gamePlayer.getPlayer().setMetadata("cooltime_magic", new FixedMetadataValue(TanoRPG.getPlugin(), true));
        String id = CustomItemManager.getID(gamePlayer.getPlayer().getEquipment().getItemInMainHand());
        final int[] cool = {Math.round(CustomItemManager.getCustomItem(id).getCooltime())};
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute " + gamePlayer.getName() + " ~ ~ ~ /playsound entity.ghast.shoot player @s ~ ~ ~ 10 1");
        CustomItem item = CustomItemManager.getCustomItem(CustomItemManager.getID(gamePlayer.getPlayer().getEquipment().getItemInMainHand()));
        if (item == null) {
            regularColor = new RegularColor(new Color(255, 255, 255));
        } else {
            this.regularColor = new RegularColor(new Color(item.getRarity().getRed(), item.getRarity().getGreen(), item.getRarity().getBlue()));
        }
    }
}
