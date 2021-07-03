package com.github.tanokun.tanorpg.command;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.player.inv.SelSkillClassMenu;
import com.github.tanokun.tanorpg.util.command.Command;
import com.github.tanokun.tanorpg.util.command.CommandContext;
import com.github.tanokun.tanorpg.util.command.CommandPermission;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

public class RpgDataCommand {

    @Command(
            parentName = "rd",
            name = "set",
            desc = "色々設定"
    )
    @CommandPermission(

            permission = "tanorpg.command.rd",
            perDefault = PermissionDefault.OP
    )
    public void subSpawnLocation(CommandSender sender, CommandContext commandContext) {
        SelSkillClassMenu.getInv((Player) sender).open((Player) sender);
        Player p = (Player) sender;
        if (commandContext.getArg(0, "").equals("spawn-loc")) {
            double x = p.getLocation().getX();
            double y = p.getLocation().getY();
            double z = p.getLocation().getZ();
            World world = p.getWorld();
            TanoRPG.getPlugin().getDataManager().setStartLoc(p.getLocation());
            sender.sendMessage(TanoRPG.PX + "スポーン位置を「x:" + Math.round(x) + ",y:" + Math.round(y) + ",z:" + Math.round(z) + "」に設定しました");
        }

        if (commandContext.getArg(0, "").equals("spawn-region-name")) {
            if (commandContext.getArg(1, null) == null){
                sender.sendMessage(TanoRPG.PX + "§c名前を入力してください");
                return;
            }
            TanoRPG.getPlugin().getDataManager().setStartRegionName(commandContext.getArg(1, null));
            sender.sendMessage(TanoRPG.PX + "保護領域名を「" + commandContext.getArg(1, null) + "」に設定しました");
        }
    }

}
