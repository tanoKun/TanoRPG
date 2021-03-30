package com.github.tanokun.tanorpg.command;

import com.github.tanokun.tanorpg.command.register.Command;
import com.github.tanokun.tanorpg.game.mission.MissionManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;


public class TestCommand extends Command {

    public TestCommand(){super("test");}

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        MissionManager.loadData(((Player)sender).getUniqueId());
        return true;
    }
    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}

