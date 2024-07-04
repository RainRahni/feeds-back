package org.feeds.utils;

import java.util.Random;
import java.util.Set;

public class ColorUtils {
    private static final Random random = new Random();

    public static String addColor(Set<String> usedColors) {
        String color = String.format("#%06x", random.nextInt(0x7FFFFF + 1) + 0x800000);
        while (usedColors.contains(color)) {
            color = String.format("#%06x", random.nextInt(0x7FFFFF + 1) + 0x800000);
        }
        return color;
    }
}
