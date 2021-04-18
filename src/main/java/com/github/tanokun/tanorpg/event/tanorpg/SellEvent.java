package com.github.tanokun.tanorpg.event.tanorpg;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Contract;

public class SellEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final long price;
    private final Inventory inv;

    public SellEvent(Player player, long price, Inventory inventory) {
        this.player = player;
        this.price = price;
        this.inv = inventory;
    }

    @Contract(pure = true)
    public static HandlerList getHandlerList() {
        return handlers;
    }
    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {return player;}
    public long getPrice() {return price;}
    public Inventory getInv() {return inv;}
}
