package com.github.tanokun.tanorpg.player.status.buff;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.util.SaveMarker;
import com.github.tanokun.tanorpg.util.io.Config;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.bukkit.entity.Player;

import java.lang.reflect.Type;
import java.util.HashMap;

public class BuffMap implements SaveMarker<BuffMap> {

    public static final String PX_BUFF_UP = "§7[-｜ バフ付与 ｜-] §7=> ";
    public static final String PX_BUFF_DOWN = "§7[-｜ バフ解除 ｜-] §7=> ";

    private HashMap<BuffType, Integer> buff = new HashMap<>();

    public void addBuff(BuffType buffType, int time, Player player) {
        player.sendMessage(PX_BUFF_UP + buffType.getName());

        Member member = TanoRPG.getPlugin().getMemberManager().getMember(player.getUniqueId());
        switch (buffType) {
            case INSTANT_HEAL_HP:
                member.setHP(member.getHasHP() + time);
                return;
            case INSTANT_HEAL_MP:
                member.setMP(member.getHasMP() + time);
                return;
        }

        buff.put(buffType, time);
    }

    public void clearBuff(Player player) {
        if (buff.keySet().size() == 0) return;
        player.sendMessage(PX_BUFF_DOWN + "強制解除");
        buff.clear();
    }

    public void removeBuff(BuffType buffType, Player player) {


        
        if (!buff.keySet().contains(buffType)) return;
        player.sendMessage(PX_BUFF_DOWN + buffType.getName());
        buff.remove(buffType);
    }

    public HashMap<BuffType, Integer> getBuff() {
        return buff;
    }

    public double getValue(BuffType b) {
        if (!buff.containsKey(b)) return 0;
        return b.getPercent();
    }

    @Override
    public void save(Config config, String key) {
        config.getConfig().set(key + "buff", new Gson().toJson(buff));
    }

    @Override
    public BuffMap load(Config config, String key) {
        Type type = new TypeToken<HashMap<BuffType, Integer>>() {}.getType();
        buff = new Gson().fromJson(config.getConfig().getString(key + "buff"), type);
        return this;
    }
}
