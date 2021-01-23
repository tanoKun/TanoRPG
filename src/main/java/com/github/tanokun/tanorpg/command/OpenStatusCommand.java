package com.github.tanokun.tanorpg.command;

import com.github.tanokun.tanorpg.command.register.Command;
import com.github.tanokun.tanorpg.menu.player.StatusMainMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class OpenStatusCommand extends Command {

    public OpenStatusCommand(){super("status");}

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                "execute " + sender.getName() + " ~ ~ ~ playsound minecraft:entity.shulker.open player @s ~ ~ ~ 10 1");
        new StatusMainMenu((Player) sender).openInv((Player) sender);
        return true;
    }
   @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
