package com.github.tanokun.tanorpg.command;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.command.register.Command;
import com.github.tanokun.tanorpg.game.craft.CraftManager;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.game.shop.ShopManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TanoRPGCommand extends Command {

    private ArrayList<String> mainTab = new ArrayList<>(Arrays.asList("rei", "rem", "res", "rec", "resp"));

    public TanoRPGCommand(){super("tanorpg");}

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {error(sender); return true;}
        if (args[0].equals("rei")){
            sender.sendMessage(TanoRPG.PX + "reloading item configs....");
            ItemManager.deleteItems();
            for (String error : ItemManager.loadMaterialItem()) sender.sendMessage(TanoRPG.PX + error);
            for (String error : ItemManager.loadWeaponItem()) sender.sendMessage(TanoRPG.PX + error);
            for (String error : ItemManager.loadMagicWeaponItem()) sender.sendMessage(TanoRPG.PX + error);
            for (String error : ItemManager.loadEquipmentItem()) sender.sendMessage(TanoRPG.PX + error);
        }
        else if (args[0].equals("rem")) {
            sender.sendMessage(TanoRPG.PX + "reloading entity configs....");
            sender.sendMessage(TanoRPG.PX + EntityManager.loadData());
        }else if (args[0].equals("res")){
            sender.sendMessage(TanoRPG.PX + "reloading shop configs....");
            ShopManager.deleteShops();
            sender.sendMessage(TanoRPG.PX + ShopManager.loadShops());
        } else if (args[0].equals("rec")){
            sender.sendMessage(TanoRPG.PX + "reloading craft configs....");
            CraftManager.deleteCrafts();
            sender.sendMessage(TanoRPG.PX + CraftManager.loadCrafts());
        } else if (args[0].equals("resp")){
            sender.sendMessage(TanoRPG.PX + "reloading spawners....");
            for (String error : TanoRPG.getEntitySpawnerManager().loadSpawner()) Bukkit.getConsoleSender().sendMessage(TanoRPG.PX +  "  " + error);
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
    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return mainTab;
    }
}
