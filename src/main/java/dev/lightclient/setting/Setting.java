package dev.lightclient.setting;

import java.util.function.BooleanSupplier;

/**
 * Base class for a configurable module setting. Subclasses define a value type
 * and how it serialises to/from JSON-friendly primitives.
 */
public abstract class Setting {
    private final String name;
    private final String description;
    private BooleanSupplier visibility = () -> true;

    protected Setting(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /** Hides this setting in the ClickGUI when the supplier returns {@code false}. */
    public Setting visibleWhen(BooleanSupplier supplier) {
        this.visibility = supplier;
        return this;
    }

    public boolean isVisible() {
        return visibility.getAsBoolean();
    }

    /** Serialises the current value into a primitive understood by the config layer. */
    public abstract Object serialize();

    /** Restores the value from a previously serialised primitive. */
    public abstract void deserialize(Object value);
}
