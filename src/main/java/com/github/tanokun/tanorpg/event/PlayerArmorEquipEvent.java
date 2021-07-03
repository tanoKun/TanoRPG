package com.github.tanokun.tanorpg.event;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;

public class PlayerArmorEquipEvent extends PlayerEvent implements Cancellable{

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private final EquipMethod equipType;
    private final ArmorType type;
    private ItemStack oldArmorPiece, newArmorPiece;

    public PlayerArmorEquipEvent(final Player player, final EquipMethod equipType, final ArmorType type, final ItemStack oldArmorPiece, final ItemStack newArmorPiece){
        super(player);
        this.equipType = equipType;
        this.type = type;
        this.oldArmorPiece = oldArmorPiece;
        this.newArmorPiece = newArmorPiece;
    }

    @Contract(pure = true)
    public final static HandlerList getHandlerList(){
        return handlers;
    }

    @Contract(pure = true)
    @Override
    public final HandlerList getHandlers(){
        return handlers;
    }

    public final void setCancelled(final boolean cancel){
        this.cancel = cancel;
    }

    @Contract(pure = true)
    public final boolean isCancelled(){
        return cancel;
    }

    @Contract(pure = true)
    public final ArmorType getType(){
        return type;
    }

    @Contract(pure = true)
    public final ItemStack getOldArmorPiece(){
        return oldArmorPiece;
    }

    public final void setOldArmorPiece(final ItemStack oldArmorPiece){
        this.oldArmorPiece = oldArmorPiece;
    }

    @Contract(pure = true)
    public final ItemStack getNewArmorPiece(){
        return newArmorPiece;
    }

    public final void setNewArmorPiece(final ItemStack newArmorPiece){
        this.newArmorPiece = newArmorPiece;
    }

    @Contract(pure = true)
    public EquipMethod getMethod(){
        return equipType;
    }

    public enum EquipMethod {
        SHIFT_CLICK,
        DRAG,
        HOTBAR,
        HOTBAR_SWAP,
        DISPENSER,
        BROKE,
        DEATH,
        ;
    }

    public static ItemStack getFromPlayer(ArmorType type, HumanEntity who) {
        if (type == null || who == null)
            return null;

        ItemStack item = null;

        switch (type) {
            case HELMET:
                item = who.getInventory().getHelmet();
                break;
            case CHESTPLATE:
                item = who.getInventory().getChestplate();
                break;
            case LEGGINGS:
                item = who.getInventory().getLeggings();
                break;
            case BOOTS:
                item = who.getInventory().getBoots();
                break;
            case SHIELD:
            case OFF_HAND:
                item = who.getInventory().getItemInOffHand();
                break;
        }

        return item;
    }

    public enum ArmorType{
        HELMET(5), CHESTPLATE(6), LEGGINGS(7), BOOTS(8), SHIELD(45), OFF_HAND(45);

        private final int slot;

        ArmorType(int slot){
            this.slot = slot;
        }

        public final static ArmorType matchType(final ItemStack itemStack){
            if(itemStack == null)
                return null;

            switch (itemStack.getType()){
                case DIAMOND_HELMET:
                case GOLDEN_HELMET:
                case IRON_HELMET:
                case CHAINMAIL_HELMET:
                case LEATHER_HELMET:
                    return HELMET;
                case DIAMOND_CHESTPLATE:
                case GOLDEN_CHESTPLATE:
                case IRON_CHESTPLATE:
                case CHAINMAIL_CHESTPLATE:
                case LEATHER_CHESTPLATE:
                    return CHESTPLATE;
                case DIAMOND_LEGGINGS:
                case GOLDEN_LEGGINGS:
                case IRON_LEGGINGS:
                case CHAINMAIL_LEGGINGS:
                case LEATHER_LEGGINGS:
                    return LEGGINGS;
                case DIAMOND_BOOTS:
                case GOLDEN_BOOTS:
                case IRON_BOOTS:
                case CHAINMAIL_BOOTS:
                case LEATHER_BOOTS:
                    return BOOTS;
                case SHIELD:
                    return SHIELD;
                case AIR:
                    return null;
                default:
                    return OFF_HAND;
            }
        }

        public int getSlot(){
            return slot;
        }
    }

    public static class ArmorListener implements Listener {

        @EventHandler
        public final void onInventoryClick(final InventoryClickEvent e){
            boolean shift = false, numberkey = false;

            if(e.isCancelled())
                return;

            if(e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT))
                shift = true;

            if(e.getClick().equals(ClickType.NUMBER_KEY))
                numberkey = true;

