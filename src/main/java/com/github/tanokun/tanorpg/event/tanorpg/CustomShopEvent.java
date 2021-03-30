package com.github.tanokun.tanorpg.event.tanorpg;

import com.github.tanokun.tanorpg.game.craft.Craft;
import com.github.tanokun.tanorpg.game.item.itemtype.base.Item;
import com.github.tanokun.tanorpg.game.shop.Shop;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Contract;

public class CustomShopEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Item item;
    private final long price;
    private final Shop shop;

    public CustomShopEvent(Player player, Item item, long price, Shop shop) {
        this.player = player;
        this.item = item;
        this.price = price;
        this.shop = shop;
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
    public Item getItem() {return item;}
    public Shop getShop() {return shop;}
}
