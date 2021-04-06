package com.github.tanokun.tanorpg.command;

import com.github.tanokun.tanorpg.command.register.Command;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import net.minecraft.server.v1_15_R1.ItemStack;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;

import java.util.List;


public class TestCommand extends Command {

    public TestCommand(){super("test");}

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        EntityEquipment equipment = ((Player)sender).getEquipment();
        return true;
    }
    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}

