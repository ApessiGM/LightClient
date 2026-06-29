package dev.lightclient.modules.utility;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.BooleanSetting;
import dev.lightclient.setting.NumberSetting;

/** Quality-of-life chat improvements such as timestamps and longer history. */
public final class BetterChatModule extends Module {
    private final BooleanSetting timestamps = register(new BooleanSetting("Timestamps", "Prefix messages with time", true));
    private final BooleanSetting infiniteHistory = register(new BooleanSetting("Infinite History", "Keep more chat lines", true));
    private final NumberSetting historySize = register(new NumberSetting("History", "Stored chat lines", 500, 100, 2000, 50));

    public BetterChatModule() {
        super("Better Chat", "Improved chat with timestamps", Category.UTILITY);
    }

    public boolean timestamps() {
        return timestamps.get();
    }

    public boolean infiniteHistory() {
        return infiniteHistory.get();
    }

    public int historySize() {
        return historySize.getInt();
    }
}
