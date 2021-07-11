package com.github.tanokun.tanorpg;

import com.github.tanokun.tanorpg.util.io.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;

public class DataManager {
    private Config dataFile;

    private Location guildLoc;

    private String guildRegionName;

    private Location homeLoc;

    private String homeRegionName;

    private Location respawnLoc;

    private boolean initFile;

    private HashSet<String> permissions = new HashSet<>();

    public DataManager(Plugin plugin) {
        dataFile = new Config("data.yml", plugin);
        dataFile.saveDefaultConfig();
        String[] guildLoc = dataFile.getConfig().getString("guild-loc", "none").split(" ");
        this.guildLoc = guildLoc[0].equals("none") ? Bukkit.getWorld("world").getSpawnLocation() :
        new Location(Bukkit.getWorld(guildLoc[3]), Double.valueOf(guildLoc[0]), Double.valueOf(guildLoc[1]), Double.valueOf(guildLoc[2]));
        if (!this.guildLoc.equals(Bukkit.getWorld("world").getSpawnLocation())) {
            this.guildLoc.setPitch(Float.parseFloat(guildLoc[4]));
            this.guildLoc.setYaw(Float.parseFloat(guildLoc[5]));
        }

        String guildRegionName = dataFile.getConfig().getString("guild-region-name", "none");
        this.guildRegionName = guildRegionName.equals("none") ? "" : guildRegionName;

        String[] homeLoc = dataFile.getConfig().getString("home-loc", "none").split(" ");
        this.homeLoc = homeLoc[0].equals("none") ? Bukkit.getWorld("world").getSpawnLocation() :
                new Location(Bukkit.getWorld(homeLoc[3]), Double.valueOf(homeLoc[0]), Double.valueOf(homeLoc[1]), Double.valueOf(homeLoc[2]));
        if (!this.homeLoc.equals(Bukkit.getWorld("world").getSpawnLocation())) {
            this.homeLoc.setPitch(Float.parseFloat(homeLoc[4]));
            this.homeLoc.setYaw(Float.parseFloat(homeLoc[5]));
        }

        String homeRegionName = dataFile.getConfig().getString("home-region-name", "none");
        this.homeRegionName = homeRegionName.equals("none") ? "" : homeRegionName;

        String[] respawnLoc = dataFile.getConfig().getString("respawn-loc", "none").split(" ");
        this.respawnLoc = respawnLoc[0].equals("none") ? Bukkit.getWorld("world").getSpawnLocation() :
                new Location(Bukkit.getWorld(respawnLoc[3]), Double.valueOf(respawnLoc[0]), Double.valueOf(respawnLoc[1]), Double.valueOf(respawnLoc[2]));
        if (!this.respawnLoc.equals(Bukkit.getWorld("world").getSpawnLocation())) {
            this.respawnLoc.setPitch(Float.parseFloat(respawnLoc[4]));
            this.respawnLoc.setYaw(Float.parseFloat(respawnLoc[5]));
        }

        this.initFile = dataFile.getConfig().getBoolean("init-file", false);
    }

    public void save(){
        dataFile.getConfig().set("guild-loc", guildLoc.equals(Bukkit.getWorld("world").getSpawnLocation()) ? "none" :
                guildLoc.getBlockX() + " " + guildLoc.getBlockY() + " " + guildLoc.getBlockZ() + " " + guildLoc.getWorld().getName() + " " + guildLoc.getPitch() + " " + guildLoc.getYaw());
        dataFile.getConfig().set("guild-region-name", guildRegionName);

        dataFile.getConfig().set("home-loc", homeLoc.equals(Bukkit.getWorld("world").getSpawnLocation()) ? "none" :
                homeLoc.getBlockX() + " " + homeLoc.getBlockY() + " " + homeLoc.getBlockZ() + " " + homeLoc.getWorld().getName() + " " + homeLoc.getPitch() + " " + homeLoc.getYaw());
        dataFile.getConfig().set("home-region-name", homeRegionName);

        dataFile.getConfig().set("respawn-loc", respawnLoc.equals(Bukkit.getWorld("world").getSpawnLocation()) ? "none" :
                guildLoc.getBlockX() + " " + respawnLoc.getBlockY() + " " + respawnLoc.getBlockZ() + " " + respawnLoc.getWorld().getName() + " " + respawnLoc.getPitch() + " " + respawnLoc.getYaw());


        dataFile.getConfig().set("init-file", true);
        dataFile.saveConfig();
    }

    public void setGuildLoc(Location guildLoc) {
        this.guildLoc = guildLoc;
    }

    public void setGuildRegionName(String guildRegionName) {
        this.guildRegionName = guildRegionName;
    }

    public void setHomeLoc(Location homeLoc) {
        this.homeLoc = homeLoc;
    }

    public void setHomeRegionName(String homeRegionName) {
        this.homeRegionName = homeRegionName;
    }

    public void setRespawnLoc(Location respawnLoc) {
        this.respawnLoc = respawnLoc;
    }

    public void setInitFile(boolean initFile) {
        this.initFile = initFile;
    }

    public Location getGuildLoc() {
        return guildLoc;
    }

    public String getGuildRegionName() {
        return guildRegionName;
    }

    public Location getHomeLoc() {
        return homeLoc;
    }

    public String getHomeRegionName() {
        return homeRegionName;
    }

    public Location getRespawnLoc() {
        return respawnLoc;
    }

    public HashSet<String> getPermissions() {
        return permissions;
    }

    public boolean isInitFile() {
        return initFile;
    }
}
