package com.github.tanokun.tanorpg.util.hologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class SelectableHologram {

    private static int versionNumber;
    private static boolean executedInit = false;
    private static boolean ready = false;
    private static Class<?> armorStand, spawnPacket, world, craftWorld, craftPlayer, iChatBaseComponent,
            chatComponentText, entityLiving, entityPlayer, playerConnection, packetRaw, nbtTagCompound, packetDestroy,
            entityTypes, packetTeleport, entity, packetEntityMetadata, dataWatcher;

    private static JavaPlugin plugin;
    private static HashMap<UUID, SelectableHologram> holograms = new HashMap<>();

    private List<ChoiceComponent> choices = new ArrayList<>();
    private HashMap<String, ChoiceComponent> choicesMap = new HashMap<>();
    private Hologram holo;
    private BukkitTask task;
    private ChoiceComponent currentChoice;
    private Player displayingPlayer;

    public static void init(JavaPlugin plugin) {
        executedInit = true;
        SelectableHologram.plugin = plugin;

        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";

        String verTemp = version.substring(3);
        versionNumber = Integer.parseInt(verTemp.substring(0, verTemp.indexOf("_")));

        try {
            armorStand = Class.forName("net.minecraft.server." + version + "EntityArmorStand");
            spawnPacket = Class.forName("net.minecraft.server." + version + "PacketPlayOutSpawnEntityLiving");
            world = Class.forName("net.minecraft.server." + version + "World");
            craftWorld = Class.forName("org.bukkit.craftbukkit." + version + "CraftWorld");
            craftPlayer = Class.forName("org.bukkit.craftbukkit." + version + "entity.CraftPlayer");
            iChatBaseComponent = Class.forName("net.minecraft.server." + version + "IChatBaseComponent");
            chatComponentText = Class.forName("net.minecraft.server." + version + "ChatComponentText");
            entityLiving = Class.forName("net.minecraft.server." + version + "EntityLiving");
            entityPlayer = Class.forName("net.minecraft.server." + version + "EntityPlayer");
            playerConnection = Class.forName("net.minecraft.server." + version + "PlayerConnection");
            packetRaw = Class.forName("net.minecraft.server." + version + "Packet");
            packetDestroy = Class.forName("net.minecraft.server." + version + "PacketPlayOutEntityDestroy");
            packetTeleport = Class.forName("net.minecraft.server." + version + "PacketPlayOutEntityTeleport");
            entity = Class.forName("net.minecraft.server." + version + "Entity");

            if (versionNumber <= 8) {
                nbtTagCompound = Class.forName("net.minecraft.server." + version + "NBTTagCompound");
            }
            if (versionNumber >= 14) {
                entityTypes = Class.forName("net.minecraft.server." + version + "EntityTypes");
            }
            if (versionNumber >= 15) {
                packetEntityMetadata = Class.forName("net.minecraft.server." + version + "PacketPlayOutEntityMetadata");
                dataWatcher = Class.forName("net.minecraft.server." + version + "DataWatcher");
            }

            HoloComponent.init(packetTeleport, armorStand, craftPlayer, entityPlayer, playerConnection, packetRaw,
                    entity);

            ready = true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Bukkit.getPluginManager().registerEvents(new ClickDetectListener(), plugin);
    }

    public static SelectableHologram create() {
        if (!executedInit)
            throw new IllegalStateException("This class is not initialized yet. Please execute SelectableHologram#init() in onEnable() in your main class.");
        if (!ready)
            throw new IllegalStateException("SelectableHologram initialization failed. Correct the error output at startup.");
        return new SelectableHologram();
    }

    private SelectableHologram() {
    }

    public static SelectableHologram getCurrentHologram(Player p) {
        return holograms.getOrDefault(p.getUniqueId(), null);
    }

    public static void resetCurrentHologram(Player p) {
        if (p == null) {
            return;
        }
        if (!holograms.containsKey(p.getUniqueId())) {
            return;
        }

        SelectableHologram currentHologram = holograms.get(p.getUniqueId());

        if (currentHologram.task != null)
            currentHologram.task.cancel();
        if (currentHologram.holo != null)
            currentHologram.holo.removeAll();

        holograms.remove(p.getUniqueId());
    }

    public static HashMap<UUID, SelectableHologram> getAllHolograms() {
        return new HashMap<>(holograms);
    }

    public void addChoice(String msg, Runnable runnable) {
        if (choicesMap.containsKey(msg)) {
            throw new IllegalArgumentException("You can't add same message as one that already registered.");
        }
        ChoiceComponent comp = new ChoiceComponent(msg, runnable);
        choices.add(comp);
        choicesMap.put(msg, comp);
    }

    public Collection<ChoiceComponent> getChoices() {
        return choices;
    }

    public void removeChoice(int index) {
        choicesMap.remove(choices.get(index).getMessage());
        choices.remove(index);
    }

    public void removeChoice(String msg) {
        ChoiceComponent comp = choicesMap.get(msg);
        choices.remove(comp);
        choicesMap.remove(msg);
    }

    public void removeChoice(ChoiceComponent comp) {
        choicesMap.remove(comp.getMessage());
        choices.remove(comp);
    }

    public void display(Player p, Location loc, boolean ignoreCurrentQuestion) {
        if (holograms.containsKey(p.getUniqueId()) && !ignoreCurrentQuestion) {
            throw new IllegalStateException("This player is answering other question now. If you want ignore it, set \"ignoreCurrentQuestion\" false.");
        }
        loc = loc.clone();
        if (versionNumber <= 8) {
            loc.add(0, 1, 0);
        }
        this.holo = Hologram.create(loc);
        this.displayingPlayer = p;

        for (ChoiceComponent choice : choices) {
            holo.addLine(choice.getMessage());
        }

        holo.setSpace(0.5);
        holo.display(p);
        runTask(p);

        holograms.put(p.getUniqueId(), this);

        for (Object armorStand : holo.holoMap.get(p).getEntityArmorStandList()) {
            try {
                String name;
                if (versionNumber < 13) {
                    name = (String) armorStand.getClass().getMethod("getCustomName").invoke(armorStand);
                } else {
                    Object iCComp = armorStand.getClass().getMethod("getCustomName").invoke(armorStand);
                    name = (String) iCComp.getClass().getMethod("getText").invoke(iCComp);
                }
                ChoiceComponent comp = choicesMap.getOrDefault(name, null);
                if (comp != null) {
                    comp.setArmorStandEntity(armorStand);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void display(Player p, Location loc) {
        display(p, loc, false);
    }

    public ChoiceComponent getCurrentChoice() {
        return currentChoice;
    }

    public void confirmChoice() {
        if (currentChoice == null) {
            return;
        }

        if (currentChoice.getRunnable() != null) {
            currentChoice.getRunnable().run();
        }
        resetCurrentHologram(displayingPlayer);
    }

    private void selecting(ChoiceComponent comp) {
        if (currentChoice == comp) {
            return;
        }
        if (currentChoice != null) {
            currentChoice.unpush(displayingPlayer);
        }
        currentChoice = comp;

        if (comp == null) {
            return;
        }

        comp.push(displayingPlayer);

        if (versionNumber <= 8) {
            displayingPlayer.playSound(displayingPlayer.getLocation(), Sound.valueOf("NOTE_STICKS"), 1, 1);
        } else if (versionNumber <= 12) {
            displayingPlayer.playSound(displayingPlayer.getLocation(), Sound.valueOf("BLOCK_NOTE_HAT"), 1, 1);
        } else {
            displayingPlayer.playSound(displayingPlayer.getLocation(), Sound.valueOf("BLOCK_NOTE_BLOCK_HAT"), 1, 1);
        }
    }

    private void runTask(Player p) {
        if (task != null) {
            task.cancel();
        }

        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (displayingPlayer == null || !displayingPlayer.isOnline()) {
                Bukkit.getScheduler().cancelTask(task.getTaskId());
                return;
            }

            HoloComponent comp = holo.holoMap.get(p);
            HashMap<ChoiceComponent, Double> distances = new HashMap<>();
            for (Object obj : comp.getEntityArmorStandList()) {
                try {
                    double x = getCoordinate(obj, LocationType.X);
                    double y = getCoordinate(obj, LocationType.Y) + 0.3d;
                    double z = getCoordinate(obj, LocationType.Z);

                    double distance = calculateShortestDistance(p, new Vector(x, y, z));
                    String name;
                    if (versionNumber < 13) {
                        name = (String) obj.getClass().getMethod("getCustomName").invoke(obj);
                    } else {
                        Object iCComp = obj.getClass().getMethod("getCustomName").invoke(obj);
                        name = (String) iCComp.getClass().getMethod("getText").invoke(iCComp);
                    }

                    if (choicesMap.containsKey(name)) {
                        distances.put(choicesMap.get(name), distance);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            List<Map.Entry<ChoiceComponent, Double>> entries = new LinkedList<>(distances.entrySet());
            entries.sort(Map.Entry.comparingByValue());

            if (entries.get(0).getValue() > 0.1) {
                selecting(null);
            } else {
                selecting(entries.get(0).getKey());
            }
        }, 1L, 5L);
    }

    private double calculateShortestDistance(Player p, Vector pos) {
        Vector direction = p.getLocation().getDirection().clone();
        Location eyeLoc = p.getEyeLocation().clone();

        return -1 * ((((direction.getX() * (pos.getX() - eyeLoc.getX()))
                + (direction.getY() * (pos.getY() - eyeLoc.getY()))
                + (direction.getZ() * (pos.getZ() - eyeLoc.getZ())))
                / (Math.pow(direction.getX(), 2) + Math.pow(direction.getY(), 2) + Math.pow(direction.getZ(), 2)))
                - p.getEyeLocation().distance(new Location(p.getWorld(), pos.getX(), pos.getY(), pos.getZ())));
    }

    private double getCoordinate(Object obj, LocationType type) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (versionNumber <= 14) {
            return (double) obj.getClass().getField("loc" + type.name()).get(obj);
        } else {
            return (double) obj.getClass().getMethod("loc" + type.name()).invoke(obj);
        }
    }

    private static void sendPacket(Player p, Object packet) throws Exception {
        Object handle = craftPlayer.getMethod("getHandle").invoke(craftPlayer.cast(p));
        Object connection = entityPlayer.getField("playerConnection").get(handle);
        playerConnection.getMethod("sendPacket", packetRaw).invoke(connection, packet);
    }

    private class ChoiceComponent {

        private final String msg;
        private final Runnable runnable;
        private Object armorStandEntity;
        private double x, y, z;

        public ChoiceComponent(String msg, Runnable runnable) {
            this.msg = msg;
            this.runnable = runnable;
        }

        public String getMessage() {
            return msg;
        }

        public Runnable getRunnable() {
            return runnable;
        }

        private void setArmorStandEntity(Object obj) {
            armorStandEntity = obj;

            try {
                x = getCoordinate(armorStandEntity, LocationType.X);
                y = getCoordinate(armorStandEntity, LocationType.Y);
                z = getCoordinate(armorStandEntity, LocationType.Z);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Object getArmorStandEntity() {
            return armorStandEntity;
        }

        public void push(Player p) {
            if (armorStandEntity == null) {
                return;
            }

            Location loc = new Location(p.getWorld(), x, y, z);
            loc.add(p.getLocation().getDirection().clone().multiply(-0.3).setY(0));
            teleport(p, armorStandEntity, loc);
        }

        public void unpush(Player p) {
            if (armorStandEntity == null) {
                return;
            }

            Location loc = new Location(p.getWorld(), x, y, z);
            teleport(p, armorStandEntity, loc);
        }

        private void teleport(Player player, Object armorStandEntity, Location loc) {
            try {
                armorStand.getMethod("setLocation", double.class, double.class, double.class, float.class, float.class)
                        .invoke(armorStandEntity, loc.getX(), loc.getY(), loc.getZ(), 0f, 0f);

                Object packet = packetTeleport.getConstructor(entity).newInstance(armorStandEntity);
                sendPacket(player, packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class Hologram {

        private List<String> messages = new ArrayList<>();
        private Location defaultLocation = null;
        private HashMap<Player, Location> locMap = new HashMap<>();

        public void addLine(String msg) {
            messages.add(msg);
        }

        public void addLines(String... msg) {
            messages.addAll(Arrays.asList(msg));
        }

        public void setLine(int num, String message) {
            messages.set(num, message);
        }

        public void setLocation(Location loc) {
            this.defaultLocation = loc.clone();
        }

        public void setSpace(double space) {
            this.space = space;
        }

        public double getSpace() {
            return this.space;
        }

        public List<String> getLines() {
            return messages;
        }

        public String getLine(int line) {
            if (messages.size() <= line) {
                return null;
            }

            return messages.get(line);
        }

        private double space = 0.3;
        private HashMap<Player, HoloComponent> holoMap = new HashMap<>();

        public void display(Player... players) {
            for (Player p : players) {

                if (holoMap.containsKey(p)) {
                    continue;
                }

                Location loc = defaultLocation;
                if (locMap.containsKey(p)) {
                    loc = locMap.get(p);
                }

                loc = loc.clone();

                try {
                    Object w = craftWorld.cast(loc.getWorld());
                    Method getHandleMethod = craftWorld.getMethod("getHandle");
                    Object wServer = getHandleMethod.invoke(w);

                    List<Object> armorStands = new ArrayList<>();

                    Collections.reverse(messages);
                    for (String msg : messages) {
                        Object armorStandEntity;
                        if (versionNumber < 14) {
                            armorStandEntity = armorStand.getConstructor(world).newInstance(world.cast(wServer));
                        } else {
                            armorStandEntity = armorStand.getConstructor(entityTypes, world)
                                    .newInstance(entityTypes.getField("ARMOR_STAND").get(entityTypes), world.cast(wServer));
                        }

                        displayArmorStand(p, armorStandEntity, loc.add(0, space, 0), msg);
                        armorStands.add(armorStandEntity);
                    }

                    Collections.reverse(armorStands);
                    HoloComponent comp = new HoloComponent(p, armorStands, space);
                    holoMap.put(p, comp);

                    Collections.reverse(messages);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void teleport(Location loc, Player... players) {
            loc = loc.clone();
            for (Player p : players) {
                if (!holoMap.containsKey(p)) {
                    continue;
                }

                HoloComponent comp = holoMap.get(p);
                comp.teleport(loc);
                locMap.put(p, loc);
            }
        }

        public void removeFrom(Player... players) {
            for (Player p : players) {

                if (!holoMap.containsKey(p)) {
                    continue;
                }

                try {
                    List<Integer> idList = holoMap.get(p).armorStandIDList();
                    int[] ids = new int[idList.size()];

                    int count = 0;
                    for (Integer i : idList) {
                        ids[count] = i;
                        count++;
                    }

                    Object packet = packetDestroy.getConstructor(int[].class).newInstance((Object) ids);
                    sendPacket(p, packet);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

                holoMap.remove(p);
            }
        }

        public void removeAll() {
            for (Player p : holoMap.keySet()) {
                removeFrom(p);
            }
        }

        public void update(Player... players) {
            removeFrom(players);
            display(players);
        }

        private int displayArmorStand(Player p, Object armorStandEntity, Location loc, String msg) {
            try {
                Method setName;
                if (versionNumber >= 13)
                    setName = armorStand.getMethod("setCustomName", iChatBaseComponent);
                else
                    setName = armorStand.getMethod("setCustomName", String.class);
                Method setNameVisible = armorStand.getMethod("setCustomNameVisible", boolean.class);
                Method invisible = armorStand.getMethod("setInvisible", boolean.class);
                Method marker = null;
                if (versionNumber >= 9)
                    marker = armorStand.getMethod("setMarker", boolean.class);
                Method gravity;
                if (versionNumber >= 10)
                    gravity = armorStand.getMethod("setNoGravity", boolean.class);
                else
                    gravity = armorStand.getMethod("setGravity", boolean.class);
                Method small = armorStand.getMethod("setSmall", boolean.class);
                Method location = armorStand.getMethod("setLocation", double.class, double.class, double.class, float.class,
                        float.class);

                if (versionNumber <= 8) {
                    Method c = armorStand.getMethod("c", nbtTagCompound);
                    Method setBoolean = nbtTagCompound.getMethod("setBoolean", String.class, boolean.class);
                    Method setString = nbtTagCompound.getMethod("setString", String.class, String.class);
                    Method f = armorStand.getMethod("f", nbtTagCompound);

                    loc = loc.clone();
                    loc.subtract(0, 1, 0);
                    Object nbtTag = nbtTagCompound.getConstructor().newInstance();
                    c.invoke(armorStandEntity, nbtTag);
                    setBoolean.invoke(nbtTag, "Small", true);
                    setString.invoke(nbtTag, "CustomName", msg);
                    setBoolean.invoke(nbtTag, "CustomNameVisible", true);
                    setBoolean.invoke(nbtTag, "Invisible", true);
                    setBoolean.invoke(nbtTag, "Marker", true);
                    f.invoke(armorStandEntity, nbtTag);
                } else {
                    if (versionNumber >= 13)
                        setName.invoke(armorStandEntity, chatComponentText.getConstructor(String.class).newInstance(msg));
                    else
                        setName.invoke(armorStandEntity, msg);
                    setNameVisible.invoke(armorStandEntity, true);
                    invisible.invoke(armorStandEntity, true);

                    if (versionNumber >= 9 && marker != null) {
                        marker.invoke(armorStandEntity, true);
                        small.invoke(armorStandEntity, true);
                    }
                }
                gravity.invoke(armorStandEntity, versionNumber >= 10);
                location.invoke(armorStandEntity, loc.getX(), loc.getY() - 0.7, loc.getZ(), 0f, 0f);

                Object packet = spawnPacket.getConstructor(entityLiving).newInstance(entityLiving.cast(armorStandEntity));

                sendPacket(p, packet);

                int id = (int) armorStand.getMethod("getId").invoke(armorStandEntity);

                if (versionNumber >= 15) {
                    Method getDataWatcher = armorStandEntity.getClass().getMethod("getDataWatcher");
                    Object metadataPacket = packetEntityMetadata.getConstructor(int.class, dataWatcher, boolean.class)
                            .newInstance(id, getDataWatcher.invoke(armorStandEntity), true);
                    sendPacket(p, metadataPacket);
                }

                return id;
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

        public static Hologram create() {

            if (!ready) {
                throw new IllegalStateException("Not ready yet or failed to initialize.");
            }

            return new Hologram();
        }

        public static Hologram create(String message) {

            if (!ready) {
                throw new IllegalStateException("Not ready yet or failed to initialize.");
            }

            return new Hologram(message);
        }

        public static Hologram create(Location loc) {

            if (!ready) {
                throw new IllegalStateException("Not ready yet or failed to initialize.");
            }

            return new Hologram(loc);
        }

        public static Hologram create(String message, Location loc) {

            if (!ready) {
                throw new IllegalStateException("Not ready yet or failed to initialize.");
            }

            return new Hologram(message, loc);
        }

        private Hologram() {

        }

        private Hologram(String message) {
            messages.add(message);
        }

        private Hologram(Location loc) {
            this.defaultLocation = loc.clone();
        }

        private Hologram(String message, Location loc) {
            this.messages.add(message);
            this.defaultLocation = loc.clone();
        }
    }

    private static class HoloComponent {

        private List<Object> entityArmorStandList;

        private static Class<?> packetTeleport, armorStand, craftPlayer, entityPlayer, playerConnection, packetRaw, entity;
        private double space;
        private Player player;

        public static void init(Class<?> packetTeleport, Class<?> armorStand, Class<?> craftPlayer, Class<?> entityPlayer,
                                Class<?> playerConnection, Class<?> packetRaw, Class<?> entity) {
            HoloComponent.packetTeleport = packetTeleport;
            HoloComponent.armorStand = armorStand;
            HoloComponent.craftPlayer = craftPlayer;
            HoloComponent.entityPlayer = entityPlayer;
            HoloComponent.playerConnection = playerConnection;
            HoloComponent.packetRaw = packetRaw;
            HoloComponent.entity = entity;
        }

        public HoloComponent(Player p, List<Object> entities, double space) {
            this.player = p;
            this.entityArmorStandList = entities;
            this.space = space;
        }

        public void teleport(Location loc) {
            loc = loc.clone();
            Collections.reverse(entityArmorStandList);

            for (Object armorStandEntity : entityArmorStandList) {
                try {
                    armorStand.getMethod("setLocation", double.class, double.class, double.class, float.class, float.class)
                            .invoke(armorStandEntity, loc.getX(), loc.getY(), loc.getZ(), 0f, 0f);

                    Object packet = packetTeleport.getConstructor(entity).newInstance(armorStandEntity);
                    sendPacket(player, packet);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                loc.add(0, space, 0);
            }

            Collections.reverse(entityArmorStandList);
        }

        public List<Integer> armorStandIDList() {
            List<Integer> idList = new ArrayList<>();

            for (Object entityArmorStand : entityArmorStandList) {
                try {
                    idList.add((int) armorStand.getMethod("getId").invoke(entityArmorStand));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return idList;
        }

        public List<Object> getEntityArmorStandList() {
            return entityArmorStandList;
        }

        private static void sendPacket(Player p, Object packet) throws Exception {
            Object handle = craftPlayer.getMethod("getHandle").invoke(craftPlayer.cast(p));
            Object connection = entityPlayer.getField("playerConnection").get(handle);
            playerConnection.getMethod("sendPacket", packetRaw).invoke(connection, packet);
        }
    }

    public enum LocationType {
        X, Y, Z
    }

    private static class ClickDetectListener implements Listener {
        @EventHandler(priority = EventPriority.LOW)
        public void onClick(PlayerInteractEvent e) {
            Player p = e.getPlayer();
            SelectableHologram holo = getCurrentHologram(p);
            if (holo == null) {
                return;
            }
            if (holo.getCurrentChoice() == null) {
                return;
            }

            e.setCancelled(true);
            holo.confirmChoice();
        }

        @EventHandler
        public void onDisable(PluginDisableEvent e) {
            if (e.getPlugin() != plugin) {
                return;
            }

            Set<UUID> uuidList = SelectableHologram.getAllHolograms().keySet();
            List<Player> players = new ArrayList<>();
            for (UUID uuid : uuidList) {
                Player p = Bukkit.getPlayer(uuid);
                if (p != null) {
                    players.add(Bukkit.getPlayer(uuid));
                }
            }

            for (Player p : players) {
                SelectableHologram.resetCurrentHologram(p);
            }
        }
    }
}