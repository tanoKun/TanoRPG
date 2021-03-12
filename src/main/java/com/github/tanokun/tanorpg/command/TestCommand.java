package com.github.tanokun.tanorpg.command;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.command.register.Command;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.game.entity.spawner.EntitySpawner;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;


public class TestCommand extends Command {

    public TestCommand(){super("test");}

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        EntitySpawner entitySpawner = new EntitySpawner(EntityManager.getEntityData("ゴブリントロル"), player.getLocation(),
                5, 16, 4, 1, 5);
        TanoRPG.getEntitySpawnerManager().registerSpawner(entitySpawner);
        return true;
    }
    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}

