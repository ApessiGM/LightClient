package dev.lightclient.setting;

/** A numeric (double precision) setting bounded by a min/max and step. */
public final class NumberSetting extends Setting {
    private double value;
    private final double min;
    private final double max;
    private final double step;

    public NumberSetting(String name, String description, double defaultValue, double min, double max, double step) {
        super(name, description);
        this.min = min;
        this.max = max;
        this.step = step;
        this.value = clamp(defaultValue);
    }

    public double get() {
        return value;
    }

    public float getFloat() {
        return (float) value;
    }

    public int getInt() {
        return (int) Math.round(value);
    }

    public void set(double value) {
        this.value = snap(clamp(value));
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getStep() {
        return step;
    }

    private double clamp(double v) {
        return Math.max(min, Math.min(max, v));
    }

    private double snap(double v) {
        if (step <= 0) {
            return v;
        }
        double snapped = Math.round(v / step) * step;
        // Avoid floating point drift in the serialized output.
        return Math.round(snapped * 1000.0) / 1000.0;
    }

    @Override
    public Object serialize() {
        return value;
    }

    @Override
    public void deserialize(Object value) {
        if (value instanceof Number number) {
            set(number.doubleValue());
        }
    }
}
