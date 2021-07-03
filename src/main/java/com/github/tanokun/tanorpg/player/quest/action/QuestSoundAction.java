package com.github.tanokun.tanorpg.player.quest.action;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.util.command.CommandContext;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class QuestSoundAction implements Action {
    private Sound sound;

    private int volume;
    private double pitch;

    public QuestSoundAction(CommandContext cc){
        sound = Sound.valueOf(cc.getArg(0, ""));
        volume = Integer.valueOf(cc.getArg(1, "1"));
        pitch = Double.valueOf(cc.getArg(2, "1"));
    }

    @Override
    public void execute(Member m) {
        TanoRPG.playSound(Bukkit.getPlayer(m.getUuid()), sound, volume, pitch);
    }
}
