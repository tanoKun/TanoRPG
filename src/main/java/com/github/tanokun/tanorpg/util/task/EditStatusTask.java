package com.github.tanokun.tanorpg.util.task;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayer;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.status.StatusType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class EditStatusTask extends BukkitRunnable {
    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            boolean bool = false;
            GamePlayer game = GamePlayerManager.getPlayer(p.getUniqueId());
            if (game == null) continue;
            ArrayList<ItemStack> items = new ArrayList<>();
            items.add(p.getEquipment().getHelmet());
            items.add(p.getEquipment().getChestplate());
            items.add(p.getEquipment().getLeggings());
            items.add(p.getEquipment().getBoots());
            int i = 1;
            boolean bool2 = false;
            for (ItemStack is : items) {
                if (!(is == null) && !(is.getType().equals(Material.AIR))) {
                    if (!game.isProper(is)) {
                        p.getInventory().addItem(is);
                        p.getInventory().setItem(40 - i, new ItemStack(Material.AIR));
                        bool = true;
                        break;
                    }
                    if (!game.isLv(is)){
                        p.getInventory().addItem(is);
                        p.getInventory().setItem(40 - i, new ItemStack(Material.AIR));
                        bool2 = true;
                        break;
                    }
                }
                i += 1;
            }
            if (bool == true) {
                p.sendMessage(TanoRPG.PX + "§c対応していない装備です");
            }
            if (bool2 == true) {
                p.sendMessage(TanoRPG.PX + "§c必要レベルが足りません");
            }
            game.setMAX_HP(game.getStatus(StatusType.HP).getLevel() + game.getTHIS_MAX_HP());
            game.setMAX_MP(game.getStatus(StatusType.MP).getLevel() + game.getTHIS_MAX_MP());
        }
    }
}
