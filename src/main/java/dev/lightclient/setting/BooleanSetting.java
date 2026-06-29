package dev.lightclient.setting;

import java.util.function.Consumer;

/** A simple on/off toggle setting. */
public final class BooleanSetting extends Setting {
    private boolean value;
    private Consumer<Boolean> onChange = b -> {
    };

    public BooleanSetting(String name, String description, boolean defaultValue) {
        super(name, description);
        this.value = defaultValue;
    }

    public boolean get() {
        return value;
    }

    public void set(boolean value) {
        if (this.value != value) {
            this.value = value;
            onChange.accept(value);
        }
    }

    public void toggle() {
        set(!value);
    }

    public BooleanSetting onChange(Consumer<Boolean> consumer) {
        this.onChange = consumer;
        return this;
    }

    @Override
    public Object serialize() {
        return value;
    }

    @Override
    public void deserialize(Object value) {
        if (value instanceof Boolean bool) {
            set(bool);
        }
    }
}
