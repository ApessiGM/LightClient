package dev.lightclient.util;

/** Small numeric helpers used across the client. */
public final class MathUtil {
    private MathUtil() {
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double lerp(double from, double to, double t) {
        return from + (to - from) * clamp(t, 0.0, 1.0);
    }

    public static float round(float value, int decimals) {
        double factor = Math.pow(10, decimals);
        return (float) (Math.round(value * factor) / factor);
    }
}
