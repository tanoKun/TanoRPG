package com.github.tanokun.tanorpg.game.craft;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.item.ItemRarityType;
import com.github.tanokun.tanorpg.game.item.type.ItemMaterial;
import com.github.tanokun.tanorpg.game.item.type.base.ItemBase;
import com.github.tanokun.tanorpg.player.status.StatusMap;
import com.github.tanokun.tanorpg.player.status.StatusType;
import com.github.tanokun.tanorpg.util.io.Config;
import com.github.tanokun.tanorpg.util.io.Folder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class CraftManager {
    private final HashMap<String, CraftItem> crafts = new HashMap<>();

    public CraftManager(Player p){
        loadCraft(p);
    }

    public void loadCraft(Player p) {
        try {
            for (Config config : new Folder("items" + File.separator + "material", TanoRPG.getPlugin()).getFiles()) {
                for (String id : config.getConfig().getKeys(false)) {

                    /**
                     furnace1:
                        name: 炉
                        permission: true
                        items:
                            stone:
                                price: 100
                                permission: true
                                necI:
                                    stone2: 10
                                necT:
                                 - hammer

                     */
                }
            }
        }catch (Exception e){
            if (p != null){
                p.sendMessage(TanoRPG.PX + "§cクラフト系のコンフィグでエラーが発生しました。");
            } else {
                TanoRPG.getPlugin().getLogger().warning("クラフト系のコンフィグでエラーが発生しました。");
            }
        }

        if (p != null){
            p.sendMessage(TanoRPG.PX + "§cクラフト系のコンフィグをロードしました。");
        } else {
            TanoRPG.getPlugin().getLogger().warning("クラフト系のコンフィグをロードしました。");
        }
    }
}
