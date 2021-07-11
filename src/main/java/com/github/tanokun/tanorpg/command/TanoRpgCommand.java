package com.github.tanokun.tanorpg.command;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.craft.CraftManager;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.game.entity.spawner.EntitySpawnerManager;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.game.shop.ShopManager;
import com.github.tanokun.tanorpg.player.quest.QuestManager;
import com.github.tanokun.tanorpg.util.command.Command;
import com.github.tanokun.tanorpg.util.command.CommandContext;
import com.github.tanokun.tanorpg.util.command.CommandPermission;
import com.github.tanokun.tanorpg.util.command.TabComplete;
import com.github.tanokun.tanorpg.util.smart_inv.inv.ClickableItem;
import com.github.tanokun.tanorpg.util.smart_inv.inv.SmartInventory;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TanoRpgCommand {
    List<String> configNames = Arrays.asList("item", "entity", "spawner", "shop", "craft", "quest", "all");

    @Command(
            parentName = "tg",
            name = "item",
            desc = ""
    )
    @CommandPermission(
            permission = "tanorpg.command.tg",
            perDefault = PermissionDefault.OP)
    public void giveItem(CommandSender sender, CommandContext commandContext) {
        Player p = (Player) sender;
        if (TanoRPG.getPlugin().getItemManager().getItem(commandContext.getArg(0, "")) == null) {
            p.sendMessage(TanoRPG.PX + "§bID「" + commandContext.getArg(0, "") + "§b」" + "§cは存在しません");
            return;
        }

        if (commandContext.getArg(1, "1").equals("1")) {

            if (!commandContext.getArg(2, "").equals("")) {
                if (Bukkit.getPlayer(commandContext.getArg(2, "")) == null) {
                    p.sendMessage(TanoRPG.PX + "§cプレイヤー「" + commandContext.getArg(2, "") + "」は存在しません");
                    return;
                }
                p = Bukkit.getPlayer(commandContext.getArg(2, ""));
            }

            p.getInventory().addItem(TanoRPG.getPlugin().getItemManager().getItem(commandContext.getArg(0, "")).init(1));
        } else {
            if (!StringUtils.isNumeric(commandContext.getArg(1, "0"))) {
                p.sendMessage(TanoRPG.PX + "§c個数は数字で指定してください");
                return;
            }

            if (!commandContext.getArg(2, "").equals("")) {
                if (Bukkit.getPlayer(commandContext.getArg(2, "")) == null) {
                    p.sendMessage(TanoRPG.PX + "§cプレイヤー「" + commandContext.getArg(2, "") + "」は存在しません");
                    return;
                }
                p = Bukkit.getPlayer(commandContext.getArg(2, ""));
            }

            p.getInventory().addItem(TanoRPG.getPlugin().getItemManager().getItem(
                    commandContext.getArg(0, "")).init(Integer.parseInt(commandContext.getArg(1, "0"))));
        }

        p.sendMessage(TanoRPG.PX + "§a" + p.getName() + "§aに" + "§bID「" + commandContext.getArg(0, "")
                + "§b」" + "§aを" + commandContext.getArg(1, "1") + "個渡しました");
    }

    @TabComplete(
            parentName = "tg",
            name = "item"
    )
    public List<String> giveItemTabComplete(CommandSender sender, CommandContext commandContext) {
        ArrayList<String> tc = new ArrayList<>();

        if (commandContext.getBaseArgs().length <= 2){
            String search = commandContext.getBaseArgs().length == 2 ? commandContext.getBaseArgs()[1] : "";
            TanoRPG.getPlugin().getItemManager().getItemIDs().stream()
                    .filter(t -> t.startsWith(search))
                    .forEach(tc::add);

        } else if (commandContext.getBaseArgs().length == 4){
            String search = commandContext.getBaseArgs().length == 4 ? commandContext.getBaseArgs()[3] : "";
            Bukkit.getOnlinePlayers().stream()
                    .filter(t -> t.getName().startsWith(search))
                    .forEach(t -> tc.add(t.getName()));
        }
        return tc;
    }

    @Command(
            parentName = "tg",
            name = "entity",
            desc = ""
    )
    @CommandPermission(
            permission = "tanorpg.command.tg",
            perDefault = PermissionDefault.OP)
    public void spawnEntity(CommandSender sender, CommandContext commandContext) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(TanoRPG.PX + "§cCONSOLEからは実行できません");
            return;
        }
        if (commandContext.args.size() == 0) {
            sender.sendMessage(TanoRPG.PX + "§cIDを入力してください");
            return;
        }

        if (TanoRPG.getPlugin().getEntityManager().getEntity(commandContext.getArg(0, "")) == null) {
            sender.sendMessage(TanoRPG.PX + "§bID「" + commandContext.getArg(0, "") + "§b」" + "§cは存在しません");
            return;
        }
        TanoRPG.getPlugin().getEntityManager().getEntity(commandContext.getArg(0, "")).spawn(((Player)sender).getLocation());
        sender.sendMessage(TanoRPG.PX + "§bID「" + commandContext.getArg(0, "") + "§b」" + "§aを召喚しました");
    }

    @TabComplete(
            parentName = "tg",
            name = "entity"
    )
    public List<String> spawnEntityTabComplete(CommandSender sender, CommandContext commandContext) {
        ArrayList<String> tc = new ArrayList<>();

        if (commandContext.getBaseArgs().length <= 2){
            String search = commandContext.getBaseArgs().length == 2 ? commandContext.getBaseArgs()[1] : "";
            TanoRPG.getPlugin().getEntityManager().getEntities().keySet().stream()
                    .filter(t -> t.startsWith(search))
                    .forEach(tc::add);

        }
        return tc;
    }

    @Command(
            parentName = "tg",
            name = "reload",
            desc = ""
    )
    @CommandPermission(
            permission = "tanorpg.command.tg",
            perDefault = PermissionDefault.OP)
    public void reloadConfig(CommandSender sender, CommandContext commandContext) {
        if (commandContext.getArg(0, "").equalsIgnoreCase("item")){
            TanoRPG.getPlugin().setItemManager(new ItemManager(sender instanceof Player ? (Player) sender : null));

        } else if (commandContext.getArg(0, "").equalsIgnoreCase("entity")){
            TanoRPG.getPlugin().setEntityManager(new EntityManager(sender instanceof Player ? (Player) sender : null));

        } else if (commandContext.getArg(0, "").equalsIgnoreCase("spawner")) {
            TanoRPG.getPlugin().setEntitySpawnerManager(new EntitySpawnerManager(sender instanceof Player ? (Player) sender : null));

        }else if (commandContext.getArg(0, "").equalsIgnoreCase("shop")){
            TanoRPG.getPlugin().setShopManager(new ShopManager(sender instanceof Player ? (Player) sender : null));

        } else if (commandContext.getArg(0, "").equalsIgnoreCase("craft")){
            TanoRPG.getPlugin().setCraftManager(new CraftManager(sender instanceof Player ? (Player) sender : null));

        } else if (commandContext.getArg(0, "").equalsIgnoreCase("quest")) {
            TanoRPG.getPlugin().setQuestManager(new QuestManager(sender instanceof Player ? (Player) sender : null, TanoRPG.getPlugin()));

        } else if (commandContext.getArg(0, "").equalsIgnoreCase("all")){
            TanoRPG.getPlugin().setItemManager(new ItemManager(sender instanceof Player ? (Player) sender : null));
            TanoRPG.getPlugin().setEntityManager(new EntityManager(sender instanceof Player ? (Player) sender : null));
            TanoRPG.getPlugin().setEntitySpawnerManager(new EntitySpawnerManager(sender instanceof Player ? (Player) sender : null));
            TanoRPG.getPlugin().setQuestManager(new QuestManager(sender instanceof Player ? (Player) sender : null, TanoRPG.getPlugin()));
            TanoRPG.getPlugin().setShopManager(new ShopManager(sender instanceof Player ? (Player) sender : null));
            TanoRPG.getPlugin().setCraftManager(new CraftManager(sender instanceof Player ? (Player) sender : null));

        } else {
            sender.sendMessage(TanoRPG.PX + "§cリロード対象を入力してください");
        }
    }

    @TabComplete(
            parentName = "tg",
            name = "reload"
    )
    public List<String> reloadConfigComplete(CommandSender sender, CommandContext commandContext) {
        ArrayList<String> tc = new ArrayList<>();

        if (commandContext.getBaseArgs().length <= 2){
            String search = commandContext.getBaseArgs().length == 2 ? commandContext.getBaseArgs()[1] : "";
            configNames.stream().filter(t -> t.startsWith(search)).forEach(tc::add);
        }
        return tc;
    }

    @Command(
            parentName = "test",
            name = "init",
            desc = ""
    )
    public void test_init(CommandSender sender, CommandContext commandContext) {
        SmartInventory.builder()
                .id("aaa")
                .title("§9§l購入確認")
                .update(false)
                .provider((player, contents) -> {
                    contents.set(1, 5, ClickableItem.of(new ItemStack(Material.STONE), e -> {
                        int lastAmount = e.getCurrentItem().getAmount();
                        if (lastAmount == 64) {
                            e.getCurrentItem().setAmount(1);
                            contents.setProperty("count", 1);
                            return;
                        }
                        lastAmount = lastAmount * 2;
                        e.getCurrentItem().setAmount(lastAmount);
                        contents.setProperty("count", lastAmount);
                    }));
                })
                .size(3, 9)
                .build().open((Player) sender);
    }
}
