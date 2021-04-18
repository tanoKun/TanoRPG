package com.github.tanokun.api.particle.data.texture;

import com.github.tanokun.api.particle.data.ParticleData;
import org.bukkit.Material;

public class ParticleTexture extends ParticleData {
    private final Material material;
    private final byte data;

    ParticleTexture(Material material, byte data) {
        this.material = material;
        this.data = data;
    }

    public Material getMaterial() {
        return this.material;
    }

    public byte getData() {
        return this.data;
    }

    public Object toNMSData() {
        return new int[]{this.getMaterial().ordinal(), this.getData()};
    }
}
