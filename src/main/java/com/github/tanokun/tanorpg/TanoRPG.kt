package com.github.tanokun.tanorpg

import com.github.tanokun.tanorpg.command.*
import com.github.tanokun.tanorpg.damage.DamageListener
import com.github.tanokun.tanorpg.event.PlayerArmorEquipEvent.ArmorListener
import com.github.tanokun.tanorpg.event.worldguard.WgEvents
import com.github.tanokun.tanorpg.game.craft.CraftManager
import com.github.tanokun.tanorpg.game.craft.listener.OpenCraftListener
import com.github.tanokun.tanorpg.game.entity.EntityManager
import com.github.tanokun.tanorpg.game.entity.gathering.GatheringListener
import com.github.tanokun.tanorpg.game.entity.gathering.GatheringManager
import com.github.tanokun.tanorpg.game.item.ItemManager
import com.github.tanokun.tanorpg.game.item.drop.DropManager
import com.github.tanokun.tanorpg.game.shop.ShopManager
import com.github.tanokun.tanorpg.game.shop.listener.OpenShopListener
import com.github.tanokun.tanorpg.listener.*
import com.github.tanokun.tanorpg.player.MemberManager
import com.github.tanokun.tanorpg.player.quests.QuestManager
import com.github.tanokun.tanorpg.player.quests.listeners.QuestRunningListener
import com.github.tanokun.tanorpg.player.quests.utils.loadActionClasses
import com.github.tanokun.tanorpg.player.quests.utils.loadConditionClasses
import com.github.tanokun.tanorpg.player.quests.utils.loadTaskClasses
import com.github.tanokun.tanorpg.player.skill.execute.SkillManager
import com.github.tanokun.tanorpg.player.status.sidebar.SidebarManager
import com.github.tanokun.tanorpg.util.command.CommandManager
import com.github.tanokun.tanorpg.util.smart_inv.inv.InventoryManager
import com.github.tanokun.tanorpg.util.variable.Variables
import com.playerislands.thirdparty.Connection
import com.playerislands.thirdparty.Spigot
import com.playerislands.thirdparty.v2.ConnectionV2
import io.lumine.xikage.mythicmobs.MythicMobs
import io.lumine.xikage.mythicmobs.utils.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.Component
import net.luckperms.api.LuckPerms
import net.milkbowl.vault.economy.Economy
import net.minecraft.util.datafix.fixes.DataConverterShulkerBoxItem
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.block.ShulkerBox
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.v1_17_R1.CraftServer
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.lang.reflect.Field

class TanoRPG : JavaPlugin() {
    lateinit var econ: Economy
    lateinit var inventoryManager: InventoryManager
    private lateinit var commandManager: CommandManager
    lateinit var itemManager: ItemManager
    lateinit var shopManager: ShopManager
    lateinit var craftManager: CraftManager
    lateinit var questManager: QuestManager
    lateinit var sidebarManager: SidebarManager
    lateinit var skillManager: SkillManager
    lateinit var memberManager: MemberManager
    lateinit var dropManager: DropManager
    lateinit var entityManager: EntityManager
    internal lateinit var gatheringManager: GatheringManager
    lateinit var variables: Variables


    lateinit var luckPerms: LuckPerms
    lateinit var mythicMobs: MythicMobs

    override fun onEnable() {
        plugin = this
        mythicMobs = Bukkit.getPluginManager().getPlugin("MythicMobs") as MythicMobs
        luckPerms = Bukkit.getServicesManager().getRegistration(LuckPerms::class.java)!!.provider
        this.variables = Variables()
        val rsp = server.servicesManager.getRegistration(Economy::class.java)
        econ = rsp!!.provider
        if (Bukkit.getPluginManager().getPlugin("ThirdParty") != null) {
            HandlerList.unregisterAll((Spigot.INSTANCE as Plugin))
        }

        setMotd("§a作成中 §b[1.17.1推奨] \n§c※今はホワイトリスト")
        loadConditionClasses()
        loadActionClasses()
        loadTaskClasses()
        setupManagers()
        registerListeners()
        registerTasks()
        registerOthers()
        skillManager.registerSkill()
        registerCommands()
        setupScheduler()
    }

    override fun onDisable() {
        Bukkit.getScheduler().cancelTasks(this)
        try { Bukkit.getOnlinePlayers().stream().forEach { p: Player -> p.kick(Component.text("reloading...")) } }catch (_: Exception) { /*結局kickされる */ }
        Bukkit.getWorlds().stream().forEach { world: World ->
            world.entities.stream().forEach { en: Entity ->
                if (en.hasMetadata(EntityManager.ENTITY)) en.remove()
                if (en.hasMetadata(EntityManager.DISPLAY_NAME)) en.remove()
                if (en.hasMetadata(EntityManager.AI)) en.remove()
                if (en.hasMetadata("gathering")) en.remove()
            }
        }

        gatheringManager.gatherings.values.forEach {gathering ->
            for (i in gathering.getSpawnLocations()) {
                i.getNearbyEntities(2.0, 2.0, 2.0).forEach { it.remove() }
            }

            for (i in 1..gathering.getSpawnLocations().size) {
                mythicMobs.spawnerManager.getSpawnerByName("gathering_${gathering.id}_${i}")?.let { mythicMobs.spawnerManager.removeSpawner(it) }
            }
        }
    }

