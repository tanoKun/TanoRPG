package com.github.tanokun.tanorpg.event.tanorpg;

import com.github.tanokun.tanorpg.player.Member;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class TanoRpgSellEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    private final Member member;

    public TanoRpgSellEvent(Player who, Member member) {
        super(who);
        this.member = member;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public Member getMember() {
        return member;
    }
}
