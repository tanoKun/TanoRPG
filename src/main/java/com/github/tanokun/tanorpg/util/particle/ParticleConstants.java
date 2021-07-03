package com.github.tanokun.tanorpg.util.particle;

import com.github.tanokun.tanorpg.util.particle.util.ReflectionUtils;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ParticleConstants {
    public static final Class ITEM_STACK_CLASS;
    public static final Class PACKET_CLASS;
    public static final Class PACKET_PLAY_OUT_WORLD_PARTICLES_CLASS;
    public static final Class PARTICLE_ENUM;
    public static final Class PARTICLE_CLASS;
    public static final Class MINECRAFT_KEY_CLASS;
    public static final Class REGISTRY_CLASS;
    public static final Class BLOCK_CLASS;
    public static final Class BLOCK_DATA_INTERFACE;
    public static final Class BLOCKS_CLASS;
    public static final Class ENTITY_PLAYER_CLASS;
    public static final Class PLAYER_CONNECTION_CLASS;
    public static final Class CRAFT_PLAYER_CLASS;
    public static final Class CRAFT_ITEM_STACK_CLASS;
    public static final Class PARTICLE_PARAM_CLASS;
    public static final Class PARTICLE_PARAM_REDSTONE_CLASS;
    public static final Class PARTICLE_PARAM_BLOCK_CLASS;
    public static final Class PARTICLE_PARAM_ITEM_CLASS;
    public static final Method REGISTRY_GET_METHOD;
    public static final Method PLAYER_CONNECTION_SEND_PACKET_METHOD;
    public static final Method CRAFT_PLAYER_GET_HANDLE_METHOD;
    public static final Method BLOCK_GET_BLOCK_DATA_METHOD;
    public static final Method CRAFT_ITEM_STACK_AS_NMS_COPY_METHOD;
    public static final Field ENTITY_PLAYER_PLAYER_CONNECTION_FIELD;
    public static final Constructor PACKET_PLAY_OUT_WORLD_PARTICLES_CONSTRUCTOR;
    public static final Constructor MINECRAFT_KEY_CONSTRUCTOR;
    public static final Constructor PARTICLE_PARAM_REDSTONE_CONSTRUCTOR;
    public static final Constructor PARTICLE_PARAM_BLOCK_CONSTRUCTOR;
    public static final Constructor PARTICLE_PARAM_ITEM_CONSTRUCTOR;
    public static final Object PARTICLE_TYPE_REGISTRY;

    public ParticleConstants() {
    }


    static {
        int version = ReflectionUtils.MINECRAFT_VERSION;
        ITEM_STACK_CLASS = ReflectionUtils.getNMSClass("ItemStack");
        PACKET_CLASS = ReflectionUtils.getNMSClass("Packet");
        PACKET_PLAY_OUT_WORLD_PARTICLES_CLASS = ReflectionUtils.getNMSClass("PacketPlayOutWorldParticles");
        PARTICLE_ENUM = version < 13 ? ReflectionUtils.getNMSClass("EnumParticle") : null;
        PARTICLE_CLASS = version < 13 ? null : ReflectionUtils.getNMSClass("Particle");
        MINECRAFT_KEY_CLASS = ReflectionUtils.getNMSClass("MinecraftKey");
        REGISTRY_CLASS = version < 13 ? null : ReflectionUtils.getNMSClass("IRegistry");
        BLOCK_CLASS = ReflectionUtils.getNMSClass("Block");
        BLOCK_DATA_INTERFACE = ReflectionUtils.getNMSClass("IBlockData");
        BLOCKS_CLASS = version < 13 ? null : ReflectionUtils.getNMSClass("Blocks");
        ENTITY_PLAYER_CLASS = ReflectionUtils.getNMSClass("EntityPlayer");
        PLAYER_CONNECTION_CLASS = ReflectionUtils.getNMSClass("PlayerConnection");
        CRAFT_PLAYER_CLASS = ReflectionUtils.getCraftBukkitClass("entity.CraftPlayer");
        CRAFT_ITEM_STACK_CLASS = ReflectionUtils.getCraftBukkitClass("inventory.CraftItemStack");
        PARTICLE_PARAM_CLASS = version < 13 ? null : ReflectionUtils.getNMSClass("ParticleParam");
        PARTICLE_PARAM_REDSTONE_CLASS = version < 13 ? null : ReflectionUtils.getNMSClass("ParticleParamRedstone");
        PARTICLE_PARAM_BLOCK_CLASS = version < 13 ? null : ReflectionUtils.getNMSClass("ParticleParamBlock");
        PARTICLE_PARAM_ITEM_CLASS = version < 13 ? null : ReflectionUtils.getNMSClass("ParticleParamItem");
        REGISTRY_GET_METHOD = version < 13 ? null : ReflectionUtils.getMethodOrNull(REGISTRY_CLASS, "get", new Class[]{MINECRAFT_KEY_CLASS});
        PLAYER_CONNECTION_SEND_PACKET_METHOD = ReflectionUtils.getMethodOrNull(PLAYER_CONNECTION_CLASS, "sendPacket", new Class[]{PACKET_CLASS});
        CRAFT_PLAYER_GET_HANDLE_METHOD = ReflectionUtils.getMethodOrNull(CRAFT_PLAYER_CLASS, "getHandle", new Class[0]);
        BLOCK_GET_BLOCK_DATA_METHOD = ReflectionUtils.getMethodOrNull(BLOCK_CLASS, "getBlockData", new Class[0]);
        CRAFT_ITEM_STACK_AS_NMS_COPY_METHOD = ReflectionUtils.getMethodOrNull(CRAFT_ITEM_STACK_CLASS, "asNMSCopy", new Class[]{ItemStack.class});
        ENTITY_PLAYER_PLAYER_CONNECTION_FIELD = ReflectionUtils.getFieldOrNull(ENTITY_PLAYER_CLASS, "playerConnection", false);
        if (version < 13) {
            PACKET_PLAY_OUT_WORLD_PARTICLES_CONSTRUCTOR = ReflectionUtils.getConstructorOrNull(PACKET_PLAY_OUT_WORLD_PARTICLES_CLASS, new Class[]{PARTICLE_ENUM, Boolean.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Integer.TYPE, int[].class});
        } else if (version < 15) {
            PACKET_PLAY_OUT_WORLD_PARTICLES_CONSTRUCTOR = ReflectionUtils.getConstructorOrNull(PACKET_PLAY_OUT_WORLD_PARTICLES_CLASS, new Class[]{PARTICLE_PARAM_CLASS, Boolean.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Integer.TYPE});
        } else {
            PACKET_PLAY_OUT_WORLD_PARTICLES_CONSTRUCTOR = ReflectionUtils.getConstructorOrNull(PACKET_PLAY_OUT_WORLD_PARTICLES_CLASS, new Class[]{PARTICLE_PARAM_CLASS, Boolean.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Integer.TYPE});
        }

        MINECRAFT_KEY_CONSTRUCTOR = ReflectionUtils.getConstructorOrNull(MINECRAFT_KEY_CLASS, new Class[]{String.class});
        PARTICLE_PARAM_REDSTONE_CONSTRUCTOR = version < 13 ? null : ReflectionUtils.getConstructorOrNull(PARTICLE_PARAM_REDSTONE_CLASS, new Class[]{Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE});
        PARTICLE_PARAM_BLOCK_CONSTRUCTOR = version < 13 ? null : ReflectionUtils.getConstructorOrNull(PARTICLE_PARAM_BLOCK_CLASS, new Class[]{PARTICLE_CLASS, BLOCK_DATA_INTERFACE});
        PARTICLE_PARAM_ITEM_CONSTRUCTOR = version < 13 ? null : ReflectionUtils.getConstructorOrNull(PARTICLE_PARAM_ITEM_CLASS, new Class[]{PARTICLE_CLASS, ITEM_STACK_CLASS});
        PARTICLE_TYPE_REGISTRY = version < 13 ? null : ReflectionUtils.readField(ReflectionUtils.getFieldOrNull(REGISTRY_CLASS, "PARTICLE_TYPE", false), (Object)null);
    }
}