    private fun setupManagers() {
        this.inventoryManager = InventoryManager(this)
        this.commandManager = CommandManager()
        this.itemManager = ItemManager(null)
        this.shopManager = ShopManager(null)
        this.craftManager = CraftManager(null)
        this.questManager = QuestManager(null)
        this.sidebarManager = SidebarManager()
        this.skillManager = SkillManager()
        this.memberManager = MemberManager()
        this.dropManager = DropManager(null)
        this.entityManager = EntityManager(null)
        this.gatheringManager = GatheringManager(null)
    }

    private fun registerListeners() {
        Bukkit.getPluginManager().registerEvents(ArmorListener(), this)
        Bukkit.getPluginManager().registerEvents(MainMenuListener(), this)
        Bukkit.getPluginManager().registerEvents(PlayerInitListener(), this)
        Bukkit.getPluginManager().registerEvents(StopEventListener(), this)
        Bukkit.getPluginManager().registerEvents(QuestRunningListener(), this)
        Bukkit.getPluginManager().registerEvents(DamageListener(), this)
        Bukkit.getPluginManager().registerEvents(OpenCraftListener(), this)
        Bukkit.getPluginManager().registerEvents(OpenShopListener(), this)
        Bukkit.getPluginManager().registerEvents(SkillComboListener(), this)
        Bukkit.getPluginManager().registerEvents(ChestListener(), this)
        Bukkit.getPluginManager().registerEvents(RegionListener(), this)
        Bukkit.getPluginManager().registerEvents(OtherCommand(), this)
        Bukkit.getPluginManager().registerEvents(GatheringListener(), this)
    }

    fun setMotd(vararg motds: String) {
        var motd = ""
        for (m in motds) motd = """$motd$m""".trimIndent()
        (Bukkit.getServer() as CraftServer).server.motd = motd
        if (Bukkit.getPluginManager().getPlugin("ThirdParty") != null) {
            val spigot = Spigot.INSTANCE
            var field: Field? = null
            try {
                field = spigot.javaClass.getDeclaredField("connection")
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            }
            field!!.isAccessible = true
            var connection: Connection? = null
            try {
                (field[spigot] as Connection).also { connection = it }
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
            connection!!.sendMotd(motd)
        }
    }

    fun getConnection(): Connection? {
        Bukkit.getConsoleSender()
        if (Bukkit.getPluginManager().getPlugin("ThirdParty") != null) {
            val spigot = Spigot.INSTANCE
            var field: Field? = null
            try {
                field = spigot.javaClass.getDeclaredField("connection")
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            }
            field!!.isAccessible = true
            var connection: Connection? = null
            try {
                (field[spigot] as Connection).also { connection = it }
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
            return connection
        }
        return null
    }

    fun getConnectionV2(): ConnectionV2? {
        if (Bukkit.getPluginManager().getPlugin("ThirdParty") != null) {
            val spigot = Spigot.INSTANCE
            var field: Field? = null
            try {
                field = spigot.javaClass.getDeclaredField("connectionV2")
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            }
            field!!.isAccessible = true
            var connection: ConnectionV2? = null
            try {
                (field[spigot] as ConnectionV2).also { connection = it }
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
            return connection
        }
        return null
    }

    private fun registerCommands() {
        commandManager.registerCommand(OpenInvCommand().javaClass)
        BackpackCommand()
        OtherCommand()
        TanoRpgCommand()
        UtilCommand()
        VariableCommand()
    }

    private fun registerTasks() {
        Bukkit.getScheduler().runTaskTimer(this, Runnable {
            for (e in Bukkit.getWorld("world")!!
                .entities) {
                e.fireTicks = -1
            }
        }, 1, 1)
    }

    private fun registerOthers() {
        WgEvents.setup()
    }


    private fun setupScheduler() {
        val world = Bukkit.getWorld("world")!!
        Bukkit.getScheduler().runTaskTimer(this, Runnable {
            world.time = world.time + 1
        }, 0, 2)
    }

    fun setGatheringManager(gatheringManager: GatheringManager) {
        this.gatheringManager.gatherings.values.forEach {gathering ->
            for (i in 1..gathering.getSpawnLocations().size) {
                mythicMobs.spawnerManager.getSpawnerByName("gathering_${gathering.id}_${i}")?.let { mythicMobs.spawnerManager.removeSpawner(it) }
            }
        }

        gatheringManager.gatherings.values.forEach { gathering -> gathering.init() }

        Bukkit.getWorlds().stream().forEach { world: World ->
            world.entities.stream().forEach { en: Entity ->
                if (en.hasMetadata("gathering")) en.remove()
            }
        }

        this.gatheringManager = gatheringManager
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!commandManager.hasCommand(command.name)) return true
        commandManager.getCommand(command.name).execute(sender, label, args)
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String>? {
        return if (!commandManager.hasCommand(command.name)) null else commandManager.getCommand(command.name).tabComplete(sender, alias, args)
    }

    companion object {
        @JvmStatic
        lateinit var plugin: TanoRPG
        const val PX = "§6[§a-｜ §b§lSystem§a ｜-§6] §7=> §b"
        const val MONEY = "セル"

        @JvmStatic
        fun playSound(player: Player, sound: Sound, volume: Int, v2: Double) {
            player.playSound(player.location, sound, volume.toFloat(), v2.toFloat())
        }

        @JvmStatic
        fun playSound(players: Array<Player>, sound: Sound, volume: Double, v2: Int) {
            for (player in players) {
                player.playSound(player.location, sound, volume.toFloat(), v2.toFloat())
            }
        }

        @JvmStatic
        fun playSound(players: Set<Player>, sound: Sound, volume: Float, v2: Float) {
            for (player in players) {
                player.playSound(player.location, sound, volume, v2)
            }
        }
    }
}