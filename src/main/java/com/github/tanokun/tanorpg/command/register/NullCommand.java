package com.github.tanokun.tanorpg.command.register;

import org.bukkit.command.CommandSender;

import java.util.List;

public class NullCommand extends Command{
    public NullCommand() {super(null);}

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage("§cError: NullPointerException");
        return true;
    }
    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