            if((e.getSlotType() != SlotType.ARMOR || e.getSlotType() != SlotType.QUICKBAR) && !e.getInventory().getType().equals(InventoryType.CRAFTING))
                return;

            if(!(e.getWhoClicked() instanceof Player))
                return;

            if(e.getCurrentItem() == null)
                return;

            ArmorType newArmorType = ArmorType.matchType(shift ? e.getCurrentItem() : e.getCursor());
            if(!shift && newArmorType != null && e.getRawSlot() != newArmorType.getSlot())
                return;

            if (shift) {
                newArmorType = ArmorType.matchType(e.getCurrentItem());
                if (newArmorType != null && !newArmorType.equals(ArmorType.OFF_HAND)) {

                    boolean equipping = true;
                    if(e.getRawSlot() == newArmorType.getSlot())
                        equipping = false;

                    if (!equipping) {

                        PlayerArmorEquipEvent armorEquipEvent = new PlayerArmorEquipEvent(
                                (Player) e.getWhoClicked(),
                                PlayerArmorEquipEvent.EquipMethod.SHIFT_CLICK, newArmorType,
                                e.getCurrentItem(),
                                null);

                        Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);

                        if(armorEquipEvent.isCancelled())
                            e.setCancelled(true);

                    } else {
                        ItemStack old = PlayerArmorEquipEvent.getFromPlayer(newArmorType, e.getWhoClicked());

                        PlayerArmorEquipEvent armorEquipEvent = new PlayerArmorEquipEvent(
                                (Player) e.getWhoClicked(),
                                PlayerArmorEquipEvent.EquipMethod.SHIFT_CLICK, newArmorType,
                                old,
                                e.getCurrentItem());

                        Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);

                        if(armorEquipEvent.isCancelled())
                            e.setCancelled(true);
                    }
                }

            } else {

                ItemStack newArmorPiece = e.getCursor();
                ItemStack oldArmorPiece = e.getCurrentItem();

                if (numberkey) {
                    if (e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
                        ItemStack hotbarItem = e.getClickedInventory().getItem(e.getHotbarButton());
                        if (hotbarItem != null) {
                            newArmorType = ArmorType.matchType(hotbarItem);
                            newArmorPiece = hotbarItem;
                            oldArmorPiece = e.getClickedInventory().getItem(e.getSlot());
                        } else {
                            newArmorType = ArmorType.matchType(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR ? e.getCurrentItem() : e.getCursor());
                        }
                    }
                } else {
                    newArmorType = ArmorType.matchType(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR ? e.getCurrentItem() : e.getCursor());
                }

                if(newArmorType != null && e.getRawSlot() == newArmorType.getSlot()){
                    PlayerArmorEquipEvent.EquipMethod method = PlayerArmorEquipEvent.EquipMethod.DRAG;
                    if(e.getAction().equals(InventoryAction.HOTBAR_SWAP) || numberkey) method = PlayerArmorEquipEvent.EquipMethod.HOTBAR_SWAP;
                    PlayerArmorEquipEvent armorEquipEvent = new PlayerArmorEquipEvent((Player) e.getWhoClicked(), method, newArmorType, oldArmorPiece, newArmorPiece);
                    Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
                    if (armorEquipEvent.isCancelled())
                        e.setCancelled(true);

                }
            }
        }

        @EventHandler
        public void playerInteractEvent(PlayerInteractEvent e) {
            if (e.getAction() == Action.PHYSICAL)
                return;

            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

                final Player player = e.getPlayer();

                    ArmorType newArmorType = ArmorType.matchType(e.getItem());
                    if (newArmorType != null) {

                        if (newArmorType.equals(ArmorType.HELMET) && e.getPlayer().getInventory().getHelmet() == null
                                || newArmorType.equals(ArmorType.CHESTPLATE) && e.getPlayer().getInventory().getChestplate() == null
                                || newArmorType.equals(ArmorType.LEGGINGS) && e.getPlayer().getInventory().getLeggings() == null
                                || newArmorType.equals(ArmorType.BOOTS) && e.getPlayer().getInventory().getBoots() == null
                                || newArmorType.equals(ArmorType.SHIELD) && e.getPlayer().getInventory().getItemInOffHand() == null
                                || newArmorType.equals(ArmorType.OFF_HAND) && e.getPlayer().getInventory().getItemInOffHand() == null) {

                            PlayerArmorEquipEvent armorEquipEvent = new PlayerArmorEquipEvent(
                                    e.getPlayer(),
                                    PlayerArmorEquipEvent.EquipMethod.HOTBAR,
                                    ArmorType.matchType(e.getItem()),
                                    null, e.getItem());

                            Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
                            if (armorEquipEvent.isCancelled()) {
                                e.setCancelled(true);
                                player.updateInventory();
                        }
                    }
                }
            }
        }

        @EventHandler
        public void dispenserFireEvent(BlockDispenseEvent e){
            ArmorType type = ArmorType.matchType(e.getItem());

            if (ArmorType.matchType(e.getItem()) == null)
                return;

            Location loc = e.getBlock().getLocation();

            for (Player p : loc.getWorld().getPlayers()) {

                if (loc.getBlockY() - p.getLocation().getBlockY() >= -1 && loc.getBlockY() - p.getLocation().getBlockY() <= 1) {

                    if (p.getInventory().getHelmet() == null && type.equals(ArmorType.HELMET)
                            || p.getInventory().getChestplate() == null && type.equals(ArmorType.CHESTPLATE)
                            || p.getInventory().getLeggings() == null && type.equals(ArmorType.LEGGINGS)
                            || p.getInventory().getBoots() == null && type.equals(ArmorType.BOOTS)
                            || p.getInventory().getItemInOffHand() == null && type.equals(ArmorType.SHIELD)) {

                        org.bukkit.block.Dispenser dispenser = (org.bukkit.block.Dispenser) e.getBlock().getState();
                        org.bukkit.material.Dispenser dis = (org.bukkit.material.Dispenser) dispenser.getData();

                        BlockFace directionFacing = dis.getFacing();
                        if(directionFacing == BlockFace.EAST
                                && p.getLocation().getBlockX() != loc.getBlockX()
                                && p.getLocation().getX() <= loc.getX() + 2.3
                                && p.getLocation().getX() >= loc.getX() || directionFacing == BlockFace.WEST
                                && p.getLocation().getX() >= loc.getX() - 1.3 && p.getLocation().getX() <= loc.getX() || directionFacing == BlockFace.SOUTH
                                && p.getLocation().getBlockZ() != loc.getBlockZ()
                                && p.getLocation().getZ() <= loc.getZ() + 2.3
                                && p.getLocation().getZ() >= loc.getZ() || directionFacing == BlockFace.NORTH
                                && p.getLocation().getZ() >= loc.getZ() - 1.3
                                && p.getLocation().getZ() <= loc.getZ()){

                            PlayerArmorEquipEvent armorEquipEvent = new PlayerArmorEquipEvent(
                                    p,
                                    PlayerArmorEquipEvent.EquipMethod.DISPENSER,
                                    ArmorType.matchType(e.getItem()),
                                    null, e.getItem());

                            Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);

                            if(armorEquipEvent.isCancelled())
                                e.setCancelled(true);

                            return;
                        }
                    }
                }
            }
        }

        @EventHandler
        public void itemBreakEvent(PlayerItemBreakEvent e){
            ArmorType type = ArmorType.matchType(e.getBrokenItem());

            if (type == null || type.equals(ArmorType.OFF_HAND))
                return;

            Player p = e.getPlayer();
            PlayerArmorEquipEvent armorEquipEvent = new PlayerArmorEquipEvent(p, PlayerArmorEquipEvent.EquipMethod.BROKE, type, e.getBrokenItem(), null);
            Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);

            if (armorEquipEvent.isCancelled()) {

                ItemStack i = e.getBrokenItem().clone();
                i.setAmount(1);
                i.setDurability((short) (i.getDurability() - 1));
                if (type.equals(ArmorType.HELMET))
                    p.getInventory().setHelmet(i);
                else if (type.equals(ArmorType.CHESTPLATE))
                    p.getInventory().setChestplate(i);
                else if (type.equals(ArmorType.LEGGINGS))
                    p.getInventory().setLeggings(i);
                else if (type.equals(ArmorType.BOOTS))
                    p.getInventory().setBoots(i);
                else if (type.equals(ArmorType.SHIELD))
                    p.getInventory().setItemInOffHand(i);
            }
        }

        @EventHandler
        public void playerDeathEvent(PlayerDeathEvent e){
            Player p = e.getEntity();
            for(ItemStack i : p.getInventory().getArmorContents()){
                if(i != null && !i.getType().equals(Material.AIR)){
                    Bukkit.getServer().getPluginManager().callEvent(new PlayerArmorEquipEvent(p, PlayerArmorEquipEvent.EquipMethod.DEATH, ArmorType.matchType(i), i, null));
                }
            }
        }
    }
}