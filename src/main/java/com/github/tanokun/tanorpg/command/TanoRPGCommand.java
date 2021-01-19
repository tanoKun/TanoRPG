package com.github.tanokun.tanorpg.command;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.command.register.Command;
import com.github.tanokun.tanorpg.game.item.CustomItemManager;
import com.github.tanokun.tanorpg.game.mob.CustomEntityManager;
import com.github.tanokun.tanorpg.game.shop.ShopManager;
import com.github.tanokun.tanorpg.game.craft.CraftManager;
import org.bukkit.command.CommandSender;

public class TanoRPGCommand extends Command {
    public TanoRPGCommand(){super("tanorpg");}

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {error(sender); return true;}
        if (args[0].equals("rei")){
            sender.sendMessage(TanoRPG.PX + "reloading item configs....");
            CustomItemManager.deleteCustomItems();
            sender.sendMessage(TanoRPG.PX + CustomItemManager.loadCustomItemAll());
        }
        else if (args[0].equals("rem")) {
            sender.sendMessage(TanoRPG.PX + "reloading entity configs....");
            CustomEntityManager.deleteEntities();
            sender.sendMessage(TanoRPG.PX + CustomEntityManager.loadCustomEntity());
        }else if (args[0].equals("res")){
            sender.sendMessage(TanoRPG.PX + "reloading shop configs....");
            ShopManager.deleteShops();
            sender.sendMessage(TanoRPG.PX + ShopManager.loadShops());
        } else if (args[0].equals("rec")){
            sender.sendMessage(TanoRPG.PX + "reloading craft configs....");
            CraftManager.deleteCrafts();
            sender.sendMessage(TanoRPG.PX + CraftManager.loadCrafts());
        }
        else{error(sender); return true;}

        return true;
    }
    private void error(CommandSender sender){
        sender.sendMessage("§e------------------------------");
        sender.sendMessage("§b/tanorpg rei  §fアイテムコンフィグをreloadします");
        sender.sendMessage("§b/tanorpg rem  §fエンティティコンフィグをreloadします");
        sender.sendMessage("§b/tanorpg res  §fショップコンフィグをreloadします");
        sender.sendMessage("§b/tanorpg rec  §fクラフトコンフィグをreloadします");
        sender.sendMessage("§e------------------------------");
    }
}
