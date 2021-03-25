package com.github.tanokun.tanorpg.command;

import com.github.tanokun.tanorpg.command.register.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ReloadCommand extends Command {
    public ReloadCommand() {
        super("re");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        for(Player player : Bukkit.getOnlinePlayers()){
            player.kickPlayer("Server reload");
        }
        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
        Bukkit.getServer().reload();
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
