package com.github.tanokun.tanorpg.game.mission.model;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.mission.Mission;
import com.github.tanokun.tanorpg.game.mission.MissionManager;
import com.github.tanokun.tanorpg.game.mission.task.EntityKillTask;
import com.github.tanokun.tanorpg.game.player.status.Sidebar;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static com.github.tanokun.tanorpg.game.mission.MissionManager.addClearMission;

public class Merchant_GuardianKillMission extends Mission {
    private final String PX = "§a§l商人>> §f";

    public Merchant_GuardianKillMission() {
        super("商人の手伝い", 207);
        addMissionTask(new EntityKillTask(EntityManager.getEntityData("ディファニー"), 12, "ディファニーを12体倒す"));
    }

    public void showNPCMessages(Player player) throws Exception {
        sendMessage(player, PX + "最近、このあたりは危険になったと思わんかい？");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2500);
        sendMessage(player, PX + "そこでお主に頼みごとがあるんじゃがいいかな？");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(3500);
        sendMessage(player, PX + "具体的には敵を倒してきてほしい。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2000);
    }

    public void startMission(Player player) throws Exception {
        Thread.sleep(500);
        sendMessage(player, PX + "おお本当か！");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(1500);
        sendMessage(player, PX + "わしは見ての通り釣りをしてるんじゃがな。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(3000);
        sendMessage(player, PX + "「ディファニー」という新種の魚のせいで全部在来種が食われてしまったんじゃ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(4000);
        sendMessage(player, PX + "そこでそいつらを12体倒してきてほしいのだ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2500);
        sendMessage(player, PX + "倒してきてくれたらお礼にいいものを上げよう。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2500);
        playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 3, 2);
        sendMessage(player, MissionManager.PX + "§aミッション「" + getMissionName() + "」を開始しました。");
        MissionManager.addActiveMission(player.getUniqueId(), this);
    }


    public void finishMission(Player player) throws Exception {
        Thread.sleep(500);
        sendMessage(player, PX + "おお、あの厄介な魚を倒してきてくれたのか。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(3000);
        sendMessage(player, PX + "本当にありがとう。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2000);
        sendMessage(player, PX + "これで誰にも邪魔されずに釣りができる...");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(4000);
        sendMessage(player, PX + "もし他の街で私を見かけたら商品を覗いていってくれたまえ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(4000);
        sendMessage(player, PX + "これが報酬だ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2000);

        addClearMission(player.getUniqueId(), this);
        GamePlayerManager.getPlayer(player.getUniqueId()).setActive_mission_NPC_ID(-1);
        sendMessage(player, MissionManager.PX + "§e〇=-=-=-=-=-=-=-=〇");
        sendMessage(player, MissionManager.PX + "§a+500" + " " + TanoRPG.MONEY);
        sendMessage(player, MissionManager.PX + "§a+5 fish");
        sendMessage(player, MissionManager.PX + "§a+3 iron_ore");
        sendMessage(player, MissionManager.PX + "§a+70 EXP");
        sendMessage(player, MissionManager.PX + "§e〇=-=-=-=-=-=-=-=〇");
        sendMessage(player, MissionManager.PX + "ミッションをクリアしました。");
        GamePlayerManager.getPlayer(player.getUniqueId()).addMoney(500);
        GamePlayerManager.getPlayer(player.getUniqueId()).setHAS_EXP(GamePlayerManager.getPlayer(player.getUniqueId()).getMAX_EXP() + 70);

        ItemStack fish = ItemManager.getItem("fish").getItem();
        fish.setAmount(5);
        ItemStack iron_ore = ItemManager.getItem("iron_ore").getItem();
        iron_ore.setAmount(3);

        player.getInventory().addItem(fish, iron_ore);
        Sidebar.updateSidebar(player);
    }

    public void cancelMission(Player player) throws Exception {
        Thread.sleep(500);
        sendMessage(player, PX + "そんな冷たいこと言わんでおくれよう。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2000);
        sendMessage(player, PX + "（´・ω・｀）");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(1000);
        sendMessage(player, MissionManager.PX + "§aミッションを取り消しました。");
    }
}