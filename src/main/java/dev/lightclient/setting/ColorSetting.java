package dev.lightclient.setting;

/** Stores an ARGB color value. */
public final class ColorSetting extends Setting {
    private int argb;

    public ColorSetting(String name, String description, int defaultArgb) {
        super(name, description);
        this.argb = defaultArgb;
    }

    public int get() {
        return argb;
    }

    public void set(int argb) {
        this.argb = argb;
    }

    public int getAlpha() {
        return (argb >> 24) & 0xFF;
    }

    public int getRed() {
        return (argb >> 16) & 0xFF;
    }

    public int getGreen() {
        return (argb >> 8) & 0xFF;
    }

    public int getBlue() {
        return argb & 0xFF;
    }

    @Override
    public Object serialize() {
        return argb;
    }

    @Override
    public void deserialize(Object value) {
        if (value instanceof Number number) {
            this.argb = number.intValue();
        }
    }
}
