package com.github.tanokun.tanorpg.menu.player;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayer;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.skill.Skill;
import com.github.tanokun.tanorpg.game.player.skill.SkillManager;
import com.github.tanokun.tanorpg.menu.Menu;
import com.github.tanokun.tanorpg.menu.MenuManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatusSkillMenu extends Menu {
    public StatusSkillMenu(Player player) {
        super("§d§lPlayerStatus §7>> §aSkill", 6);
        if (player == null) return;
        GamePlayer gamePlayer = GamePlayerManager.getPlayer(player.getUniqueId());
        HashMap<String, Skill> job_skills = null;
        HashMap<String, Skill> all_skills = SkillManager.getAllSkills();
        switch (gamePlayer.getJob()){
            case PRIEST:
                job_skills = SkillManager.getPriestSkills();
                break;
            case MAGE:
                job_skills = SkillManager.getMageSkills();
                break;
            case WARRIOR:
                job_skills = SkillManager.getWarriorSkills();
                break;
        }
        int t = 0;
        List<String> lore = new ArrayList<>();
        for (Skill skill : all_skills.values()){
            lore.clear();
            if (gamePlayer.hasSkill(skill.getName())){
                lore.add("§e〇=-=-=-=-=§b説明§e=-=-=-=-=-〇");
                for (String setumei : skill.getLore()){lore.add(setumei);}
                lore.add("§e〇=-=-=-=-=-=-=-=-=-=-=-=-〇");
                lore.add("§7必要レベル: §b" + skill.getLvl());
                lore.add("§7必要MP: §b" + skill.getMp());
                lore.add("§7必要コンボ: §b" + skill.getCombo());
                lore.add("§7クールタイム: §b" + skill.getCT() + "秒");
                if (gamePlayer.getSkill_F() != null){
                    if (gamePlayer.getSkill_F().equals(skill.getName())){
                        lore.add(" ");
                        lore.add("§aスキルショートカット「F」に設定されています");
                    }
                }
                if (gamePlayer.getSkill_Shift_F() != null){
                    if (gamePlayer.getSkill_Shift_F().equals(skill.getName())) {
                        lore.add(" ");
                        lore.add("§aスキルショートカット「Shift_F」に設定されています");
                    }
                }
                setItem(t, MenuManager.createItem(skill.getItem(), "§a" + skill.getName(), lore, 1, true));
            } else {
                lore.add("§e〇=-=-=-=-=§b説明§e=-=-=-=-=-〇");
                for (String setumei : skill.getLore()){lore.add(setumei);}
                lore.add("§e〇=-=-=-=-=-=-=-=-=-=-=-=-〇");
                lore.add("§7必要レベル: §b???");
                lore.add("§7必要MP: §b???");
                lore.add("§7必要コンボ: §b???");
                lore.add("§7クールタイム: §b???秒");
                setItem(t, MenuManager.createItem(Material.BARRIER, "§c" + skill.getName(), lore, 1, false));
            }
            t++;
        }
        for (Skill skill : job_skills.values()){
            lore.clear();
            if (gamePlayer.hasSkill(skill.getName())){
                lore.add("§e〇=-=-=-=-=§b説明§e=-=-=-=-=-〇");
                for (String setumei : skill.getLore()){lore.add(setumei);}
                lore.add("§e〇=-=-=-=-=-=-=-=-=-=-=-=-〇");
                lore.add("§7必要レベル: §b" + skill.getLvl());
                lore.add("§7必要MP: §b" + skill.getMp());
                lore.add("§7必要コンボ: §b" + skill.getCombo());
                lore.add("§7クールタイム: §b" + skill.getCT() + "秒");
                if (gamePlayer.getSkill_F() != null){
                    if (gamePlayer.getSkill_F().equals(skill.getName())){
                        lore.add(" ");
                        lore.add("§aスキルショートカット「F」に設定されています");
                    }
                }
                if (gamePlayer.getSkill_Shift_F() != null){
                    if (gamePlayer.getSkill_Shift_F().equals(skill.getName())) {
                        lore.add(" ");
                        lore.add("§aスキルショートカット「Shift_F」に設定されています");
                    }
                }
                setItem(t, MenuManager.createItem(skill.getItem(), "§a" + skill.getName(), lore, 1, true));
            } else {
                lore.add("§e〇=-=-=-=-=§b説明§e=-=-=-=-=-〇");
                for (String setumei : skill.getLore()){lore.add(setumei);}
                lore.add("§e〇=-=-=-=-=-=-=-=-=-=-=-=-〇");
                lore.add("§7必要レベル: §b???");
                lore.add("§7必要MP: §b???");
                lore.add("§7必要コンボ: §b???");
                lore.add("§7クールタイム: §b???秒");
                setItem(t, MenuManager.createItem(Material.BARRIER, "§c" + skill.getName(), lore, 1, false));
            }
            t++;
        }
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getCurrentItem().getType().equals(Material.BARRIER)) return;
        if (!e.getClickedInventory().equals(e.getWhoClicked().getOpenInventory().getTopInventory())
                && e.getView().getTitle().equals("§d§lPlayerStatus §7>> §aSkill")) return;
        ItemMeta item = e.getCurrentItem().getItemMeta();
        String skill_name = item.getDisplayName().replace("§a", "");
        GamePlayer player = GamePlayerManager.getPlayer(e.getWhoClicked().getUniqueId());
        if (e.getClick().equals(ClickType.LEFT)){
            player.setSkill_F(skill_name);
            TanoRPG.playSound((Player)e.getWhoClicked(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
            player.getPlayer().sendMessage(TanoRPG.PX + "「" + skill_name + "」をスキルショートカット「F」に設定しました");
            new StatusSkillMenu((Player)e.getWhoClicked()).openInv((Player) e.getWhoClicked());
        } else if (e.getClick().equals(ClickType.SHIFT_LEFT)) {
            player.setSkill_Shift_F(skill_name);
            TanoRPG.playSound((Player)e.getWhoClicked(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
            player.getPlayer().sendMessage(TanoRPG.PX + "「" + skill_name + "」をスキルショートカット「Shift_F」に設定しました");
            new StatusSkillMenu((Player)e.getWhoClicked()).openInv((Player) e.getWhoClicked());
        }
    }

    public void onClose(InventoryCloseEvent e) {}
}
