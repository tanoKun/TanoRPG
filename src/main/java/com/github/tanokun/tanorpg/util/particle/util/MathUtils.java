package com.github.tanokun.tanorpg.util.particle.util;

import java.util.Random;

public class MathUtils {
    public static final Random RANDOM = new Random();

    public MathUtils() {
    }

    public static int generateRandomInteger(int minimum, int maximum) {
        return minimum + (int)(RANDOM.nextDouble() * (double)(maximum - minimum + 1));
    }

    public static int getMaxOrMin(int value, int max, int min) {
        return value < max ? Math.max(value, min) : max;
    }
}
