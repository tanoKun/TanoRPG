package com.github.tanokun.tanorpg.player;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.player.status.StatusType;
import com.github.tanokun.tanorpg.player.status.buff.BuffType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class MemberRunnable extends BukkitRunnable {
    private Member member;

    private Location last = new Location(Bukkit.getWorld("world"), 0, 0, 0);

    private int heal = 0;

    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public void run() {
        if (!Bukkit.getOfflinePlayer(member.getUuid()).isOnline()) {
            cancel();
            return;
        }

        if (member.getPlayer().hasMetadata("die")) return;

        if (heal >= 3) {
            Location location = member.getPlayer().getLocation();
            location.setYaw(0);
            location.setPitch(0);
            if (location.equals(last)) {
                if (member.getHasHP() < member.getStatusMap().getStatus(StatusType.HP)) {
                    int hp = (int) Math.round(member.getStatusMap().getStatus(StatusType.HP) * 0.03);
                    member.setHasHP(member.getHasHP() + hp);
                    if (member.getHasHP() >= member.getStatusMap().getStatus(StatusType.HP))
                        member.setHasHP((int) (Math.round(member.getStatusMap().getStatus(StatusType.HP))));
                    int r = (int) (Math.round(member.getStatusMap().getStatus(StatusType.HP)) / 20);
                    if (r <= 0) r = 1;
                    hp = member.getHasHP() / r;
                    if (hp <= 20) {
                        if (member.getHasHP() / r > 0.5) member.getPlayer().setHealth(member.getHasHP() / r);
                        TanoRPG.getPlugin().getSidebarManager().updateSidebar(member.getPlayer(), member);
                    }
                    TanoRPG.getPlugin().getSidebarManager().updateSidebar(Objects.requireNonNull(Bukkit.getPlayer(member.getUuid())), member);
                }
            } else {
                heal = location.equals(last) ? heal + 1 : 0;
                last = location;
            }
        } else {
            Location location = member.getPlayer().getLocation();
            location.setYaw(0);
            location.setPitch(0);
            heal = location.equals(last) ? heal + 1 : 0;
            last = location;
        }

        for (BuffType buffType : member.getBuffMap().getBuff().keySet()) {
            member.getBuffMap().getBuff().put(buffType, member.getBuffMap().getBuff().get(buffType) - 1);
            if (member.getBuffMap().getBuff().get(buffType) < 0) {
                member.getBuffMap().removeBuff(buffType, member.getPlayer());
            }
        }

        if (member.getHasMP() >= member.getStatusMap().getStatus(StatusType.MP)) return;
        double p = member.getStatusMap().getStatus(StatusType.HEAL_MP);
        p = p / 100;
        member.setHasMP((int) Math.round(member.getHasMP() + (member.getStatusMap().getStatus(StatusType.MP) * p)));
        if (member.getHasMP() >= member.getStatusMap().getStatus(StatusType.MP))
            member.setHasMP((int) Math.round(member.getStatusMap().getStatus(StatusType.MP)));
        TanoRPG.getPlugin().getSidebarManager().updateSidebar(Objects.requireNonNull(Bukkit.getPlayer(member.getUuid())), member);
    }
}