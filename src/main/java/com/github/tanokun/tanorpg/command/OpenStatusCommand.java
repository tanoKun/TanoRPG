package com.github.tanokun.tanorpg.command;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.command.register.Command;
import com.github.tanokun.tanorpg.menu.player.StatusMainMenu;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class OpenStatusCommand extends Command {

    public OpenStatusCommand(){super("status");}

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        TanoRPG.playSound((Player)sender, Sound.ENTITY_SHULKER_OPEN, 10, 1);
        new StatusMainMenu((Player) sender).openInv((Player) sender);
        return true;
    }
   @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
