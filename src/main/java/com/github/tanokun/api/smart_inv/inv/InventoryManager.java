package com.github.tanokun.api.smart_inv.inv;

import com.github.tanokun.api.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.api.smart_inv.inv.opener.ChestInventoryOpener;
import com.github.tanokun.api.smart_inv.inv.opener.InventoryOpener;
import com.github.tanokun.api.smart_inv.inv.opener.SpecialInventoryOpener;
import com.github.tanokun.tanorpg.event.tanorpg.SellEvent;
import com.github.tanokun.tanorpg.game.shop.sell.Sell;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class InventoryManager
{
    private JavaPlugin plugin;
    private PluginManager pluginManager;

    private Map<Player, SmartInventory> inventories;
    private Map<Player, InventoryContents> contents;

    private Map<Player, InvTask> updates;

    private List<InventoryOpener> defaultOpeners;
    private List<InventoryOpener> openers;

    public InventoryManager(JavaPlugin plugin)
    {
        this.plugin = plugin;
        this.pluginManager = Bukkit.getPluginManager();

        this.inventories = new HashMap<>();
        this.contents = new HashMap<>();
        this.updates = new HashMap<>();

        this.defaultOpeners = Arrays.asList(
                new ChestInventoryOpener(),
                new SpecialInventoryOpener()
        );

        this.openers = new ArrayList<>();
    }

    public void init()
    {
        pluginManager.registerEvents(new InvListener(), plugin);
    }

    public Optional<InventoryOpener> findOpener(InventoryType type)
    {
        Optional<InventoryOpener> opInv = this.openers.stream()
                .filter(opener -> opener.supports(type))
                .findAny();

        if (!opInv.isPresent())
        {
            opInv = this.defaultOpeners.stream()
                    .filter(opener -> opener.supports(type))
                    .findAny();
        }

        return opInv;
    }

    public void registerOpeners(InventoryOpener... openers)
    {
        this.openers.addAll(Arrays.asList(openers));
    }

    public List<Player> getOpenedPlayers(SmartInventory inv)
    {
        List<Player> list = new ArrayList<>();

        this.inventories.forEach((player, playerInv) -> {
            if(inv.equals(playerInv))
                list.add(player);
        });

        return list;
    }

    public Optional<SmartInventory> getInventory(Player p)
    {
        return Optional.ofNullable(this.inventories.get(p));
    }

    protected void setInventory(Player p, SmartInventory inv)
    {
        if (inv == null) {
            this.inventories.remove(p);
            if (updates.get(p) != null) {
                InvTask invTask = updates.get(p);
                invTask.cancel();
                updates.remove(p);
            }
        }
        else {
            this.inventories.put(p, inv);
            if (inv.isUpdate()) {
                InvTask invTask = new InvTask(p);
                invTask.runTaskTimer(plugin, 1, inv.getUpdatePeriod());
                updates.put(p, invTask);
            }
        }
    }

    public Optional<InventoryContents> getContents(Player p)
    {
        return Optional.ofNullable(this.contents.get(p));
    }

    protected void setContents(Player p, InventoryContents contents)
    {
        if (contents == null) this.contents.remove(p);
        else this.contents.put(p, contents);
    }

    @SuppressWarnings("unchecked")
    class InvListener implements Listener
    {
        @EventHandler
        public void onInventoryClick(InventoryClickEvent e)
        {
            Player p = (Player) e.getWhoClicked();
            if (!inventories.containsKey(p)) return;

            SmartInventory inv = inventories.get(p);

             e.setCancelled(inv.isCancelable());

            if (e.getClickedInventory() == p.getOpenInventory().getTopInventory())
            {

                int row = e.getSlot() / 9;
                int column = e.getSlot() % 9;

                if (row < 0 || column < 0) return;

                if (row >= inv.getRows() || column >= inv.getColumns()) return;

                inv.getListeners().stream()
                        .filter(listener -> listener.getType() == InventoryClickEvent.class)
                        .forEach(listener -> ((InventoryListener<InventoryClickEvent>) listener).accept(e));

                contents.get(p).get(row, column).ifPresent(item -> item.run(e));
            }
        }
        @EventHandler
        public void onInventoryDrag(InventoryDragEvent e)
        {
            Player p = (Player) e.getWhoClicked();

            if (!inventories.containsKey(p)) return;

            SmartInventory inv = inventories.get(p);

            for (int slot : e.getRawSlots())
            {
                if (slot >= p.getOpenInventory().getTopInventory().getSize()) continue;
                e.setCancelled(inv.isCancelable());
                break;
            }

            inv.getListeners().stream()
                    .filter(listener -> listener.getType() == InventoryDragEvent.class)
                    .forEach(listener -> ((InventoryListener<InventoryDragEvent>) listener).accept(e));
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onInventoryOpen(InventoryOpenEvent e)
        {
            Player p = (Player) e.getPlayer();

            if (!inventories.containsKey(p)) return;

            SmartInventory inv = inventories.get(p);

            inv.getListeners().stream()
                    .filter(listener -> listener.getType() == InventoryOpenEvent.class)
                    .forEach(listener -> ((InventoryListener<InventoryOpenEvent>) listener).accept(e));
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onInventoryClose(InventoryCloseEvent e)
        {
            Player p = (Player) e.getPlayer();

            if (!inventories.containsKey(p)) return;

            SmartInventory inv = inventories.get(p);

            inv.getListeners().stream()
                    .filter(listener -> listener.getType() == InventoryCloseEvent.class)
                    .forEach(listener -> ((InventoryListener<InventoryCloseEvent>) listener).accept(e));

            if (inv.isCloseable())
            {
                if (inv.isUpdate()) {
                    InvTask invTask = updates.get(p);
                    invTask.cancel();
                    updates.remove(p);
                }

                e.getInventory().clear();
                inventories.remove(p);
                contents.remove(p);
            }


            else Bukkit.getScheduler().runTask(plugin, () -> p.openInventory(e.getInventory()));
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerQuit(PlayerQuitEvent e)
        {
            Player p = e.getPlayer();

            if (!inventories.containsKey(p)) return;

            SmartInventory inv = inventories.get(p);

            inv.getListeners().stream()
                    .filter(listener -> listener.getType() == PlayerQuitEvent.class)
                    .forEach(listener -> ((InventoryListener<PlayerQuitEvent>) listener).accept(e));

            inventories.remove(p);
            contents.remove(p);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPluginDisable(PluginDisableEvent e)
        {
            new HashMap<>(inventories).forEach((player, inv) -> {
                inv.getListeners().stream()
                        .filter(listener -> listener.getType() == PluginDisableEvent.class)
                        .forEach(listener -> ((InventoryListener<PluginDisableEvent>) listener).accept(e));
                inv.close(player);
            });

            inventories.clear();
            contents.clear();
        }
        @EventHandler
        public void onSell(SellEvent e){
            new HashMap<>(inventories).forEach((player, inv) -> {
                inv.getListeners().stream()
                        .filter(listener -> listener.getType() == SellEvent.class)
                        .forEach(listener -> ((InventoryListener<SellEvent>) listener).accept(e));
                inv.close(player);
            });
        }
    }

    class InvTask extends BukkitRunnable {
        private final Player player;

        InvTask(Player player) {
            this.player = player;
        }


        @Override
        public void run()
        {
            inventories.get(player).getProvider().update(player, contents.get(player));
        }
    }
}