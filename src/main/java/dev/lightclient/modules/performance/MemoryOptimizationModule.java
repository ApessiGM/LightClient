package dev.lightclient.modules.performance;

import dev.lightclient.event.EventTarget;
import dev.lightclient.event.events.TickEvent;
import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.NumberSetting;

/** Periodically requests garbage collection to keep memory usage low. */
public final class MemoryOptimizationModule extends Module {
    private final NumberSetting interval =
            register(new NumberSetting("Interval", "Seconds between collections", 60, 10, 600, 5));

    private long lastRun;

    public MemoryOptimizationModule() {
        super("Memory Optimization", "Frees unused memory periodically", Category.PERFORMANCE);
    }

    @Override
    public void onEnable() {
        lastRun = System.currentTimeMillis();
    }

    @EventTarget
    public void onTick(TickEvent event) {
        long now = System.currentTimeMillis();
        if (now - lastRun >= interval.getInt() * 1000L) {
            System.gc();
            lastRun = now;
        }
    }
}
