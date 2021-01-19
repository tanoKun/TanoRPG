package com.github.tanokun.tanorpg.game.mob;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.item.CustomItem;
import com.github.tanokun.tanorpg.game.item.CustomItemManager;
import com.github.tanokun.tanorpg.util.io.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;


public class CustomEntityManager {
    private static HashMap<String, CustomEntity> customEntities = new HashMap<>();

    private static HashMap<Creature, NewEntity> newEntities = new HashMap<>();

    private static Config mobEntity;
    public static String loadCustomEntity(){
        String message = ChatColor.GREEN + "All entity config loaded without errors.";
        try {
            Bukkit.getScheduler().runTaskAsynchronously(TanoRPG.getPlugin(), () -> {
                mobEntity = new Config("mobEntity.yml", "mobs", TanoRPG.getPlugin());
                CustomEntity customEntity = null;
                for (String value : mobEntity.getConfig().getKeys(false)) {
                    EntityType entityType = EntityType.valueOf((String) mobEntity.getConfig().get(value + ".type"));
                    int lvl = mobEntity.getConfig().getInt(value + ".level");
                    int hp = mobEntity.getConfig().getInt(value + ".hp");
                    long exp = mobEntity.getConfig().getLong(value + ".exp");

                    int ATK = 0;
                    int DEF = 0;
                    int MATK = 0;
                    int MDEF = 0;
                    int AGI = 0;
                    int ING = 0;
                    int INT = 0;
                    for (String key : mobEntity.getConfig().getConfigurationSection(value + ".status").getKeys(false)) {
                        if (key.equalsIgnoreCase("atk")){ATK = mobEntity.getConfig().getInt(value + ".status." + key);}
                        if (key.equalsIgnoreCase("def")){DEF = mobEntity.getConfig().getInt(value + ".status." + key);}
                        if (key.equalsIgnoreCase("matk")){MATK = mobEntity.getConfig().getInt(value + ".status." + key);}
                        if (key.equalsIgnoreCase("mdef")){MDEF = mobEntity.getConfig().getInt(value + ".status." + key);}
                        if (key.equalsIgnoreCase("agi")){AGI = mobEntity.getConfig().getInt(value + ".status." + key);}
                        if (key.equalsIgnoreCase("ing")){ING = mobEntity.getConfig().getInt(value + ".status." + key);}
                        if (key.equalsIgnoreCase("int")){INT = mobEntity.getConfig().getInt(value + ".status." + key);}
                    }
                    String mainHand = "";
                    String offHand = "";
                    String helmet = "";
                    String chestPlate = "";
                    String leggings = "";
                    String boots = "";
                    for (String key : mobEntity.getConfig().getConfigurationSection(value + ".armor").getKeys(false)) {
                        if (key.equals("main")) {
                            mainHand = mobEntity.getConfig().getString(value + ".armor." + key);
                        }
                        if (key.equals("sub")) {
                            offHand = mobEntity.getConfig().getString(value + ".armor." + key);
                        }
                        if (key.equals("helmet")) {
                            helmet = mobEntity.getConfig().getString(value + ".armor." + key);
                        }
                        if (key.equals("chestplate")) {
                            chestPlate = mobEntity.getConfig().getString(value + ".armor." + key);
                        }
                        if (key.equals("leggings")) {
                            leggings = mobEntity.getConfig().getString(value + ".armor." + key);
                        }
                        if (key.equals("boots")) {
                            boots = mobEntity.getConfig().getString(value + ".armor." + key);
                        }
                    }
                    ArrayList<String> drops = (ArrayList<String>) mobEntity.getConfig().getList(value + ".drop");
                    CustomEntityDropItems customEntityDropItems = new CustomEntityDropItems();
                    for (String drop : drops){
                        String[] temp = drop.split("@");
                        int percent = Integer.valueOf(temp[1]);
                        CustomItem ci = CustomItemManager.getCustomItem(temp[0]);
                        customEntityDropItems.addItem(ci, percent);
                    }
                    customEntity = new CustomEntity(value, entityType, lvl, hp, exp);
                    customEntity.setDropItems(customEntityDropItems);
                    customEntity.setArmors(mainHand, offHand, helmet, chestPlate, leggings, boots);
                    customEntity.setStatuses(ATK, DEF, MATK, MDEF, AGI, ING, INT);
                    customEntities.put(value, customEntity);

                }
            });
        } catch (Exception e){
            message = ChatColor.RED + e.getClass().getName() + ": " + e.getMessage() + ChatColor.GRAY + "(" + "Entity" + ")";
            return message;
        }
        return message;
    }
    public static void deleteEntities(){
        customEntities = null;
        customEntities = new HashMap<>();
    }

    public static CustomEntity getEntity(String name) {
        return customEntities.get(name);
    }
    public static CustomEntity getEntity(Entity entity) {
        String[] name = entity.getName().split(" ");
        return customEntities.get(name[0]);
    }
    public static boolean isExists(String name){
        return (customEntities.get(name) != null);
        }
    public static boolean isExists(Entity entity){
        String[] name = entity.getName().split(" ");
        return (customEntities.get(name[0]) != null);
    }
    public static void addNewEntity(NewEntity entity){newEntities.put(entity.getCreature(), entity);}
    public static NewEntity getNewEntity(Creature creature){return newEntities.get(creature);}
    public static void removeNewEntity(Creature creature){newEntities.remove(creature);}

}
