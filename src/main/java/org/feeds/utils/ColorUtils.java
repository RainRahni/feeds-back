package org.feeds.utils;

import java.util.Random;
import java.util.Set;

public class ColorUtils {
    private static final Random random = new Random();

    public static String addColor(Set<String> usedColors) {
        String color;
        do {
            color = generateHarmoniousColor();
        } while (usedColors.contains(color));
        return color;
    }

    private static String generateHarmoniousColor() {
        float r = random.nextFloat() / 2f + 0.5f;
        float g = random.nextFloat() / 2f + 0.5f;
        float b = random.nextFloat() / 2f + 0.5f;
        return String.format("#%02X%02X%02X",
                Math.round(r * 255),
                Math.round(g * 255),
                Math.round(b * 255));
    }
}
