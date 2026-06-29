package dev.lightclient.manager;

import dev.lightclient.Reference;
import dev.lightclient.util.ColorUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Holds the active colour theme. Themes define the accent gradient and the
 * dark surface colours used across the ClickGUI, HUD and main menu.
 */
public final class ThemeManager {

    /** An immutable colour theme definition. */
    public static final class Theme {
        public final String name;
        public final int accentStart;
        public final int accentEnd;
        public final int surface;
        public final int background;
        public final int text;

        public Theme(String name, int accentStart, int accentEnd, int surface, int background, int text) {
            this.name = name;
            this.accentStart = accentStart;
            this.accentEnd = accentEnd;
            this.surface = surface;
            this.background = background;
            this.text = text;
        }
    }

    private final Map<String, Theme> themes = new LinkedHashMap<>();
    private Theme active;

    public ThemeManager() {
        register(new Theme("Light Neon",
                ColorUtil.opaque(Reference.COLOR_PURPLE_NEON),
                ColorUtil.opaque(Reference.COLOR_PURPLE_BRIGHT),
                ColorUtil.withAlpha(Reference.COLOR_DARK_PRIMARY, 220),
                ColorUtil.withAlpha(Reference.COLOR_DARK_SECONDARY, 235),
                0xFFFFFFFF));
        register(new Theme("Crystal Violet",
                ColorUtil.opaque(Reference.COLOR_PURPLE_BLUEVIOLET),
                ColorUtil.opaque(Reference.COLOR_PURPLE_NEON),
                ColorUtil.withAlpha(0x161122, 220),
                ColorUtil.withAlpha(0x0B0814, 235),
                0xFFEFEAFF));
        register(new Theme("Midnight",
                ColorUtil.opaque(0x6A1FD0),
                ColorUtil.opaque(0x9A30FF),
                ColorUtil.withAlpha(0x101014, 225),
                ColorUtil.withAlpha(0x060608, 240),
                0xFFFFFFFF));
        active = themes.values().iterator().next();
    }

    public void register(Theme theme) {
        themes.put(theme.name.toLowerCase(), theme);
    }

    public Theme getActive() {
        return active;
    }

    public boolean setActive(String name) {
        Theme theme = themes.get(name.toLowerCase());
        if (theme != null) {
            active = theme;
            return true;
        }
        return false;
    }

    public Map<String, Theme> getThemes() {
        return themes;
    }

    public int accent() {
        return active.accentStart;
    }

    public int accentEnd() {
        return active.accentEnd;
    }
}
