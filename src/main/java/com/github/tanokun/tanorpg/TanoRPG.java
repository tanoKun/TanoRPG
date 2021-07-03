package com.github.tanokun.tanorpg;

import com.github.tanokun.tanorpg.command.RpgDataCommand;
import com.github.tanokun.tanorpg.command.TanoRpgCommand;
import com.github.tanokun.tanorpg.event.PlayerArmorEquipEvent;
import com.github.tanokun.tanorpg.event.worldguard.WgEvents;
import com.github.tanokun.tanorpg.game.craft.CraftManager;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.game.entity.spawner.EntitySpawnerManager;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.game.shop.ShopManager;
import com.github.tanokun.tanorpg.listener.DamageListener;
import com.github.tanokun.tanorpg.listener.MainMenuListener;
import com.github.tanokun.tanorpg.listener.PlayerInitListener;
import com.github.tanokun.tanorpg.listener.StopEventListener;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.player.MemberManager;
import com.github.tanokun.tanorpg.player.quest.QuestManager;
import com.github.tanokun.tanorpg.player.quest.listener.ActionQuestListener;
import com.github.tanokun.tanorpg.player.quest.listener.TaskEventListener;
import com.github.tanokun.tanorpg.player.status.sidebar.SidebarManager;
import com.github.tanokun.tanorpg.util.command.CommandManager;
import com.github.tanokun.tanorpg.util.hologram.SelectableHologram;
import com.github.tanokun.tanorpg.util.io.Coding;
import com.github.tanokun.tanorpg.util.io.Config;
import com.github.tanokun.tanorpg.util.smart_inv.inv.InventoryManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

public final class TanoRPG extends JavaPlugin {
    private static TanoRPG plugin;

    private Economy econ;

    private DataManager dataManager;

    private InventoryManager inventoryManager;

    private CommandManager commandManager;

    private ItemManager itemManager;

    private ShopManager shopManager;

    private CraftManager craftManager;

    private MemberManager memberManager;

    private SidebarManager sidebarManager;

    private QuestManager questManager;

    private EntityManager entityManager;

    private EntitySpawnerManager entitySpawnerManager;

    public static final String PX = "§6[§a-｜ §b§lSystem§a ｜-§6] §7=> §b";
    public static final String PX_BUFF_UP = "§7[-｜ バフ付与 ｜-] §7=> ";
    public static final String PX_BUFF_DOWN = "§7[-｜ バフ解除 ｜-] §7=> ";
    public static final String OPEN_KYE = Coding.decode("a2plb2lqT0lIKSRoMjN1aDUzbzgyaGppanF3bjkpKCNIUklVTzJoOTg=");
    public static String MONEY = "セル";

    public void onEnable() {
        plugin = this;
        SelectableHologram.init(this);
        setupManagers();
        registerListeners();
        registerCommands();
        registerTasks();
        registerOthers();
        initData();
    }


    public void onDisable() {
        Bukkit.getOnlinePlayers().stream().forEach(p -> {
            Member member = memberManager.getMember(p.getUniqueId());
            if (member == null) {
                p.kickPlayer("reloading...");
                return;
            }
            member.saveData();
        });

        dataManager.save();

    }

    private void setupManagers() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        econ = rsp.getProvider();

        dataManager = new DataManager(this);

        inventoryManager = new InventoryManager(this);

        commandManager = new CommandManager(this);

        itemManager = new ItemManager(null);

        shopManager = new ShopManager(null);

        craftManager = new CraftManager(null);

        sidebarManager = new SidebarManager();

        memberManager = new MemberManager();

        questManager = new QuestManager(null, this);

        entityManager = new EntityManager(null);

        entitySpawnerManager = new EntitySpawnerManager(null);

    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerArmorEquipEvent.ArmorListener(), this);
        Bukkit.getPluginManager().registerEvents(new MainMenuListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInitListener(), this);
        Bukkit.getPluginManager().registerEvents(new StopEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new ActionQuestListener(), this);
        Bukkit.getPluginManager().registerEvents(new TaskEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new DamageListener(), this);
    }

    private void registerCommands() {
        getCommandManager().registerCommand(new RpgDataCommand().getClass());
        getCommandManager().registerCommand(new TanoRpgCommand().getClass());
    }

    private void registerTasks(){
        entitySpawnerManager.runTaskTimerAsynchronously(plugin, 1, 20);
    }

    private void registerOthers() {
        WgEvents.setup();
    }

    private void initData(){
        if (!dataManager.isInitFile()){
            new Config("quests" + File.separator + "Sample.yml", this).saveDefaultConfig();
        }
    }

    public static TanoRPG getPlugin() {
        return plugin;
    }

    public Economy getEcon() {
        return econ;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public ShopManager getShopManager() {
        return shopManager;
    }

    public CraftManager getCraftManager() {
        return craftManager;
    }

    public MemberManager getMemberManager() {
        return memberManager;
    }

    public SidebarManager getSidebarManager() {
        return sidebarManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public QuestManager getQuestManager() {
        return questManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public EntitySpawnerManager getEntitySpawnerManager() {
        return entitySpawnerManager;
    }

    public void setEcon(Economy econ) {
        this.econ = econ;
    }

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void setInventoryManager(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public void setItemManager(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    public void setShopManager(ShopManager shopManager) {
        this.shopManager = shopManager;
    }

    public void setCraftManager(CraftManager craftManager) {
        this.craftManager = craftManager;
    }

    public void setMemberManager(MemberManager memberManager) {
        this.memberManager = memberManager;
    }

    public void setSidebarManager(SidebarManager sidebarManager) {
        this.sidebarManager = sidebarManager;
    }

    public void setQuestManager(QuestManager questManager) {
        this.questManager = questManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void setEntitySpawnerManager(EntitySpawnerManager entitySpawnerManager) {
        this.entitySpawnerManager = entitySpawnerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!commandManager.hasCommand(command.getName())) return true;

        commandManager.getCommand(command.getName()).execute(sender, label, args);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!commandManager.hasCommand(command.getName())) return null;
        return commandManager.getCommand(command.getName()).tabComplete(sender, alias, args);
    }

    public static void playSound(Player player, Sound sound, int volume, double v2) {
        player.playSound(player.getLocation(), sound, volume, (float) v2);
    }

    public static void playSound(Player[] players, Sound sound, int volume, double v2) {
        for (Player player : players) {
            player.playSound(player.getLocation(), sound, volume, (float) v2);
        }
    }
}