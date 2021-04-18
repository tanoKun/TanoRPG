package com.github.tanokun.tanorpg.menu.player;

import com.github.tanokun.api.smart_inv.inv.ClickableItem;
import com.github.tanokun.api.smart_inv.inv.SmartInventory;
import com.github.tanokun.api.smart_inv.inv.contents.InventoryContents;
import com.github.tanokun.api.smart_inv.inv.contents.InventoryProvider;
import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayer;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.skill.Skill;
import com.github.tanokun.tanorpg.game.player.skill.SkillManager;
import com.github.tanokun.tanorpg.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class StatusSkillMenu implements InventoryProvider {

    public static SmartInventory INVENTORY() {
        return SmartInventory.builder()
                .id("player_StatusSKillMenu")
                .provider(new StatusSkillMenu())
                .size(6, 9)
                .title("§d§lPlayerStatus §7>> §aSkill")
                .closeable(true)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.clear();
        GamePlayer gamePlayer = GamePlayerManager.getPlayer(player.getUniqueId());
        List<Skill> skills = new ArrayList<>();
        skills.addAll(SkillManager.getAllSkills().values());
        switch (gamePlayer.getJob()){
            case PRIEST:
                skills.addAll(SkillManager.getPriestSkills().values());
                break;
            case MAGE:
                skills.addAll(SkillManager.getMageSkills().values());
                break;
            case WARRIOR:
                skills.addAll(SkillManager.getWarriorSkills().values());
                break;
        }
        int t = 0;
        List<String> lore = new ArrayList<>();
        for (Skill skill : skills){
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
                addSelItem(gamePlayer, skill, ItemUtils.createItem(skill.getItem(), "§a" + skill.getName(), lore, 1, true), contents);
            } else {
                lore.add("§e〇=-=-=-=-=§b説明§e=-=-=-=-=-〇");
                for (String setumei : skill.getLore()){lore.add(setumei);}
                lore.add("§e〇=-=-=-=-=-=-=-=-=-=-=-=-〇");
                lore.add("§7必要レベル: §b???");
                lore.add("§7必要MP: §b???");
                lore.add("§7必要コンボ: §b???");
                lore.add("§7クールタイム: §b???秒");
                addDoNotSelItem(gamePlayer, skill, ItemUtils.createItem(Material.BARRIER, "§a" + skill.getName(), lore, 1, true), contents);
            }
            t++;
        }
    }

    private void addSelItem(GamePlayer gamePlayer, Skill skill, ItemStack item, InventoryContents contents){
        contents.add(ClickableItem.of(item, e -> {
            if (e.getClick().equals(ClickType.LEFT)){
                gamePlayer.setSkill_F(skill.getName());
                TanoRPG.playSound((Player)e.getWhoClicked(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
                gamePlayer.getPlayer().sendMessage(TanoRPG.PX + "「" + skill.getName() + "」をスキルショートカット「F」に設定しました");
                init(gamePlayer.getPlayer(), contents);
            } else if (e.getClick().equals(ClickType.SHIFT_LEFT)) {
                gamePlayer.setSkill_Shift_F(skill.getName());
                TanoRPG.playSound((Player)e.getWhoClicked(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
                gamePlayer.getPlayer().sendMessage(TanoRPG.PX + "「" + skill.getName() + "」をスキルショートカット「Shift_F」に設定しました");
                init(gamePlayer.getPlayer(), contents);
            }
        }));
    }
    private void addDoNotSelItem(GamePlayer gamePlayer, Skill skill, ItemStack item, InventoryContents contents){
        contents.add(ClickableItem.empty(item));
    }
}
