package com.github.tanokun.tanorpg.player;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.craft.OpenPermissionMap;
import com.github.tanokun.tanorpg.player.quest.QuestMap;
import com.github.tanokun.tanorpg.player.skill.SkillClass;
import com.github.tanokun.tanorpg.player.skill.SkillMap;
import com.github.tanokun.tanorpg.player.status.PlayerStatusMap;
import com.github.tanokun.tanorpg.player.status.StatusType;
import com.github.tanokun.tanorpg.player.warppoint.WarpPointMap;
import com.github.tanokun.tanorpg.util.io.Config;
import com.google.gson.Gson;
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
        members.remove(uuid);
    }

    public Member getMember(UUID uuid) {
        if (!members.containsKey(uuid)) return null;
        return members.get(uuid);
    }

    public Member loadData(UUID uuid) {
        Config data = new Config("player_database" + File.separator + uuid.toString() + ".yml", TanoRPG.getPlugin());
        if (!data.exists()) return null;
        Member member;
        SkillClass skillClass = SkillClass.valueOf(data.getConfig().getString("skillClass"));
        PlayerStatusMap statusMap = new PlayerStatusMap().load(data, "");
        EquipmentMap equipMap = new EquipmentMap().load(data, "");
        SkillMap skillMap = new SkillMap();
        WarpPointMap warpPointMap = new WarpPointMap();
        QuestMap questMap = new QuestMap().load(data, "");
        OpenPermissionMap openPermissionMap = new OpenPermissionMap().load(data, "");
        int hasHP = data.getConfig().getInt("hasHP");
        int hasMP = data.getConfig().getInt("hasMP");
        long hasEXP = data.getConfig().getInt("hasEXP");
        MemberLevelType hasLevel = MemberLevelType.valueOf(data.getConfig().getString("hasLevel"));

        member = new Member(uuid, skillClass, statusMap, equipMap, skillMap, warpPointMap, questMap, openPermissionMap, new Attack(), hasHP, hasMP, hasEXP, hasLevel);
        return member;
    }
}