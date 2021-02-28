package com.github.tanokun.tanorpg.game.entity;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.item.CustomItem;
import com.github.tanokun.tanorpg.game.item.CustomItemManager;
import com.github.tanokun.tanorpg.util.io.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EntityManager {
    private static HashMap<String, EntityData> customEntities = new HashMap<>();
    private static List<String> customEntityIDs = new ArrayList<>();

    private static HashMap<UUID, EntityCreature> existsEntities = new HashMap<>();

    private static Config mobEntity;

    public static String loadData() {
        String message = ChatColor.GREEN + "All entity config loaded without errors.";
        try {
            Bukkit.getScheduler().runTaskAsynchronously(TanoRPG.getPlugin(), () -> {
                mobEntity = new Config("mobEntity.yml", "mobs", TanoRPG.getPlugin());
                EntityData customEntity;
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
                    EntityDropItems customEntityDropItems = new EntityDropItems();
                    for (String drop : drops){
                        String[] temp = drop.split("@");
                        int percent = Integer.valueOf(temp[1]);
                        CustomItem ci = CustomItemManager.getCustomItem(temp[0]);
                        customEntityDropItems.addItem(ci, percent);
                    }
                    customEntity = new EntityData(value, entityType, lvl, hp, exp);
                    customEntity.setDropItems(customEntityDropItems);
                    customEntity.setArmors(mainHand, offHand, helmet, chestPlate, leggings, boots);
                    customEntity.setStatuses(ATK, DEF, MATK, MDEF, AGI, ING, INT);
                    customEntities.put(value, customEntity);
                    customEntityIDs.add(value);
                }
            });
        } catch (Exception e){
            message = ChatColor.RED + e.getClass().getName() + ": " + e.getMessage() + ChatColor.GRAY + "(" + "Entity" + ")";
            return message;
        }
        return message;
    }

    public static void addEntity(EntityCreature entityCreature){existsEntities.put(entityCreature.getCreature().getUniqueId(), entityCreature);}
    public static void removeEntity(Creature creature){existsEntities.remove(creature.getUniqueId());}
    public static EntityCreature getEntity(Creature creature){return existsEntities.get(creature.getUniqueId());}

    public static EntityData getEntityData(String name) {
        return customEntities.get(name);
    }
    public static EntityData getEntityData(org.bukkit.entity.Entity entity) {
        String[] name = entity.getName().split(" ");
        return customEntities.get(name[0]);
    }

    public static List<String> getEntityIDs() {return customEntityIDs;}

}
