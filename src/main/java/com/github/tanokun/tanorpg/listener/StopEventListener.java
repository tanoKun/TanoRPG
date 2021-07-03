package com.github.tanokun.tanorpg.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.event.PlayerArmorEquipEvent;
import com.github.tanokun.tanorpg.game.entity.ActiveEntity;
import com.github.tanokun.tanorpg.game.entity.spawner.EntitySpawnerManager;
import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.player.status.StatusType;
import com.github.tanokun.tanorpg.util.EntityUtils;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;
import java.util.stream.Stream;

public class StopEventListener implements Listener {
    @EventHandler
    public void onEquip(PlayerArmorEquipEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onChangeBlock(EntityChangeBlockEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (e.getEntity().isOp()) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!EntityUtils.isActiveEntity(e.getEntity())) return;
        Member member = TanoRPG.getPlugin().getMemberManager().getMember(e.getDamager().getUniqueId());

        int speed = 20;

        //ActiveEntity activeEntity = EntityUtils.getActiveEntity(e.getEntity());
//
        //int atk = (member.getStatusMap().getStatus(StatusType.ATK) *
        //        (5 + member.getHasLevel().getValue()) /
        //                (5 + (activeEntity.getObjectEntity().getHasLevel() - member.getHasLevel().getValue())*5 + activeEntity.getObjectEntity().getStatusMap().getStatus(StatusType.DEF)));
//
        //e.getDamager().sendMessage("§a" + atk);

    }
}
