package com.github.tanokun.tanorpg.util.particle.data.color;


import com.github.tanokun.tanorpg.util.particle.ParticleConstants;
import com.github.tanokun.tanorpg.util.particle.ParticleEffect;
import com.github.tanokun.tanorpg.util.particle.util.MathUtils;
import com.github.tanokun.tanorpg.util.particle.util.ReflectionUtils;

import java.awt.*;

public class RegularColor extends ParticleColor {

    public RegularColor(Color color) {
        super(color.getRed(), color.getGreen(), color.getBlue());
    }

    public RegularColor(int red, int green, int blue) {
        super(MathUtils.getMaxOrMin(red, 255, 0), MathUtils.getMaxOrMin(green, 255, 0), MathUtils.getMaxOrMin(blue, 255, 0));
    }

    public float getRed() {
        return super.getRed() / 255.0F;
    }

    public float getGreen() {
        return super.getGreen() / 255.0F;
    }

    public float getBlue() {
        return super.getBlue() / 255.0F;
    }

    public Object toNMSData() {
        if (getEffect() == ParticleEffect.REDSTONE && ReflectionUtils.MINECRAFT_VERSION >= 13) {
            try {
                return ParticleConstants.PARTICLE_PARAM_REDSTONE_CONSTRUCTOR.newInstance(this.getRed(), this.getGreen(), this.getBlue(), 1.0F);
            } catch (Exception var2) {
                return null;
            }
        } else {
            return new int[0];
        }
    }

    public static RegularColor random() {
        return random(true);
    }

    public static RegularColor random(boolean highSaturation) {
        return highSaturation ? fromHSVHue(MathUtils.generateRandomInteger(0, 360)) : new RegularColor(new Color(MathUtils.RANDOM.nextInt(256), MathUtils.RANDOM.nextInt(256), MathUtils.RANDOM.nextInt(256)));
    }

    public static RegularColor fromHSVHue(int hue) {
        return new RegularColor(new Color(Color.HSBtoRGB((float)hue / 360.0F, 1.0F, 1.0F)));
    }
}
