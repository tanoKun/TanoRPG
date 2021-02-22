package com.github.tanokun.tanorpg.game.player.skill;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.player.GamePlayer;
import com.github.tanokun.tanorpg.game.player.GamePlayerJobType;
import com.github.tanokun.tanorpg.game.player.status.StatusType;
import com.github.tanokun.tanorpg.game.player.status.buff.Buff;
import com.github.tanokun.tanorpg.game.player.status.buff.BuffType;
import com.github.tanokun.tanorpg.listener.EditComboEventListener;
import com.github.tanokun.tanorpg.util.Glowing;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SkillManager {
    private static String CT = "skill_cooltime";

    private static HashMap<String, Skill> allSkills = new HashMap<>();
    private static HashMap<String, Skill> mageSkills = new HashMap<>();
    private static HashMap<String, Skill> warriorSkills = new HashMap<>();
    private static HashMap<String, Skill> priestSkills = new HashMap<>();

    private static HashMap<String, Skill> skillNames = new HashMap<>();
    private static ArrayList<String> skillIDs = new ArrayList<>();

    public static void addAllSkill(Skill skill){allSkills.put(skill.getCombo().toString(), skill); skillNames.put(skill.getName(), skill); skillIDs.add(skill.getName());}
    public static void addMageSkill(Skill skill){mageSkills.put(skill.getCombo().toString(), skill); skillNames.put(skill.getName(), skill);skillIDs.add(skill.getName());}
    public static void addWarriorSkill(Skill skill){warriorSkills.put(skill.getCombo().toString(), skill); skillNames.put(skill.getName(), skill);skillIDs.add(skill.getName());}
    public static void addPriestSkill(Skill skill){priestSkills.put(skill.getCombo().toString(), skill); skillNames.put(skill.getName(), skill);skillIDs.add(skill.getName());}

    public static boolean runPlayerSkill(GamePlayer player, List<String> combos){
        GamePlayerJobType job = player.getJob();
        if (allSkills.get(combos.toString()) != null){
            if (!player.hasSkill(allSkills.get(combos.toString()).getName())) return false;
            runSkill(allSkills.get(combos.toString()), player);
            return true;
        }
        switch (job) {
            case WARRIOR:
                if (warriorSkills.get(combos.toString()) != null) {
                    if (!player.hasSkill(warriorSkills.get(combos.toString()).getName())) return false;
                    runSkill(warriorSkills.get(combos.toString()), player);
                    return true;
                }
            case MAGE:
                if (mageSkills.get(combos.toString()) != null) {
                    if (!player.hasSkill(mageSkills.get(combos.toString()).getName())) return false;
                    runSkill(mageSkills.get(combos.toString()), player);
                    return true;
                }
            case PRIEST:
                if (priestSkills.get(combos.toString()) != null) {
                    if (!player.hasSkill(priestSkills.get(combos.toString()).getName())) return false;
                    runSkill(priestSkills.get(combos.toString()), player);
                    return true;
                }
        }
        return false;
    }
    private static void runSkill(Skill skill, GamePlayer player) {
        EditComboEventListener.combos.remove(player.getUuid());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter byString = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        if (!player.getPlayer().hasMetadata(CT + "_" + skill.getName())){
            player.getPlayer().setMetadata(CT + "_" + skill.getName(), new FixedMetadataValue(TanoRPG.getPlugin(), "" + now.format(byString)));
            skill.execute(player.getPlayer());
            player.setHAS_MP(player.getHAS_MP() - skill.getMp());
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date1 = null;
        Date date2 = null;
        if (player.getPlayer().getMetadata(CT + "_" + skill.getName()).size() == 0) player.getPlayer().setMetadata(CT + "_" + skill.getName(), new FixedMetadataValue(TanoRPG.getPlugin(), "" + now.format(byString)));
        try {date1 = format.parse(player.getPlayer().getMetadata(CT + "_" + skill.getName()).get(0).asString());} catch (ParseException e) {e.printStackTrace();}
        try {date2 = format.parse(now.format(byString));} catch (ParseException e) {e.printStackTrace();}
        long difference = date2.getTime() - date1.getTime();
        long seconds = difference / 1000 % 60;
        double down = (player.getStatus(StatusType.CT_SKILL).getLevel() / 100)
                + Buff.getBuffPercent(player.getPlayer(), BuffType.SKILL_COOL_TIME_15)
                + Buff.getBuffPercent(player.getPlayer(), BuffType.SKILL_COOL_TIME_25);
        long ct = skill.getCT() - Math.round(skill.getCT() * down);
        long show = ct - seconds;
        if (!(seconds > ct)) {
            player.getPlayer().sendMessage(TanoRPG.PX + "§cクールタイム中です §7(残り: " + show + "秒)");
            return;
        }
        if (player.getHAS_MP() < skill.getMp()){
            player.getPlayer().sendMessage(TanoRPG.PX + "§cマナが足りません");
            return;
        }
        player.getPlayer().setMetadata(CT + "_" + skill.getName(), new FixedMetadataValue(TanoRPG.getPlugin(), "" + now.format(byString)));
        skill.execute(player.getPlayer());
        player.setHAS_MP(player.getHAS_MP() - skill.getMp());
    }
    public static ItemStack getSkillItem(String name){
        Skill skill = skillNames.get(name);
        List<String> lore = new ArrayList<>();
        lore.add("§e〇=-=-=-=-=§b説明§e=-=-=-=-=-〇");
        for (String setumei : skill.getLore()){lore.add(setumei);}
        lore.add("§e〇=-=-=-=-=-=-=-=-=-=-=-=-〇");
        lore.add("§7対応職業: §b" + skill.getJob());
        lore.add("§7必要レベル: §b" + skill.getLvl());
        lore.add("§7必要MP: §b" + skill.getMp());
        lore.add("§7必要コンボ: §b" + skill.getCombo());
        lore.add("§7クールタイム: §b" + skill.getCT() + "秒");
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§bスキル習得書: §6" + name);
        meta.addEnchant(new Glowing(), 1, true);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    public static boolean isExists(String name){
        return skillNames.get(name) == null ? false : true;
    }

    public static Skill getSkillNames(String name) {return skillNames.get(name);}
    public static ArrayList<String> getSkillNames() {return skillIDs;}

    public static HashMap<String, Skill> getAllSkills() {return allSkills;}
    public static HashMap<String, Skill> getMageSkills() {return mageSkills;}
    public static HashMap<String, Skill> getPriestSkills() {return priestSkills;}
    public static HashMap<String, Skill> getWarriorSkills() {return warriorSkills;}
}
