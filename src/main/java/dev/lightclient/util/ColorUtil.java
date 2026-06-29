package dev.lightclient.util;

/** Helper routines for manipulating packed ARGB colours. */
public final class ColorUtil {
    private ColorUtil() {
    }

    public static int argb(int a, int r, int g, int b) {
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    /** Turns an RGB value into a fully opaque ARGB value. */
    public static int opaque(int rgb) {
        return 0xFF000000 | (rgb & 0xFFFFFF);
    }

    public static int withAlpha(int rgb, int alpha) {
        return ((alpha & 0xFF) << 24) | (rgb & 0xFFFFFF);
    }

    public static int alpha(int argb) {
        return (argb >> 24) & 0xFF;
    }

    public static int red(int argb) {
        return (argb >> 16) & 0xFF;
    }

    public static int green(int argb) {
        return (argb >> 8) & 0xFF;
    }

    public static int blue(int argb) {
        return argb & 0xFF;
    }

    /** Linearly interpolates between two ARGB colours. */
    public static int lerp(int from, int to, float t) {
        t = Math.max(0f, Math.min(1f, t));
        int a = (int) (alpha(from) + (alpha(to) - alpha(from)) * t);
        int r = (int) (red(from) + (red(to) - red(from)) * t);
        int g = (int) (green(from) + (green(to) - green(from)) * t);
        int b = (int) (blue(from) + (blue(to) - blue(from)) * t);
        return argb(a, r, g, b);
    }

    /** Multiplies the alpha channel by the given factor (0..1). */
    public static int multiplyAlpha(int argb, float factor) {
        int a = (int) (alpha(argb) * Math.max(0f, Math.min(1f, factor)));
        return argb(a, red(argb), green(argb), blue(argb));
    }

    /** Builds an ARGB colour from HSB plus an explicit alpha. */
    public static int hsb(float hue, float saturation, float brightness, int alpha) {
        int rgb = java.awt.Color.HSBtoRGB(hue, saturation, brightness) & 0xFFFFFF;
        return withAlpha(rgb, alpha);
    }
}
