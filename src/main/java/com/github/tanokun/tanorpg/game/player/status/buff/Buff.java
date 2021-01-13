package com.github.tanokun.tanorpg.game.player.status.buff;

import com.github.tanokun.tanorpg.TanoRPG;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;

public class Buff {
    private static HashMap<Entity, ArrayList<BuffSelf>> buffs = new HashMap<>();

    public static double getBuffPercent(Entity entity, BuffType type){
        if (buffs.get(entity) == null) return 0;
        for (BuffSelf buff : buffs.get(entity)){
            if (buff.getType().equals(type)){
                return buff.getType().getPercent();
            }
        }
        return 0;
    }
    public static void addBuff(BuffSelf buff){
        if (buffs.get(buff.getOwner()) == null){
            ArrayList<BuffSelf> buffList = new ArrayList<>();
            buffList.add(buff);
            buffs.put(buff.getOwner(), buffList);
            buff.getOwner().sendMessage(TanoRPG.PX_BUFF_UP + buff.getType().getName());
        } else {
            ArrayList<BuffSelf> buffList = buffs.get(buff.getOwner());
            ListIterator<BuffSelf> iterator = buffList.listIterator();
            while (iterator.hasNext()) {
                BuffSelf value = iterator.next();
                if (value.getType().equals(buff.getType())) {
                    iterator.set(buff);
                }
            }
            buffList.add(buff);
            buff.getOwner().sendMessage(TanoRPG.PX_BUFF_UP + buff.getType().getName());
        }
    }
    public static void start(){
        new BukkitRunnable(){
            @Override
            public void run() {
                for(ArrayList list : buffs.values()){
                    Iterator<BuffSelf> iterator = list.iterator();
                    while (iterator.hasNext()){
                        BuffSelf buff = iterator.next();
                        buff.temp_seconds++;
                        if (buff.temp_seconds >= buff.getSeconds()) {
                            iterator.remove();
                            buff.getOwner().sendMessage(TanoRPG.PX_BUFF_DOWN + buff.getType().getName());
                        }
                    }
                }
                Iterator<ArrayList<BuffSelf>> iterator = buffs.values().iterator();
            }
        }.runTaskTimerAsynchronously(TanoRPG.getPlugin(), 0, 20);
    }
}
