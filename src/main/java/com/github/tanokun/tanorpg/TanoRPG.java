package com.github.tanokun.tanorpg;

import com.github.tanokun.tanorpg.command.register.Register;
import com.github.tanokun.tanorpg.game.craft.CraftManager;
import com.github.tanokun.tanorpg.game.item.CustomItemManager;
import com.github.tanokun.tanorpg.game.mob.CustomEntityManager;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.status.buff.Buff;
import com.github.tanokun.tanorpg.game.shop.ShopManager;
import com.github.tanokun.tanorpg.listener.EntitySpawnEventListener;
import com.github.tanokun.tanorpg.util.io.Coding;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public final class TanoRPG extends JavaPlugin {
    private static Plugin plugin;
    private static Economy econ = null;
    public static final String PX = "§6[§a-｜ §b§lSystem§a ｜-§6] §7=> §b";
    public static final String PX_BUFF_UP = "§7[-｜ バフ付与 ｜-] §7=> ";
    public static final String PX_BUFF_DOWN = "§7[-｜ バフ解除 ｜-] §7=> ";
    public static final String OPEN_KYE = Coding.decode("a2plb2lqT0lIKSRoMjN1aDUzbzgyaGppanF3bjkpKCNIUklVTzJoOTg=");

    public void onEnable() {
        plugin = this;
        Bukkit.broadcastMessage(TanoRPG.PX + "プレイヤーデータ読み込み中...");
        for(Player player : Bukkit.getOnlinePlayers()){
            GamePlayerManager.loadData(player.getUniqueId());
            player.setMaximumNoDamageTicks(0);
        }
        Bukkit.broadcastMessage(TanoRPG.PX + "完了");
        CustomEntityManager.loadCustomEntity();
        setupEcon();
        Buff.start();
        Registration registration = new Registration(this);
        registration.registerConfigs();
        registration.registerCommand();
        registration.registerTask();
        registration.registerMenus();
        registration.registerOthers();
        registration.registerListener();
        registration.registerSkills();
        Bukkit.getConsoleSender().sendMessage(PX + CustomItemManager.loadCustomItemAll());
        Bukkit.getConsoleSender().sendMessage(PX + CustomEntityManager.loadCustomEntity());
        Bukkit.getConsoleSender().sendMessage(PX + ShopManager.loadShops());
        Bukkit.getConsoleSender().sendMessage(PX + CraftManager.loadCrafts());
        removeEntities();
    }

    private void setupEcon() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        econ = rsp.getProvider();
    }

    public static Economy getEcon() {return econ;}

    public void onDisable () {
        Bukkit.broadcastMessage(TanoRPG.PX + "オートセーブ中...");
        GamePlayerManager.saveDataAll();
        Bukkit.broadcastMessage(TanoRPG.PX + "オートセーブ完了");
    }
    public static Plugin getPlugin () {return plugin;}
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return Register.getCommand(command.getName()).execute(sender, args);
    }
    private static void removeEntities(){
        for(Entity entity : Bukkit.getWorld("world").getEntities()) {
            if (entity instanceof Monster) {
                entity.remove();
            }
        }
        EntitySpawnEventListener.counts = new HashMap<>();
    }
    public static Entity[] getNearbyEntities(Location l, double radius) {
        double chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        HashSet <Entity> radiusEntities = new HashSet< Entity >();
        for (double chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
            for (double chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
                int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
                for (Entity e: new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities()) {
                    if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock())
                        radiusEntities.add(e);
                }
            }
        }

        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Register.getCommand(command.getName()).tabComplete(sender, args);
    }
}