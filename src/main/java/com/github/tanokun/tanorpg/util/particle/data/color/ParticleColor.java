package com.github.tanokun.tanorpg.util.particle.data.color;


import com.github.tanokun.tanorpg.util.particle.data.ParticleData;

public abstract class ParticleColor extends ParticleData {
    private final int red;
    private final int green;
    private final int blue;

    ParticleColor(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public abstract Object toNMSData();

    public float getRed() {
        return (float)this.red;
    }

    public float getGreen() {
        return (float)this.green;
    }

    public float getBlue() {
        return (float)this.blue;
    }
}