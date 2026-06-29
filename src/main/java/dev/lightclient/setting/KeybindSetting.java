package dev.lightclient.setting;

import org.lwjgl.glfw.GLFW;

/** Stores a single GLFW key code used to bind an action. */
public final class KeybindSetting extends Setting {
    private int key;

    public KeybindSetting(String name, String description, int defaultKey) {
        super(name, description);
        this.key = defaultKey;
    }

    public int get() {
        return key;
    }

    public void set(int key) {
        this.key = key;
    }

    public boolean isBound() {
        return key != GLFW.GLFW_KEY_UNKNOWN;
    }

    @Override
    public Object serialize() {
        return key;
    }

    @Override
    public void deserialize(Object value) {
        if (value instanceof Number number) {
            this.key = number.intValue();
        }
    }
}
