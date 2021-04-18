package com.github.tanokun.api.particle;

import com.github.tanokun.api.particle.data.ParticleData;
import com.github.tanokun.api.particle.data.color.NoteColor;
import com.github.tanokun.api.particle.data.color.ParticleColor;
import com.github.tanokun.api.particle.data.color.RegularColor;
import com.github.tanokun.api.particle.data.texture.BlockTexture;
import com.github.tanokun.api.particle.data.texture.ItemTexture;
import com.github.tanokun.api.particle.util.ReflectionUtils;
import org.bukkit.Location;

import java.lang.reflect.Constructor;

public class ParticlePacket {
    private final ParticleEffect particle;
    private final float offsetX;
    private final float offsetY;
    private final float offsetZ;
    private final float speed;
    private final int amount;
    private final ParticleData particleData;

    public ParticlePacket(ParticleEffect particle, float offsetX, float offsetY, float offsetZ, float speed, int amount, ParticleData particleData) {
        this.particle = particle;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.speed = speed;
        this.amount = amount;
        this.particleData = particleData;
    }

    public ParticlePacket(ParticleEffect particle, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        this.particle = particle;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.speed = speed;
        this.amount = amount;
        this.particleData = null;
    }

    public ParticleEffect getParticle() {
        return this.particle;
    }

    public float getOffsetX() {
        return this.offsetX;
    }

    public float getOffsetY() {
        return this.offsetY;
    }

    public float getOffsetZ() {
        return this.offsetZ;
    }

    public float getSpeed() {
        return this.speed;
    }

    public int getAmount() {
        return this.amount;
    }

    public ParticleData getParticleData() {
        return this.particleData;
    }

    public Object createPacket(Location location) {
        try {
            ParticleData data = this.getParticleData();
            ParticleEffect effect = this.getParticle();
            int version = ReflectionUtils.MINECRAFT_VERSION;
            if (effect == null || effect.getFieldName().equals("NONE")) {
                return null;
            }

            if (data != null) {
                if (data.getEffect() != effect) {
                    return null;
                }

                if (data instanceof BlockTexture && this.getParticle().hasProperty(PropertyType.REQUIRES_BLOCK) || data instanceof ItemTexture && this.getParticle().hasProperty(PropertyType.REQUIRES_ITEM)) {
                    return this.createPacket(version < 13 ? effect.getNMSObject() : data.toNMSData(), (float)location.getX(), (float)location.getY(), (float)location.getZ(), this.getOffsetX(), this.getOffsetY(), this.getOffsetZ(), this.getSpeed(), this.getAmount(), version < 13 ? (int[])((int[])data.toNMSData()) : new int[0]);
                }

                if (data instanceof ParticleColor && effect.hasProperty(PropertyType.COLORABLE)) {
                    if (data instanceof NoteColor && effect.equals(ParticleEffect.NOTE)) {
                        return this.createPacket(effect.getNMSObject(), (float)location.getX(), (float)location.getY(), (float)location.getZ(), ((NoteColor)data).getRed(), 0.0F, 0.0F, this.getSpeed(), this.getAmount(), new int[0]);
                    }

                    if (data instanceof RegularColor) {
                        RegularColor color = (RegularColor)data;
                        if (version >= 13 && effect.equals(ParticleEffect.REDSTONE)) {
                            return this.createPacket(data.toNMSData(), (float)location.getX(), (float)location.getY(), (float)location.getZ(), this.getOffsetX(), this.getOffsetY(), this.getOffsetZ(), this.getSpeed(), this.getAmount(), new int[0]);
                        }

                        return this.createPacket(effect.getNMSObject(), (float)location.getX(), (float)location.getY(), (float)location.getZ(), effect.equals(ParticleEffect.REDSTONE) && color.getRed() == 0.0F ? 1.17549435E-38F : color.getRed(), color.getGreen(), color.getBlue(), 1.0F, 0, new int[0]);
                    }
                }

                return null;
            }

            if (!effect.hasProperty(PropertyType.REQUIRES_BLOCK) && !effect.hasProperty(PropertyType.REQUIRES_ITEM)) {
                return this.createPacket(effect.getNMSObject(), (float)location.getX(), (float)location.getY(), (float)location.getZ(), this.getOffsetX(), this.getOffsetY(), this.getOffsetZ(), this.getSpeed(), this.getAmount(), new int[0]);
            }
        } catch (Exception var6) {
        }

        return null;
    }

    private Object createPacket(Object param, float locationX, float locationY, float locationZ, float offsetX, float offsetY, float offsetZ, float speed, int amount, int[] data) {
        Constructor packetConstructor = ParticleConstants.PACKET_PLAY_OUT_WORLD_PARTICLES_CONSTRUCTOR;

        try {
            if (ReflectionUtils.MINECRAFT_VERSION < 13) {
                return packetConstructor.newInstance(param, true, locationX, locationY, locationZ, offsetX, offsetY, offsetZ, speed, amount, data);
            } else {
                return ReflectionUtils.MINECRAFT_VERSION < 15 ? packetConstructor.newInstance(param, true, locationX, locationY, locationZ, offsetX, offsetY, offsetZ, speed, amount) : packetConstructor.newInstance(param, true, (double)locationX, (double)locationY, (double)locationZ, offsetX, offsetY, offsetZ, speed, amount);
            }
        } catch (Exception var13) {
            return null;
        }
    }
}