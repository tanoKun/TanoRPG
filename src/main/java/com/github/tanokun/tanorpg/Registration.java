package com.github.tanokun.tanorpg;

import com.github.tanokun.tanorpg.command.*;
import com.github.tanokun.tanorpg.command.register.Register;
import com.github.tanokun.tanorpg.game.craft.CraftManager;
import com.github.tanokun.tanorpg.game.player.skill.SkillManager;
import com.github.tanokun.tanorpg.game.player.skill.execute.PlayerSkJump;
import com.github.tanokun.tanorpg.game.player.skill.execute.mage.PlayerSkExplosionAttack;
import com.github.tanokun.tanorpg.game.player.skill.execute.mage.PlayerSkLiningAttack;
import com.github.tanokun.tanorpg.game.player.skill.execute.priest.PlayerSkCircleHeal;
import com.github.tanokun.tanorpg.game.player.skill.execute.priest.PlayerSkCoolTimeUpBuff;
import com.github.tanokun.tanorpg.game.player.skill.execute.priest.PlayerSkHeal;
import com.github.tanokun.tanorpg.game.player.skill.execute.warrior.PlayerSkAtkUp;
import com.github.tanokun.tanorpg.game.player.skill.execute.warrior.PlayerSkJumpAttack;
import com.github.tanokun.tanorpg.game.shop.ShopManager;
import com.github.tanokun.tanorpg.game.shop.sell.Sell;
import com.github.tanokun.tanorpg.listener.*;
import com.github.tanokun.tanorpg.menu.MenuManager;
import com.github.tanokun.tanorpg.menu.SetJobMenu;
import com.github.tanokun.tanorpg.menu.player.StatusMainMenu;
import com.github.tanokun.tanorpg.menu.player.StatusSkillMenu;
import com.github.tanokun.tanorpg.util.Glowing;
import com.github.tanokun.tanorpg.util.io.Config;
import com.github.tanokun.tanorpg.util.io.Folder;
import com.github.tanokun.tanorpg.util.task.ActionBarTask;
import com.github.tanokun.tanorpg.util.task.AutoSaveTask;
import com.github.tanokun.tanorpg.util.task.EditStatusTask;
import com.github.tanokun.tanorpg.util.task.PlayerRegenerationTask;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.lang.reflect.Field;

public class Registration {
    private static Plugin plugin;
    public Registration(Plugin plugin){this.plugin = plugin;}

    public void registerConfigs() {
        plugin.saveDefaultConfig();
        new Config("itemMaterial.yml", "items" + File.separator + "material", plugin).saveDefaultConfig();
        new Config("itemWeapon.yml", "items" + File.separator + "weapon", plugin).saveDefaultConfig();
        new Config("itemMagicWeapon.yml", "items" + File.separator + "magicWeapon", plugin).saveDefaultConfig();
        new Config("itemEquipment.yml", "items" + File.separator + "equip", plugin).saveDefaultConfig();

        new Config("mobEntity.yml", "mobs", plugin).saveDefaultConfig();
        new Config("mobBoss.yml", "mobs", plugin).saveDefaultConfig();

        new Folder("player_database", plugin).createExists();
        new Folder("shop", plugin).createExists();
        new Folder("craft", plugin).createExists();
    }
    public void registerCommand(){
        Register.register(new TanoRPGCommand());
        Register.register(new GiveItemCommand());
        Register.register(new TestCommand());
        Register.register(new MobSpawnCommand());
        Register.register(new GiveMobSpawnerCommand());
        Register.register(new OpenCraftCommand());
        Register.register(new OpenShopCommand());
        Register.register(new OpenSellCommand());
        Register.register(new GiveSkItemCommand());
        Register.register(new OpenStatusCommand());
    }
    public void registerListener(){
        Bukkit.getPluginManager().registerEvents(new DamageEventListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new MenuManager(), plugin);
        Bukkit.getPluginManager().registerEvents(new EntitySpawnEventListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinEventListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitEventListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new EditComboEventListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new LeftClickEventListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new DeathEventListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new ShopManager(), plugin);
        Bukkit.getPluginManager().registerEvents(new CraftManager(), plugin);
        Bukkit.getPluginManager().registerEvents(new Sell(), plugin);
    }
    public void registerOthers() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Glowing glow = new Glowing(80);
            Enchantment.registerEnchantment(glow);
        }
        catch (IllegalArgumentException e){
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void registerTask(){
        new AutoSaveTask().runTaskTimerAsynchronously(plugin, 1, 6000);
        new ActionBarTask().runTaskTimerAsynchronously(plugin, 1, 4);
        new EditStatusTask().runTaskTimerAsynchronously(plugin, 1, 1);
        new PlayerRegenerationTask().runTaskTimerAsynchronously(plugin, 1, 80);
    }

    public void registerMenus() {
        MenuManager.registerMenu(new SetJobMenu());
        MenuManager.registerMenu(new StatusMainMenu(null));
        MenuManager.registerMenu(new StatusSkillMenu(null));
    }

    public void registerSkills() {
        //すべて
        SkillManager.addAllSkill(new PlayerSkJump());

        //ウォーリア
        SkillManager.addWarriorSkill(new PlayerSkAtkUp());
        SkillManager.addWarriorSkill(new PlayerSkJumpAttack());

        //メイジ
        SkillManager.addMageSkill(new PlayerSkLiningAttack());
        SkillManager.addMageSkill(new PlayerSkExplosionAttack());

        //プリースト
        SkillManager.addPriestSkill(new PlayerSkHeal());
        SkillManager.addPriestSkill(new PlayerSkCircleHeal());
        SkillManager.addPriestSkill(new PlayerSkCoolTimeUpBuff());
    }
}
