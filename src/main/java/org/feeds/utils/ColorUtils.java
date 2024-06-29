package org.feeds.utils;

import java.util.List;
import java.util.Random;

public class ColorUtils {
    public static String addColor(List<String> usedColors) {
        Random random = new Random();
        String color = "";
        while (usedColors.contains(color)) {
            color = String.format("#%06x", random.nextInt(0xFFFFFF + 1));
        }
        return color;
    }
}
