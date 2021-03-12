package com.github.tanokun.tanorpg.command;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.command.register.Command;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class HatCommand extends Command {
    public HatCommand(){super("hat");}

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        EntityEquipment equip = player.getEquipment();
        ItemStack hand = (equip.getItemInMainHand() == null) ? new ItemStack(Material.AIR) : equip.getItemInMainHand();
        ItemStack helmet = (equip.getHelmet() == null) ? new ItemStack(Material.AIR) : equip.getHelmet();
        equip.setHelmet(hand);
        equip.setItemInMainHand(helmet);
        sender.sendMessage(TanoRPG.PX + "装備を交換しました");
        TanoRPG.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 1);

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
