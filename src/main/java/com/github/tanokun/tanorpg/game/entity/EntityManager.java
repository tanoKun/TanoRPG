package com.github.tanokun.tanorpg.game.entity;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.base.ObjectEntity;
import com.github.tanokun.tanorpg.game.entity.exception.TanoEntityException;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.game.item.itemtype.base.Item;
import com.github.tanokun.tanorpg.util.io.Config;
import com.github.tanokun.tanorpg.util.io.Folder;
import com.github.tanokun.tanorpg.util.io.MapNode;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class EntityManager {
    private static Map<String, ObjectEntity> entityList = new HashMap<>();

    private static Map<Entity, ActiveEntity> activeEntityList = new HashMap<>();

    public static void registerEntity(ObjectEntity objectEntity){
        entityList.put(objectEntity.getName(), objectEntity);
    }
    public static ObjectEntity getEntityList(String name) {
        return entityList.get(entityList);
    }

    public static void registerActiveEntity(ActiveEntity activeEntity){
        activeEntityList.put(activeEntity.getActiveEntity(), activeEntity);
    }
    public static ActiveEntity removeActiveEntity(Entity entity){
        return activeEntityList.remove(entity);
    }
    public static ActiveEntity getActiveEntity(Entity entity) {return activeEntityList.get(entity);}

    public static ObjectEntity getBaseEntity(String name){
        return entityList.get(name);
    }
    public static ObjectEntity getBaseEntity(Entity entity){
        String[] name = entity.getName().split(" ");
        return entityList.get(name[0]);
    }

    public static List<String> getEntityIDs() {
        List<String> list = new ArrayList();
        for (Map.Entry<String, ObjectEntity> entry : entityList.entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }

    public static HashSet<String> loadEntities() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        HashSet<String> errors = new HashSet<>();
        MapNode<String, Object> data = null;

        boolean allError = false;

        for (Config config : new Folder("mobs", TanoRPG.getPlugin()).getFiles()) {
            boolean successFull = true;

            EntityTypes entityType = null;
            try {
                data = ItemManager.get("EntityType", config);
                if (data.getValue() != null) {
                    try {
                        entityType = EntityTypes.valueOf((String) data.getValue());
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("EntityType「" + data.getValue() + "」は存在しません");
                    }
                } else {
                    throw new NullPointerException("EntityTypeが設定されていません");
                }
            } catch (Exception e){
                successFull = false;
                allError = true;
                errors.add(ChatColor.RED + e.getMessage() + ChatColor.GRAY + "(Path: " + config.getName() + "/" + data.getKey() + ")");
            }

            if (successFull){
                ObjectEntity objectEntity;
                Class<? extends ObjectEntity> e = entityType.getCLASS();
                Constructor<? extends ObjectEntity> constructor = e.getConstructor(Config.class);
                try {
                    objectEntity = constructor.newInstance(config);
                    registerEntity(objectEntity);
                    errors.add("§aEntities config loaded without errors.");
                } catch (InvocationTargetException e2){
                    allError = true;
                    TanoEntityException exception = (TanoEntityException) e2.getTargetException();
                    errors.add(ChatColor.RED + exception.getMessage() + ChatColor.GRAY + "(Path: " + config.getName() + "/" + exception.getMapNode().getKey() + ")");
                }
            }
        }
        if (allError) errors.remove("§aEntities config loaded without errors.");
        return errors;
    }

}
