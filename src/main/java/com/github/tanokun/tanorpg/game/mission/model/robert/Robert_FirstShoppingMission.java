package com.github.tanokun.tanorpg.game.mission.model.robert;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.game.mission.Mission;
import com.github.tanokun.tanorpg.game.mission.MissionManager;
import com.github.tanokun.tanorpg.game.mission.task.EntityKillTask;
import com.github.tanokun.tanorpg.game.mission.task.ShoppingTask;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.status.Sidebar;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Robert_FirstShoppingMission extends Mission {
    private final String PX = "§a§lロバート>> §f";

    public Robert_FirstShoppingMission() {
        super("チュートリアル①", 206);
        addMissionTask(new ShoppingTask(1, null, "欲しい物を1つ買う"));
    }

    public void showNPCMessages(Player player) throws Exception {
        sendMessage(player, PX + "やぁやぁ。君が新しく入ってきた冒険者か。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2500);
        sendMessage(player, PX + "初めはこの町で暮らすために必要な知識を得よう。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(3000);
        sendMessage(player, PX + "なに。簡単なことだ。覚えるか？");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2000);
    }

    public void startMission(Player player) throws Exception {
        Thread.sleep(500);
        sendMessage(player, PX + "まず、はじめは買い物の仕方を覚えよう。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(3000);
        sendMessage(player, PX + "俺から見て左回りに回っていくと商店街があるんだ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(4000);
        sendMessage(player, PX + "その中で1回自分が欲しいものを買ってみろ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(3000);
        sendMessage(player, PX + "NPCを右クリックしてアイテムを選択すると購入できるぞ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(5000);
        sendMessage(player, PX + "買い物ができたらまた話しかけてくれ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(3000);
        playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 3, 2);
        sendMessage(player, MissionManager.PX + "§aミッション「" + getMissionName() + "」を開始しました。");
        MissionManager.addActiveMission(player.getUniqueId(), this);
    }

    public void finishMission(Player player) throws Exception {
        Thread.sleep(500);
        sendMessage(player, PX + "どうだ買い物はできたか？");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2000);
        sendMessage(player, PX + "これで1つ知識がついたな。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2000);
        sendMessage(player, PX + "まだまだ必要な知識はあるぞ。頑張れ");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(3000);
        playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 3, 0.5);
        MissionManager.addClearMission(player.getUniqueId(), this);
        GamePlayerManager.getPlayer(player.getUniqueId()).setActive_mission_NPC_ID(-1);
        sendMessage(player, MissionManager.PX + "§e〇=-=-=-=-=-=-=-=〇");
        sendMessage(player, MissionManager.PX + "§a+200" + " " + TanoRPG.MONEY);
        sendMessage(player, MissionManager.PX + "§a+60" + " EXP");
        sendMessage(player, MissionManager.PX + "§e〇=-=-=-=-=-=-=-=〇");
        sendMessage(player, MissionManager.PX + "ミッションをクリアしました。");
        GamePlayerManager.getPlayer(player.getUniqueId()).addMoney(200);
        GamePlayerManager.getPlayer(player.getUniqueId()).setHAS_EXP(GamePlayerManager.getPlayer(player.getUniqueId()).getMAX_EXP() + 60);
        Sidebar.updateSidebar(player);
    }

    public void cancelMission(Player player) throws Exception {
        Thread.sleep(1000);
        sendMessage(player, PX + "必要な知識だぞ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2000);
        sendMessage(player, MissionManager.PX + "§aミッションを取り消しました。");
    }
}
