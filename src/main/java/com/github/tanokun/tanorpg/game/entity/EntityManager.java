package com.github.tanokun.tanorpg.game.entity;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.base.ObjectEntity;
import com.github.tanokun.tanorpg.player.EquipmentMap;
import com.github.tanokun.tanorpg.player.status.StatusMap;
import com.github.tanokun.tanorpg.player.status.StatusType;
import com.github.tanokun.tanorpg.util.command.CommandContext;
import com.github.tanokun.tanorpg.util.io.Config;
import com.github.tanokun.tanorpg.util.io.Folder;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;

public class EntityManager {
    private HashMap<String, ObjectEntity> entities = new HashMap<>();

    public EntityManager(Player player){
        loadEntity(player);
    }

    public void loadEntity(Player p) {
        try {
            for (Config config : new Folder("mobs", TanoRPG.getPlugin()).getFiles()) {
                String name = config.getConfig().getString("name", "unknown");

                EntityType entityType = EntityType.valueOf(config.getConfig().getString("entityType", "ZOMBIE"));

                int level = config.getConfig().getInt("level", 0);

                int exp = config.getConfig().getInt("exp", 0);

                StatusMap statusMap = new StatusMap(); config.getConfig().getConfigurationSection("statuses").getKeys(false).forEach(text -> {
                    statusMap.addStatus(StatusType.valueOf(text), config.getConfig().getInt("statuses." + text, 0));
                });

                EquipmentMap equipMap = new EquipmentMap(); config.getConfig().getConfigurationSection("armors").getKeys(false).forEach(text -> {
                    equipMap.setEquip(EquipmentMap.EquipmentType.valueOf(text),
                            TanoRPG.getPlugin().getItemManager().getItem(config.getConfig().getString("armors." + text)).init(1));
                });

                EntityDropItems dropItems = new EntityDropItems(); final CommandContext cc = new CommandContext(null, new String[]{""});
                config.getConfig().getList("drops", new ArrayList<String>()).forEach(text -> {
                    String text2 = text + "";
                    cc.init(null, text2.split(" "));
                    dropItems.addDrop(
                            TanoRPG.getPlugin().getItemManager().getItem(cc.getArg(0, "")).init(Integer.valueOf(cc.getArg(1, "null"))),
                            Double.valueOf(cc.getArg(2, "null")));
                });
                ObjectEntity objectEntity;
                Constructor<? extends ObjectEntity> constructor =
                        entityType.getClazz().getConstructor(Config.class, String.class, StatusMap.class, EquipmentMap.class, EntityDropItems.class, int.class, int.class);
                objectEntity = constructor.newInstance(config, name, statusMap, equipMap, dropItems, exp, level);
                entities.put(name, objectEntity);

            }
        }catch (Exception e){
            if (p != null){
                p.sendMessage(TanoRPG.PX + "§cエンティティ系のコンフィグでエラーが発生しました。");
            } else {
                TanoRPG.getPlugin().getLogger().warning("エンティティ系のコンフィグでエラーが発生しました。");
            }
        }

        if (p != null){
            p.sendMessage(TanoRPG.PX + "§aエンティティ系のコンフィグをロードしました。");
        } else {
            TanoRPG.getPlugin().getLogger().warning("エンティティ系のコンフィグをロードしました。");
        }
    }

    public HashMap<String, ObjectEntity> getEntities() {
        return entities;
    }

    public ObjectEntity getEntity(String name){
        return entities.get(name);
    }
}
