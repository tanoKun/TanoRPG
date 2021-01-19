package com.github.tanokun.tanorpg.util.particle.data;

import com.github.tanokun.tanorpg.util.particle.ParticleEffect;

public abstract class ParticleData {
    private ParticleEffect effect;

    public void setEffect(ParticleEffect effect) {
        this.effect = effect;
    }

    public abstract Object toNMSData();

    public ParticleEffect getEffect() {
        return effect;
    }
}