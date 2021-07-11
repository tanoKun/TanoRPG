package com.github.tanokun.tanorpg.event.tanorpg;

import com.github.tanokun.tanorpg.game.craft.CraftItem;
import com.github.tanokun.tanorpg.player.Member;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class TanoRpgCraftEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    private final Member member;

    private final CraftItem item;

    public TanoRpgCraftEvent(Player who, Member member, CraftItem item) {
        super(who);
        this.member = member;
        this.item = item;
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

    public CraftItem getItem() {
        return item;
    }
}
