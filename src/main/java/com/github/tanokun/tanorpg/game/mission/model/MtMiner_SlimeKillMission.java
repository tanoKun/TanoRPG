package com.github.tanokun.tanorpg.game.mission.model;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.game.mission.Mission;
import com.github.tanokun.tanorpg.game.mission.MissionManager;
import com.github.tanokun.tanorpg.game.mission.task.EntityKillTask;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.status.Sidebar;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static com.github.tanokun.tanorpg.game.mission.MissionManager.addClearMission;

public class MtMiner_SlimeKillMission extends Mission {
    private final String PX = "§a§l鉱夫>> §f";

    public MtMiner_SlimeKillMission() {
        super("鉱石が欲しくばお願いを聞け", 210);
        addMissionTask(new EntityKillTask(EntityManager.getBaseEntity("スライム"), 13, "スライムを13体倒す"));
    }

    public void showNPCMessages(Player player) throws Exception {
        Thread.sleep(500);
        sendMessage(player, PX + "こんなところに人が来るなんて珍しいな。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2500);
        sendMessage(player, PX + "鉱石が欲しいのか？");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(1000);
        sendMessage(player, PX + "だが無料で鉱石をやるというのは...");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(3000);
        sendMessage(player, PX + "そうだ！ そこの洞窟の奥にスライムが住み着いてしまってな。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(3400);
        sendMessage(player, PX + "そいつらを13体ほど倒してくれたら、石炭鉱石を5個やろう。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(4000);
        sendMessage(player, PX + "どうだ？ お前にとっても悪い取引じゃないだろう。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(3000);
    }
    public void startMission(Player player) throws Exception {
        Thread.sleep(500);
        sendMessage(player, PX + "倒してくれるのか。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(1000);
        sendMessage(player, PX + "俺は大体ここにいるから倒し終わったら、話しかけてくれ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(4000);
        sendMessage(player, PX + "よろしく頼むぞ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2000);
        playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 3, 2);
        sendMessage(player, MissionManager.PX + "§aミッション「" + getMissionName() + "」を開始しました。");
        MissionManager.addActiveMission(player.getUniqueId(), this);
    }

    public void finishMission(Player player) throws Exception {
        Thread.sleep(500);
        sendMessage(player, PX + "倒してきてくれたのか　早かったな。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(3000);
        sendMessage(player, PX + "これが報酬の石炭鉱石だ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2000);
        sendMessage(player, PX + "これで誰にも邪魔されずに釣りができる...");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(4000);
        sendMessage(player, PX + "それと、その鉱石は鍛冶屋の炉などで精錬して石炭にすることができるぞ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(4000);
        sendMessage(player, PX + "これでやっと洞窟に入ることができる...");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2000);

        addClearMission(player.getUniqueId(), this);
        GamePlayerManager.getPlayer(player.getUniqueId()).setActive_mission_NPC_ID(-1);
        sendMessage(player, MissionManager.PX + "§e〇=-=-=-=-=-=-=-=〇");
        sendMessage(player, MissionManager.PX + "§a+100" + " " + TanoRPG.MONEY);
        sendMessage(player, MissionManager.PX + "§a+5 coal_ore");
        sendMessage(player, MissionManager.PX + "§a+80 EXP");
        sendMessage(player, MissionManager.PX + "§e〇=-=-=-=-=-=-=-=〇");
        sendMessage(player, MissionManager.PX + "ミッションをクリアしました。");
        GamePlayerManager.getPlayer(player.getUniqueId()).addMoney(100);
        GamePlayerManager.getPlayer(player.getUniqueId()).setHAS_EXP(GamePlayerManager.getPlayer(player.getUniqueId()).getHAS_EXP() + 80);

        ItemStack coal_ore = ItemManager.getItem("coal_ore").getItem();
        coal_ore.setAmount(5);

        player.getInventory().addItem(coal_ore);
        Sidebar.updateSidebar(player);
    }

    public void cancelMission(Player player) throws Exception {
        Thread.sleep(500);
        sendMessage(player, PX + "そうか、それなら鉱石はやれないな。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2500);
        sendMessage(player, PX + "気が向いたらまた話しかけてくれ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2500);
        sendMessage(player, MissionManager.PX + "§aミッションを取り消しました。");
    }
}
