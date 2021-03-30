package com.github.tanokun.tanorpg.event.tanorpg;

import com.github.tanokun.tanorpg.game.craft.Craft;
import com.github.tanokun.tanorpg.game.item.itemtype.base.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Contract;

public class CustomCraftEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Item item;
    private final Craft craft;

    public CustomCraftEvent(Player player, Item item, Craft craft) {
        this.player = player;
        this.item = item;
        this.craft = craft;
    }

    @Contract(pure = true)
    public static HandlerList getHandlerList() {
        return handlers;
    }
    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {return player;}
    public Craft getCraft() {return craft;}
    public Item getItem() {return item;}
}
