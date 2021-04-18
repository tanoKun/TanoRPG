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
    private File folder;
    private String fileName;
    private String folderPath;
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
    public Config(String fileName, String folder, Plugin plugin){
        this.file = new File(plugin.getDataFolder() + File.separator + folder, fileName);
        this.folder = new File(plugin.getDataFolder() + File.separator + folder);
        this.fileName = fileName;
        this.plugin = plugin;
        this.folderPath = folder;
    }
    public boolean exists(){
        return file.exists();
    }
    public boolean createExists() {
        if (file.exists()){
            return true;
        }else{
            if (folder != null) {
                folder.mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public void saveDefaultConfig() {
        if (!file.exists()) {
            if (folder != null) {
                plugin.saveResource(folderPath + File.separator + fileName, false);
            }else {
                plugin.saveResource(fileName, false);
            }
        }
    }
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
        InputStream defConfigStream;
        if (folder != null) {
            defConfigStream = plugin.getResource(folderPath + File.separator + fileName);
        }else {
            defConfigStream = plugin.getResource(fileName);
        }
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