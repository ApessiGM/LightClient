package dev.lightclient.modules.misc;

import dev.lightclient.event.EventTarget;
import dev.lightclient.event.events.TickEvent;
import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.NumberSetting;

/** Periodically runs a configurable tip command on supported servers. */
public final class AutoTipModule extends Module {
    private final NumberSetting interval = register(new NumberSetting("Interval", "Minutes between tips", 5, 1, 30, 1));

    private long lastTip;

    public AutoTipModule() {
        super("AutoTip", "Automatically tips periodically", Category.MISC);
    }

    @Override
    public void onEnable() {
        lastTip = System.currentTimeMillis();
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (mc.player == null) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now - lastTip >= interval.getInt() * 60_000L) {
            mc.player.networkHandler.sendChatMessage("/tip all");
            lastTip = now;
        }
    }
}
