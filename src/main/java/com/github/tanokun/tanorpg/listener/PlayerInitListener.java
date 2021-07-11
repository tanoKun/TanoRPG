package com.github.tanokun.tanorpg.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.event.WgRegionLeftEvent;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.player.inv.SelSkillClassMenu;
import com.github.tanokun.tanorpg.util.ItemUtils;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.*;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class PlayerInitListener implements Listener {
    private ArrayList<UUID> isActive = new ArrayList<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Member member = TanoRPG.getPlugin().getMemberManager().loadData(e.getPlayer().getUniqueId());
        Player p = e.getPlayer();
        if (member == null){
            e.setJoinMessage(TanoRPG.PX + "§d" + e.getPlayer().getName() + "が初ログインしました！ 082!");
            TanoRPG.playSound(Bukkit.getOnlinePlayers().toArray(new Player[0]), Sound.ENTITY_PLAYER_LEVELUP, 3, 1);

            p.getInventory().clear();
            p.teleport(TanoRPG.getPlugin().getDataManager().getHomeLoc());
            p.getInventory().setItem(4, ItemUtils.createItem(Material.PAPER, "クリックで夢から覚める", 1, false));

        } else {
            e.setJoinMessage(TanoRPG.PX + e.getPlayer().getName() + "がログインしました。");
            TanoRPG.getPlugin().getMemberManager().registerMember(member);
            TanoRPG.getPlugin().getSidebarManager().setupSidebar(e.getPlayer(), member);

            e.getPlayer().getInventory().setItem(8, ItemUtils.createItem(
                    Material.COMPASS, "§bMenu",
                    Arrays.asList(new String[]{"§f初期アイテム", "§f自分の情報を見ることができる", "§fコンパスはクエストの場所を指し示してくれるぞ"}), 1, true));
        }

        if (!p.isOp()) p.setGameMode(GameMode.SURVIVAL);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        e.setQuitMessage(TanoRPG.PX + e.getPlayer().getName() + "がログオフしました。");
        TanoRPG.getPlugin().getSidebarManager().removeSidebar(e.getPlayer());
        Member member = TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId());
        if (member == null) return;
        TanoRPG.getPlugin().getMemberManager().unregisterMember(member.getUuid());
        member.saveData();
    }

    @EventHandler
    public void onStartRegionLeave(WgRegionLeftEvent e){
        if (TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId()) != null) return;
        Player p = e.getPlayer();

        if (e.getRegionName().equals(TanoRPG.getPlugin().getDataManager().getHomeRegionName())) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "effect give " + p.getName() + " minecraft:blindness 9 1");

            Bukkit.getScheduler().runTask(TanoRPG.getPlugin(), () -> p.teleport(new Location(Bukkit.getWorld("world"), 0, 1, 0)));

            Bukkit.getScheduler().runTaskAsynchronously(TanoRPG.getPlugin(), () -> {
                try { Thread.sleep(2000);} catch (InterruptedException ex) {ex.printStackTrace();}

                p.sendMessage("§c" + p.getName() + "§7「今日は冒険者になるための試験だ」");
                TanoRPG.playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
                try { Thread.sleep(2000);} catch (InterruptedException ex) {ex.printStackTrace();}

                p.sendMessage("§c" + p.getName() + "§7「色々わからないが、ギルドの人が教えてくれると§a先輩冒険者「ロバート」§7が言っていた」");
                TanoRPG.playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
                try { Thread.sleep(3000);} catch (InterruptedException ex) {ex.printStackTrace();}

                p.sendMessage("§c" + p.getName() + "§7「とりあえず頑張ろう...」");
                TanoRPG.playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
                try { Thread.sleep(2000);} catch (InterruptedException ex) {ex.printStackTrace();}

                Bukkit.getScheduler().runTask(TanoRPG.getPlugin(), () -> p.teleport(TanoRPG.getPlugin().getDataManager().getGuildLoc()));

                p.sendMessage(TanoRPG.PX + "試験を受けるために§a「試験官」§bに話しかけに行こう！");
                TanoRPG.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 1);
            });

            return;
        }

        if (e.getRegionName().equals(TanoRPG.getPlugin().getDataManager().getGuildRegionName())) e.setCancelled(true);
    }

    @EventHandler
    public void onSpeakToNPC(NPCRightClickEvent e){
        Player p = e.getClicker();
        if (!e.getNPC().getName().contains("試験官") || TanoRPG.getPlugin().getMemberManager().getMember(p.getUniqueId()) != null || isActive.contains(p.getUniqueId()))  return;
        isActive.add(p.getUniqueId());

        Bukkit.getScheduler().runTaskAsynchronously(TanoRPG.getPlugin(), () -> {
            try { Thread.sleep(1000);} catch (InterruptedException ex) {ex.printStackTrace();}

            p.sendMessage("§a試験官 §7「やぁ。 君が冒険者になりたい人か？」");
            TanoRPG.playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
            try { Thread.sleep(2700);} catch (InterruptedException ex) {ex.printStackTrace();}

            p.sendMessage("§c" + p.getName() + "§7「そうです」");
            TanoRPG.playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
            try { Thread.sleep(1400);} catch (InterruptedException ex) {ex.printStackTrace();}

            p.sendMessage("§a試験官 §7「じゃあ早速試験...」");
            TanoRPG.playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
            try { Thread.sleep(1600);} catch (InterruptedException ex) {ex.printStackTrace();}

            p.sendMessage("§a試験官 §7「と言いたいが、初めに職業を決めないと話にならない」");
            TanoRPG.playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
            try { Thread.sleep(3000);} catch (InterruptedException ex) {ex.printStackTrace();}

            p.sendMessage("§c" + p.getName() + "§7「職業？」");
            TanoRPG.playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
            try { Thread.sleep(1700);} catch (InterruptedException ex) {ex.printStackTrace();}

            p.sendMessage("§a試験官 §7「ああ、冒険者はそれぞれ自分にあった武器やスタイルで戦うんだ」");
            TanoRPG.playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
            try { Thread.sleep(3000);} catch (InterruptedException ex) {ex.printStackTrace();}

            p.sendMessage("§a試験官 §7「それを今から決めてもらう」");
            TanoRPG.playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
            try { Thread.sleep(2500);} catch (InterruptedException ex) {ex.printStackTrace();}

            p.sendMessage("§a試験官 §7「4つの職業から好きなのを選んでまた私に話しかけてくれ」");
            TanoRPG.playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
            try { Thread.sleep(3700);} catch (InterruptedException ex) {ex.printStackTrace();}

            p.sendMessage("§c" + p.getName() + "§7「わかりました」");
            TanoRPG.playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
            try { Thread.sleep(1000);} catch (InterruptedException ex) {ex.printStackTrace();}

            Bukkit.getScheduler().runTask(TanoRPG.getPlugin(), () -> new SelSkillClassMenu().getInv().open(p));

            isActive.remove(p.getUniqueId());
        });

    }

    @EventHandler
    public void onPlayerDoorOpen(PlayerInteractEvent e) {
        Member member = TanoRPG.getPlugin().getMemberManager().loadData(e.getPlayer().getUniqueId());
        if (member == null) {
            if ((e.getAction() == Action.RIGHT_CLICK_BLOCK) && !e.getPlayer().hasMetadata("start"))
                if (("" + e.getClickedBlock().getType()).contains("DOOR")) e.setCancelled(true);

            if (e.getItem() == null) return;
            if (e.getItem().getType().equals(Material.AIR)) return;

            if (e.getItem().getItemMeta().getDisplayName().equals("クリックで夢から覚める")) {
                start(e.getPlayer());
                e.getPlayer().getInventory().remove(e.getItem());
            }
        }
    }

    private void start(Player p) {
        p.teleport(TanoRPG.getPlugin().getDataManager().getHomeLoc());

        Bukkit.getScheduler().runTask(TanoRPG.getPlugin(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                "effect give " + p.getName() + " minecraft:blindness 8 1"));

        Bukkit.getScheduler().runTaskAsynchronously(TanoRPG.getPlugin(), () -> {
            try { Thread.sleep(2000);} catch (InterruptedException ex) {ex.printStackTrace();}

            p.sendMessage("§c" + p.getName() + "§7「ふわぁ...」");
            TanoRPG.playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
            try { Thread.sleep(3000);} catch (InterruptedException ex) {ex.printStackTrace();}

            p.sendMessage("§c" + p.getName() + "§7「もう朝か....」");
            TanoRPG.playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
            try { Thread.sleep(3000);} catch (InterruptedException ex) {ex.printStackTrace();}

            p.sendMessage("§c" + p.getName() + "§7「え！ 8時！？ 早くしないと試験に遅れる！」");
            TanoRPG.playSound(p, Sound.ENTITY_CHICKEN_EGG, 3, 0.5);
            try { Thread.sleep(3000);} catch (InterruptedException ex) {ex.printStackTrace();}

            p.sendMessage(TanoRPG.PX + "扉を出て早く§a「ギルド」§bに行こう！");
            TanoRPG.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 1);

            Bukkit.getScheduler().runTask(TanoRPG.getPlugin(), () -> p.setMetadata("start", new FixedMetadataValue(TanoRPG.getPlugin(), true)));
        });
    }
}
