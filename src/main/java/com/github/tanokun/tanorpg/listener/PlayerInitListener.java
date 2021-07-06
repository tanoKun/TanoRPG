package com.github.tanokun.tanorpg.listener;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.event.WgRegionLeftEvent;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerLoadEvent;

import java.util.Arrays;

public class PlayerInitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Member member = TanoRPG.getPlugin().getMemberManager().loadData(e.getPlayer().getUniqueId());
        if (member == null){
            e.setJoinMessage(TanoRPG.PX + "§d" + e.getPlayer().getName() + "が初ログインしました！ 082!");
            e.getPlayer().sendMessage(TanoRPG.PX + "§a目の前にいる人に話しかけてみよう");
            TanoRPG.playSound(Bukkit.getOnlinePlayers().toArray(new Player[0]), Sound.ENTITY_PLAYER_LEVELUP, 3, 1);
            e.getPlayer().teleport(TanoRPG.getPlugin().getDataManager().getStartLoc());
        } else {
            e.setJoinMessage(TanoRPG.PX + e.getPlayer().getName() + "がログインしました。");
            TanoRPG.getPlugin().getMemberManager().registerMember(member);
            TanoRPG.getPlugin().getSidebarManager().setupSidebar(e.getPlayer(), member);
        }
        e.getPlayer().getInventory().setItem(8, ItemUtils.createItem(
                Material.COMPASS, "§bMenu",
                Arrays.asList(new String[]{"§f初期アイテム", "§f自分の情報を見ることができる", "§fコンパスはクエストの場所を指し示してくれるぞ"}), 1, true));
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
        if (!e.getRegionName().contains(TanoRPG.getPlugin().getDataManager().getStartRegionName())) return;
        if (TanoRPG.getPlugin().getMemberManager().getMember(e.getPlayer().getUniqueId()) != null) return;
        e.getPlayer().sendMessage(TanoRPG.PX + "§cチュートリアルを完了するまで出ることはできません");
        e.setCancelled(true);
    }
}
