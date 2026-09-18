package com.pharma.consultoria_pharma.utils;

public final class SlugUtil {

    private SlugUtil() {
    }

    public static String generate(String text) {
        return text
                .toLowerCase()
                .trim()
                .replaceAll("[áàäâã]", "a")
                .replaceAll("[éèëê]", "e")
                .replaceAll("[íìïî]", "i")
                .replaceAll("[óòöôõ]", "o")
                .replaceAll("[úùüû]", "u")
                .replaceAll("[ñ]", "n")
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("[\\s]+", "-")
                .replaceAll("-+", "-");
    }
}
