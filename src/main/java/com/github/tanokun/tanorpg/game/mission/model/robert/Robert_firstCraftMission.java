package com.github.tanokun.tanorpg.game.mission.model.robert;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.game.mission.Mission;
import com.github.tanokun.tanorpg.game.mission.MissionManager;
import com.github.tanokun.tanorpg.game.mission.condition.ClearMissionCondition;
import com.github.tanokun.tanorpg.game.mission.condition.MissionCondition;
import com.github.tanokun.tanorpg.game.mission.task.CraftTask;
import com.github.tanokun.tanorpg.game.mission.task.ShoppingTask;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.status.Sidebar;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@ClearMissionCondition("チュートリアル①")
public class Robert_firstCraftMission extends Mission implements MissionCondition {
    private final String PX = "§a§lロバート>> §f";

    public Robert_firstCraftMission() {
        super("チュートリアル②", 206);
        addMissionTask(new CraftTask(1, null, "レンガを1つ作る"));
    }

    public void showNPCMessages(Player player) throws Exception {
        sendMessage(player, PX + "次はクラフトについてだ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2500);
        sendMessage(player, PX + "なに。簡単なことだ。覚えるか？");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2000);
    }

    public void startMission(Player player) throws Exception {
        Thread.sleep(500);

        sendMessage(player, PX + "さっきの商店街の奥に鍛冶屋がある。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(4000);

        player.getInventory().addItem(ItemManager.getItem("clay").getItem(), ItemManager.getItem("coal").getItem());

        sendMessage(player, PX + "このアイテムを使って、その中の奥の溶鉱炉でレンガを作ってみてくれ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(5000);
        sendMessage(player, PX + "溶鉱炉の奥の壁をクリックすることでクラフトできるぞ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(3000);
        sendMessage(player, PX + "クラフトができたらまた話しかけてくれ。");
        playSound(player, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
        Thread.sleep(2000);
        playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 3, 2);
        sendMessage(player, MissionManager.PX + "§aミッション「" + getMissionName() + "」を開始しました。");
        MissionManager.addActiveMission(player.getUniqueId(), this);
    }

    public void finishMission(Player player) throws Exception {
        Thread.sleep(500);
        sendMessage(player, PX + "どうだクラフトはできたか？");
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
        long exp = GamePlayerManager.getPlayer(player.getUniqueId()).getLEVEL().getMAX_EXP() + 60;
        sendMessage(player, MissionManager.PX + "§e〇=-=-=-=-=-=-=-=〇");
        sendMessage(player, MissionManager.PX + "§a+50" + " " + TanoRPG.MONEY);
        sendMessage(player, MissionManager.PX + "§a+" + exp + " EXP");
        sendMessage(player, MissionManager.PX + "§e〇=-=-=-=-=-=-=-=〇");
        sendMessage(player, MissionManager.PX + "ミッションをクリアしました。");
        GamePlayerManager.getPlayer(player.getUniqueId()).addMoney(50);
        GamePlayerManager.getPlayer(player.getUniqueId()).setHAS_EXP(exp);
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
