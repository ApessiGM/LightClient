package dev.lightclient.modules.utility;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.BooleanSetting;

/** Enhances the player list overlay with ping numbers and head icons. */
public final class BetterTabModule extends Module {
    private final BooleanSetting showPing = register(new BooleanSetting("Show Ping", "Display numeric ping", true));
    private final BooleanSetting showHeads = register(new BooleanSetting("Show Heads", "Display player heads", true));
    private final BooleanSetting highlightFriends = register(new BooleanSetting("Highlight Friends", "Colour friends", true));

    public BetterTabModule() {
        super("Better Tab", "Improved player list", Category.UTILITY);
    }

    public boolean showPing() {
        return showPing.get();
    }

    public boolean showHeads() {
        return showHeads.get();
    }

    public boolean highlightFriends() {
        return highlightFriends.get();
    }
}
