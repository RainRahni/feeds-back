package org.feeds.utils;

import java.awt.*;
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
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();
        Color randomColor = new Color(r, g, b);
        Color brighterColor = randomColor.brighter();
        return String.format("#%02X%02X%02X",
                brighterColor.getRed(),
                brighterColor.getGreen(),
                brighterColor.getBlue());
    }
}
