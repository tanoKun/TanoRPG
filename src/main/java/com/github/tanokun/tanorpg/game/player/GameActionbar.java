package com.github.tanokun.tanorpg.game.player;


import net.minecraft.server.v1_15_R1.ChatMessageType;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class GameActionbar {
    private PacketPlayOutChat packet;
    public GameActionbar(String text){
        ChatMessageType b = net.minecraft.server.v1_15_R1.ChatMessageType.GAME_INFO;
        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}"), b);
        this.packet = packet;
    }
    public void showActionBar(Player player){
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }
}
