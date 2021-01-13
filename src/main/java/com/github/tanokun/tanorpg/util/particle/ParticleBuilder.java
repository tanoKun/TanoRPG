package com.github.tanokun.tanorpg.util.particle;

import com.github.tanokun.tanorpg.util.particle.data.ParticleData;
import com.github.tanokun.tanorpg.util.particle.data.color.RegularColor;
import com.github.tanokun.tanorpg.util.particle.util.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

public class ParticleBuilder {
    private final ParticleEffect particle;
    private final Location location;
    private float offsetX = 0.0F;
    private float offsetY = 0.0F;
    private float offsetZ = 0.0F;
    private float speed = 1.0F;
    private int amount = 0;
    private ParticleData particleData = null;

    public ParticleBuilder(ParticleEffect particle, Location location) {
        this.particle = particle;
        this.location = location;
    }

    public ParticleBuilder setOffsetX(float offsetX) {
        this.offsetX = offsetX;
        return this;
    }

    public ParticleBuilder setOffsetY(float offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    public ParticleBuilder setOffsetZ(float offsetZ) {
        this.offsetZ = offsetZ;
        return this;
    }

    public ParticleBuilder setSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    public ParticleBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ParticleBuilder setParticleData(ParticleData particleData) {
        this.particleData = particleData;
        return this;
    }

    public ParticleBuilder setColor(Color color) {
        if (this.particle.hasProperty(PropertyType.COLORABLE)) {
            this.particleData = new RegularColor(color);
        }

        return this;
    }

    public Object toPacket() {
        if (this.particleData != null) {
            this.particleData.setEffect(this.particle);
        }

        ParticlePacket packet = new ParticlePacket(this.particle, this.offsetX, this.offsetY, this.offsetZ, this.speed, this.amount, this.particleData);
        return packet.createPacket(this.location);
    }

    public void display() {
        this.display(Bukkit.getOnlinePlayers());
    }

    public void display(Player... players) {
        this.display((Collection) Arrays.asList(players));
    }

    public void display(Predicate<Player> filter) {
        Object packet = this.toPacket();
        Bukkit.getOnlinePlayers().stream().filter(filter).forEach((player) -> {
            ReflectionUtils.sendPacket(player, packet);
        });
    }

    public void display(Collection<? extends Player> players) {
        Object packet = this.toPacket();
        players.forEach((player) -> {
            ReflectionUtils.sendPacket(player, packet);
        });
    }
}