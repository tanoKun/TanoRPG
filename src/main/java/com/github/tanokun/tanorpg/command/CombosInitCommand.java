package com.github.tanokun.tanorpg.command;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.command.register.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CombosInitCommand extends Command {
    public CombosInitCommand(){super("cinit");}

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        ((Player)sender).removeMetadata("COMBO", TanoRPG.getPlugin());
        sender.sendMessage(TanoRPG.PX + "Comboを初期化しました");
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
