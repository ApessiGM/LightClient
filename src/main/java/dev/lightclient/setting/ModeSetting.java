package dev.lightclient.setting;

import java.util.Arrays;
import java.util.List;

/** A setting that cycles through a fixed list of string options. */
public final class ModeSetting extends Setting {
    private final List<String> options;
    private int index;

    public ModeSetting(String name, String description, String defaultValue, String... options) {
        super(name, description);
        this.options = Arrays.asList(options);
        int found = this.options.indexOf(defaultValue);
        this.index = Math.max(0, found);
    }

    public String get() {
        return options.get(index);
    }

    public boolean is(String value) {
        return get().equalsIgnoreCase(value);
    }

    public List<String> getOptions() {
        return options;
    }

    public int getIndex() {
        return index;
    }

    public void set(String value) {
        int found = options.indexOf(value);
        if (found >= 0) {
            index = found;
        }
    }

    public void cycle() {
        index = (index + 1) % options.size();
    }

    public void cycleBackwards() {
        index = (index - 1 + options.size()) % options.size();
    }

    @Override
    public Object serialize() {
        return get();
    }

    @Override
    public void deserialize(Object value) {
        if (value instanceof String string) {
            set(string);
        }
    }
}
