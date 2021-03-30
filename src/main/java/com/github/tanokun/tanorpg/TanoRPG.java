package com.github.tanokun.tanorpg;

import com.github.tanokun.tanorpg.command.register.Register;
import com.github.tanokun.tanorpg.game.craft.CraftManager;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.game.entity.spawner.EntitySpawnerManager;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.mission.MissionManager;
import com.github.tanokun.tanorpg.game.player.skill.combo.ComboManager;
import com.github.tanokun.tanorpg.game.player.status.Sidebar;
import com.github.tanokun.tanorpg.game.player.status.buff.Buff;
import com.github.tanokun.tanorpg.game.shop.ShopManager;
import com.github.tanokun.tanorpg.util.io.Coding;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

public final class TanoRPG extends JavaPlugin {
    private static Plugin plugin;
    private static Economy econ = null;
    private static EntitySpawnerManager entitySpawnerManager = null;
    public static final String PX = "§6[§a-｜ §b§lSystem§a ｜-§6] §7=> §b";
    public static final String PX_BUFF_UP = "§7[-｜ バフ付与 ｜-] §7=> ";
    public static final String PX_BUFF_DOWN = "§7[-｜ バフ解除 ｜-] §7=> ";
    public static final String OPEN_KYE = Coding.decode("a2plb2lqT0lIKSRoMjN1aDUzbzgyaGppanF3bjkpKCNIUklVTzJoOTg=");
    public static String IP;
    public static String MONEY = "TANO";

    public void onEnable() {
        plugin = this;
        entitySpawnerManager = new EntitySpawnerManager();
        IP = getPlugin().getConfig().getString("server-ip");
        Bukkit.broadcastMessage(TanoRPG.PX + "プレイヤーデータ読み込み中...");
        for(Player player : Bukkit.getOnlinePlayers()){
            GamePlayerManager.loadData(player.getUniqueId());
            ComboManager.comboRunnable.put(player.getUniqueId(), new ArrayList<>());
            player.setMaximumNoDamageTicks(0);
        }
        Bukkit.broadcastMessage(TanoRPG.PX + "完了");
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
        Bukkit.getConsoleSender().sendMessage(TanoRPG.PX + "reloading item configs....");
        ItemManager.deleteItems();
        for (String error : ItemManager.loadMaterialItem()) Bukkit.getConsoleSender().sendMessage(TanoRPG.PX + "  " + error);
        for (String error : ItemManager.loadWeaponItem()) Bukkit.getConsoleSender().sendMessage(TanoRPG.PX +  "  " + error);
        for (String error : ItemManager.loadMagicWeaponItem()) Bukkit.getConsoleSender().sendMessage(TanoRPG.PX +  "  " + error);
        for (String error : ItemManager.loadEquipmentItem()) Bukkit.getConsoleSender().sendMessage(TanoRPG.PX +  "  " + error);
        try {
            for (String error : EntityManager.loadEntities()) Bukkit.getConsoleSender().sendMessage(TanoRPG.PX + error);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        Bukkit.getConsoleSender().sendMessage(PX + ShopManager.loadShops());
        Bukkit.getConsoleSender().sendMessage(PX + CraftManager.loadCrafts());
        for (String error : entitySpawnerManager.loadSpawner()) Bukkit.getConsoleSender().sendMessage(TanoRPG.PX + error);
        registration.registerMissions();
        removeEntities();
        for(Player player : Bukkit.getOnlinePlayers()) {
            MissionManager.loadData(player.getUniqueId());
        }
        for(Player player : Bukkit.getOnlinePlayers()){
            Sidebar.setupSidebar(player);
            player.removeMetadata("COMBO", this);
        }
    }

    public void onDisable () {
        for(Entity entity : Bukkit.getWorld("world").getEntities()) {
            if (entity.hasMetadata("TanoRPG_entity")) {
                entity.remove();
            }
        }
        Bukkit.broadcastMessage(TanoRPG.PX + "オートセーブ中...");
        GamePlayerManager.saveDataAll();
        MissionManager.saveDataAll();
        Bukkit.broadcastMessage(TanoRPG.PX + "オートセーブ完了");
    }

    private void setupEcon() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        econ = rsp.getProvider();
    }

    public static Economy getEcon() {return econ;}
    public static Plugin getPlugin () {return plugin;}
    public static EntitySpawnerManager getEntitySpawnerManager() {return entitySpawnerManager;}

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Register.getCommand(command.getName()).tabComplete(sender, args);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return Register.getCommand(command.getName()).execute(sender, args);
    }

    public static void playSound(Player player, Sound sound, int volume, double v2){
        player.playSound(player.getLocation(), sound, volume, (float) v2);
    }

    private static void removeEntities(){
        for(Entity entity : Bukkit.getWorld("world").getEntities()) {
            if (entity instanceof Monster) {
                entity.remove();
            }
        }
    }

    public static Entity[] getNearbyEntities(Location l, double radius) {
        double chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        HashSet <Entity> radiusEntities = new HashSet< Entity >();
        try {
            for (double chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
                for (double chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
                    int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
                    for (Entity e: new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities()) {
                        if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock())
                            radiusEntities.add(e);
                    }
                }
            }
        }catch (NoSuchElementException | NullPointerException e){
            return radiusEntities.toArray(new Entity[radiusEntities.size()]);
        }
        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }
    public static Entity[] getNearbyPlayers(Location l, double radius) {
        double chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        HashSet <Entity> radiusEntities = new HashSet< Entity >();
        try {
            for (double chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
                for (double chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
                    int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
                    for (Entity e: new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities()) {
                        if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock() && e instanceof Player)
                            radiusEntities.add(e);
                    }
                }
            }
        }catch (NoSuchElementException | NullPointerException e){
            return radiusEntities.toArray(new Entity[radiusEntities.size()]);
        }
        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }
}