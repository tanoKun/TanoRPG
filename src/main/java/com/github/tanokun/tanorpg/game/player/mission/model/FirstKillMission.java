package com.github.tanokun.tanorpg.game.player.mission.model;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.mission.Mission;
import com.github.tanokun.tanorpg.game.player.mission.MissionManager;
import com.github.tanokun.tanorpg.game.player.mission.listener.EventKillListener;
import com.github.tanokun.tanorpg.game.player.mission.listener.NpcClickListener;
import com.github.tanokun.tanorpg.game.player.mission.task.EntityKillTask;
import com.github.tanokun.tanorpg.game.player.status.Sidebar;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class FirstKillMission extends Mission {
    private final String PX = "§a§lロバート>> §f";

    public FirstKillMission() {
        super("ロバートからのお願い", 206);
        addMissionTask(new EntityKillTask(EntityManager.getEntityData("ゴブリン"), 10, "ゴブリンを10体倒す"));
    }

    public void showNPCMessages(Player player) throws Exception {
        sendMessage(player, PX + "やぁやぁ。 新人の冒険者かい？");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2000);
        sendMessage(player, PX + "ちょっと今ゴブリンが外にうろついていて困ってるんだ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(3000);
        sendMessage(player, PX + "頼み後をしてもいいかな？ 報酬はしっかり払うよ。");
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
        sendMessage(player, EventKillListener.PX + "§aミッション「" + getMissionName() + "」を開始しました。");
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
        sendMessage(player, EventKillListener.PX + "§e〇=-=-=-=-=-=-=-=〇");
        sendMessage(player, EventKillListener.PX + "§a+350" + " " + TanoRPG.MONEY);
        sendMessage(player, EventKillListener.PX + "§a+2 apples");
        sendMessage(player, EventKillListener.PX + "§e〇=-=-=-=-=-=-=-=〇");
        sendMessage(player, EventKillListener.PX + "ミッションをクリアしました。");
        GamePlayerManager.getPlayer(player.getUniqueId()).addMoney(350);
        player.getInventory().addItem(ItemManager.getItem("apple").getItem(), ItemManager.getItem("apple").getItem());
        Sidebar.updateSidebar(player);
    }

    public void cancelMission(Player player) throws Exception {
        Thread.sleep(1000);
        sendMessage(player, PX + "そうかい。急に頼んで悪かったね。気が変わったら頼むよ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2000);
        sendMessage(player, EventKillListener.PX + "§aミッションを取り消しました。");
    }
}
