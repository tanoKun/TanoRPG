package com.github.tanokun.tanorpg.command;

import com.github.tanokun.tanorpg.command.register.Command;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EntityZombieVillager;
import net.minecraft.server.v1_15_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftVillagerZombie;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.EntityEquipment;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.List;


public class TestCommand extends Command {

    public TestCommand(){super("test");}

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        CraftWorld craftWorld = (CraftWorld) ((Player)sender).getWorld();
        Entity target = new CraftVillagerZombie((CraftServer) Bukkit.getServer(),
                new EntityZombieVillager(EntityTypes.ZOMBIE_VILLAGER, craftWorld.getHandle().getMinecraftWorld()));
        craftWorld.spawn(((Player)sender).getLocation(), target.getClass());
        return true;
    }
    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }

}



