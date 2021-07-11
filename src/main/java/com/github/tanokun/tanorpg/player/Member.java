package com.github.tanokun.tanorpg.player;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.event.tanorpg.TanoRpgLevelUpEvent;
import com.github.tanokun.tanorpg.game.PermissionMap;
import com.github.tanokun.tanorpg.player.quest.QuestMap;
import com.github.tanokun.tanorpg.player.skill.SkillClass;
import com.github.tanokun.tanorpg.player.skill.SkillMap;
import com.github.tanokun.tanorpg.player.status.PlayerStatusMap;
import com.github.tanokun.tanorpg.player.status.StatusType;
import com.github.tanokun.tanorpg.player.warppoint.WarpPointMap;
import com.github.tanokun.tanorpg.util.io.Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.UUID;

import static org.bukkit.Bukkit.getOfflinePlayer;
import static org.bukkit.Bukkit.getPlayer;

public class Member {
    private final UUID uuid;

    private SkillClass skillClass;

    private final PlayerStatusMap statusMap;

    private final EquipmentMap equipMap;

    private final SkillMap skillMap;

    private final WarpPointMap warpPointMap;

    private final QuestMap questMap;

    private final PermissionMap permissionMap;

    private int hasHP;

    private int hasMP;

    private long hasEXP = 0;

    private MemberLevelType hasLevel;

    private Attack attack;

    public Member(UUID uuid, SkillClass skillClass, PlayerStatusMap pb, EquipmentMap equip, SkillMap skillMap, WarpPointMap warpPointMap, QuestMap questMap, PermissionMap permissionMap, Attack attack){
        this.uuid = uuid;
        this.skillClass = skillClass;
        this.statusMap = pb;
        this.equipMap = equip;
        this.skillMap = skillMap;
        this.warpPointMap = warpPointMap;
        this.questMap = questMap;
        this.permissionMap = permissionMap;
        this.attack = attack;
        this.hasLevel = MemberLevelType.Lv_1;
        this.hasHP = pb.getPointAndStatus(StatusType.HP);
        this.hasMP = pb.getPointAndStatus(StatusType.MP);
    }

    public Member(UUID uuid, SkillClass skillClass, PlayerStatusMap pb, EquipmentMap equip, SkillMap skillMap, WarpPointMap warpPointMap, QuestMap questMap, PermissionMap permissionMap, Attack attack, int hasHP, int hasMP, long hasEXP, MemberLevelType level) {
        this.uuid = uuid;
        this.skillClass = skillClass;
        this.statusMap = pb;
        this.equipMap = equip;
        this.skillMap = skillMap;
        this.warpPointMap = warpPointMap;
        this.questMap = questMap;
        this.permissionMap = permissionMap;
        this.attack = attack;
        this.hasLevel = level;
        this.hasHP = hasHP;
        this.hasMP = hasMP;
        this.hasEXP = hasEXP;
    }

    public void setMoney(long money) {
        TanoRPG.getPlugin().getEcon().depositPlayer(Bukkit.getPlayer(uuid), getMoney() - money);
        TanoRPG.getPlugin().getSidebarManager().updateSidebar(Bukkit.getPlayer(uuid),this);
    }

    public void removeMoney(long money) {
        TanoRPG.getPlugin().getEcon().withdrawPlayer(Bukkit.getPlayer(uuid), money);
        TanoRPG.getPlugin().getSidebarManager().updateSidebar(Bukkit.getPlayer(uuid),this);
    }

    public void addMoney(long money) {
        TanoRPG.getPlugin().getEcon().depositPlayer(Bukkit.getPlayer(uuid), money);
        TanoRPG.getPlugin().getSidebarManager().updateSidebar(Bukkit.getPlayer(uuid),this);
    }

    public void setHasHP(int hasHP) {
        this.hasHP = hasHP;
        TanoRPG.getPlugin().getSidebarManager().updateSidebar(Bukkit.getPlayer(uuid), this);

    }

    public void setHasMP(int hasMP) {
        this.hasMP = hasMP;
        TanoRPG.getPlugin().getSidebarManager().updateSidebar(Bukkit.getPlayer(uuid), this);

    }

    public void setHasEXP(long hasEXP) {
        this.hasEXP = hasEXP;

        if (!hasLevel.hasNext()){
            this.hasEXP = 0;
            return;
        }

        for (int i = 0; this.hasEXP >= hasLevel.getMaxEXP(); i++){
            if (!hasLevel.hasNext()){
                this.hasEXP = 0;
                TanoRPG.getPlugin().getSidebarManager().updateSidebar(Bukkit.getPlayer(uuid), this);
                return;
            }
            this.hasEXP = this.hasEXP - hasLevel.getMaxEXP();
            hasLevel = (hasLevel.getNext());
            Bukkit.getPlayer(uuid).sendMessage(TanoRPG.PX + "§aレベルが §b" + hasLevel + "Lv §aになりました！");
            getStatusMap().setStatusPoint(getStatusMap().getStatusPoint() + 2);
            Bukkit.getPluginManager().callEvent(new TanoRpgLevelUpEvent(getPlayer(uuid), this));
        }
        TanoRPG.getPlugin().getSidebarManager().updateSidebar(Bukkit.getPlayer(uuid), this);
    }

    public void setHasLevel(MemberLevelType hasLevel) {
        this.hasLevel = hasLevel;
        TanoRPG.getPlugin().getSidebarManager().updateSidebar(Bukkit.getPlayer(uuid), this);

    }

    public void setAttack(Attack attack) {
        this.attack = attack;
    }

    public UUID getUuid() {
        return uuid;
    }

    public SkillClass getSkillClass() {
        return skillClass;
    }

    public PlayerStatusMap getStatusMap() {
        return statusMap;
    }

    public EquipmentMap getEquipMap() {
        return equipMap;
    }

    public SkillMap getSkillMap() {
        return skillMap;
    }

    public WarpPointMap getWarpPointMap() {
        return warpPointMap;
    }

    public QuestMap getQuestMap() {
        return questMap;
    }

    public PermissionMap getOpenPermissionMap() {
        return permissionMap;
    }

    public long getMoney() {
        return Math.round(TanoRPG.getPlugin().getEcon().getBalance(getOfflinePlayer(uuid)));
    }

    public int getHasHP() {
        return hasHP;
    }

    public int getHasMP() {
        return hasMP;
    }

    public long getHasEXP() {
        return hasEXP;
    }

    public MemberLevelType getHasLevel() {
        return hasLevel;
    }

    public Attack getAttack() {
        return attack;
    }

    public void addHasEXP(long HAS_EXP){
        setHasEXP(getHasEXP() + HAS_EXP);
    }

    public void saveData(){
        Config data = new Config("player_database" + File.separator + uuid.toString() + ".yml", TanoRPG.getPlugin());
        FileConfiguration c = data.getConfig();
        data.createExists();
        c.set("skillClass", skillClass.name());
        statusMap.save(data, "");
        equipMap.save(data, "");
        questMap.save(data, "");
        permissionMap.save(data, "");
        c.set("hasHP", hasHP);
        c.set("hasMP", hasMP);
        c.set("hasEXP", hasEXP);
        c.set("hasLevel", hasLevel.name());
        data.saveConfig();
    }
}

