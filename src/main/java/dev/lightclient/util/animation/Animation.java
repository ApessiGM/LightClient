package dev.lightclient.util.animation;

/**
 * A time based animation that eases a value between 0 and 1. Used for fade,
 * scale, slide and hover effects throughout the UI.
 */
public final class Animation {
    private final long durationMs;
    private final Easing easing;
    private long startTime;
    private double from;
    private double to = 1.0;

    public Animation(long durationMs, Easing easing) {
        this.durationMs = durationMs;
        this.easing = easing;
        this.startTime = System.currentTimeMillis() - durationMs;
        this.from = 0.0;
        this.to = 0.0;
    }

    /** Animates towards the given target if it differs from the current goal. */
    public void animateTo(double target) {
        if (Double.compare(target, to) == 0) {
            return;
        }
        this.from = getValue();
        this.to = target;
        this.startTime = System.currentTimeMillis();
    }

    public double getValue() {
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed >= durationMs) {
            return to;
        }
        double progress = (double) elapsed / durationMs;
        double eased = easing.apply(progress);
        return from + (to - from) * eased;
    }

    public float getFloat() {
        return (float) getValue();
    }

    public boolean isFinished() {
        return System.currentTimeMillis() - startTime >= durationMs;
    }
}
