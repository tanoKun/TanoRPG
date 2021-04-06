package com.github.tanokun.tanorpg.game.mission.model.robert;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.game.mission.condition.ClearMissionCondition;
import com.github.tanokun.tanorpg.game.mission.condition.MissionCondition;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.mission.Mission;
import com.github.tanokun.tanorpg.game.mission.MissionManager;
import com.github.tanokun.tanorpg.game.mission.task.EntityKillTask;
import com.github.tanokun.tanorpg.game.player.skill.SkillManager;
import com.github.tanokun.tanorpg.game.player.status.Sidebar;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@ClearMissionCondition({"チュートリアル①", "チュートリアル②"})
public class Robert_GoblinKillMission extends Mission implements MissionCondition {
    private final String PX = "§a§lロバート>> §f";

    public Robert_GoblinKillMission() {
        super("ロバートからのお願い", 206);
        addMissionTask(new EntityKillTask(EntityManager.getBaseEntity("ゴブリン"), 10, "ゴブリンを10体倒す"));
    }

    public void showNPCMessages(Player player) throws Exception {
        sendMessage(player, PX + "さっきの冒険者か。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2000);
        sendMessage(player, PX + "ちょっと今ゴブリンが外にうろついていて困ってるんだ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(3000);
        sendMessage(player, PX + "頼み事をしてもいいかな？ 報酬はしっかり払うよ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2000);
    }

    public void startMission(Player player) throws Exception {
        Thread.sleep(500);
        sendMessage(player, PX + "それはよかった。町の外にいるゴブリンを10体倒してきてほしいんだ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(4000);
        sendMessage(player, PX + "倒し終わったら、また私に話しかけてくれ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2000);
        sendMessage(player, PX + "よろしく頼むぞ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2000);
        playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 3, 2);
        sendMessage(player, MissionManager.PX + "§aミッション「" + getMissionName() + "」を開始しました。");
        MissionManager.addActiveMission(player.getUniqueId(), this);
    }
    public void finishMission(Player player) throws Exception {
        Thread.sleep(500);
        sendMessage(player, PX + "おお君か、もう倒し終わったのか。助かるよ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(3000);
        sendMessage(player, PX + "早速報酬を渡そう。何かまた頼むかもしれないが、");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2000);
        sendMessage(player, PX + "その時はよろしくな。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2000);
        playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 3, 0.5);
        MissionManager.addClearMission(player.getUniqueId(), this);
        GamePlayerManager.getPlayer(player.getUniqueId()).setActive_mission_NPC_ID(-1);
        long exp = GamePlayerManager.getPlayer(player.getUniqueId()).getLEVEL().getMAX_EXP() + 60;
        sendMessage(player, MissionManager.PX + "§e〇=-=-=-=-=-=-=-=〇");
        sendMessage(player, MissionManager.PX + "§a+350" + " " + TanoRPG.MONEY);
        sendMessage(player, MissionManager.PX + "§a+2 apples");
        sendMessage(player, MissionManager.PX + "§a+" + exp + " EXP");
        sendMessage(player, MissionManager.PX + "§a+1" + " §f「§bスキル習得書: §6ジャンプ§f」");
        sendMessage(player, MissionManager.PX + "§e〇=-=-=-=-=-=-=-=〇");
        sendMessage(player, MissionManager.PX + "ミッションをクリアしました。");
        GamePlayerManager.getPlayer(player.getUniqueId()).addMoney(350);
        player.getInventory().addItem(ItemManager.getItem("apple").getItem(), ItemManager.getItem("apple").getItem());
        GamePlayerManager.getPlayer(player.getUniqueId()).setHAS_EXP(exp);
        player.getInventory().addItem(SkillManager.getSkillItem("ジャンプ"));
        Sidebar.updateSidebar(player);

    }

    public void cancelMission(Player player) throws Exception {
        Thread.sleep(1000);
        sendMessage(player, PX + "そうかい。急に頼んで悪かったね。気が変わったら頼むよ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2000);
        sendMessage(player, MissionManager.PX + "§aミッションを取り消しました。");
    }
}
