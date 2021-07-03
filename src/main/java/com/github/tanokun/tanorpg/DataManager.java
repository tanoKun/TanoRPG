package com.github.tanokun.tanorpg;

import com.github.tanokun.tanorpg.util.io.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class DataManager {
    private Config dataFile;

    private Location startLoc;

    private String startRegionName;

    private boolean initFile;

    public DataManager(Plugin plugin) {
        dataFile = new Config("data.yml", plugin);
        dataFile.saveDefaultConfig();
        String[] startLoc = dataFile.getConfig().getString("start-loc", "none").split(" ");
        this.startLoc = startLoc[0].equals("none") ? Bukkit.getWorld("world").getSpawnLocation() :
        new Location(Bukkit.getWorld(startLoc[3]), Double.valueOf(startLoc[0]), Double.valueOf(startLoc[1]), Double.valueOf(startLoc[2]));
        if (!this.startLoc.equals(Bukkit.getWorld("world").getSpawnLocation())) {
            this.startLoc.setPitch(Float.parseFloat(startLoc[4]));
            this.startLoc.setYaw(Float.parseFloat(startLoc[5]));
        }

        String startRegionName = dataFile.getConfig().getString("start-region-name", "none");
        this.startRegionName = startRegionName.equals("none") ? "" : startRegionName;

        this.initFile = dataFile.getConfig().getBoolean("init-file", false);
    }

    public void save(){
        dataFile.getConfig().set("start-loc", startLoc.equals(Bukkit.getWorld("world").getSpawnLocation()) ? "none" :
                startLoc.getBlockX() + " " + startLoc.getBlockY() + " " + startLoc.getBlockZ() + " " + startLoc.getWorld().getName() + " " + startLoc.getPitch() + " " + startLoc.getYaw());
        dataFile.getConfig().set("start-region-name", startRegionName);
        dataFile.getConfig().set("init-file", true);
        dataFile.saveConfig();
    }

    public void setStartLoc(Location startLoc) {
        this.startLoc = startLoc;
    }

    public void setStartRegionName(String startRegionName) {
        this.startRegionName = startRegionName;
    }

    public void setInitFile(boolean initFile) {
        this.initFile = initFile;
    }

    public Location getStartLoc() {
        return startLoc;
    }

    public String getStartRegionName() {
        return startRegionName;
    }

    public boolean isInitFile() {
        return initFile;
    }
}
