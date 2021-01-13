package com.github.tanokun.tanorpg.command.register;

import org.bukkit.command.CommandSender;

public class NullCommand extends Command{
    public NullCommand() {super(null);}

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage("§cError: NullPointerException");
        return true;
    }
}
