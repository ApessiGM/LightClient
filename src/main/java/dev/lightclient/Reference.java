package dev.lightclient;

/**
 * Global, immutable references for the Light Client.
 */
public final class Reference {
    private Reference() {
    }

    public static final String MOD_ID = "lightclient";
    public static final String NAME = "Light Client";
    public static final String VERSION = "1.0.0";
    public static final String GAME_VERSION = "1.21.11";

    /** Command prefix used by the in-game chat command system. */
    public static final String COMMAND_PREFIX = ".";

    /** Brand palette (RGB, no alpha). */
    public static final int COLOR_PURPLE_BLUEVIOLET = 0x8A2BE2;
    public static final int COLOR_PURPLE_NEON = 0x7B2DFF;
    public static final int COLOR_PURPLE_BRIGHT = 0xAA00FF;
    public static final int COLOR_DARK_PRIMARY = 0x121212;
    public static final int COLOR_DARK_SECONDARY = 0x0A0A0A;
}
