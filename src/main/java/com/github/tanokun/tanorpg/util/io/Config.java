package com.github.tanokun.tanorpg.util.io;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Config {
    private FileConfiguration config;
    private File file;
    private String fileName;
    private Plugin plugin;
    public Config(Plugin plugin){
        this.file = new File(plugin.getDataFolder(), "config.yml");
        this.plugin = plugin;
        this.fileName = "config.yml";
    }
    public Config(File file, Plugin plugin){
        this.file = file;
        this.plugin = plugin;
        this.fileName = file.getName();
    }
    public Config(String fileName, Plugin plugin){
        this.file = new File(plugin.getDataFolder(), fileName);
        this.fileName = fileName;
        this.plugin = plugin;
    }
    public boolean exists(){
        return file.exists();
    }
    public boolean createExists() {
        if (file.exists()){
            return true;
        }else{
            try {
                file.createNewFile();
            } catch (IOException e) {
                return true;
            }
        }
        return false;
    }
    public void saveDefaultConfig() {
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
    }
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
        InputStream defConfigStream;
        defConfigStream = plugin.getResource(fileName);
        if (defConfigStream == null) {
            return;
        }

        config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)));
        try {
            defConfigStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }
    public void saveConfig() {
        if (config == null) {
            return;
        }
        try {
            getConfig().save(file);
        } catch (IOException ex) {
            plugin.getLogger().info("§cConfigError");
        }
    }
    public String getName(){
        return fileName;
    }
}