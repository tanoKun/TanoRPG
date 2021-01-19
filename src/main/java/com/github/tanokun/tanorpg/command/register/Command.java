package com.github.tanokun.tanorpg.command.register;

import org.bukkit.command.CommandSender;

abstract public class Command {
    private String commandName;
    public Command(String name){this.commandName = name;}
    abstract public boolean execute(CommandSender sender, String[] args);
    public String getName() {return commandName;}
}