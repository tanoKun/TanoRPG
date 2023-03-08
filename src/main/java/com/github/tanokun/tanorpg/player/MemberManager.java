package com.github.tanokun.tanorpg.player;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.player.menu.main.backpack.BackpackMenu;
import com.github.tanokun.tanorpg.player.quests.QuestMap;
import com.github.tanokun.tanorpg.player.skill.SkillClass;
import com.github.tanokun.tanorpg.player.skill.SkillMap;
import com.github.tanokun.tanorpg.player.status.PlayerStatusMap;
import com.github.tanokun.tanorpg.player.status.buff.BuffMap;
import com.github.tanokun.tanorpg.player.warppoint.WarpPointMap;
import com.github.tanokun.tanorpg.util.io.Config;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class MemberManager {
    private HashMap<UUID, Member> members = new HashMap<>();

    public MemberManager() {
        Bukkit.getOnlinePlayers().stream().forEach(p -> {
            Member member = loadData(p.getUniqueId());
            this.registerMember(member);
            TanoRPG.getPlugin().getSidebarManager().setupSidebar(p, member);
        });
    }

    public void registerMember(Member member) {
        members.put(member.getUuid(), member);
    }

    public void unregisterMember(UUID uuid) {
        members.remove(uuid).offline();
    }

    public Member getMember(UUID uuid) {
        if (!members.containsKey(uuid)) return null;
        return members.get(uuid);
    }

    public Member loadData(UUID uuid) {
        Config data = new Config(TanoRPG.getPlugin(), "player_database" + File.separator + uuid.toString() + ".yml");
        if (!data.isExists()) return null;
        Member member;
        SkillClass skillClass = SkillClass.valueOf(data.getConfig().getString("skillClass", "SOLDIER"));
        PlayerStatusMap statusMap = new PlayerStatusMap(null).load(data, "");
        EquipmentMap equipMap = new EquipmentMap().load(data, "");
        SkillMap skillMap = new SkillMap().load(data, "");
        WarpPointMap warpPointMap = new WarpPointMap();
        QuestMap questMap = new QuestMap().load(data, "");
        BuffMap buffMap = new BuffMap().load(data, "");
        ChestMap chestMap = new ChestMap(new HashMap<>()).load(data, "");
        BackpackMenu backpackMenu = new BackpackMenu(uuid).load(data, "");
        int hasHP = data.getConfig().getInt("hasHP");
        int hasMP = data.getConfig().getInt("hasMP");
        long hasEXP = data.getConfig().getInt("hasEXP");
        MemberLevelType hasLevel = MemberLevelType.valueOf(data.getConfig().getString("hasLevel"));

                member = new Member(uuid, skillClass, statusMap, equipMap, skillMap, warpPointMap, questMap, buffMap,
                new Attack(), backpackMenu, chestMap, hasHP, hasMP, hasEXP, hasLevel, new MemberRunnable(), null);
        statusMap.setMember(member);
        return member;
    }
}