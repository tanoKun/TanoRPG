package com.github.tanokun.tanorpg;

import com.github.tanokun.tanorpg.util.io.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class DataManager {
    public static void setLocation(Location location, Config config, String key) {
        String locT = location.getWorld().getName() + " " + location.getX() + " " + location.getY() + " " + location.getZ() + " " + location.getPitch() + " " + location.getYaw();
        config.getConfig().set(key, locT);
    }

    public static Location getLocation(Config config, String key) {
        try {
            String[] locT = config.getConfig().getString(key).split(" ");

            World world = Bukkit.getWorld(locT[0]);
            double x = Double.parseDouble(locT[1]);
            double y = Double.parseDouble(locT[2]);
            double z = Double.parseDouble(locT[3]);

            float p = Float.valueOf(locT[4]);
            float y2 = Float.valueOf(locT[5]);

            return new Location(world, x, y, z, y2, p);
        } catch (Exception e) {
            e.printStackTrace();
            return new Location(Bukkit.getWorld("world"), 0, 0, 0);
        }
    }

    public static Location getLocation(String stringLoc) {
        try {
            String[] locT = stringLoc.split(" ");

            World world = Bukkit.getWorld(locT[0]);
            double x = Double.valueOf(locT[1]);
            double y = Double.valueOf(locT[2]);
            double z = Double.valueOf(locT[3]);

            float p = Float.valueOf(locT[4]);
            float y2 = Float.valueOf(locT[5]);

            return new Location(world, x, y, z, y2, p);
        } catch (Exception e) {
            e.printStackTrace();
            return new Location(Bukkit.getWorld("world"), 0, 0, 0);
        }
    }

    public static Location getLocationNoYawAndPitch(Config config, String key) {
        try {
            String[] locT = config.getConfig().getString(key).split(" ");

            World world = Bukkit.getWorld(locT[0]);
            double x = Double.valueOf(locT[1]);
            double y = Double.valueOf(locT[2]);
            double z = Double.valueOf(locT[3]);

            return new Location(world, x, y, z);
        } catch (Exception e) {
            e.printStackTrace();
            return new Location(Bukkit.getWorld("world"), 0, 0, 0);
        }
    }

    public static Location getLocationNoYawAndPitch(String stringLoc) {
        try {
            String[] locT = stringLoc.split(" ");

            World world = Bukkit.getWorld(locT[0]);
            double x = Double.valueOf(locT[1]);
            double y = Double.valueOf(locT[2]);
            double z = Double.valueOf(locT[3]);

            return new Location(world, x, y, z);
        } catch (Exception e) {
            e.printStackTrace();
            return new Location(Bukkit.getWorld("world"), 0, 0, 0);
        }
    }

    public static String LocationToString(Location location) {
        return Math.ceil(location.getX()) + " " + Math.ceil(location.getY()) + " " + Math.ceil(location.getZ());
    }
}
