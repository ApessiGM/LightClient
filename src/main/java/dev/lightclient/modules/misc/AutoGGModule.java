package dev.lightclient.modules.misc;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.NumberSetting;

/**
 * Automatically sends a "good game" message at the end of a match. The
 * configured delay and phrase are consumed by the chat listener.
 */
public final class AutoGGModule extends Module {
    private final NumberSetting delay = register(new NumberSetting("Delay", "Delay in ticks", 20, 0, 100, 5));

    public AutoGGModule() {
        super("AutoGG", "Sends gg at the end of a game", Category.MISC);
    }

    public void send() {
        if (mc.player != null) {
            mc.player.networkHandler.sendChatMessage("gg");
        }
    }

    public int getDelay() {
        return delay.getInt();
    }
}
